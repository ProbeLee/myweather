package com.demo.weather;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.weather.gson.HeWeatherBean;
import com.demo.weather.util.HttpUtils;
import com.demo.weather.util.T;
import com.demo.weather.util.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.swipe_layout)
    public SwipeRefreshLayout swipeLayout;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.title_city)
    TextView titleCity;
    @BindView(R.id.title_update_time)
    TextView titleUpdateTime;
    @BindView(R.id.degree_text)
    TextView degreeText;
    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;
    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;
    @BindView(R.id.aqi_text)
    TextView aqiText;
    @BindView(R.id.pm25_text)
    TextView pm25Text;
    @BindView(R.id.comfort_text)
    TextView comfortText;
    @BindView(R.id.car_wash_text)
    TextView carWashText;
    @BindView(R.id.sport_text)
    TextView sportText;
    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;
    @BindView(R.id.banner_id)
    ImageView bannerId;
    @BindView(R.id.nav_button)
    Button navButton;
    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        状态栏透明 5.0以上
        if (Build.VERSION.SDK_INT >= 21) {
//            拿到当前活动的decorView
            View decorView = getWindow().getDecorView();
//            改变当前活动的UI显示   两个属性就表示活动的布局会显示在状态栏上
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            状态栏设置成透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        sp();
    }

    //   缓存天气
    private void sp() {
        swipeLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weather = sharedPreferences.getString("weather", null);
        String bing_pic = sharedPreferences.getString("bing_pic", null);
        if (bing_pic != null) {
            Glide.with(this).load(bing_pic).into(bannerId);
        } else {
            loadBingPic();
        }
        if (weather != null) {
            HeWeatherBean HeWeatherBean = Utility.handleWeatherResponse(weather);
            mWeatherId = HeWeatherBean.getBasic().getId();
            showWeatherInfo(HeWeatherBean);

        } else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);

        }
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }

    //    背景图
    private void loadBingPic() {
        String bing_pic = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkHttpRequest(bing_pic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.showShort(WeatherActivity.this, "更新图片失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("bing_pic", string);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(string).into(bannerId);
                    }
                });

            }
        });
    }

    //    天气参数
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=4da56c0e6de946b7b990f3d4bd21c4f7";
        HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        T.showShort(WeatherActivity.this, "获取失败");
                        swipeLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                final HeWeatherBean bean = Utility.handleWeatherResponse(string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bean != null && "ok".equals(bean.getStatus())) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            edit.putString("weather", string);
                            edit.apply();
                            showWeatherInfo(bean);

                        } else {
                            T.showShort(WeatherActivity.this, "获取失败");
                        }
                        swipeLayout.setRefreshing(false);
                    }
                });

            }
        });
    }

    private void showWeatherInfo(HeWeatherBean bean) {
        String cityName = bean.getBasic().getCity();
        String loc = bean.getBasic().getUpdate().getLoc().split(" ")[1];
        String degree = bean.getNow().getTmp() + "℃";
        String wetherInfo = bean.getNow().getCond().getTxt();
        titleCity.setText(cityName);
        titleUpdateTime.setText(loc);
        degreeText.setText(degree);
        weatherInfoText.setText(wetherInfo);
        forecastLayout.removeAllViews();
        for (HeWeatherBean.DailyForecastBean forecastBean : bean.getDaily_forecast()) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecastBean.getDate());
            infoText.setText(forecastBean.getCond().getTxt_d());
            maxText.setText(forecastBean.getTmp().getMax());
            minText.setText(forecastBean.getTmp().getMin());
            forecastLayout.addView(view);

        }
        if (bean.getAqi() != null) {
            aqiText.setText(bean.getAqi().getCity().getAqi());
            pm25Text.setText(bean.getAqi().getCity().getPm25());
        }
        String cmfort = "舒适度" + bean.getSuggestion().getComf().getTxt();
        String carWash = "洗车指数" + bean.getSuggestion().getCw().getTxt();
        String sport = "运动建议" + bean.getSuggestion().getSport().getTxt();
        comfortText.setText(cmfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.nav_button)
    public void onViewClicked() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
