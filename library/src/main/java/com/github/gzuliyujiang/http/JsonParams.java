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

import org.json.JSONObject;

/**
 * Created by liyujiang on 2020/7/14.
 */
public class JsonParams extends Params {
    private String json;

    public JsonParams(String json) {
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
