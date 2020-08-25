package com.fadada.api;

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

            // 获取授权地址
            accountDemo.getAuthurl();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取授权地址
     *
     * @throws ApiException
     */
    public void getAuthurl() throws ApiException {
        GetAuthorizeUrlReq req = new GetAuthorizeUrlReq();
        req.setUnionId(unionId);
        req.setRedirectUrl(returnUrl);
        req.setScope("1");
        BaseRsp<GetAuthorizeUrlRsp> rsp = accountClient.getAuthorizeUrl(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取个人信息确定地址
     *
     * @throws ApiException
     */
    public void getPersonUnionIdUrl() throws ApiException {
        GetPersonUnionIdUrlReq req = new GetPersonUnionIdUrlReq();
        req.setClientId(clientId);
        req.setRedirectUrl(returnUrl);
        BaseRsp<GetUnionIdUrlRsp> rsp = accountClient.getPersonUnionIdUrl(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取个人信息
     *
     * @throws ApiException
     */
    public void getPersonInfo() throws ApiException {
        GetPersonInfoReq req = new GetPersonInfoReq();
        req.setUnionId(unionId);
        BaseRsp<GetPersonInfoRsp> rsp = accountClient.getPersonInfo(token, req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 获取企业信息确定地址
     *
     * @throws ApiException
     */
    public void getCompanyUnionIdUrl() throws ApiException {
        GetCompanyUnionIdUrlReq req = new GetCompanyUnionIdUrlReq();

        req.setClientId(clientId);
        req.setRedirectUrl(returnUrl);
        req.setAllowModify(1);
        CompanyReq companyReq = new CompanyReq();
        companyReq.setCompanyName("测试有限公司");
        ApplicantReq applicantR = new ApplicantReq();
        applicantR.setUnionId(unionId);
        req.setCompany(companyReq);
        req.setApplicant(applicantR);
        BaseRsp<GetUnionIdUrlRsp> rsp = accountClient.getCompanyUnionIdUrl(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取企业信息
     *
     * @throws ApiException
     */
    public void getCompanyInfo() throws ApiException {
        GetCompanyInfoReq req = new GetCompanyInfoReq();
        req.setUnionId(unionId);
        BaseRsp<GetCompanyInfoRsp> rsp = accountClient.getCompanyInfo(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 账号信息校验
     *
     * @throws ApiException
     */
    public void checkAccountInfo() throws ApiException {
        CheckAccountInfoReq req = new CheckAccountInfoReq();
        req.setMobile("手机号码");
        BaseRsp<CheckAccountInfoRsp> rsp = accountClient.checkAccountInfo(token, req);
        CommonUtil.checkResult(rsp);
    }

}
