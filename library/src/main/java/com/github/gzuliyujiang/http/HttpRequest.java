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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.logger.Logger;

import java.io.File;

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

    public static void enableLog() {
        Logger.useDefaultPrinter(HttpRequest.class.getSimpleName());
    }

    public static void setAdapter(HttpAdapter adapter) {
        getInstance().adapter = adapter;
    }

    /**
     * 须添加依赖：runtimeOnly 'com.lzy.net:okgo:3.0.4'
     */
    public static void useOkGo(Application application) {
        Logger.print("use `com.lzy.net:okgo`");
        setAdapter(new OkGoImpl(application));
    }

    /**
     * 须添加依赖：runtimeOnly 'com.amitshekhar.android:android-networking:1.0.2'
     */
    public static void useFastNetworking(Application application) {
        Logger.print("use `com.amitshekhar.android:android-networking`");
        setAdapter(new FastNetworkingImpl(application));
    }

    public static void doGet(String url, @Nullable HttpAdapter.Params params, @Nullable HttpAdapter.Callback callback) {
        Logger.print("GET " + url + "\nPARAMS " + (params == null ? "EMPTY" : params.toString()));
        getAdapter().doGet(url, params, callback);
    }

    public static void doPost(String url, @Nullable HttpAdapter.Params params, HttpAdapter.Callback callback) {
        Logger.print("POST " + url + "\nPARAMS " + (params == null ? "EMPTY" : params.toString()));
        getAdapter().doPost(url, params, callback);
    }

    public static void upload(String url, @NonNull File file, @Nullable HttpAdapter.Callback callback) {
        Logger.print("POST " + url + "\nFILE " + file);
        getAdapter().upload(url, file, callback);
    }

    public static void cancel(Object tag) {
        Logger.print("cancel request by tag: " + tag);
        getAdapter().cancel(tag);
    }

    public static void cancelAll() {
        Logger.print("cancel all request");
        getAdapter().cancelAll();
    }

    private static HttpAdapter getAdapter() {
        if (getInstance().adapter == null) {
            throw new NullPointerException("Please specify http adapter in your application");
        }
        return getInstance().adapter;
    }

}
