package cn.krisez.network;

import java.util.concurrent.TimeUnit;

import cn.krisez.network.handler.NetHandler;
import cn.krisez.network.handler.ResultHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unchecked")
public class NetWorkUtils {

    private volatile static NetWorkUtils INSTANCE;
    private static Retrofit mRetrofit;
    private RequestSubscribe mRequestSubscribe;

    private NetWorkUtils() {
        init();
    }

    public static NetWorkUtils INSTANCE() {
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if (INSTANCE == null) {
            synchronized (NetWorkUtils.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if (INSTANCE == null) {
                    INSTANCE = new NetWorkUtils();
                }
            }
        }

        return INSTANCE;
    }

    private void init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS).build();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://krisez.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        this.mRequestSubscribe = new RequestSubscribe();
    }

    public void url(String url) {
        mRetrofit = mRetrofit.newBuilder().baseUrl(url).build();
    }

    public NetWorkUtils handler(ResultHandler resultHandler) {
        this.mRequestSubscribe.handler(new NetHandler(resultHandler));
        return this;
    }

    /**
     * @param o
     * @param <T>
     */
    public <T> NetWorkUtils create(Observable<T> o) {
        o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRequestSubscribe);
        return this;
    }

    public static class NetApi {
        public <T> T api(Class<T> apiClass) {
            return mRetrofit.create(apiClass);
        }
    }
}
