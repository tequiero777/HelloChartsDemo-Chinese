package com.tianjian.hellochartsdemo_chinese.ui.activity.other;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tianjian.hellochartsdemo_chinese.R;
import com.tianjian.hellochartsdemo_chinese.ui.activity.base.BaseActivity;
import com.tianjian.hellochartsdemo_chinese.ui.activity.scene.WeatherActivity;


/**
 * @author xiarui 2016.09.08
 * @description 应用场景页面
 */
public class UseSceneActivity extends BaseActivity {

    private ImageView titleImage,weatherImage;

    private CardView weatherCard;
    private CardView asqCard;
    private CardView countCard;
    private CardView moreCard;

    @Override
    public int getLayoutId() {
        return R.layout.activity_use_scene;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        titleImage = (ImageView) findViewById(R.id.iv_use_title);

        weatherCard = (CardView)findViewById(R.id.cv_weather_info);
        asqCard = (CardView) findViewById(R.id.cv_asq);
        countCard = (CardView) findViewById(R.id.cv_flow_count);
        moreCard = (CardView) findViewById(R.id.cv_wait_add);

        weatherImage = (ImageView) findViewById(R.id.iv_weather_info);
    }

    @Override
    public void initData() {
        Glide.with(this).load(R.mipmap.heng_4).into(titleImage);
    }

    @Override
    public void initListener() {
        weatherCard.setOnClickListener(this);
        asqCard.setOnClickListener(this);
        countCard.setOnClickListener(this);
        moreCard.setOnClickListener(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.cv_weather_info:
//                startActivity(new Intent(this, WeatherActivity.class));
//                break;
                Intent i1 = new Intent(this, WeatherActivity.class);
                startActivity(i1, ActivityOptions.makeSceneTransitionAnimation(this, weatherImage, "weather").toBundle());
                break;
            default:
                Toast.makeText(this, "暂未添加此功能", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
