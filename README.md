### 介绍

本项目工程主要是法大大 Api3.0 SDK的案例代码，其中使用的参数请到法大大开发者中心申请，使用到的SDK依赖请到法大大开发者中心下载，接口说明等请下载SDK文档查看。

### 用法
（1）首先初始化配置
默认使用构造函数DefaultFadadaApiClient(String appId, String appKey),如果需要对接不同的环境请使用构造函数
DefaultFadadaApiClient(String appId, String appKey, String serverUrl)，serverUrl为不同环境请求地址。
FadadaApiConfig该类里面字段变量主要用来设置签名的类型和超时时间的设置。
FadadaApiService该接口提供了json相关接口和http请求相关接口，如果不想使用默认的方法可以实现该接口。
(2)根据不同需求调用对应的client
例如：要获取token

```
DefaultFadadaApiClient fadadaApiClient = new DefaultFadadaApiClient(APPID, APPKEY);
Oauth2Client oauth2Client = new Oauth2Client(fadadaApiClient);
BaseRsp<AccessTokenRsp> rsp = oauth2Client.getToken();
```


### 注意事项

(1) 由于本项目只是一个demo，所以对token的时效性没有处理，真正引入SDK使用请对token时效做好控制，避免token失效带来的问题。
(2) 本项目中引入的pdf,图片资源只是为了完成本项目的使用，切不可把这些文件拿到现网运行。





