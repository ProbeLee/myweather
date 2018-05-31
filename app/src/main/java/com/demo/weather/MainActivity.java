package com.demo.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 和风天气申请的key
 * http://guolin.tech/api/weather?cityid=CN101190401&key=4da56c0e6de946b7b990f3d4bd21c4f7
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
