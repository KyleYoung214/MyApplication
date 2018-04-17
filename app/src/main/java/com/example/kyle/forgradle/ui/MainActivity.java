package com.example.kyle.forgradle.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyle.forgradle.Info;
import com.example.kyle.forgradle.InfoAdapter;
import com.example.kyle.forgradle.InfoItemDecoration;
import com.example.kyle.forgradle.R;
import com.example.kyle.forgradle.com.example.kyle.retrofit.DemoService;
import com.example.kyle.forgradle.com.example.kyle.retrofit.gson.GankData;
import com.example.kyle.forgradle.com.example.kyle.retrofit.gson.Result;
import com.example.kyle.forgradle.entity.AndroidBook;
import com.example.kyle.forgradle.entity.GankRsp;
import com.example.kyle.forgradle.testview.MyView;
import com.example.kyle.forgradle.testview.SoundWaveView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nick.jartest.JarTest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private LocalBroadcastManager localBroadcastManager;
    private RecyclerView recyclerView;
    private TextView mainTv;
    private Button mAddBtn;
    private Button mMinusBtn;
    private MyView mMyView;
    private int mProgress = 0;

    private SoundWaveView mSoundView;
    private ImageView mFrameImg;

    private boolean mIsRecording = false;
    private AnimationDrawable mStartRecordingAnim;
    private AnimationDrawable mStopRecordingAnim;
    private AnimationDrawable mSoundAnim;
    private AnimationDrawable mNoSoundAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartRecordingAnim = new AnimationDrawable();
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.mic1), 100);
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.mic2), 100);
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.mic3), 100);
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross1), 100);
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross2), 100);
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross3), 100);
        mStartRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross4), 100);
        mStartRecordingAnim.setOneShot(true);


        mStopRecordingAnim = new AnimationDrawable();
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross4), 100);
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross3), 100);
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross2), 100);
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.cross1), 100);
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.mic3), 100);
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.mic2), 100);
        mStopRecordingAnim.addFrame(getResources().getDrawable(R.drawable.mic1), 100);

        mStopRecordingAnim.setOneShot(true);

        mFrameImg = (ImageView) findViewById(R.id.img_frame);
        mFrameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsRecording = !mIsRecording;

                if (mIsRecording) {
                    mFrameImg.setBackground(mStartRecordingAnim);
                    mStartRecordingAnim.start();
                } else {
                    mFrameImg.setBackground(mStopRecordingAnim);
                    mStopRecordingAnim.start();
                }
            }
        });

        mSoundView = (SoundWaveView) findViewById(R.id.sound_view);
        mSoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundView.reset();
            }
        });

        mAddBtn = (Button) findViewById(R.id.btn_add);
        mMinusBtn = (Button) findViewById(R.id.btn_minus);
        mMyView = (MyView) findViewById(R.id.my_view);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress += 10;

                mSoundView.updateAmplitude(mProgress);

                mMyView.setProgress(mProgress);

                //                if (mProgress > 100) {
                //                    mProgress = 100;
                //                }

            }
        });
        mMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress -= 8;

                mSoundView.updateAmplitude(mProgress);

                mMyView.setProgress(mProgress);

                if (mProgress < 0) {
                    mProgress = 0;
                }
            }
        });


        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Info.INFO_ACTION_1);
        intentFilter.addAction(Info.INFO_ACTION_2);
        intentFilter.addAction(Info.INFO_ACTION_3);
        intentFilter.addAction(Info.INFO_ACTION_4);
        intentFilter.addAction(Info.INFO_ACTION_5);
        intentFilter.addAction(Info.INFO_ACTION_6);
        intentFilter.addAction(Info.INFO_ACTION_7);
        intentFilter.addAction(Info.INFO_ACTION_8);
        intentFilter.addAction(Info.INFO_ACTION_9);
        localBroadcastManager.registerReceiver(infoReceiver, intentFilter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                JarTest.printInfo(JarTest.HAHA + ", 1 + 2 = " + JarTest.sum(1, 2));
            }
        });

        mainTv = (TextView) findViewById(R.id.main_tv);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        List<Info> list = new ArrayList<>();
        list.add(new Info("one", "rxTest"));
        list.add(new Info("two", "retrofitTest"));
        list.add(new Info("three", "gsonTest"));
        list.add(new Info("four", "44444444444\n4444444\n444retrofitRxjavaTest"));
        list.add(new Info("five", "55555testLooper"));
        list.add(new Info("six", "66MSG_LOG_WHAT_1"));
        list.add(new Info("seven", "7777777MSG_LOG_WHAT_2"));
        list.add(new Info("eight", "8888MSG_LOG_WHAT_3quit"));
        list.add(new Info("night", "9999999999999999999999"));

        InfoAdapter adapter = new InfoAdapter(list, this);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new InfoItemDecoration(this, InfoItemDecoration
                .VERTICAL_LIST));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(infoReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver infoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Info.INFO_ACTION_1:
                    //                    rxTest();
                    startActivity(new Intent(MainActivity.this, HandlerActivity.class));
//                    startActivity(new Intent(MainActivity.this, SoundBtnActivity.class));
//                    startActivity(new Intent(MainActivity.this, DragViewActivity.class));
                    break;
                case Info.INFO_ACTION_2:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            retrofitTest();
                        }
                    }).start();
                    break;
                case Info.INFO_ACTION_3:
                    gsonTest();
                    break;
                case Info.INFO_ACTION_4:
                    retrofitRxjavaTest();
                    break;
                case Info.INFO_ACTION_5:
                    testLooper();
                    break;
                case Info.INFO_ACTION_6:
                    mTestHandler.obtainMessage(MSG_LOG_WHAT_1, "MSG_LOG_WHAT_1").sendToTarget();
                    break;
                case Info.INFO_ACTION_7:
                    mTestHandler.obtainMessage(MSG_LOG_WHAT_2, "MSG_LOG_WHAT_2").sendToTarget();
                    break;
                case Info.INFO_ACTION_8:
                    mTestHandler.obtainMessage(MSG_LOG_WHAT_3, "MSG_LOG_WHAT_3").sendToTarget();
                    break;
                case Info.INFO_ACTION_9:
                    startActivity(new Intent(MainActivity.this, AnimActivity.class));
                    break;
            }
        }
    };

    private Handler mTestHandler;
    private static final int MSG_LOG_WHAT_1 = 1;
    private static final int MSG_LOG_WHAT_2 = 2;
    private static final int MSG_LOG_WHAT_3 = 3;

    private void testLooper() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "before looper");
                Looper.prepare();
                Log.i(TAG, "into looper");

                mTestHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case MSG_LOG_WHAT_1:
                                Log.i(TAG, "handleMessage: MSG_LOG_WHAT_1" + Thread.currentThread
                                        ().getName());
                                break;
                            case MSG_LOG_WHAT_2:
                                Log.i(TAG, "handleMessage: MSG_LOG_WHAT_2" + Thread.currentThread
                                        ().getName());
                                break;
                            case MSG_LOG_WHAT_3:
                                Log.i(TAG, "handleMessage: MSG_LOG_WHAT_3, quit looper" + Thread
                                        .currentThread().getName());
                                getLooper().quit();
                                break;
                        }
                    }
                };

                Log.e(TAG, "handler obj: " + mTestHandler);

                Looper.loop();
                Log.i(TAG, "after looper");
            }
        });
        thread.start();
    }

    private void rxTest() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("in subscribe " + System.currentTimeMillis());
                Thread.sleep(3 * 1000);
                e.onNext("after 3s the message came");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("in accept " + System.currentTimeMillis());
                mainTv.setText(s);
            }
        });
    }

    private void retrofitRxjavaTest() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory
                .create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl
                (DemoService.BASE_URL).build();
        DemoService service = retrofit.create(DemoService.class);
        service.getAndroidInfoRx(3, 1).subscribeOn(Schedulers.newThread()).flatMap(new Function<GankRsp, ObservableSource<AndroidBook>>() {
            @Override
            public ObservableSource<AndroidBook> apply(GankRsp gankRsp) throws Exception {
                return Observable.fromArray(gankRsp.getResultsArray());
            }
        }).subscribe(new Consumer<AndroidBook>() {
            @Override
            public void accept(AndroidBook androidBook) throws Exception {
                Log.i(TAG, "android book: " + androidBook.toString());
            }
        });
    }

    private void retrofitTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DemoService.BASE_URL).build();
        DemoService service = retrofit.create(DemoService.class);
        Call<ResponseBody> call = service.getAndroidInfo(3, 1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, response.toString() + "\n" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    private void gsonTest() {
        InputStream is = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            is = getAssets().open("json.txt");
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = stringBuilder.toString();

        Gson gson = new Gson();
        GankData gankData = gson.fromJson(json, GankData.class);
        Log.i(TAG, gankData.toString());

        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(json);
        if (root.isJsonObject()) {
            Log.i(TAG, "isJsonObject");
            JsonObject jobj = root.getAsJsonObject();
            JsonPrimitive error = jobj.getAsJsonPrimitive("error");
            if (error.isBoolean()) {
                Log.i(TAG, "error->" + error.getAsBoolean());
            } else {
                Log.e(TAG, "error->not boolean");
            }

            JsonElement resultsE = jobj.get("results");
            if (resultsE.isJsonArray()) {
                JsonArray results = resultsE.getAsJsonArray();
                int size = results.size();
                for (int i = 0; i < size; i++) {
                    JsonElement resultE = results.get(i);
                    if (resultE.isJsonObject()) {
                        JsonObject result = resultE.getAsJsonObject();
                        Result r = new Gson().fromJson(result, Result.class);
                        Log.i(TAG, "result: " + r.toString());
                    }
                }
            }
        } else if (root.isJsonArray()) {
            Log.i(TAG, "isJsonArray");
            JsonArray jArray = root.getAsJsonArray();
        }


        Info test = new Info();
        test.info1 = "test-info1";
        test.info2 = "test-info2";

        Log.i(TAG, gson.toJson(test));
    }

}
