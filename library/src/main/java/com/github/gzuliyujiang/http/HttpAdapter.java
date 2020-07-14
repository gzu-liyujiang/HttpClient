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

    void doGet(String url, @Nullable Params params, @Nullable Callback callback);

    void doPost(String url, @Nullable Params params, @Nullable Callback callback);

    void upload(String url, @NonNull MultipartParams params, @Nullable Callback callback);

    void cancel(Object tag);

    void cancelAll();

}
