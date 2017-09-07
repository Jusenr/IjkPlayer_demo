package com.hx.ijkplayer_demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hx.ijkplayer_demo.common.PlayerManager;

public class MainActivity extends AppCompatActivity implements PlayerManager.PlayerStateListener
        , PlayerManager.OnInfoListener {
    public static final String TAG = MainActivity.class.getSimpleName();
//    private String videoPath = "http://pl.youku.com/playlist/m3u8?ctype=12&ep=cCaVGE6OUc8H4ircjj8bMiuwdH8KXJZ0vESH%2f7YbAMZuNaHQmjbTwg%3d%3d&ev=1&keyframe=1&oip=996949050&sid=241273717793612e7b085&token=3825&type=hd2&vid=XNzk2NTI0MzMy";
    private String videoPath = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";

    private PlayerManager player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (videoPath == null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            } else {
                videoPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/video_boot.mp4";
                initPlayer();
            }
        } else {
            initPlayer();
        }
    }

    private void initPlayer() {
        player = new PlayerManager(this);
        player.setFullScreenOnly(true);
        player.setScaleType(PlayerManager.SCALETYPE_WRAPCONTENT);
        player.setDefaultRetryTime(1000);
        player.live(false);
        player.setPlayerStateListener(this);
        player.play(videoPath);

        findViewById(R.id.tv_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
            }
        });
        findViewById(R.id.tv_zt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.onPause();
            }
        });
        findViewById(R.id.tv_jx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });
        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPlayer();
            }
        });

        int currentPosition = player.getCurrentPosition();
        Log.i(TAG, "initPlayer: currentPosition=" + currentPosition);
        int duration = player.getDuration();
        Log.i(TAG, "initPlayer: duration=" + duration);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (player.gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "onComplete: ");
    }

    @Override
    public void onError() {
        Log.e(TAG, "onError: ");
    }

    @Override
    public void onLoading() {
        Log.w(TAG, "onLoading: ");
    }

    @Override
    public void onPlay() {
        Log.v(TAG, "onPlay: ");
    }

    @Override
    public void onInfo(int what, int extra) {
        Log.i(TAG, "onInfo: extra=" + extra);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    videoPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/video_boot.mp4";
                    initPlayer();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
