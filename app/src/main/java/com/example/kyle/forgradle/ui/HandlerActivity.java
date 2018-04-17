package com.example.kyle.forgradle.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.example.kyle.forgradle.IMyAidlInterface;
import com.example.kyle.forgradle.R;
import com.example.kyle.forgradle.entity.EntityParcelable;

import java.util.Arrays;
import java.util.Random;


public class HandlerActivity extends AppCompatActivity {
    private static final String TAG = "HandlerActivity";

    private TextView mShowTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        Log.i(TAG, "onCreate: " + Thread.currentThread().getName());

        mShowTv = (TextView) findViewById(R.id.tv_show);

        SparseArray<String> sparseArray = new SparseArray<>();
        for (int i = 0; i < 5; i++) {
            sparseArray.append(i, "StringVal" + i);
        }

        ArrayMap<Integer, String> arrayMap = new ArrayMap<>();
        for (int i = 0; i < 5; i++) {
            arrayMap.put(i, "StringVal" + i);
        }

        entityParcelable = new EntityParcelable();
        entityParcelable.setValues("from Activity", 1.2, 0, false);

    }

    static class MyTask extends AsyncTask<String, Integer, String> {
        int counter = 0;
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            threadLocal.set("fadfasfasf");
            counter = 0;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute: " + s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: " + values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "doInBackground: " + Arrays.toString(strings));
            publishProgress(15);
            try {
                Thread.sleep(120 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "finish, " + strings[0];
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Intent in = new Intent();
        in.setClassName(HandlerActivity.this, "com.example.kyle.forgradle.service" + "" + ""
                + ".MyAIDLService");
        in.setPackage("com.example.kyle.forgradle.service");
        in.setAction("com.example.kyle.forgradle.service");
        bindService(in, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(conn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onLruClick(View view) {
        int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
        int cacheSize = maxMemory / 8;

        LruCache<String, Bitmap> imgCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public void onTaskOOMClick(View view) {
        while (true) {
            MyTask myTask = new MyTask();
            myTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "haha" + counter++);
        }
    }


    public void onTaskClick(View view) {
        MyTask myTask = new MyTask();
        myTask.execute("haha");
        counter = 0;
    }

    public void onTask2Click(View view) {
        MyTask myTask = new MyTask();
        myTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "haha" + counter++);
    }

    public void onTask3Click(View view) {
        MyTask myTask = new MyTask();
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "haha" + counter++);
    }

    private EntityParcelable entityParcelable;
    private IMyAidlInterface myAIDLService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAIDLService = IMyAidlInterface.Stub.asInterface(service);

            Log.e(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: ");
            counter = 0;
            myAIDLService = null;
        }
    };

    public void onHelloAIDL(View view) {
        if (myAIDLService != null) {
            try {
                String str = myAIDLService.helloAIDL();
                Log.e(TAG, "onHelloAIDL: " + str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {

        }
    }


    public void onUpdateServerInfo(View view) {
        if (myAIDLService != null) {
            try {
                entityParcelable.setString("from activity" + counter++);
                Log.i(TAG, "before c->s: " + entityParcelable.toString());
                myAIDLService.updateServerInfo(entityParcelable);
                Log.i(TAG, "after c->s: " + entityParcelable.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onUpdateFromServerInfo(View view) {
        if (myAIDLService != null) {
            try {
                Log.i(TAG, "before s->c: " + entityParcelable.toString());
                myAIDLService.dealWithClientInfo(entityParcelable);
                Log.i(TAG, "after s->c: " + entityParcelable.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onInfoChange(View view) {
        if (myAIDLService != null) {
            try {
                entityParcelable.setString("activity info change " + counter++);
                Log.i(TAG, "before s<->c: " + entityParcelable.toString());
                myAIDLService.useSameInfo(entityParcelable);
                Log.i(TAG, "after s<->c: " + entityParcelable.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onViewStubShow(View view) {
        try {
            ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub);
            View inflateView = viewStub.inflate();
            Button button = (Button) inflateView.findViewById(R.id.btn_view_stub);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: clicked!");
                }
            });

            TextView textView = (TextView) findViewById(R.id.tv_view_stub);

            Log.i(TAG, "new view id is view_stub_finish: " + (inflateView.getId() == R.id
                    .view_stub_finish));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    HandlerThread handlerThread = null;
    Handler testHandler;
    int threadCount = 0;

    public void onHandlerThreadStart(View view) {
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
            testHandler = null;
            counter = 0;
        }
        handlerThread = new HandlerThread("test handler thread " + threadCount++);
        handlerThread.start();

        testHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handlerMessage: " + msg.obj + ", " + Thread.currentThread().toString());
            }
        };
    }

    public void onSendHandlerThreadMsg(View view) {
        if (testHandler != null) {
            testHandler.sendMessage(testHandler.obtainMessage(1, counter++));
        }
    }

    public void onHandlerThreadEnd(View view) {
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
            testHandler = null;
            counter = 0;
        }
    }


    public void onHandlerStart(View view) {
        if (looperThread != null) {
            looperThread.handler.sendEmptyMessage(1001);

            counter = 0;
            looperThread = null;
        }
        looperThread = new LooperThread();
        looperThread.start();
    }

    public void onSendMsg(View view) {
        if (looperThread != null) {
            looperThread.handler.sendMessage(looperThread.handler.obtainMessage(1, "counter: " +
                    "" + counter++));
        }
    }

    public void onHandlerEnd(View view) {
        if (looperThread != null) {
            looperThread.handler.sendEmptyMessage(1001);

            counter = 0;
            looperThread = null;
        } else {
            Log.i(TAG, "onHandlerEnd: already end.");
        }
    }

    private LooperThread looperThread = null;
    private int counter = 0;

    private static class LooperThread extends Thread {

        public Handler handler;

        @Override
        public void run() {
            Looper.prepare();

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Log.i(TAG, "callback handlerMessage: " + msg.obj + ", " + Thread
                            .currentThread().toString());

                    if (msg.what == 1001) {
                        Log.e(TAG, "quit looper");
                        Looper.myLooper().quit();
                    }
                    return false;
                }
            });

            Looper.loop();
        }
    }


}
