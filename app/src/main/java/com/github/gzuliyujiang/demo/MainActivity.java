/*
 * Copyright (c) 2019-2020 gzu-liyujiang <1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 *     http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 *
 */
package com.github.gzuliyujiang.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.gzuliyujiang.http.HttpAdapter;
import com.github.gzuliyujiang.http.HttpRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressBar progressBar = findViewById(R.id.main_progress);
        final TextView textView = findViewById(R.id.main_text);
        progressBar.setVisibility(View.VISIBLE);
        HttpRequest.doGet("http://wthrcdn.etouch.cn/weather_mini?city=%E8%B4%B5%E9%98%B3", null, new HttpAdapter.Callback() {
            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);
                textView.setText(result);
            }

            @Override
            public void onError(int code, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                textView.setText(Log.getStackTraceString(throwable));
            }
        });
    }

}
