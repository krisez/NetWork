package cn.krisez.network.handler;

import android.os.Handler;
import android.os.Message;

import cn.krisez.network.bean.Result;

public class NetHandler extends Handler {

    private ResultHandler mResultHandler;

    public NetHandler(ResultHandler resultHandler) {
        this.mResultHandler = resultHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case StateCode.SUCCESS:
                mResultHandler.onSuccess((Result) msg.obj);
                break;
            case StateCode.FAILED:
                if (null != msg.obj) {
                    Result result = (Result) msg.obj;
                    mResultHandler.onFailed(result.extra);
                } else
                    mResultHandler.onFailed("出现一些错误~");
                break;
            case StateCode.CONNECT_ERROR:
                mResultHandler.onFailed("网络未连接~");
                break;
            case StateCode.CONNECT_TIME_OUT:
                mResultHandler.onFailed("网络连接超时~");
                break;
        }
    }
}
