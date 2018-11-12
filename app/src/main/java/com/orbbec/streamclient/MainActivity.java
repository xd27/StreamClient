package com.orbbec.streamclient;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.IOException;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private SurfaceHolder holder;
    //解码器
    private MediaCodecUtil codecUtil;
    //读取文件解码线程
    private MediaCodecThread thread;
    //文件路径
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.h264";
    private boolean useInternet = false;

    private SurfaceView sv;
    private Button btn_start;
    private Button btn_stop;
    private Button btn_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ButterKnife.bind(this);
        sv = (SurfaceView)findViewById(R.id.surfaceView1);
        btn_start = (Button)findViewById(R.id.button);
        btn_stop = (Button)findViewById(R.id.button2);
        btn_connect = (Button)findViewById(R.id.btn_connect);

        sv = (SurfaceView)findViewById(R.id.surfaceView1);
        initSurface();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread == null) {
                    thread = new MediaCodecThread(codecUtil, path, useInternet);
                    thread.start();
                }
                }

        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread != null) {
                    thread.stopThread();
                }
            }
        });
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useInternet = true;
            }
        });
    }

    //初始化播放相关
    private void initSurface() {
        holder = sv.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (codecUtil == null) {
                    codecUtil = new MediaCodecUtil(holder);
                    codecUtil.startCodec();
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (codecUtil != null) {
                    codecUtil.stopCodec();
                    codecUtil = null;
                }
                if (thread != null) {
                    thread.stopThread();
                }
            }
        });
    }


}