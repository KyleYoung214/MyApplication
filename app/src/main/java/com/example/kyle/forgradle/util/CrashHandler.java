package com.example.kyle.forgradle.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() +
            "/MyAppliaction/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private static class CrashHandlerHolder {
        private static CrashHandler INSTANCE = new CrashHandler();
    }

    private CrashHandler() {
    }

    public static final CrashHandler getInstance() {
        return CrashHandlerHolder.INSTANCE;
    }

    private Context mContext;

    public void init(Context ctx) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = ctx.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        dumpExceptionToSDCard(e);
        e.printStackTrace();

        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted, skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File file = new File((PATH + FILE_NAME + time + FILE_NAME_SUFFIX));

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
        } catch (Exception e) {
            Log.e(TAG, "dumpExceptionToSDCard: " + e.getMessage());
        } finally {
            pw.close();
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager
                .GET_ACTIVITIES);
        pw.print("App Version:");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);

        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        pw.print("Model: ");
        pw.println(Build.MODEL);

        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }
}
