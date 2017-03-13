package com.ggpl.player.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ggpl.player.R;
import com.ggpl.player.adpter.PlayerPopAdpter;
import com.ggpl.player.common.AppConfig;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.manager.DataManager;
import com.ggpl.player.media.IRenderView;
import com.ggpl.player.media.IjkVideoView;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.util.UiScreen;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.ggpl.player.activity.BaseActivity.KEY_OBJ;

/**
 * Created by zhangxiaoming on 2017/2/14.
 */

public class Player2_Activity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final static int MAX_VIDEO_SEEK = 1000;


    private final static int SYSYN = 1001;
    private final static int HINDEN = 1002;


    private final static int DEFAULT_HIDE_TIMEOUT = 8000;


    private Context mContext;

    private Videobean ben;

    private PopupWindow popupWindow;

    private List<Videobean> list;

    private PlayerPopAdpter adpter;

    private boolean isscreen;

    private IjkVideoView videoView;

    private ImageView player_menu;   //选集按钮

    private LinearLayout back_layout;  //返回布局

    private TextView title;   //标题

    private SeekBar palyerseek;

    private TextView endTime, duct_time;  //总时长和当前时长

    private ImageView forword, player, go;   //播放键

    private ImageView sdk;  //锁定

    private RelativeLayout kzqlayout, top, bottom;

    private LinearLayout locklayout;

    private ImageView center_textview;


    private boolean isSk = false;   //是否锁定

    private boolean isv = true;   //控制器是否显示

    private boolean devicezc = false;   //设备是否支持

    private long curPosition;   //当前的进度

    private long mTargetPosition;   //拖动的位置

    private int screenWidthPixels;

    private int heightPixels;

    private boolean mIsForbidOrientation = true;   //是否能翻转,默认不能

    // 屏幕旋转角度监听
    private OrientationEventListener mOrientationListener;

    private int mMaxVolume;

    private float brightness = -1;
    private int volume = -1;

    private boolean iscom;  //视频播放是否完成

    private ProgressBar loading_progress;

    private LinearLayout center_layout;

    // 手势控制
    private GestureDetector mGestureDetector;
    private AudioManager audioManager;
    private Activity mAttachActivity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgindex = msg.what;

            if (msgindex == SYSYN) {   //进度同步
                final int pos = _setProgress();
                if (videoView.isPlaying()) {
                    // 这里会重复发送MSG，已达到实时更新 Seek 的效果
                    iscom = false;
                    msg = obtainMessage(SYSYN);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }

            } else if (msgindex == HINDEN) {

                center_layout.setVisibility(View.GONE);

            }
        }
    };

    public Player2_Activity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        isscreen = AppConfig.getAppConfig(this).GetPlayConfig().isScreen();

        if (isscreen) {
            DebugLog.d("可翻转");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            DebugLog.d("不可翻转");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }


        setContentView(R.layout.player2_activity);
        mAttachActivity = this;
        initView();
        list = DataManager.getIntence(this).getVideoList();
        ListSort(1);
        ben = (Videobean) getIntent().getSerializableExtra(KEY_OBJ);


        PlayerPup();
        initPlayer();

