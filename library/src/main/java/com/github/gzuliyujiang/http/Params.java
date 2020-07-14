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

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP请求参数
 * Created by liyujiang on 2020/7/14.
 */
public class Params {
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
