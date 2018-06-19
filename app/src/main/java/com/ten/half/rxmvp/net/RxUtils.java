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

import android.util.Log;

import com.ten.half.rxmvp.bean.ConfigBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxDialogFragment;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Info: 封装 Rx 的一些方法
 */
public class RxUtils {

    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 可自定义线程
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper(Scheduler scheduler) {
        return tObservable -> tObservable.subscribeOn(scheduler)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public static <T> Observable.Transformer<Response<T>, T> rxErrorHelper() {

        return responseObservable -> responseObservable.map(tResponse -> {

           // Log.d("请求成功", tResponse.getData() + "");
            if (!tResponse.isSuccess()) {
                throw new ServerException(tResponse.getMsg(), tResponse.getCode());
            } else {
                return tResponse.getData();
            }
        }).onErrorResumeNext(new HttpResponseFunc<>());


    }

    public static <T> Observable.Transformer<T, T> binToLifecycle(Object view) {
        if (view instanceof RxAppCompatActivity) {
            return responseObservable -> responseObservable.compose(((RxAppCompatActivity) view).bindToLifecycle());
        } else if (view instanceof RxFragment) {
            return responseObservable -> responseObservable.compose(((RxFragment) view).bindToLifecycle());
        } else if (view instanceof RxFragmentActivity) {
            return responseObservable -> responseObservable.compose(((RxFragmentActivity) view).bindToLifecycle());
        } else if (view instanceof RxDialogFragment) {
            return responseObservable -> responseObservable.compose(((RxDialogFragment) view).bindToLifecycle());
        }
        return responseObservable -> responseObservable;

    }

    public static <T> Observable.Transformer<Response<T>, T> rxNormalHelper() {
        return responseObservable -> responseObservable.map(new Func1<Response<T>, T>() {
            @Override
            public T call(Response<T> tResponse) {
                return tResponse.getData();
            }
        });
    }

    public static <T> Observable.Transformer<T, T> rxEmptyHelper() {
        return responseObservable -> responseObservable.map(entity -> {
            if (entity == null) {
                throw new EmptyException("返回数据失败", "100");
            } else {
                return entity;
            }
        }).onErrorResumeNext(new HttpResponseFunc<>());
    }

    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable throwable) {
            //ExceptionEngine为处理异常的驱动器
            return Observable.error(throwable);
        }
    }


}
