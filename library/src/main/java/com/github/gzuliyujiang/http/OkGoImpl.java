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

import com.github.gzuliyujiang.logger.Logger;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.BodyRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okgo.utils.OkLogger;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 参见 https://github.com/jeasonlzy/okhttp-OkGo
 * <p>
 * Created by liyujiang on 2018/9/14 13:03
 */
public class OkGoImpl implements HttpAdapter {

    public OkGoImpl(Application application) {
        OkLogger.debug(false);
        // See https://github.com/jeasonlzy/okhttp-OkGo/wiki/Init#%E5%85%A8%E5%B1%80%E9%85%8D%E7%BD%AE
        OkGo.getInstance().init(application)
                .setOkHttpClient(Utils.buildOkHttpClient(new CookieJarImpl(new SPCookieStore(application))))
                .setRetryCount(1)
                .setCacheMode(CacheMode.DEFAULT)
                .addCommonHeaders(new HttpHeaders("User-Agent", USER_AGENT));
    }

    @Override
    public void doGet(String url, Params params, Callback callback) {
        query(OkGo.get(url), params, callback);
    }

    @Override
    public void doPost(String url, Params params, Callback callback) {
        query(OkGo.post(url), params, callback);
    }

    @Override
    public void upload(String url, @NonNull MultipartParams params, Callback callback) {
        query(OkGo.post(url), params, callback);
    }

    private void query(Request<String, ?> request, Params params, final Callback callback) {
        if (params != null) {
            request.tag(params.getTag());
            for (Map.Entry<String, String> entry : params.toHeaderMap().entrySet()) {
                request.headers(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : params.toBodyMap().entrySet()) {
                request.params(entry.getKey(), entry.getValue());
            }
        }
        if (request instanceof BodyRequest) {
            BodyRequest<?, ?> bodyRequest = (BodyRequest<?, ?>) request;
            if (params instanceof MultipartParams) {
                MultipartParams multipartParams = (MultipartParams) params;
                List<File> files = multipartParams.toFiles();
                if (files != null && files.size() > 0) {
                    if (files.size() == 1) {
                        bodyRequest.params("file", files.get(0));
                    } else {
                        bodyRequest.addFileParams("file", files);
                    }
                }
            } else if (params instanceof JSONParams) {
                JSONParams jsonParams = (JSONParams) params;
                // 注意使用该方法上传数据会清空实体中其他所有的参数，头信息不清除
                bodyRequest.upJson(jsonParams.toBodyJson());
            }
        }
        if (callback == null) {
            request.execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {

                }
            });
            return;
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    callback.onSuccess(response.body());
                } catch (Exception e) {
                    Logger.print(e);
                    callback.onError(-1, e);
                }
            }

            @Override
            public void onError(Response<String> response) {
                Throwable throwable = response.getException();
                int code = response.code();
                try {
                    callback.onError(code, throwable);
                } catch (Exception e) {
                    Logger.print(e);
                    callback.onError(-1, e);
                }
            }
        });
    }

    @Override
    public void cancel(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    @Override
    public void cancelAll() {
        OkGo.getInstance().cancelAll();
    }

}
