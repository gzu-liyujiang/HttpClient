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

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.webkit.WebSettings;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by liyujiang on 2020/7/6.
 */
@SuppressWarnings("unused")
public class Utils {
    private static Application application;

    public static void setApplication(Application application) {
        Utils.application = application;
    }

    @Nullable
    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public static Application getApplication() {
        if (application != null) {
            return application;
        }
        try {
            // Accessing hidden method Landroid/app/ActivityThread;->getApplication()Landroid/app/Application;
            // or Landroid/app/AppGlobals;->getInitialApplication()Landroid/app/Application; (greylist, JNI, allowed)
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object at = currentActivityThread.invoke(null);
            Class<?> currentActivityThreadClass = Objects.requireNonNull(at).getClass();
            Method getApplication = currentActivityThreadClass.getMethod("getApplication");
            return (Application) getApplication.invoke(at);
        } catch (Throwable ignore) {
            return null;
        }
    }

    public static OkHttpClient buildOkHttpClient(@Nullable CookieJar cookieJar) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.readTimeout(8, TimeUnit.SECONDS);
        builder.writeTimeout(8, TimeUnit.SECONDS);
        builder.followRedirects(false);
        builder.followSslRedirects(true);
        builder.addInterceptor(new LoggingInterceptor());
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            UnsafeTrustManager unsafeTrustManager = new UnsafeTrustManager();
            sslContext.init(null, new TrustManager[]{unsafeTrustManager}, new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), unsafeTrustManager);
        } catch (Exception ignore) {
        }
        builder.hostnameVerifier(new UnsafeHostnameVerifier());
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        return builder.build();
    }

    public static String getDefaultUserAgent() {
        String ua;
        try {
            ua = WebSettings.getDefaultUserAgent(getApplication());
        } catch (Throwable ignore) {
            ua = "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " +
                    Build.MODEL + " Build/" + Build.ID + "; wv) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Safari/537.36";
        }
        return ua.concat(" HttpRequest/" + BuildConfig.VERSION_NAME);
    }

}
