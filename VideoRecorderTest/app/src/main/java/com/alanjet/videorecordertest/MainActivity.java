package com.alanjet.videorecordertest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "MainActivity";
    private SurfaceView mSurfaceview;
    private ImageButton mBtnStartStop;
    private ImageButton mBtnSet;
    private ImageButton mBtnShowFile;
    private boolean mStartedFlg = false;
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private Camera myCamera;
    private Camera.Parameters myParameters;
    private Camera.AutoFocusCallback mAutoFocusCallback=null;
    private boolean isView = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAutoFocusCallback=new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if(success){
                Log.i(TAG, "myAutoFocusCallback: success...");
            }else {
                Log.i(TAG, "myAutoFocusCallback: failure...");
            }
        }
    };
    requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

    // 设置横屏显示
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    // 选择支持半透明模式,在有surfaceview的activity中使用。
    getWindow().setFormat(PixelFormat.TRANSLUCENT);

    setContentView(R.layout.activity_main);
    mSurfaceview  = (SurfaceView)findViewById(R.id.capture_surfaceview);
    mBtnStartStop = (ImageButton) findViewById(R.id.ib_stop);
    mBtnSet= (ImageButton) findViewById(R.id.capture_imagebutton_setting);
    mBtnShowFile= (ImageButton) findViewById(R.id.capture_imagebutton_showfiles);
    mBtnShowFile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,ShowVideoActivity.class);
            startActivity(intent);
            finish();
        }
    });
    mBtnSet.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this,"相机设置待开发~",Toast.LENGTH_SHORT).show();
//            Intent intent=new Intent(MainActivity.this,SetVideoActivity.class);
//            startActivity(intent);
        }
    });

    SurfaceHolder holder = mSurfaceview.getHolder();// 取得holder

    holder.addCallback(this); // holder加入回调接口

    // setType必须设置，要不出错.
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

}

    /**
     * 获取系统时间
     * @return
     */
    public static String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "" + year + (month + 1 )+ day + hour + minute + second;
        Log.d(TAG, "date:" + date);

        return date;
    }

    /**
     * 获取SD path
     * @return
     */
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }
    public void initCamera()
    {
        if(myCamera == null && !isView)
        {
            myCamera = Camera.open();
            Log.i(TAG, "camera.open");
        }
        if(myCamera != null && !isView) {
            try {

                myParameters = myCamera.getParameters();
                myParameters.setPreviewSize(1920, 1080);
                myParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                myCamera.setParameters(myParameters);
                myCamera.setPreviewDisplay(mSurfaceHolder);
                myCamera.startPreview();
                isView = true;
//                myCamera.autoFocus(mAutoFocusCallback);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "初始化相机错误",
                        Toast.LENGTH_SHORT).show();
            }
        }
//        if(myCamera != null && isView)
//            myCamera.autoFocus(mAutoFocusCallback);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
        Log.d(TAG, "surfaceChanged 1");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder

        mSurfaceHolder = holder;
        initCamera();
        mBtnStartStop.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!mStartedFlg) {
                    // Start
                    if (mRecorder == null) {
                        mRecorder = new MediaRecorder(); // Create MediaRecorder
                    }
                    try {
                        myCamera.unlock();
                        mRecorder.setCamera(myCamera);
                        // Set audio and video source and encoder
                        // 这两项需要放在setOutputFormat之前
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

//                        // Set output file format
//                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//
//                        // 这两项需要放在setOutputFormat之后
//                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
//                        mRecorder.setVideoSize(1920, 1080);
//                        mRecorder.setVideoFrameRate(12);
                        mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

//                         Set output file path
                        String path = getSDPath();
                        if (path != null) {

                            File dir = new File(path + "/recordtest");
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            path = dir + "/" + getDate() + ".mp4";
                            mRecorder.setOutputFile(path);
                            Log.d(TAG, "bf mRecorder.prepare()");
                            mRecorder.prepare();
                            Log.d(TAG, "af mRecorder.prepare()");
                            Log.d(TAG, "bf mRecorder.start()");
                            mRecorder.start();   // Recording is now started
                            Log.d(TAG, "af mRecorder.start()");
                            mStartedFlg = true;
                            mBtnStartStop.setBackground(getDrawable(R.drawable.rec_stop));
                            Log.d(TAG, "Start recording ...");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // stop
                    if (mStartedFlg) {
                        try {
                            Log.d(TAG, "Stop recording ...");
                            Log.d(TAG, "bf mRecorder.stop(");
                            mRecorder.stop();
                            Log.d(TAG, "af mRecorder.stop(");
                            mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
                            mBtnStartStop.setBackground(getDrawable(R.drawable.rec_start));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mStartedFlg = false; // Set button status flag
                }
            }

        });

        Log.d(TAG, "surfaceChanged 2");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // surfaceDestroyed的时候同时对象设置为null
        mSurfaceview = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;
            Log.d(TAG, "surfaceDestroyed release mRecorder");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

