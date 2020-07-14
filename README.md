# 网络请求组件

[![Release APK](https://github.com/gzu-liyujiang/HttpRequest/workflows/Release%20APK/badge.svg)](https://github.com/gzu-liyujiang/HttpRequest/actions) [![Gradle Package](https://github.com/gzu-liyujiang/HttpRequest/workflows/Gradle%20Package/badge.svg)](https://github.com/gzu-liyujiang/HttpRequest/actions) [![jitpack](https://jitpack.io/v/gzu-liyujiang/HttpRequest.svg)](https://jitpack.io/#gzu-liyujiang/HttpRequest)

自用的 Android 网络请求组件，面向接口编程，默认实现了 okhttp-OkGo、Fast-Android-Networking，可无缝切换底层网络请求框架。

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.gzu-liyujiang:HttpRequest:latest.version'
    runtimeOnly 'com.lzy.net:okgo:3.0.4'
    //runtimeOnly 'com.amitshekhar.android:android-networking:1.0.2'
}
```
```java
        progressBar.setVisibility(View.VISIBLE);
        String url = "http://wthrcdn.etouch.cn/weather_mini?city=%E8%B4%B5%E9%98%B3";
        HttpRequest.doGet(url, new QueryParams(), new SafetyCallback(this) {
            @Override
            public void onSuccessSafety(String result) {
                progressBar.setVisibility(View.GONE);
                textView.setText(result);
            }

            @Override
            public void onErrorSafety(int code, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                textView.setText(Log.getStackTraceString(throwable));
            }
        });
```
```text
GET http://wthrcdn.etouch.cn/weather_mini?city=%E8%B4%B5%E9%98%B3 http/1.1
Accept-Language: zh-CN,zh;q=0.8
User-Agent: Mozilla/5.0 (Linux; Android 7.1.2; G011C Build/N2G48H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.158 Safari/537.36 HttpRequest/2020.7.14 OkGo/3.0.4 

200 OK http://wthrcdn.etouch.cn/weather_mini?city=%E8%B4%B5%E9%98%B3 (56ms）
Date: Tue, 14 Jul 2020 03:03:35 GMT
Connection: keep-alive
Access-Control-Allow-Headers: *
Access-Control-Allow-Methods: *
Access-Control-Allow-Origin: *
Cache-Control: must-revalidate, max-age=300
Age: 0
X-Via-Ucdn: HIT by 60.163.132.210, HIT by 58.216.9.192
Server: Tengine/2.2.3 
Body: maybe [binary body], omitted!
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
