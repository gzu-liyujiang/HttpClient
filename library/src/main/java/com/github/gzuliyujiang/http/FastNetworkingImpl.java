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
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Method;
import com.androidnetworking.common.RequestBuilder;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.gzuliyujiang.logger.Logger;

import java.io.File;
import java.util.Map;

/**
 * 参见 https://github.com/amitshekhariitbhu/Fast-Android-Networking
 * <p>
 * Created by liyujiang on 2020/6/23 13:50
 */
@SuppressWarnings("unused")
public class FastNetworkingImpl implements HttpAdapter {

    public FastNetworkingImpl(Application application) {
        Logger.print("use `com.amitshekhar.android:android-networking`");
        AndroidNetworking.initialize(application, Utils.buildOkHttpClient(null));
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
    public void upload(String url, @NonNull File file, @Nullable Callback callback) {
        query(Method.POST, url, new FileParams(file), callback);
    }

    private void query(int method, String url, Params params, final Callback callback) {
        if (params instanceof MultipartParams) {
            MultipartParams multipartParams = (MultipartParams) params;
            //noinspection rawtypes
            ANRequest.MultiPartBuilder builder = AndroidNetworking.upload(url);
            addHeaders(builder, params);
            builder.addMultipartFile(multipartParams.toFileMap());
            builder.build().getAsString(new StringRequestListener() {
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
            return;
        }
        ANRequest.DynamicRequestBuilder builder = AndroidNetworking.request(url, method);
        if (params != null) {
            addHeaders(builder, params);
            boolean resetBodyParams = false;
            if (params instanceof FormParams) {
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                resetBodyParams = true;
                FormParams formParams = (FormParams) params;
                builder.setContentType("application/x-www-form-urlencoded");
                builder.addStringBody(formParams.toBodyString());
            } else if (params instanceof JSONParams) {
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                resetBodyParams = true;
                JSONParams jsonParams = (JSONParams) params;
                builder.setContentType("application/json");
                builder.addStringBody(jsonParams.toBodyJson());
            } else if (params instanceof StreamParams) {
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                resetBodyParams = true;
                StreamParams streamParams = (StreamParams) params;
                builder.setContentType("application/octet-stream");
                builder.addByteBody(streamParams.toBodyBytes());
            } else if (params instanceof FileParams) {
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                resetBodyParams = true;
                FileParams fileParams = (FileParams) params;
                builder.setContentType("application/octet-stream");
                builder.addFileBody(fileParams.toFile());
            }
            if (!resetBodyParams) {
                for (Map.Entry<String, String> entry : params.toBodyMap().entrySet()) {
                    builder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        builder.build().getAsString(new StringRequestListener() {
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

    private void addHeaders(@NonNull RequestBuilder builder, @NonNull Params params) {
        builder.setTag(params.tag);
        boolean containsCharset = false;
        for (Map.Entry<String, String> entry : params.toHeaderMap().entrySet()) {
            if (entry.getKey().equalsIgnoreCase("charset")) {
                containsCharset = true;
            }
            builder.addHeaders(entry.getKey(), entry.getValue());
        }
        if (!containsCharset) {
            builder.addHeaders("Charset", CHARSET);
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