//        enableOrientation();  //shi屏幕能翻转
    }


    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
 if(popupWindow!=null&&popupWindow.isShowing()){
     popupWindow.dismiss();
 }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        IjkMediaPlayer.native_profileEnd();
    }

    public void initView() {
        player_menu = (ImageView) findViewById(R.id.player_menu);
        videoView = (IjkVideoView) findViewById(R.id.video_view);
        back_layout = (LinearLayout) findViewById(R.id.black_layout);
        title = (TextView) findViewById(R.id.play_srce);
        palyerseek = (SeekBar) findViewById(R.id.player_seekbar);
        endTime = (TextView) findViewById(R.id.currentposition);
        duct_time = (TextView) findViewById(R.id.duration);
        palyerseek = (SeekBar) findViewById(R.id.player_seekbar);
        forword = (ImageView) findViewById(R.id.player_forword);
        go = (ImageView) findViewById(R.id.player_go);
        player = (ImageView) findViewById(R.id.play_img);
        sdk = (ImageView) findViewById(R.id.lock_img);
        kzqlayout = (RelativeLayout) findViewById(R.id.kzq_layout);
        top = (RelativeLayout) findViewById(R.id.play_menu_top_layout);
        bottom = (RelativeLayout) findViewById(R.id.play_menu_bottom_layout);
        locklayout = (LinearLayout) findViewById(R.id.lock_layout);
        loading_progress = (ProgressBar) findViewById(R.id.loading_progress);
        center_textview = (ImageView) findViewById(R.id.center_textview);
        center_layout = (LinearLayout) findViewById(R.id.center_layout);

        player_menu.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        go.setOnClickListener(this);
        player.setOnClickListener(this);
        forword.setOnClickListener(this);
        locklayout.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }


    public void initPlayer() {
        title.setText(ben.getTitle() + "");
//        devicezc = true;
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin(getString(R.string.so_string));
            devicezc = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        palyerseek.setMax(MAX_VIDEO_SEEK);
        palyerseek.setOnSeekBarChangeListener(this);

        videoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        videoView.setVideoURI(Uri.parse(ben.getPath()));
        screenWidthPixels = mAttachActivity.getResources().getDisplayMetrics().widthPixels;
        heightPixels = mAttachActivity.getResources().getDisplayMetrics().heightPixels;
        audioManager = (AudioManager) mAttachActivity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        // 触摸控制
        mGestureDetector = new GestureDetector(mAttachActivity, new PlayerGestureListener());


        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                DebugLog.d("setOnInfoListener");
                switch (i) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START: //STATUS_LOADING
                        loading_progress.setVisibility(View.VISIBLE);

                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END: //STATUS_PLAYING
                        loading_progress.setVisibility(View.GONE);

                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //显示下载速度
//                      Toast.show("download rate:" + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START://STATUS_PLAYING
                        loading_progress.setVisibility(View.GONE);
                        break;
                }


                return false;
            }
        });
        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                DebugLog.d("setOnCompletionListener");
                iscom = true;
                player.setSelected(false);
                if (!isv) {
                    setControllerHiden();  //一定显示控制器
                }

                handler.removeMessages(SYSYN);
                endTime.setText(EcodeTime(iMediaPlayer.getDuration()));
                palyerseek.setProgress(MAX_VIDEO_SEEK);
            }
        });
        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                                         @Override
                                         public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                                             DebugLog.d("setOnErrorListener");
                                             Toast.makeText(getApplicationContext(), R.string.player_error, Toast
                                                     .LENGTH_SHORT).show();
                                             finish();

                                             return false;
                                         }
                                     }
        );

        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                DebugLog.d("setOnPreparedListener");
                player.setSelected(true);
                iscom = false;
                duct_time.setText(DataManager.EcodeTime(videoView.getDuration()) + "");

                handler.sendEmptyMessage(SYSYN);
                loading_progress.setVisibility(View.GONE);

                if (isv) {
                    handler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
                }
            }
        });
