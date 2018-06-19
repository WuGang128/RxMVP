/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ten.half.rxmvp.net;


import android.app.Activity;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.ten.half.rxmvp.base.BaseApplication;
import com.ten.half.rxmvp.bean.ConfigBean;
import com.ten.half.rxmvp.mvp.NetActivity;
import com.ten.half.rxmvp.util.SignUtils;


import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.zip.GZIPInputStream;

import okhttp3.*;
import retrofit2.*;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Info: 简化 Rx 的模式,用在只关乎 onNext() 逻辑
 */
public abstract class SimpleToastSubscriber<T, K> extends Subscriber<T> {
    private String signkey = "";
    ISuccess<ConfigBean> view;

    protected SimpleToastSubscriber(String signkey, ISuccess<ConfigBean> view) {
        this.signkey = signkey;
        this.view = view;

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        LogUtils.e(e.toString());
        if (e instanceof ServerException) {
            ServerException runtimeException = (ServerException) e;
            Log.d("请求失败", runtimeException.code + "onError");
            LogUtils.e(runtimeException.getMessage());
            onServerError(runtimeException.getMessage(), runtimeException.code);
        } else if (e instanceof EmptyException) {
            EmptyException runtimeException = (EmptyException) e;
            LogUtils.e(runtimeException.getMessage());
        } else {
            String error = "服务器繁忙,请稍后重试!";

            if (e instanceof HttpException) {             //HTTP错误
                HttpException httpException = (HttpException) e;
                LogUtils.e("网络错误");
                ToastUtils.show(BaseApplication.getMContext(), error);
            } else if (e instanceof MalformedJsonException
                    || e instanceof JSONException
                    || e instanceof ParseException
                    || e instanceof JsonSyntaxException
                    || e instanceof UnsupportedOperationException
                    ) {
                LogUtils.e("解析错误");            //均视为解析错误
                ToastUtils.show(BaseApplication.getMContext(), "服务器数据错误，请稍后重试！");

            } else if (e instanceof ConnectException) {
                LogUtils.e("连接失败");  //均视为网络错误
                ToastUtils.show(BaseApplication.getMContext(), error);
            } else {
                LogUtils.e("未知错误");//未知错误
            }
        }
    }

    @Override
    public void onNext(T t) {
        retrofit2.Response response = (retrofit2.Response) t;
        try {
            Log.d("请求成功", response.headers().toString() + "\n" + response.body().toString());
            okhttp3.Response raw = response.raw();
            okhttp3.Response raw1 = response.raw();
            Log.d("请求成功", raw1.body().string());
            String body;
//            if (raw.header("Content-Encoding").equals("gzip")) {
//                body = unzip(raw.body().byteStream());
//            } else {
//            }

                body = response.body().toString();
            String stroxstr;
            if (raw.header("Api-Sign-Model").equals("1")) {
                stroxstr = SignUtils.parDecode(body, signkey);
            } else {
                stroxstr = response.body().toString();
            }

            showToast(view);
            view.onSuccess(new Gson().fromJson(stroxstr, ConfigBean.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showToast(Object o) {
        if (o instanceof Activity) {
            Toast.makeText(BaseApplication.getMContext(), "属于activity", Toast.LENGTH_SHORT).show();
        }
    }

    public void onServerError(String message, String code) {
        if (code.startsWith("400") || code.startsWith("500")) {
            ToastUtils.show(BaseApplication.getMContext(), message);
        } else {
            String error = "服务器繁忙,请稍后重试!";
            ToastUtils.show(BaseApplication.getMContext(), error);
        }
    }

    public static String unzip(InputStream inputStream) {
        String result = null;

        try {
            GZIPInputStream e = new GZIPInputStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int i;
            while ((i = e.read()) != -1) {
                baos.write(i);
            }

            result = baos.toString();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return result;
    }
}
