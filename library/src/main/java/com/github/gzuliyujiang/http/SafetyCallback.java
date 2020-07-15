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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.github.gzuliyujiang.logger.Logger;

/**
 * 绑定{@link FragmentActivity}及{@link Fragment}生命周期，防止界面销毁后异常
 * Created by liyujiang on 2020/7/14.
 */
@SuppressWarnings("unused")
public abstract class SafetyCallback extends Callback implements LifecycleEventObserver {
    private Lifecycle.Event lifecycleEvent;

    public SafetyCallback(FragmentActivity activity) {
        activity.getLifecycle().addObserver(this);
    }

    public SafetyCallback(Fragment fragment) {
        fragment.getLifecycle().addObserver(this);
    }

    public abstract void onSuccessSafety(String result);

    public abstract void onErrorSafety(int code, Throwable throwable);

    @Override
    public final void onSuccess(String result) {
        if (lifecycleEvent == Lifecycle.Event.ON_DESTROY) {
            return;
        }
        try {
            onSuccessSafety(result);
        } catch (Throwable e) {
            Logger.print(e);
            onError(-1, e);
        }
    }

    @Override
    public final void onError(int code, Throwable throwable) {
        if (lifecycleEvent == Lifecycle.Event.ON_DESTROY) {
            return;
        }
        try {
            onErrorSafety(code, throwable);
        } catch (Throwable e) {
            Logger.print(e);
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        lifecycleEvent = event;
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.getLifecycle().removeObserver(this);
        }
    }

}

