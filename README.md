# NetWork
网络请求库

###
封装类型  *RxJava2 + Retrofit2*  应用于**Android**开发

## 请求方法
- 应用层
新建Api.java  应用Retrofit2的请求方法封装 
- url
传入URL `NetWorkUtils.INSTANCE().url("url");` 建议在Application设置全局值
- 使用
`NetWorkUtils.INSTANCE().create(Observable<T> o).handler(ResultHandler resultHandler);`

在`resultHandler`里处理即可

完整的
    
    NetWorkUtils.INSTANCE().create(new NetWorkUtils.NetApi().api(API.class).login("")).handler(new ResultHandler() {
    @Override
    public void onSuccess(Result result) {
    
    }
    
    @Override
    public void onFailed(String msg) {
    
    }
    });

库使用方法

> 未知，clone吧
