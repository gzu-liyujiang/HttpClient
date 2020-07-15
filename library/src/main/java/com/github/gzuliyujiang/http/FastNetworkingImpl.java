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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.BuildConfig;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Method;
import com.androidnetworking.common.RequestBuilder;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.Map;

/**
 * 参见 https://github.com/amitshekhariitbhu/Fast-Android-Networking
 * <p>
 * Created by liyujiang on 2020/6/23 13:50
 */
@SuppressWarnings("unused")
class FastNetworkingImpl implements HttpAdapter {
    private Application application;

    public FastNetworkingImpl(Application application) {
        AndroidNetworking.initialize(application, Utils.buildOkHttpClient(null));
        this.application = application;
    }

    @Override
    public void doGet(String url, Params params, Callback callback) {
        query(Method.GET, url, params, callback);
    }

    @Override
    public void doPost(String url, Params params, Callback callback) {
        query(Method.POST, url, params, callback);
    }

    @Override
    public void upload(String url, @NonNull MultipartParams params, @Nullable Callback callback) {
        query(Method.POST, url, params, callback);
    }

    private void query(int method, String url, Params params, final Callback callback) {
        if (params instanceof MultipartParams) {
            MultipartParams multipartParams = (MultipartParams) params;
            //noinspection rawtypes
            ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(url);
            addHeaderAndQuery(builder, params);
            builder.addMultipartFileList("file", multipartParams.toFiles());
            getAsString(builder.build(), callback);
            return;
        }
        ANRequest.DynamicRequestBuilder builder = AndroidNetworking.request(url, method);
        if (params != null) {
            addHeaderAndQuery(builder, params);
            if (params instanceof FormParams) {
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                FormParams formParams = (FormParams) params;
                builder.setContentType("application/x-www-form-urlencoded");
                builder.addStringBody(formParams.toBodyString());
            } else if (params instanceof JsonParams) {
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                JsonParams jsonParams = (JsonParams) params;
                builder.setContentType("application/json");
                builder.addStringBody(jsonParams.toBodyJson());
            }
        }
        getAsString(builder.build(), callback);
    }

    private void getAsString(ANRequest<?> request, Callback callback) {
        request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onError(ANError anError) {
                if (callback != null) {
                    callback.onError(anError.getErrorCode(), anError);
                }
            }
        });
    }

    private void addHeaderAndQuery(@NonNull RequestBuilder builder, @NonNull Params params) {
        builder.setTag(params.getTag());
        String ua = Utils.getDefaultUserAgent(application, "FastNetworking/" + BuildConfig.VERSION_NAME);
        builder.setUserAgent(ua);
        for (Map.Entry<String, String> entry : params.toHeaderMap().entrySet()) {
            builder.addHeaders(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : params.toBodyMap().entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void cancel(Object tag) {
        AndroidNetworking.cancel(tag);
    }

    @Override
    public void cancelAll() {
        AndroidNetworking.cancelAll();
    }

}