//        // 屏幕翻转控制
//        mOrientationListener = new OrientationEventListener(mAttachActivity) {
//            @Override
//            public void onOrientationChanged(int orientation) {
//                _handleOrientation(orientation);
//            }
//        };
//        if (mIsForbidOrientation) {
//            // 禁止翻转
//            mOrientationListener.disable();
//        }

        if (!devicezc) {
            DebugLog.d("设备不支持");
            return;
        }

        start();


    }

    /**
     * 设置声音控制显示
     *
     * @param
     */
    private void _setVolumeInfo(String s) {
        DebugLog.d("声音控制");
        if (center_layout.getVisibility() == View.GONE) {
            center_layout.setVisibility(View.VISIBLE);

        }

//        center_textview.setText(s);
        center_textview.setImageResource(R.mipmap.ic_volume_on);
        handler.sendEmptyMessageDelayed(HINDEN, 1500);
//        handler.postDelayed(mHideCenterRunnable, 1500);
    }

    /**
     * 设置亮度控制显示
     *
     * @param
     */
    private void _setlaingduInfo(String s) {

        DebugLog.d("亮度控制");
        if (center_layout.getVisibility() == View.GONE) {
            center_layout.setVisibility(View.VISIBLE);

        }

        center_textview.setImageResource(R.mipmap.ic_brightness);

        handler.sendEmptyMessageDelayed(HINDEN, 1500);

//        handler.postDelayed(mHideCenterRunnable, 1500);
    }


    /**
     * 使能视频翻转
     */
    public void enableOrientation() {
        mIsForbidOrientation = false;
        mOrientationListener.enable();

    }

    /**
     * 处理屏幕翻转
     *
     * @param orientation
     */
    private void _handleOrientation(int orientation) {

        DebugLog.d("翻转--?");
        if (mIsForbidOrientation) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        } else {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

    }

    public void PlayerPup() {

        adpter = new PlayerPopAdpter(list, this);
        final View popview = LayoutInflater.from(this).inflate(R.layout.payer_alert_popu, null);
        RecyclerView recy = (RecyclerView) popview.findViewById(R.id.player_pop_recv);
        TextView item1 = (TextView) popview.findViewById(R.id.player_pop_item1);
        TextView item2 = (TextView) popview.findViewById(R.id.player_pop_item2);
        TextView item3 = (TextView) popview.findViewById(R.id.player_pop_item3);
        TextView item4 = (TextView) popview.findViewById(R.id.player_pop_item4);

        TextView[] textViews = new TextView[]{item1, item2, item3, item4};

        setOnclick(0, textViews);
        setOnclick(1, textViews);
        setOnclick(2, textViews);
        setOnclick(3, textViews);

        adpter.setItemClick(new PlayerPopAdpter.ItemClick() {
            @Override
            public void itemClickListener(Videobean position, int index) {
                stopPlayer();
                ben = position;
//                adpter.setIndex(index);
                play(ben.getPath());
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(adpter);

//        MoveToPosition();

        popupWindow = new PopupWindow(popview, UiScreen.dip2px(this, 266), UiScreen.dip2px(this,
                256));

        popupWindow.setOutsideTouchable(true);   //设置点击屏幕外消失
    }

    public void setOnclick(final int index, final TextView[] textViews) {
        textViews[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSort(index);

                for (TextView textView : textViews) {
                    if (textViews[index] == textView) {
                        textViews[index].setBackgroundColor(getResources().getColor(R.color.press_title_color));
                    } else {
                        textView.setBackgroundColor(getResources().getColor(R.color.transt));
                    }
                }

            }
        });

    }


    public void ListSort(final int index) {

        final Collator collator = Collator.getInstance();
        try {
            Collections.sort(list, new Comparator<Videobean>() {

                public int compare(Videobean o1, Videobean o2) {
                    if (index == 0) {   //按时间排序
                        long time1 = Long.valueOf(o1.getLogtime()) / (1000 * 60 / 60);
                        long time2 = Long.valueOf(o2.getLogtime()) / (1000 * 60 / 60);
                        return (int) (time1 - time2);

                    } else if (index == 1) {  //文件名
                        CollationKey key1 = collator.getCollationKey(o1.getTitle());
                        CollationKey key2 = collator.getCollationKey(o2.getTitle());
                        return key1.compareTo(key2);
                    } else if (index == 2) {  //时长
                        long time1 = Long.valueOf(o1.getTime()) / (1000);
                        long time2 = Long.valueOf(o2.getTime()) / (1000);
                        return (int) (time1 - time2);

                    } else if (index == 3) {  //大小
                        String size1 = o1.getSize().substring(0, o1.getSize().length() - 1);
                        String size2 = o2.getSize().substring(0, o2.getSize().length() - 1);

                        int big1 = (int) (Double.valueOf(size1) * 10);
                        int big2 = (int) (Double.valueOf(size2) * 10);
                        return big1 - big2;
                    }
                    return 0;
                }
            });
            if (adpter != null)
                adpter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        handler.removeCallbacks(mHideBarRunnable);


        if (v == player_menu) {
            if (popupWindow != null && !popupWindow.isShowing()) {
                popupWindow.showAsDropDown(player_menu);
            }
            DebugLog.d("点击");
        } else if (back_layout == v) {
            finish();
        } else if (v == player) {
            _togglePlayStatus();


        } else if (v == go) {
//            if (videoView.isPlaying()) {
//                _onProgressSlide(0.05f);
//            }
//            videoView.stopPlayback();

            ben = getIndexObj();
            DebugLog.d("下一集是否为null-+" + ben);

            if (ben == null) {
                return;
            } else {
                stopPlayer();
                play(ben.getPath());
            }

            start();
        } else if (v == forword) {
            if (videoView.isPlaying()) {
                _onProgressSlide(-0.05f);
            }

        } else if (locklayout == v) {
            if (isSk) {

                sdk.setSelected(false);
                isSk = false;

                isv = false;
                setControllerHiden();


            } else {
                handler.removeCallbacks(mHideBarRunnable);

                sdk.setSelected(true);
                isSk = true;
                isv = false;
                setControllerHiden();

            }


        }
        handler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);

    }


    /**
     * 获取下一集
     *
     * @return
     */
    public Videobean getIndexObj() {

        if (ben == null) {
            return null;
        }
        DebugLog.d("目前播放的视频->" + ben.getTitle());
        Videobean video;
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            Videobean b = list.get(i);
            if (ben.getPath().equals(b.getPath())) {
                index = i;
            }
        }


        DebugLog.d("count-index-" + list.size() + "-" + index);


        if (index < (list.size() - 1)) {
            video = list.get(index + 1);
            DebugLog.d("下一个视频->" + video.getTitle());

        } else {
            video = list.get(0);

        }

        return video;

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        long duration = videoView.getDuration();
        curPosition = videoView.getCurrentPosition();
        // 计算目标位置
        mTargetPosition = (duration * progress) / MAX_VIDEO_SEEK;

        endTime.setText(EcodeTime(mTargetPosition));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        curPosition = videoView.getCurrentPosition();
        handler.removeCallbacks(mHideBarRunnable);
        handler.removeMessages(SYSYN);

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // 视频跳转
        DebugLog.d("拖动停止-->" + mTargetPosition);

        seekTo((int) mTargetPosition);
        mTargetPosition = -1;
        _setProgress();
//
        if (iscom) {
            start();
        }
        handler.sendEmptyMessage(SYSYN);
        handler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
    }


    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;


        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
