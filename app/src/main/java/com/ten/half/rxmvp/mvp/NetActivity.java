package com.ten.half.rxmvp.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ten.half.rxmvp.R;
import com.ten.half.rxmvp.base.BaseApplication;
import com.ten.half.rxmvp.bean.ConfigBean;
import com.ten.half.rxmvp.net.ISuccess;
import com.ten.half.rxmvp.net.RetrofitSingleton;
import com.ten.half.rxmvp.net.RxUtils;
import com.ten.half.rxmvp.net.SimpleToastSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxDialogFragment;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

import okhttp3.Headers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by wugang on 2017/12/26.
 */

public class NetActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        Button viewById = (Button) findViewById(R.id.btn_request);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<Response<String>> observable = RetrofitSingleton.getInstance()
                        .getApiService()
                        .getHotList(1, 12)
                        .compose(RxUtils.rxSchedulerHelper())
                        .compose(RxUtils.rxErrorHelper());
                binToLifecycle(NetActivity.this,observable);

                observable.subscribe(new Subscriber<Response<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {
                        Headers headers = stringResponse.headers();
                        Log.d("当前线程",Thread.currentThread().getName());
                        Log.d("请求成功",headers.toString());
                        Log.d("请求成功",stringResponse.body().toString());
                    }
                });
            }
        });
    }



    protected <T> Observable<T> binToLifecycle(Object view, Observable<T> observer) {
        if (view instanceof RxAppCompatActivity) {
            observer.compose(((RxAppCompatActivity) view).bindToLifecycle());
        } else if (view instanceof RxFragment) {
            observer.compose(((RxFragment) view).bindToLifecycle());
        } else if (view instanceof RxFragmentActivity) {
            observer.compose(((RxFragmentActivity) view).bindToLifecycle());
        } else if (view instanceof RxDialogFragment) {
            observer.compose(((RxDialogFragment) view).bindToLifecycle());
        }
        return observer;
    }

}
