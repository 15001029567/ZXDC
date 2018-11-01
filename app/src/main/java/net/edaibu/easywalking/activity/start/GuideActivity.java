package net.edaibu.easywalking.activity.start;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.BaseActivity;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.utils.SPUtil;
import java.util.ArrayList;

/**
 * 导航页
 */
public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;
    private ImageView imgBtn;
    //用来存放导航图片实例
    private ArrayList<ImageView> imageViews;
    //导航页资源
    private int[] images = new int[]{
            R.mipmap.guide_1,
            R.mipmap.guide_2,
            R.mipmap.guide_3,
            R.mipmap.guide_4,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MyApplication.spUtil.getBoolean(SPUtil.IS_FIRST_OPEN)){
            setClass(WellcomeActivity.class);
            finish();
        }
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imgBtn=(ImageView)findViewById(R.id.img_ag_btn);
        imageViews = new ArrayList<>();
        //初始化导航页面
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(images[i]);
            imageViews.add(iv);
        }
        //为ViewPager添加适配器
        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //从设置页跳转不显示按钮
                if(i==3){
                    imgBtn.setVisibility(View.VISIBLE);
                    imgBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            setClass(WellcomeActivity.class);
                            MyApplication.spUtil.addBoolean(SPUtil.IS_FIRST_OPEN, true);
                            finish();
                        }
                    });
                }else{
                    imgBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    //PagerAdapter有四个方法
    class MyAdapter extends PagerAdapter {
        //返回导航页的个数
        @Override
        public int getCount() {
            return images.length;
        }

        //判断是否由对象生成
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //加载页面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViews.get(position);
            container.addView(iv);
            return iv;
        }

        //移除页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
