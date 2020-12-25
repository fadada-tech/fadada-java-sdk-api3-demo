package com.fadada.api;

import com.fadada.api.bean.req.BaseReq;
import com.fadada.api.bean.req.account.*;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.account.*;
import com.fadada.api.client.AccountClient;
import com.fadada.api.exception.ApiException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author yh
 * @version 1.0.0
 * @ClassName AccountDemo.java
 * @Description 账户接口使用demo
 * @Param
 * @createTime 2020年07月06日 15:36:00
 */
public class AccountDemo extends BaseDemo {

    private String returnUrl = "https://fadada.com";
    private String grantCode = "授权码";
    private String mobile = "手机号码";


    private AccountClient accountClient;

    public Timestamp ts = new Timestamp(System.currentTimeMillis());
    public DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public String clientId;

    {
        clientId = "clientId" + sdf.format(ts);
    }

    public AccountDemo(FadadaApiClient fadadaApiClient) {
        this.accountClient = new AccountClient(fadadaApiClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            AccountDemo accountDemo = new AccountDemo(client);
            baseDemo.getToken(client);

            // 获取个人信息确定地址
            accountDemo.getPersonUnionIdUrl();
            // 获取个人信息
            accountDemo.getPersonInfo();
            // 获取企业信息
            accountDemo.getCompanyInfo();
            // 获取企业信息确定地址
            accountDemo.getCompanyUnionIdUrl();
            // 账号信息校验
            accountDemo.checkAccountInfo();
            // 获取接入方信息
            accountDemo.getAccessObjectInfo();
            // 根据clientId获取unionId
            accountDemo.getUnionIds();

            // 根据uuid下载文件base64
            accountDemo.getFileBase64();

            //  ----- 第三方服务 -------
            // 获取开通第三方服务地址
            accountDemo.getOpenServerUrl();
            // 获取userToken
            accountDemo.getUserToken();
            // 取消第三方服务地址
            accountDemo.cancelServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取个人信息确定地址
     *
     * @throws ApiException
     */
    public void getPersonUnionIdUrl() throws ApiException {
        GetPersonUnionIdUrlReq req = new GetPersonUnionIdUrlReq();
        req.setToken(token);
        req.setClientId(clientId);
        req.setRedirectUrl(returnUrl);
        BaseRsp<GetUnionIdUrlRsp> rsp = accountClient.getPersonUnionIdUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取个人信息
     *
     * @throws ApiException
     */
    public void getPersonInfo() throws ApiException {
        GetPersonInfoReq req = new GetPersonInfoReq();
        req.setToken(token);
        req.setUnionId(unionId);
        BaseRsp<GetPersonInfoRsp> rsp = accountClient.getPersonInfo(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 获取企业信息确定地址
     *
     * @throws ApiException
     */
    public void getCompanyUnionIdUrl() throws ApiException {
        GetCompanyUnionIdUrlReq req = new GetCompanyUnionIdUrlReq();
        req.setToken(token);
        req.setClientId(clientId);
        req.setRedirectUrl(returnUrl);
        req.setAllowModify(1);
        CompanyReq companyReq = new CompanyReq();
        companyReq.setCompanyName("测试有限公司");
        ApplicantReq applicantR = new ApplicantReq();
        applicantR.setUnionId(unionId);
        req.setCompany(companyReq);
        req.setApplicant(applicantR);
        BaseRsp<GetUnionIdUrlRsp> rsp = accountClient.getCompanyUnionIdUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取企业信息
     *
     * @throws ApiException
     */
    public void getCompanyInfo() throws ApiException {
        GetCompanyInfoReq req = new GetCompanyInfoReq();
        req.setToken(token);
        req.setUnionId(unionId);
        BaseRsp<GetCompanyInfoRsp> rsp = accountClient.getCompanyInfo(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 账号信息校验
     *
     * @throws ApiException
     */
    public void checkAccountInfo() throws ApiException {
        CheckAccountInfoReq req = new CheckAccountInfoReq();
        req.setToken(token);
        req.setMobile(mobile);
        BaseRsp<CheckAccountInfoRsp> rsp = accountClient.checkAccountInfo(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取接入方信息
     *
     * @throws ApiException
     */
    public void getAccessObjectInfo() throws ApiException {
        BaseReq baseReq = new BaseReq();
        baseReq.setToken(token);
        BaseRsp<GetAccessObjectInfoRsp> rsp = accountClient.getAccessObjectInfo(baseReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取开通第三方服务地址
     *
     * @throws ApiException
     */
    public void getOpenServerUrl() throws ApiException {
        GetOpenServerUrlReq req = new GetOpenServerUrlReq();
        req.setToken(token);
        req.setRedirectUrl(returnUrl);
        req.setUnionId(unionId);
        BaseRsp<GetOpenServerUrlRsp> rsp = accountClient.getOpenServerUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取userToken
     *
     * @throws ApiException
     */
    public void getUserToken() throws ApiException {

        GetUserTokenReq req = new GetUserTokenReq();
        req.setToken(token);
        req.setGrantCode(grantCode);
        BaseRsp<GetUserTokenRsp> rsp = accountClient.getUserToken(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 取消第三方服务地址
     *
     * @throws ApiException
     */
    public void cancelServer() throws ApiException {
        CancelServerReq req = new CancelServerReq();
        req.setToken(token);
        req.setUnionId(unionId);
        BaseRsp<CheckAccountInfoRsp> rsp = accountClient.cancelServer(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 根据uuid下载文件base64
     *
     * @throws ApiException
     */
    public void getFileBase64() throws ApiException {
        GetFileBase64Req req = new GetFileBase64Req();
        req.setToken(token);
        req.setUuid("文件uuid");
        BaseRsp<GetFileBase64Rsp> rsp = accountClient.getFileBase64(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 根据clientId获取unionId
     *
     * @throws ApiException
     */
    public void getUnionIds() throws ApiException {
        GetUnionIdsReq req = new GetUnionIdsReq();
        req.setToken(token);
        req.setClientId("6666");
        BaseRsp<GetUnionIdsRsp> rsp = accountClient.getUnionIds(req);
        System.out.println(rsp.toString());
    }


}
