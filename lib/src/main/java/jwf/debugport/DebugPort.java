package jwf.debugport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Provides access for application to set object that can be accessed from debugging console.
 * Use DebugPort.set("object", someObject).
 */

public class DebugPort {

    static HashMap<String, Object> map = new HashMap<>();
    static ArrayList<Interpreter> interpreters = new ArrayList<>();

    public static void set(String name, Object value) {
        map.put(name, value);
        for (Interpreter interpreter: interpreters) {
            try {
                interpreter.set(name, value);
            } catch (EvalError evalError) {
                evalError.printStackTrace();
            }
        }
    }

    public static void registerInterpreter(Interpreter interpreter) {
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entri = iterator.next();
            try {
                interpreter.set(entri.getKey(), entri.getValue());
            } catch (EvalError evalError) {
                evalError.printStackTrace();
            }
        }
        interpreters.add(interpreter);
    }

    public static void deregisterInterpreter(Interpreter interpreter) {
        interpreters.remove(interpreter);
    }

}
