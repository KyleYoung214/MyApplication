package com.example.kyle.forgradle.util;

import android.app.Activity;

import java.util.Stack;


final public class ActivityStack {
    private static Stack<Activity> activityStack;
    private static final ActivityStack instance = new ActivityStack();

    private ActivityStack() {
    }

    public static ActivityStack getInstance() {
        return instance;
    }

    public int getCount() {
        return activityStack.size();
    }


    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity bottomActivity() {
        if (activityStack == null) {
            throw new NullPointerException("Activity stack is Null,your Activity must extend BaseActivity");
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.firstElement();
        return activity;
    }

    public Activity topActivity() {
        if (activityStack == null) {
            throw new NullPointerException("Activity stack is Null,your Activity must extend BaseActivity");
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            // activity.finish();//此处不用finish
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishActivitiesExcept(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivities() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                (activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }


    public void killAppProcess() {
        try {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public void exitApp() {
        finishAllActivities();
        killAppProcess();
    }
}