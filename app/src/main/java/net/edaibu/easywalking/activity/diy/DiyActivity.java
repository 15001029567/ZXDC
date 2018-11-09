package net.edaibu.easywalking.activity.diy;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.BaseActivity;
import net.edaibu.easywalking.activity.webview.WebViewActivity;
import net.edaibu.easywalking.adapter.DiyAdapter;
import net.edaibu.easywalking.adapter.MyPagerAdapter;
import net.edaibu.easywalking.bean.BaseBean;
import net.edaibu.easywalking.bean.DiyBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.Constant;
import net.edaibu.easywalking.utils.TimerUtil;
import net.edaibu.easywalking.utils.Util;
import net.edaibu.easywalking.utils.bletooth.SendBleAgreement;
import net.edaibu.easywalking.view.CircleImageView;
import net.edaibu.easywalking.view.MyGridView;
import net.edaibu.easywalking.view.RoundProgressBar;
import java.util.ArrayList;
import java.util.List;

/**
 * 炫轮
 * 201711142022
 * Created by Administrator on 2017/11/6 0006.
 */

public class DiyActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener {

    private ImageView imgLeftBtn,imgRightBtn;
    private CircleImageView circleImageView,imgLeft,imgRight;
    private ViewPager viewPager;
    /**
     * 图片的view
     */
    private List<View> vList=new ArrayList<>();
    //是否触动onTouch事件
    private boolean isTouch=false;
    /**
     * 拖动图片到轮子的动画
     */
    private ObjectAnimator animatorY=null,animatorX=null;
    private AnimatorSet animatorSet= new AnimatorSet();
    /**
     * 车轮加载进度条
     */
    private RoundProgressBar roundProgressBarLeft,roundProgressBarRight;
    /**
     * 计时器
     */
    private TimerUtil timerUtil,timerUtil2;
    //用户炫轮模板编号
    private String userTemplateId;
    //炫轮模板数据
    private List<DiyBean.templateListBean> templateListAll=new ArrayList<>();
    private int total;
    public final static String ACTION_SEND_DIY_DATA="net.edaibu.adminapp.ACTION_SEND_DIY_DATA";
    //先后轮的图片id
    private String leftWheel,rightWheel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy);
        initView();
        //查询炫轮模板
        getDiyList();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        imgLeftBtn=(ImageView)findViewById(R.id.img_left_btn);
        imgRightBtn=(ImageView)findViewById(R.id.img_right_btn);
        imgLeft=(CircleImageView)findViewById(R.id.img_left);
        imgRight=(CircleImageView)findViewById(R.id.img_right);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        circleImageView=(CircleImageView)findViewById(R.id.img_dazzle);
        roundProgressBarLeft=(RoundProgressBar)findViewById(R.id.roundProgressBar_left);
        roundProgressBarRight=(RoundProgressBar)findViewById(R.id.roundProgressBar_right);
        imgLeftBtn.setOnClickListener(this);
        imgRightBtn.setOnClickListener(this);
        findViewById(R.id.rel_ad_help).setOnClickListener(this);
        findViewById(R.id.lin_back).setOnClickListener(this);
    }



    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            clearTask();
            switch (msg.what){
                //查询炫轮模板
                case HandlerConstant.GET_DIY_LIST_SUCCESS:
                    DiyBean diyBean= (DiyBean) msg.obj;
                    if(null==diyBean){
                        break;
                    }
                    if(diyBean.isSussess() && diyBean.getData()!=null){
                        //设置炫轮模板
                        setViewPager(diyBean.getData().getTemplateList());
                        //加载已经设置过的炫轮图片
                        setDazzleImg(diyBean.getData().getTemplateDeatil());
                        //获取用户炫轮模板编号
                        if(diyBean.getData().getUserTemplate()!=null){
                            userTemplateId=diyBean.getData().getUserTemplate().getId();
                        }
                    }else{
                        showMsg(diyBean.getMsg());
                    }
                    break;
                //更新车轮
                case HandlerConstant.SET_DIY_SUCCESS:
                    final BaseBean baseBean= (BaseBean) msg.obj;
                    if(null==baseBean){
                        break;
                    }
                    if(baseBean.isSussess()){

                    }else{
                        showMsg(baseBean.getMsg());
                    }
                    break;
                case HandlerConstant.REQUST_ERROR:
                    showMsg(getString(R.string.http_error));
                    break;
                case HandlerConstant.GET_DATA_ERROR:
                    showMsg(getString(R.string.Data_parsing_error_please_try_again_later));
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 设置炫轮模板数据
     */
    private void setViewPager(List<DiyBean.templateListBean> templateList){
        if(templateList.size()==0){
            return;
        }
        templateListAll.addAll(templateList);
        boolean is=true;
        int num=0;
        while (is){
            if((templateListAll.size()-num)>=8){
                MyGridView myGridView=new MyGridView(mContext);
                myGridView.setNumColumns(4);
                myGridView.setAdapter(new DiyAdapter(mContext, templateListAll.subList(num,num+=8)));
                vList.add(myGridView);
            }else{
                if((templateListAll.size()-num)>0){
                    MyGridView myGridView=new MyGridView(mContext);
                    myGridView.setNumColumns(4);
                    myGridView.setAdapter(new DiyAdapter(mContext, templateListAll.subList(num,templateListAll.size())));
                    vList.add(myGridView);
                }
                is=false;
                viewPager.setAdapter(new MyPagerAdapter(vList));
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    public void onPageScrolled(int i, float v, int i1) {

                    }
                    public void onPageSelected(int i) {
                        index=i;
                        clickViewPager(i);
                    }
                    public void onPageScrollStateChanged(int i) {

                    }
                });
            }
        }
        total=templateListAll.size()/8;
        if(templateListAll.size()%8>0){
            total++;
        }
        if(total>1){
            imgRightBtn.setImageDrawable(getResources().getDrawable(R.mipmap.right_btn_pre));
        }
    }


    /**
     * 加载已经设置过的炫轮图片
     */
    private void setDazzleImg(List<DiyBean.templateDeatilBean> templateDeatil){
        if(templateDeatil==null){
            return;
        }
        if(templateDeatil.size()>0){
            for(int i=0;i<templateDeatil.size();i++){
                if(templateDeatil.get(i).getBikeWheelNum()==1){
                    leftWheel=templateDeatil.get(i).getWheelNumber();
                    Glide.with(mContext).load(getImgUrl(templateDeatil.get(i).getWheelId())).error(R.mipmap.xl_loding_fault).into(imgLeft);
                }else{
                    rightWheel=templateDeatil.get(i).getWheelNumber();
                    Glide.with(mContext).load(getImgUrl(templateDeatil.get(i).getWheelId())).error(R.mipmap.xl_loding_fault).into(imgRight);
                }
            }
        }
    }


    /**
     * 根据炫轮模板id获取图片路径
     * @param id
     * @return
     */
    private String getImgUrl(String id){
        String imgUrl = null;
        for(int i=0;i<templateListAll.size();i++){
            if(templateListAll.get(i).getId().equals(id)){
                imgUrl=templateListAll.get(i).getImgUrl();
                break;
            }
        }
        return imgUrl;
    }

    private int index=0;
    public void onClick(View v) {
        switch (v.getId()){
            //左滑动
            case R.id.img_left_btn:
                index--;
                clickViewPager(index);
                break;
            //右滑动
            case R.id.img_right_btn:
                index++;
                clickViewPager(index);
                break;
            //使用帮助
            case R.id.rel_ad_help:
                 Intent intent=new Intent(getApplicationContext(), WebViewActivity.class);
                 intent.putExtra("type",15);
                 startActivity(intent);
                 break;
            case R.id.lin_back:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * viewpager点击事件
     */
    private void clickViewPager(int num){

        if(index<0){
            ++index;
            return;
        }
        if(num==total){
            --index;
            return;
        }
        viewPager.setCurrentItem(num);
        if(num==0){
            imgLeftBtn.setImageDrawable(getResources().getDrawable(R.mipmap.left_btn_nor));
            imgRightBtn.setImageDrawable(getResources().getDrawable(R.mipmap.right_btn_pre));
        }
        if(num>0){
            imgLeftBtn.setImageDrawable(getResources().getDrawable(R.mipmap.left_btn_pre));
            imgRightBtn.setImageDrawable(getResources().getDrawable(R.mipmap.right_btn_pre));
        }
        if(num==(total-1)){
            imgRightBtn.setImageDrawable(getResources().getDrawable(R.mipmap.right_btn_nor));
        }

    }


    /**
     * 拖动图片
     * x：选中图片的x坐标
     * y：选中图片的y坐标
     * imgLeftX：左轮子的x坐标
     * imgLeftY：左轮子的y坐标
     * imgRightX：右轮子的x坐标
     * imgRightY：右轮子的y坐标
     * nextX：移动时的图片x坐标
     * nextY：移动时的图片y坐标
     * @param imageView
     */
    int x,y,imgLeftX,imgLeftY,imgRightX,imgRightY,nextY,nextX;
    public void setDazzle(final ImageView imageView,final String templateId){
        // 确保蓝牙在设备上可以开启
        if(Constant.PLAY_STATUS==1 || Constant.PLAY_STATUS==2){
            if(!SendBleAgreement.getInstance().isEnabled(DiyActivity.this)){
                return;
            }
        }

        int[] location = new int[2];
        //获取选中图片的坐标
        imageView.getLocationInWindow (location);
        x = location[0];
        y = (location[1]- Util.getStatusBarHeight(mContext));

        //获取前轮的坐标
        imgLeft.getLocationInWindow (location);
        imgLeftX = location[0];
        imgLeftY= (location[1]- Util.getStatusBarHeight(mContext));

        //获取后轮的坐标
        imgRight.getLocationInWindow (location);
        imgRightX = location[0];
        imgRightY= (location[1]- Util.getStatusBarHeight(mContext));

        Util.setLayout(circleImageView,x,y);
        circleImageView.setTag(templateId);
        mHandler.postDelayed(new Runnable() {
            public void run() {
                circleImageView.setImageDrawable(imageView.getDrawable());
            }
        },200);

        nextX=x;
        nextY=y;
        isTouch=true;
    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                circleImageView.setX(event.getRawX()-circleImageView.getWidth()/2);
                circleImageView.setY(event.getRawY()-circleImageView.getHeight()/2- Util.getStatusBarHeight(mContext));
                nextY=(int)circleImageView.getY();
                nextX=(int)circleImageView.getX();
                break;
            case MotionEvent.ACTION_UP:
                boolean b=false,c=false,e=false,f=false;
                //左轮判断
                if(nextX>imgLeftX){
                    if(nextX-imgLeftX<(v.getWidth()/3)){
                        b=true;
                    }
                }else{
                    if(imgLeftX-nextX<(v.getWidth()/3)){
                        b=true;
                    }
                }

                if(nextY>imgLeftY){
                    if(nextY-imgLeftY<(v.getWidth()/3)){
                        c=true;
                    }
                }else{
                    if(imgLeftY-nextY<(v.getWidth()/3)){
                        c=true;
                    }
                }


                //右轮判断
                if(nextX>imgRightX){
                    if(nextX-imgRightX<(v.getWidth()/3)){
                        e=true;
                    }
                }else{
                    if(imgRightX-nextX<(v.getWidth()/3)){
                        e=true;
                    }
                }

                if(nextY>imgRightY){
                    if(nextY-imgRightY<(v.getWidth()/3)){
                        f=true;
                    }
                }else{
                    if(imgRightY-nextY<(v.getWidth()/3)){
                        f=true;
                    }
                }

                if(b && c){
                    animatorY = ObjectAnimator.ofFloat(circleImageView, "y", nextY, imgLeftY);
                    animatorX = ObjectAnimator.ofFloat(circleImageView, "x", nextX, imgLeftX);
                }else if(e && f){
                    animatorY = ObjectAnimator.ofFloat(circleImageView, "y", nextY, imgRightY);
                    animatorX = ObjectAnimator.ofFloat(circleImageView, "x", nextX, imgRightX);
                }else{
                    animatorY = ObjectAnimator.ofFloat(circleImageView, "y", nextY, y);
                    animatorX = ObjectAnimator.ofFloat(circleImageView, "x", nextX, x);
                }
                animatorSet.playTogether(animatorX, animatorY);
                animatorSet.setDuration(200);
                animatorSet.start();
                isTouch=false;

                if(b && c){
                    imgLeft.setImageDrawable(circleImageView.getDrawable());
                    updateProgress(1);
                }else if(e && f){
                    imgRight.setImageDrawable(circleImageView.getDrawable());
                    updateProgress(2);
                }else{
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            circleImageView.setImageDrawable(null);
                        }
                    },200);
                }
                break;
            default:
                break;
        }
        return false;
    }


    /**
     * 设置扇形进度条
     * @param x
     * @param y
     */
    private int progressLeft,progressRight;
    private void updateProgress(int bikeWheelNum){
        animatorY = ObjectAnimator.ofFloat(circleImageView, "y", nextY, y);
        animatorX = ObjectAnimator.ofFloat(circleImageView, "x", nextX, x);
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.setDuration(0);
        animatorSet.start();
        circleImageView.setImageDrawable(null);
        if(circleImageView.getTag()!=null){
            final String templateId=circleImageView.getTag().toString();
            //设置扇形加载动画
            if(bikeWheelNum==1){
                startLeftProgress();
            }else{
                startRightProgress();
            }
            //更新已选旋轮
            updateDiy(templateId,bikeWheelNum);
        }
    }


    /**
     * 发送广播
     */
    private void sendBroadcast(String leftWheel,String rightWheel ,String duration){
        Intent intent=new Intent(ACTION_SEND_DIY_DATA);
        intent.putExtra("leftWheel",leftWheel);
        intent.putExtra("rightWheel",rightWheel);
        intent.putExtra("duration",duration);
        sendBroadcast(intent);
    }


    private void startLeftProgress(){
        stopTimer1();
        progressLeft=100;
        roundProgressBarLeft.setVisibility(View.VISIBLE);
        timerUtil=new TimerUtil(0, 400, new TimerUtil.TimerCallBack() {
            public void onFulfill() {
                if(progressLeft>0){
                    progressLeft-=5;
                    roundProgressBarLeft.setProgress(progressLeft);
                }else{
                    roundProgressBarLeft.setVisibility(View.GONE);
                    stopTimer1();
                }
            }
        });
        timerUtil.start();
    }

    private void startRightProgress(){
        stopTimer2();
        progressRight=100;
        roundProgressBarRight.setVisibility(View.VISIBLE);
        timerUtil2=new TimerUtil(0, 400, new TimerUtil.TimerCallBack() {
            public void onFulfill() {
                if(progressRight>0){
                    progressRight-=5;
                    roundProgressBarRight.setProgress(progressRight);
                }else{
                    roundProgressBarRight.setVisibility(View.GONE);
                    stopTimer2();
                }
            }
        });
        timerUtil2.start();
    }


    /**
     * 查询炫轮模板
     */
    private void getDiyList(){
        showProgress(getString(R.string.loading),true);
        HttpMethod.getDiyList(mHandler);
    }


    /**
     * 更新已选旋轮
     * @param templateId
     * @param bikeWheelNum
     */
    private void updateDiy(String templateId,int bikeWheelNum){
        HttpMethod.setDiy(userTemplateId,templateId,bikeWheelNum,mHandler);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isTouch){
            return onTouch(circleImageView, ev);
        }
        return super.dispatchTouchEvent(ev);
    }


    private void stopTimer1(){
        if(timerUtil!=null){
            timerUtil.stop();
        }
    }

    private void stopTimer2(){
        if(timerUtil2!=null){
            timerUtil2.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer1();
        stopTimer2();
        //删除handler中的消息
        removeHandler(mHandler);
    }
}
