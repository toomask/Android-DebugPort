package jwf.debugport.internal.debug;

import android.content.Context;

import java.io.IOException;
import java.net.Socket;

import bsh.Interpreter;
import jwf.debugport.DebugPort;
import jwf.debugport.internal.debug.commands.Commands;
import jwf.debugport.internal.ClientConnection;
import jwf.debugport.internal.TelnetServer;

/**
 *
 */
public class DebugClientConnection extends ClientConnection implements Commands.ExitListener {
    private TelnetConsoleInterface mConsole;
    Interpreter interpreter;

    public DebugClientConnection(Context context, Socket client, TelnetServer parent, String[] startupCommands) {
        super(context, client, parent, startupCommands);
    }

    @Override
    public void closeConnection() {
        // nothing to do here..
    }

    @Override
    public void run() {
        try {
            mConsole = new TelnetConsoleInterface(getSocket().getInputStream(), getSocket().getOutputStream());
            interpreter = new Interpreter(mConsole);
            interpreter.setShowResults(true);
            interpreter.set("cmd", new Commands(this));
            interpreter.set("app", getApp());
            interpreter.eval("setAccessibility(true)");
            interpreter.eval("importCommands(\"jwf.debugport.internal.debug.commands\")");
            DebugPort.registerInterpreter(interpreter);
            for (String startupCommand : getStartupCommands()) {
                interpreter.eval(startupCommand);
            }

            interpreter.setExitOnEOF(false);
            interpreter.run();
        } catch (Exception e) {
            logError("Error: ", e);
        } finally {
            close();
        }
    }

    @Override
    public void onExit() {
        try {
            mConsole.close();
            DebugPort.deregisterInterpreter(interpreter);
        } catch (IOException e) {
            // m'eh
        }
        close();
    }
}
