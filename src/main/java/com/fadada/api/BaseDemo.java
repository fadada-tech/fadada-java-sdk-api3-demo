package com.fadada.api;

import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.oauth2.AccessTokenRsp;
import com.fadada.api.client.Oauth2Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yh
 * @version 1.0.0
 * @ClassName BaseDemo.java
 * @Description 获取token接口使用demo
 * @Param
 * @createTime 2020年07月06日 15:36:00
 */
public class BaseDemo {
    public static Logger log = LoggerFactory.getLogger(BaseDemo.class);
    public static String APPID = "FA18769044";
    public static String APPKEY = "IIODEQWAGQUPVDSIHJDPLB3DJ7KI2ZYH";
    public static String SERVERURL = "https://v4demo-gw.fadada.com/api/v3/";

    public static String unionId = "用户unionId";

    public static String token = "accessToken值";
    public static String userToken = "userToken值";


    /**
     * 初始化客户端
     *
     * @return
     */
    public DefaultFadadaApiClient getClient() {
        return new DefaultFadadaApiClient(APPID, APPKEY, SERVERURL);
    }

    /**
     * 获取token
     *
     * @return
     * @throws Exception
     */
    public Oauth2Client getToken(DefaultFadadaApiClient client) throws Exception {
        Oauth2Client oauth2Client = new Oauth2Client(client);
        BaseRsp<AccessTokenRsp> rsp = oauth2Client.getToken();

        CommonUtil.checkResult(rsp);
        token = rsp.getData().getAccessToken();
        return oauth2Client;
    }


    public static void main(String[] args) throws Exception {
        BaseDemo baseDemo = new BaseDemo();
        DefaultFadadaApiClient client = baseDemo.getClient();
        baseDemo.getToken(client);
    }
}
