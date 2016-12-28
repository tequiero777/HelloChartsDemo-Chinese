package com.tianjian.hellochartsdemo_chinese.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianjian.hellochartsdemo_chinese.R;
import com.tianjian.hellochartsdemo_chinese.bean.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import static android.R.attr.lines;

/**
 * Recycler adapter
 *
 * @author Qiushui
 */
public class WeatherRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    //列表项数量
    private static final int ITEM_COUNT = 2;

    /*========== 列表项样式 ==========*/
    private static final int TYPE_BASIC = 0;
    private static final int TYPE_TEMP = 1;
    private JSONObject jsonObject;
    /*=========== 数据相关 ==========*/
    private LineChartData mLineData;                    //图表数据
    private int numberOfLines = 2;                      //图上折线/曲线的显示条数
    private int maxNumberOfLines = 2;                   //图上折线/曲线的最多条数
    private int numberOfPoints = 6;                    //图上的节点数

    /*=========== 状态相关 ==========*/
    private boolean isHasAxes = true;                   //是否显示坐标轴
    private boolean isHasAxesNames = true;              //是否显示坐标轴名称
    private boolean isHasLines = true;                  //是否显示折线/曲线
    private boolean isHasPoints = true;                 //是否显示线上的节点
    private boolean isFilled = false ;                   //是否填充线下方区域
    private boolean isHasPointsLabels = false;          //是否显示节点上的标签信息
    private boolean isCubic = false;                    //是否是立体的
    private boolean isPointsHasSelected = false;        //设置节点点击后效果(消失/显示标签)
    private boolean isPointsHaveDifferentColor = false;         //节点是否有不同的颜色
    private ValueShape pointsShape = ValueShape.CIRCLE; //点的形状(圆/方/菱形)

    public static String[] dayStrs;

    public WeatherRecyclerAdapter(Context context,JSONObject jsonObject) {
        this.context = context;
        this.jsonObject = jsonObject;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BASIC) {
            return new BasicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_info_basic, parent, false));
        } else {
            return new TempViewHolder(LayoutInflater.from(context).inflate(R.layout.item_info_temp, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 1) {
            setInitialLineDatas((TempViewHolder) holder);

        }
    }

    /**
     * 设置初始化线性图数据
     *
     * @param holder
     */
    private void setInitialLineDatas(TempViewHolder holder) {
        int numValues = 6;                      //7个值 注意与定义的X轴数量相同
        Gson gson = new Gson();
        Weather weather = gson.fromJson(jsonObject.toString(), new TypeToken<Weather>(){}.getType());
        dayStrs = new String[]{weather.getData().getYesterday().getDate().split("日")[1],"今天",weather.getData().getForecast().get(1).getDate().split("日")[1],weather.getData().getForecast().get(2).getDate().split("日")[1],weather.getData().getForecast().get(3).getDate().split("日")[1],weather.getData().getForecast().get(4).getDate().split("日")[1]};
        List<AxisValue> axisValues = new ArrayList<>();

        //设置默认值 都为0
        for (int i = 0; i < numValues; ++i) {
            axisValues.add(new AxisValue(i).setLabel(dayStrs[i]));
        }

        //设置线
        List<Line> lines = new ArrayList<>();
        //循环将每条线都设置成对应的属性
        for (int i = 0; i < numberOfLines; ++i) {
            if(i==0){
                //节点的值
                List<PointValue> values = new ArrayList<>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    if(j==0){
                        values.add(new PointValue(j, Float.parseFloat(weather.getData().getYesterday().getHigh().replace("高温","").replace("℃","").trim())));
                    }else{
                        values.add(new PointValue(j, Float.parseFloat(weather.getData().getForecast().get(j-1).getHigh().replace("高温","").replace("℃","").trim())));
                    }

                }

            /*========== 设置线的一些属性 ==========*/
                Line line = new Line(values);               //根据值来创建一条线
                line.setColor(ChartUtils.COLORS[i]);        //设置线的颜色
                line.setShape(pointsShape);                 //设置点的形状
                line.setHasLines(isHasLines);               //设置是否显示线
                line.setHasPoints(isHasPoints);             //设置是否显示节点
                line.setCubic(isCubic);                     //设置线是否立体或其他效果
                line.setFilled(isFilled);                   //设置是否填充线下方区域
                line.setHasLabels(true);       //设置是否显示节点标签
                //设置节点点击的效果
                line.setHasLabelsOnlyForSelected(isPointsHasSelected);
                //如果节点与线有不同颜色 则设置不同颜色
                if (isPointsHaveDifferentColor) {
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                lines.add(line);
            }else{
                //节点的值
                List<PointValue> values = new ArrayList<>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    if(j==0){
                        values.add(new PointValue(j, Float.parseFloat(weather.getData().getYesterday().getLow().replace("低温","").replace("℃","").trim())));
                    }else{
                        values.add(new PointValue(j, Float.parseFloat(weather.getData().getForecast().get(j-1).getLow().replace("低温","").replace("℃","").trim())));
                    }

                }

            /*========== 设置线的一些属性 ==========*/
                Line line = new Line(values);               //根据值来创建一条线
                line.setColor(ChartUtils.COLORS[i]);        //设置线的颜色
                line.setShape(pointsShape);                 //设置点的形状
                line.setHasLines(isHasLines);               //设置是否显示线
                line.setHasPoints(isHasPoints);             //设置是否显示节点
                line.setCubic(isCubic);                     //设置线是否立体或其他效果
                line.setFilled(isFilled);                   //设置是否填充线下方区域
                line.setHasLabels(isHasPointsLabels);       //设置是否显示节点标签
                line.setStrokeWidth(1);
                line.setPointRadius(3);
                //设置节点点击的效果
                line.setHasLabelsOnlyForSelected(isPointsHasSelected);
                //如果节点与线有不同颜色 则设置不同颜色
                if (isPointsHaveDifferentColor) {
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                lines.add(line);
            }

        }

        //对数据进行一些设置 类似Line Chart
        mLineData = new LineChartData(lines);
        mLineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setLineColor(Color.parseColor("#ffffff")).setTextColor(Color.parseColor("#ffffff")));
        mLineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3).setName("摄氏度").setLineColor(Color.parseColor("#ffffff")).setTextColor(Color.parseColor("#ffffff")));
        holder.dailyChart.setLineChartData(mLineData);
        holder.dailyChart.setViewportCalculationEnabled(false);

        //设置到窗口上
        Viewport v = new Viewport(0, 40, 6, -10);   //防止曲线超过范围 边界保护
        holder.dailyChart.setMaximumViewport(v);
        holder.dailyChart.setCurrentViewport(v);
        holder.dailyChart.setZoomType(ZoomType.HORIZONTAL);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BASIC;
        } else {
            return TYPE_TEMP;
        }
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder {
        BasicViewHolder(View itemView) {
            super(itemView);
            TextView tempText = (TextView) itemView.findViewById(R.id.tv_basic_temp);
            TextView weatherText = (TextView) itemView.findViewById(R.id.tv_basic_weather);
            TextView windText = (TextView) itemView.findViewById(R.id.tv_basic_wind);
            TextView windPowerText = (TextView) itemView.findViewById(R.id.tv_basic_wind_power);
            TextView humPowerText = (TextView) itemView.findViewById(R.id.tv_basic_hum_power);
            TextView flPowerText = (TextView) itemView.findViewById(R.id.tv_basic_fl_power);
            TextView infoText = (TextView) itemView.findViewById(R.id.tv_basic_fl_info);

            Gson gson = new Gson();
            Weather weather = gson.fromJson(jsonObject.toString(), new TypeToken<Weather>(){}.getType());

            tempText.setText(weather.getData().getWendu()+"℃");
            weatherText.setText(weather.getData().getCity()+" | "+weather.getData().getForecast().get(0).getType());
            windText.setText(weather.getData().getForecast().get(0).getFengxiang());
            windPowerText.setText(weather.getData().getForecast().get(0).getFengli());
            humPowerText.setText(weather.getData().getForecast().get(0).getHigh().replace("高温",""));
            flPowerText.setText(weather.getData().getForecast().get(0).getLow().replace("低温",""));
            infoText.setText(weather.getData().getGanmao());

        }
    }

    private class TempViewHolder extends RecyclerView.ViewHolder {
        LineChartView dailyChart;           //每日预报
        ColumnChartView hourlyChart;        //每小时预报

        TempViewHolder(View itemView) {
            super(itemView);
            dailyChart = (LineChartView) itemView.findViewById(R.id.lcv_weather_daily);
            hourlyChart = (ColumnChartView) itemView.findViewById(R.id.ccv_weather_hourly);
        }
    }

}
