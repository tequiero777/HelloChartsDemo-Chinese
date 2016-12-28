package com.tianjian.hellochartsdemo_chinese.ui.activity.scene;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.tianjian.hellochartsdemo_chinese.R;
import com.tianjian.hellochartsdemo_chinese.adapter.WeatherRecyclerAdapter;
import com.tianjian.hellochartsdemo_chinese.ui.activity.base.BaseActivity;
import com.tianjian.hellochartsdemo_chinese.ui.view.BlurredView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import static android.R.attr.button;


/**
 * @author xiarui 2016.09.09
 * @description 实战场景 天气预报
 */
public class WeatherActivity extends BaseActivity {

    /*========== 控件相关 ===========*/
    private BlurredView weatherBView;           //背景模糊图
    private RecyclerView weatherRView;          //滑动列表


    /*========== 其他 ===========*/
    private int mScrollerY;                     //滚动距离
    private int mAlpha;                         //透明值
    private static final String weather_url = "http://wthrcdn.etouch.cn/weather_mini?city=%E9%87%8D%E5%BA%86";
    private JSONObject jo;
    @Override
    public int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        weatherBView = (BlurredView) findViewById(R.id.bv_weather);
        weatherRView = (RecyclerView) findViewById(R.id.rv_weather);
        weatherRView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void initData() {
        new Thread()
        {
            public void run()
            {
                try {
                    jo = readJsonFromUrl(weather_url);
                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public Handler mHandler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    weatherRView.setAdapter(new WeatherRecyclerAdapter(WeatherActivity.this,jo));
                    System.out.println(jo.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void initListener() {
        weatherRView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;                       //滚动距离
                if (Math.abs(mScrollerY) > 1000) {      //根据滚动距离控制模糊程度 滚动距离是模糊程度的十倍
                    mAlpha = 100;
                } else {
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                weatherBView.setBlurredLevel(mAlpha);    //设置透明度等级
            }
        });
    }

    @Override
    public void processClick(View v) {

    }
}

