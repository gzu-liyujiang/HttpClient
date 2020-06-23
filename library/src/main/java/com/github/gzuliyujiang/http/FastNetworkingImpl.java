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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Method;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.Map;
import java.util.UUID;

/**
 * 参见 https://github.com/amitshekhariitbhu/Fast-Android-Networking
 * <p>
 * Created by liyujiang on 2020/6/23 13:50
 */
@SuppressWarnings("unused")
public class FastNetworkingImpl implements HttpAdapter {

    public FastNetworkingImpl(Application application) {
        AndroidNetworking.initialize(application);
    }

    @Override
    public void doGet(String url, Params params, Callback callback) {
        query(Method.GET, url, params, callback);
    }

    @Override
    public void doPost(String url, Params params, Callback callback) {
        query(Method.POST, url, params, callback);
    }

    private void query(int method, String url, Params params, final Callback callback) {
        ANRequest.DynamicRequestBuilder requestBuilder = AndroidNetworking.request(url, method);
        requestBuilder.setTag(UUID.randomUUID());
        requestBuilder.setUserAgent(USER_AGENT);
        if (params != null) {
            boolean containsCharset = false;
            for (Map.Entry<String, String> entry : params.toHeaderMap().entrySet()) {
                if (entry.getKey().equalsIgnoreCase("charset")) {
                    containsCharset = true;
                }
                requestBuilder.addHeaders(entry.getKey(), entry.getValue());
            }
            if (!containsCharset) {
                requestBuilder.addHeaders("Charset", CHARSET);
            }
            for (Map.Entry<String, String> entry : params.toBodyMap().entrySet()) {
                requestBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        requestBuilder.build().getAsString(new StringRequestListener() {
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

    @Override
    public void cancelAll() {
        AndroidNetworking.cancelAll();
    }

}