//            videoView.toggleAspectRatio();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            return super.onDown(e);
        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                DebugLog.d("第一次滑动");
//                handler.postDelayed(mHideCenterRunnable,1500);
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = mOldX > screenWidthPixels * 0.5f;
                firstTouch = false;
            }

            if (toSeek) {

//                    _onProgressSlide(-deltaX / videoView.getWidth());

            } else {
                float percent = deltaY / videoView.getHeight();
                if (volumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {


            float dex = e.getY();
            DebugLog.d("heightPixels-->" + heightPixels);
            DebugLog.d("wid-->" + screenWidthPixels);
            DebugLog.d("单击的Y坐标-->" + dex);

            if (dex >= (heightPixels - UiScreen.dip2px(mAttachActivity, 56)) || dex < UiScreen.dip2px
                    (mAttachActivity, 50)) {
                return true;
            }


            setControllerHiden();

            return true;
        }
    }


    public void setControllerHiden() {

        if (isv) {
            closePop();
            kzqlayout.setVisibility(View.GONE);

            isv = false;
        } else {
            kzqlayout.setVisibility(View.VISIBLE);
            if (isSk) {
                top.setVisibility(View.GONE);
                bottom.setVisibility(View.GONE);
                locklayout.setVisibility(View.VISIBLE);
            } else {

                top.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.VISIBLE);
                locklayout.setVisibility(View.VISIBLE);

            }

            isv = true;


        }

        if (isv && videoView.isPlaying()) {

            handler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
        } else {
            handler.removeCallbacks(mHideBarRunnable);
        }


    }

    public void closePop() {

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {


        DebugLog.d("改变亮度");
        if (brightness < 0) {
            brightness = mAttachActivity.getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }

        DebugLog.d("brightness:" + brightness + ",percent:" + percent);
        WindowManager.LayoutParams lpa = mAttachActivity.getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }

        mAttachActivity.getWindow().setAttributes(lpa);

        _setlaingduInfo("");

    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
//        handler.removeCallbacks(mHideCenterRunnable);
        DebugLog.d("改变声音");
        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0)
                volume = 0;
        }
        DebugLog.d("系统声音-->" + volume);


        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }

        _setVolumeInfo(s);

        DebugLog.d("onVolumeSlide:" + s);
    }

    /**
     * ---------------------------------播放器设置----------------------------
     **/

    /**
     * 隐藏视图Runnable
     */
    private Runnable mHideBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (isv)
                setControllerHiden();  //一定显示控制器
        }
    };

    /**
     * 隐藏隐藏声音提示框
     */
    private Runnable mHideCenterRunnable = new Runnable() {
        @Override
        public void run() {
            if (isv)
//                setControllerHiden();  //一定显示控制器
                center_layout.setVisibility(View.GONE);
        }

    };


    /**
     * 更新进度条
     *
     * @return
     */
    private int   _setProgress() {
        // 视频播放的当前进度
        int position = videoView.getCurrentPosition();
        // 视频总的时长
        int duration = videoView.getDuration();
//        if (position == duration) {
//            handler.removeMessages(SYSYN);
//            palyerseek.setProgress(MAX_VIDEO_SEEK);
//            duct_time.setText(EcodeTime(duration));
//            endTime.setText(EcodeTime(position));
//            return 0;
//        }


//        if (iscom) {
//            handler.removeMessages(SYSYN);
//            palyerseek.setProgress(MAX_VIDEO_SEEK);
//            return 0;
//        }


        if (videoView == null) {
            return 0;
        }

        if (duration > 0) {
            // 转换为 Seek 显示的进度值
            long pos = (long) MAX_VIDEO_SEEK * position / duration;
            palyerseek.setProgress((int) pos);

        }
        // 获取缓冲的进度百分比，并显示在 Seek 的次进度
        int percent = videoView.getBufferPercentage();
        palyerseek.setSecondaryProgress(percent * 10);

        // 更新播放时间
        duct_time.setText(EcodeTime(duration));
        endTime.setText(EcodeTime(position));
        // 返回当前播放进度
        return position;
    }

    /**
     * 快进或者快退滑动改变进度，这里处理触摸滑动不是拉动 SeekBar
     *
     * @param percent 拖拽百分比
     */
    private void _onProgressSlide(float percent) {
        handler.removeMessages(SYSYN);
        int position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        // 单次拖拽最大时间差为100秒或播放时长的1/2
        long deltaMax = Math.min(100 * 1000, duration / 2);
        // 计算滑动时间
        long delta = (long) (deltaMax * percent);
        // 目标位置
        mTargetPosition = delta + position;


        if (mTargetPosition > duration) {
            mTargetPosition = duration;
        } else if (mTargetPosition <= 0) {
            mTargetPosition = 0;
        }
        seekTo((int) mTargetPosition);
        handler.sendEmptyMessage(SYSYN);

    }

    /**
     * 显示控制栏
     *
     * @param timeout 延迟隐藏时间
     */
    private void _showControlBar(int timeout) {

//            _setProgress();

        handler.sendEmptyMessage(SYSYN);


    }

    public void play(String url) {
        title.setText(ben.getTitle() + "");
        if (devicezc) {
            videoView.setVideoPath(url);
            videoView.start();
        }
    }

    /**
     * 跳转
     *
     * @param position 位置
     */
    public void seekTo(int position) {

        videoView.seekTo(position);

    }

    /**
     * 开始播放
     *
     * @return
     */
    public void start() {

        if (!videoView.isPlaying()) {
            player.setSelected(true);
            videoView.start();
            // 更新进度
            handler.sendEmptyMessage(SYSYN);
        }

    }

    public void prasePlayer() {
        videoView.pause();
    }

    public void stopPlayer() {
        videoView.stopPlayback();
    }

    //    /**
//     * 暂停
//     */
//    public void pause() {
//        player.setSelected(false);
//        if (videoView.isPlaying()) {
//            videoView.pause();
//        }
//
//    }
    public void Pause() {
        player.setSelected(false);

        if (videoView.isPlaying()) {
            videoView.pause();

            curPosition = videoView.getCurrentPosition();

        }
    }

    /**
     * 切换播放状态，点击播放按钮时
     */
    private void _togglePlayStatus() {
        if (videoView.isPlaying()) {
            Pause();
        } else {
            start();
        }
    }


    public static String EcodeTime(long time) {


        long min = time / (1000 * 60);

        long ss = time % (1000 * 60) / 1000;

        String timeEcd = "";
        if (min < 10) {
            timeEcd = "0" + min;
        } else {
            timeEcd = "" + min;
        }

        if (ss < 10) {
            timeEcd += ":0" + ss;

        } else {
            timeEcd += ":" + ss;

        }

        return timeEcd;


    }
}
