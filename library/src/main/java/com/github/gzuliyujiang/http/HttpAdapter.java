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

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设计思想是使用接口对各模块解耦规范化，不强依赖某些明确的三方库，使得三方库可自由搭配组装。
 * <p>
 * 集成第三方HTTP框架（如：Kalle、okhttp-OkGo、Fast-Android-Networking、android-async-http、Volley），
 * <p>
 * https://github.com/jeasonlzy/okhttp-OkGo
 * https://github.com/yanzhenjie/Kalle
 * https://github.com/amitshekhariitbhu/Fast-Android-Networking
 * https://github.com/yanzhenjie/NoHttp
 * https://github.com/loopj/android-async-http
 * https://github.com/litesuits/android-lite-http
 * https://android.googlesource.com/platform/frameworks/volley
 * https://github.com/Konloch/HTTPRequest
 * <p>
 * Created by liyujiang on 2016/12/31 15:37
 * Updated by liyujiang on 2020/5/14.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public interface HttpAdapter {
    String CHARSET = "UTF-8";
    String USER_AGENT = "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " +
            Build.MANUFACTURER + " " + Build.MODEL + "; AppleWebKit/537.36 (KHTML, like Gecko) " +
            "HttpRequest/" + BuildConfig.VERSION_NAME;

    void doGet(String url, @Nullable Params params, @Nullable Callback callback);

    void doPost(String url, @Nullable Params params, @Nullable Callback callback);

    void upload(String url, @NonNull MultipartParams params, @Nullable Callback callback);

    void cancel(Object tag);

    void cancelAll();

    /**
     * HTTP请求回调
     */
    abstract class Callback {

        public abstract void onSuccess(String result);

        public abstract void onError(int code, Throwable throwable);

    }

    /**
     * HTTP请求参数
     */
    abstract class Params {
        private Object tag;
        private ConcurrentHashMap<String, String> header;
        private ConcurrentHashMap<String, String> body;

        public Params() {
            this.tag = UUID.randomUUID();
            header = new ConcurrentHashMap<>();
            body = new ConcurrentHashMap<>();
        }

        /**
         * 通常我们在{@link android.app.Activity}中做网络请求，
         * 当销毁时要取消请求否则会发生内存泄露，可通过该标记取消该请求。
         */
        public void setTag(Object tag) {
            this.tag = tag;
        }

        public final Object getTag() {
            return tag;
        }

        public void putHeader(String key, String value) {
            if (value == null) {
                value = "";
            }
            if (!TextUtils.isEmpty(key)) {
                header.put(key, value);
            }
        }

        public void putBody(String key, String value) {
            if (value == null) {
                value = "";
            }
            if (!TextUtils.isEmpty(key)) {
                body.put(key, value);
            }
        }

        public Map<String, String> toHeaderMap() {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                if (entry.getValue() == null) {
                    header.remove(entry.getKey());
                }
            }
            return header;
        }

        public Map<String, String> toBodyMap() {
            for (Map.Entry<String, String> entry : body.entrySet()) {
                if (entry.getValue() == null) {
                    body.remove(entry.getKey());
                }
            }
            return body;
        }

        public void clearHeader() {
            header.clear();
        }

        public void clearBody() {
            body.clear();
        }

        @NonNull
        @Override
        public String toString() {
            return "{header=" + header + ", body=" + body + "}";
        }

    }

    class QueryParams extends Params {

        public String toBodyString() {
            StringBuilder result = new StringBuilder();
            for (ConcurrentHashMap.Entry<String, String> entry : toBodyMap().entrySet()) {
                if (result.length() > 0) {
                    result.append("&");
                }
                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }
            return result.toString();
        }

        @NonNull
        @Override
        public String toString() {
            return toBodyString();
        }

    }

    class FormParams extends QueryParams {
        private String bodyString;

        public FormParams(String body) {
            this.bodyString = body;
        }

        @Override
        public String toBodyString() {
            if (bodyString != null) {
                return bodyString;
            }
            return super.toBodyString();
        }

    }

    class JSONParams extends Params {
        private String json;

        public JSONParams(String json) {
            this.json = json;
        }

        public String toBodyJson() {
            if (json != null) {
                return json;
            }
            return new JSONObject(toBodyMap()).toString();
        }

        @NonNull
        @Override
        public String toString() {
            return toBodyJson();
        }

    }

    class MultipartParams extends QueryParams {
        private List<File> files = new ArrayList<>();

        public MultipartParams(File file) {
            this(Collections.singletonList(file));
        }

        public MultipartParams(List<File> files) {
            this.files.addAll(files);
        }

        public List<File> toFiles() {
            return files;
        }

    }

}
