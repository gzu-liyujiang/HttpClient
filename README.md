# 网络请求组件

[![Release APK](https://github.com/gzu-liyujiang/HttpRequest/workflows/Release%20APK/badge.svg)](https://github.com/gzu-liyujiang/HttpRequest/actions) [![Gradle Package](https://github.com/gzu-liyujiang/HttpRequest/workflows/Gradle%20Package/badge.svg)](https://github.com/gzu-liyujiang/HttpRequest/actions) [![jitpack](https://jitpack.io/v/gzu-liyujiang/HttpRequest.svg)](https://jitpack.io/#gzu-liyujiang/HttpRequest)

自用的 Android 网络请求组件，面向接口编程，默认实现了 okhttp-OkGo、Fast-Android-Networking

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.gzu-liyujiang:HttpRequest:2020.6.23'
    runtimeOnly 'com.lzy.net:okgo:3.0.4'
    //runtimeOnly 'com.amitshekhar.android:android-networking:1.0.2'
}

//HttpRequest.setAdapter(new OkGoImpl(this));
HttpRequest.useOkGo(this);

HttpRequest.doGet("http://wthrcdn.etouch.cn/weather_mini?city=%E8%B4%B5%E9%98%B3", null, new HttpService.Callback() {
    @Override
    public void onSuccess(String result) {
    }

    @Override
    public void onError(int code, Throwable throwable) {
    }
});
```



## License

```text
Copyright (c) 2019-2020 gzu-liyujiang <1032694760@qq.com>

The software is licensed under the Mulan PSL v1.
You can use this software according to the terms and conditions of the Mulan PSL v1.
You may obtain a copy of Mulan PSL v1 at:
    http://license.coscl.org.cn/MulanPSL
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
PURPOSE.
See the Mulan PSL v1 for more details.
```
