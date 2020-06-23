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

package com.github.gzuliyujiang.http;

import android.app.Application;

import androidx.annotation.Nullable;

/**
 * Created by liyujiang on 2020/6/23.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "UnusedReturnValue"})
public class HttpRequest {
    private static volatile HttpRequest instance;
    private HttpAdapter adapter;

    private HttpRequest() {
        super();
    }

    private static HttpRequest getInstance() {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }

    public static void setAdapter(HttpAdapter adapter) {
        getInstance().adapter = adapter;
    }

    /**
     * 须添加依赖：runtimeOnly 'com.lzy.net:okgo:3.0.4'
     */
    public static void useOkGo(Application application) {
        setAdapter(new OkGoImpl(application));
    }

    /**
     * 须添加依赖：runtimeOnly 'com.amitshekhar.android:android-networking:1.0.2'
     */
    public static void useFastNetworking(Application application) {
        setAdapter(new FastNetworkingImpl(application));
    }

    public static void doGet(String url, @Nullable HttpAdapter.Params params, @Nullable HttpAdapter.Callback callback) {
        getAdapter().doGet(url, params, callback);
    }

    public static void doPost(String url, @Nullable HttpAdapter.Params params, HttpAdapter.Callback callback) {
        getAdapter().doPost(url, params, callback);
    }

    public static void cancelAll() {
        getAdapter().cancelAll();
    }

    private static HttpAdapter getAdapter() {
        if (getInstance().adapter == null) {
            throw new NullPointerException("Please specify http adapter in your application");
        }
        return getInstance().adapter;
    }

}
