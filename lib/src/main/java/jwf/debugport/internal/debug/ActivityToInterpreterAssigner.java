package jwf.debugport.internal.debug;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Assignes "activity" and "activities" values to interpreter for easy access.
 */
public class ActivityToInterpreterAssigner {

    public static ActivityToInterpreterAssigner INSTANCE;

    Application application;
    ArrayList<Activity> activities = new ArrayList<>();
    Activity activity;
    Interpreter interpreter;

    public ActivityToInterpreterAssigner(Application application) {
        this.application = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            application.registerActivityLifecycleCallbacks(new LifecycleCallbackListener());
        }
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    class LifecycleCallbackListener implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            ActivityToInterpreterAssigner.this.activity = activity;
            assignActivity();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            ActivityToInterpreterAssigner.this.activity = null;
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
