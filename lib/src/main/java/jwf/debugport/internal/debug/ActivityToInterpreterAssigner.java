package jwf.debugport.internal.debug;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Assignes "activity" and "activities" values to interpreter for easy access.
 */
public class ActivityToInterpreterAssigner implements Application.ActivityLifecycleCallbacks {

    public static ActivityToInterpreterAssigner INSTANCE;
    Application application;
    ArrayList<Activity> activities = new ArrayList<>();
    Activity activity;
    Interpreter interpreter;

    public ActivityToInterpreterAssigner(Application application) {
        this.application = application;
        application.registerActivityLifecycleCallbacks(this);
    }


    public void assign(Interpreter interpreter) {
        this.interpreter = interpreter;
        try {
            interpreter.set("activities", activities);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
        assignActivity();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.activity = activity;
        assignActivity();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        this.activity = null;
        assignActivity();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }

    private void assignActivity() {
        if (interpreter != null) {
            try {
                interpreter.set("activity", activity);
            } catch (EvalError evalError) {
                evalError.printStackTrace();
            }
        }
    }
}
