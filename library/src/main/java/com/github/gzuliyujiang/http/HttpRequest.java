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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.logger.Logger;

import java.io.File;
import java.util.List;

/**
 * Created by liyujiang on 2020/6/23.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "UnusedReturnValue"})
public class HttpRequest {
    private static final String MESSAGE = "Please add dependency `runtimeOnly 'com.lzy.net:okgo:3.0.4'`" +
            " or `runtimeOnly 'com.amitshekhar.android:android-networking:1.0.2'` in your app/build.gradle";
    private static volatile HttpRequest instance;
    private HttpAdapter adapter;

    private HttpRequest() {
        super();
        try {
            Class.forName(com.lzy.okgo.OkGo.class.getName());
            adapter = new OkGoImpl(Utils.getApplication());
        } catch (Throwable e) {
            // ClassNotFoundException | NoClassDefFoundError
            try {
                Class.forName(com.androidnetworking.AndroidNetworking.class.getName());
                adapter = new FastNetworkingImpl(Utils.getApplication());
            } catch (Throwable ignore) {
            }
        }
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

    /**
     * 可选添加依赖：runtimeOnly `com.orhanobut:logger:latest.version`
     */
    public static void enableLog() {
        Logger.enableDefaultPrinter(HttpRequest.class.getSimpleName());
    }

    public static void setAdapter(HttpAdapter adapter) {
        getInstance().adapter = adapter;
    }

    public static void doGet(String url, @Nullable Params params, @Nullable Callback callback) {
        getAdapter().doGet(url, params, callback);
    }

    public static void doPost(String url, @Nullable Params params, Callback callback) {
        getAdapter().doPost(url, params, callback);
    }

    public static void upload(String url, @NonNull List<File> files, @Nullable Callback callback) {
        doPost(url, new MultipartParams(files), callback);
    }

    public static void cancel(Object tag) {
        getAdapter().cancel(tag);
    }

    public static void cancelAll() {
        getAdapter().cancelAll();
    }

    private static HttpAdapter getAdapter() {
        HttpAdapter adapter = getInstance().adapter;
        if (adapter == null) {
            throw new RuntimeException(MESSAGE);
        }
        return adapter;
    }

}
