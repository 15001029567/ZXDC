/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.edaibu.easywalking.activity.scan;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.BaseActivity;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.persenter.scan.ScanPersenter;
import net.edaibu.easywalking.persenter.scan.ScanPersenterImpl;
import net.edaibu.easywalking.utils.scan.cameras.CameraManager;
import net.edaibu.easywalking.utils.scan.decoding.InactivityTimer;
import net.edaibu.easywalking.utils.scan.decoding.ScanActivityHandler;
import net.edaibu.easywalking.utils.scan.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

/**
 * 扫描二维码
 */
public class ScanActivity extends BaseActivity implements SurfaceHolder.Callback, OnClickListener,ScanPersenter {

    private ScanPersenterImpl scanPersenter;
    private EditText et;
    private LinearLayout linAs, linSetCode;
    //编码存储
    private char[] arr;
    private ScanActivityHandler handler;// 消息中心
    private ViewfinderView viewfinderView;// 绘制扫描区域
    private boolean hasSurface;// 控制调用相机属性
    private Vector<BarcodeFormat> decodeFormats;// 存储二维格式的数组
    private String characterSet;// 字符集
    private InactivityTimer inactivityTimer;// 相机扫描刷新timer
    private MediaPlayer mediaPlayer;// 播放器
    private boolean playBeep;// 声音布尔
    private static final float BEEP_VOLUME = 0.10f;// 声音大小
    private boolean vibrate;// 振动布尔
    private Handler mHandler=new Handler();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_scan);
        //初始化MVP接口
        initPersenter();
        CameraManager.init(this);
        inactivityTimer = new InactivityTimer(this);
        initView();
    }

    /**
     * 初始化MVP接口
     */
    private void initPersenter(){
        scanPersenter=new ScanPersenterImpl(ScanActivity.this,this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        LinearLayout linCode = (LinearLayout) findViewById(R.id.lin_aqs_code);
        linAs = (LinearLayout) findViewById(R.id.lin_as);
        linSetCode = (LinearLayout) findViewById(R.id.lin_setcode);
        final TextView tv1 = (TextView) findViewById(R.id.t1);
        final TextView tv2 = (TextView) findViewById(R.id.t2);
        final TextView tv3 = (TextView) findViewById(R.id.t3);
        final TextView tv4 = (TextView) findViewById(R.id.t4);
        final TextView tv5 = (TextView) findViewById(R.id.t5);
        final TextView tv6 = (TextView) findViewById(R.id.t6);
        final TextView tv7 = (TextView) findViewById(R.id.t7);
        et = (EditText) findViewById(R.id.editHide);
        findViewById(R.id.lin_back).setOnClickListener(this);
        findViewById(R.id.lin_code_back).setOnClickListener(this);
        findViewById(R.id.btn_as_submit).setOnClickListener(this);
        findViewById(R.id.lin_aqs_light).setOnClickListener(this);
        linCode.setOnClickListener(this);
        //输入车辆编号
        et.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void afterTextChanged(Editable s) {
                arr = s.toString().toCharArray();
                tv1.setText(null);
                tv2.setText(null);
                tv3.setText(null);
                tv4.setText(null);
                tv5.setText(null);
                tv6.setText(null);
                tv7.setText(null);
                if (TextUtils.isEmpty(s.toString())) {
                    tv1.setBackground(getResources().getDrawable(R.drawable.scan_share));
                }
                for (int i = 0; i < arr.length; i++) {
                    if (i == 0) {
                        tv1.setText(String.valueOf(arr[0]));
                        tv1.setBackgroundColor(getResources().getColor(R.color.main_color));
                        tv2.setBackground(getResources().getDrawable(R.drawable.scan_share));
                    } else if (i == 1) {
                        tv2.setText(String.valueOf(arr[1]));
                        tv2.setBackgroundColor(getResources().getColor(R.color.main_color));
                        tv3.setBackground(getResources().getDrawable(R.drawable.scan_share));
                    } else if (i == 2) {
                        tv3.setText(String.valueOf(arr[2]));
                        tv3.setBackgroundColor(getResources().getColor(R.color.main_color));
                        tv4.setBackground(getResources().getDrawable(R.drawable.scan_share));
                    } else if (i == 3) {
                        tv4.setText(String.valueOf(arr[3]));
                        tv4.setBackgroundColor(getResources().getColor(R.color.main_color));
                        tv5.setBackground(getResources().getDrawable(R.drawable.scan_share));
                    } else if (i == 4) {
                        tv5.setText(String.valueOf(arr[4]));
                        tv5.setBackgroundColor(getResources().getColor(R.color.main_color));
                        tv6.setBackground(getResources().getDrawable(R.drawable.scan_share));
                    } else if (i == 5) {
                        tv6.setText(String.valueOf(arr[5]));
                        tv6.setBackgroundColor(getResources().getColor(R.color.main_color));
                        tv7.setBackground(getResources().getDrawable(R.drawable.scan_share));
                    } else if (i == 6) {
                        tv7.setText(String.valueOf(arr[6]));
                        tv7.setBackgroundColor(getResources().getColor(R.color.main_color));
                        //隐藏键盘
                        lockKey(et);
                    }
                }
            }
        });
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        resumeScan();
        scanPersenter.scanResult(result);
    }

    /**
     * 重复扫描
     */
    public void resumeScan() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (null != handler) {
                    handler.restartPreviewAndDecode();
                }
            }
        }, 3000);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            //开关灯
            case R.id.lin_aqs_light:
                scanPersenter.openLight(true);
                break;
            //输入车辆编号
            case R.id.lin_aqs_code:
                 linSetCode.setVisibility(View.VISIBLE);
                 linAs.setVisibility(View.GONE);
                 //自动打开软键盘
                 et.setFocusable(true);
                 et.setFocusableInTouchMode(true);
                 et.requestFocus();
                 openKey(et);
                break;
            //设置编码返回
            case R.id.lin_code_back:
                linSetCode.setVisibility(View.GONE);
                linAs.setVisibility(View.VISIBLE);
                //隐藏键盘
                lockKey(et);
                break;
            //设置编码后提交
            case R.id.btn_as_submit:
                 scanPersenter.setBikeCode(arr);
                 break;
            case R.id.lin_back:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 获得车辆信息
     * @param bikeBean
     */
    public void getBikeBean(BikeBean bikeBean) {
        Intent intent=new Intent();
        intent.putExtra("bikeBean",bikeBean);
        setResult(1,intent);
        finish();
    }


    /**
     * 初始化相机
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception ioe) {
            return;
        }
        if (handler == null) {
            handler = new ScanActivityHandler(this, decodeFormats, characterSet);
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 声音设置
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    /**
     * 结束后的声音
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    /**
     * 展示加载滚动条
     */
    public void showLoding(String msg) {
        showProgress(msg,true);
    }

    /**
     * 关闭加载滚动条
     */
    public void closeLoding() {
        clearTask();
    }

    /**
     * 展示Toast数据
     * @param msg
     */
    public void showToast(String msg) {
        showMsg(msg);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 初始化相机画布
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        // 声音
        playBeep = true;
        // 初始化音频管理器
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        // 振动
        vibrate = true;
    }



    @Override
    protected void onPause() {
        // 停止相机 关闭闪光灯
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        clearTask();
        // 停止相机扫描刷新timer
        inactivityTimer.shutdown();
        scanPersenter.onDestory();
        super.onDestroy();
    }

}
