package cn.krisez.network;

import android.os.Message;
import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.krisez.network.bean.Result;
import cn.krisez.network.handler.NetHandler;
import cn.krisez.network.handler.StateCode;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RequestSubscribe<T> implements Observer<Result> {

    private NetHandler mResultHandler;

    public RequestSubscribe handler(NetHandler handler) {
        this.mResultHandler = handler;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    private Result mResult;

    @Override
    public void onNext(Result result) {
        this.mResult = result;
        NetConst.count++;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof TimeoutException || e instanceof SocketTimeoutException
                || e instanceof ConnectException) {
            if (mResultHandler != null)
                mResultHandler.sendEmptyMessage(StateCode.CONNECT_TIME_OUT);
        } else if (e instanceof UnknownHostException) {
            if (mResultHandler != null)
                mResultHandler.sendEmptyMessage(StateCode.CONNECT_ERROR);
        } else {
            if (mResultHandler != null)
                mResultHandler.sendEmptyMessage(StateCode.FAILED);
        }
        NetConst.count--;
    }

    @Override
    public void onComplete() {
        if (mResultHandler != null) {
            Message message = new Message();
            if("ok".equals(mResult.statue)){
                message.what = StateCode.SUCCESS;
                message.obj = mResult;
            }else if("error".equals(mResult.statue)){
                message.what = StateCode.FAILED;
                message.obj = mResult;
            }
            mResultHandler.sendMessage(message);
        }
        NetConst.count--;
    }
}
