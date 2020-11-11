package com.fadada.api;

import com.fadada.api.bean.req.account.GetAuthorizeUrlReq;
import com.fadada.api.bean.req.oauth2.CancelAuthSignAuthReq;
import com.fadada.api.bean.req.oauth2.GetAutoSignAuthUrlReq;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.account.GetAuthorizeUrlRsp;
import com.fadada.api.bean.rsp.oauth2.GetAutoSignAuthUrlRsp;
import com.fadada.api.client.Oauth2Client;
import com.fadada.api.exception.ApiException;


public class Oauth2Demo extends BaseDemo {

    private String returnUrl = "https://www.baidu.com";

    private Oauth2Client oauth2Client;

    public Oauth2Demo(FadadaApiClient fadadaApiClient) {
        this.oauth2Client = new Oauth2Client(fadadaApiClient);
    }

    public static void main(String[] args) {
        BaseDemo baseDemo = new BaseDemo();
        DefaultFadadaApiClient client = baseDemo.getClient();
        Oauth2Demo oauth2Demo = new Oauth2Demo(client);
        try {
            baseDemo.getToken(client);
            // 获取授权地址
            oauth2Demo.getAuthorizeUrl();
            // 获取自动签授权地址
            oauth2Demo.getAutoSignAuthUrl();
            // 取消自动签授权
            oauth2Demo.cancelAuthSignAuth();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取授权地址
     *
     * @throws ApiException
     */
    public void getAuthorizeUrl() throws ApiException {
        GetAuthorizeUrlReq req = new GetAuthorizeUrlReq();
        req.setToken(token);
        req.setRedirectUrl(returnUrl);
        req.setUnionId(unionId);
        req.setScope("");
        BaseRsp<GetAuthorizeUrlRsp> rsp = oauth2Client.getAuthorizeUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取自动签授权地址
     *
     * @throws ApiException
     */
    public void getAutoSignAuthUrl() throws ApiException {
        GetAutoSignAuthUrlReq req = new GetAutoSignAuthUrlReq();
        req.setToken(token);
        req.setUnionId(unionId);
        req.setRedirectUrl(returnUrl);
        BaseRsp<GetAutoSignAuthUrlRsp> rsp = oauth2Client.getAutoSignAuthUrl(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 取消自动签授权
     *
     * @throws ApiException
     */
    public void cancelAuthSignAuth() throws ApiException {
        CancelAuthSignAuthReq req = new CancelAuthSignAuthReq();
        req.setToken(token);
        req.setUnionId(unionId);
        BaseRsp rsp = oauth2Client.cancelAuthSignAuth(req);
        CommonUtil.checkResult(rsp);
    }


}
