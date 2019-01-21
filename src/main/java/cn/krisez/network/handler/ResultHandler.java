package cn.krisez.network.handler;

import cn.krisez.network.bean.Result;

public interface ResultHandler {
    void onSuccess(Result result);
    void onFailed(String msg);
}
