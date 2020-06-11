package com.emdoor.mybroadcastlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyBroadcastLite";
    //VideoView
    private static final String CLASS_NAME_VIDEOVIEW= "com.emdoor.videoview.MainActivity";
    private static final String PACKAGE_NAME_VIDEOVIEW = "com.emdoor.videoview";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mHandler.removeCallbacks(r);
        mHandler.postDelayed(r, 5000);
    }

    private Handler mHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run !!!!!!!!!! " );
            /*Log.d(TAG, "run time = " + time);
            mHandler.postDelayed(this, 2000);
            time +=1;
            if(time >=10){
                time = 0;
            }*/

            if(!getRunningTasks().equals(PACKAGE_NAME_VIDEOVIEW)) {
                Log.d(TAG, "Top app is not VideoView, going to start it up!" );
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(CLASS_NAME_VIDEOVIEW, PACKAGE_NAME_VIDEOVIEW);
                intent.setComponent(cn);
                if ( getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "VideoView is not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(r);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(r);
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            mHandler.removeCallbacks(r);
            mHandler.postDelayed(r, 5000);
        }
        return super.onTouchEvent(event);

    }

    public String getRunningTasks(){
        String TopTaskName = "unknown";

        Log.d(TAG, "getRunningTasks()");
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices =
                activityManager.getRunningServices(Integer.MAX_VALUE);

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityInfo aInfo = null;
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        for (ActivityManager.RunningTaskInfo task : list) {
            Log.d(TAG, task.baseActivity.getPackageName());
            TopTaskName = task.baseActivity.getPackageName();
            return TopTaskName;
        }
        return TopTaskName;
    }

}
