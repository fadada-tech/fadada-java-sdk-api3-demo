package com.fadada.api;

import com.fadada.api.bean.req.seal.*;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.seal.AddCompanySealRsp;
import com.fadada.api.bean.rsp.seal.CompanySealDetailRsp;
import com.fadada.api.bean.rsp.seal.CompanySealListRsp;
import com.fadada.api.client.SealClient;
import com.fadada.api.exception.ApiException;

import java.io.File;

/**
 * @author yh
 * @version 1.0.0
 * @ClassName SealDemo.java
 * @Description 签章接口demo
 * @Param
 * @createTime 2020年07月06日 15:36:00
 */
public class SealDemo extends BaseDemo {

    private String sealId;

    public SealDemo(FadadaApiClient fadadaApiClient) {
        this.sealClient = new SealClient(fadadaApiClient);
    }

    private SealClient sealClient;

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            SealDemo sealDemo = new SealDemo(client);
            // 获取token
            baseDemo.getToken(client);

            // 新增企业印章
            sealDemo.addCompanySeal();
            // 删除企业印章
            sealDemo.delCompanySeal();
            // 企业印章授权
            sealDemo.sealAuth();
            // 企业印章取消授权
            sealDemo.cancelSealAuth();
            // 企业印章列表
            sealDemo.companySealList();
            // 企业印章详请
            sealDemo.companySealDetail();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传企业签章
     */
    public void addCompanySeal() throws ApiException {
        AddCompanySealReq addCompanySealReq = new AddCompanySealReq();
        addCompanySealReq.setToken(token);
        addCompanySealReq.setSealInfo(null, "人事章");
        String path = getClass().getClassLoader().getResource("sampleSeal.png").getFile();
        File imageFile = new File(path);
        BaseRsp<AddCompanySealRsp> rsp = sealClient.addCompanySeal(addCompanySealReq, imageFile);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 删除企业签章
     */
    public void delCompanySeal() throws ApiException {
        DelCompanySealReq delCompanySealReq = new DelCompanySealReq();
        delCompanySealReq.setToken(token);
        delCompanySealReq.setSealInfo(sealId);
        BaseRsp rsp = sealClient.delCompanySeal(delCompanySealReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 签章授权
     */
    public void sealAuth() throws ApiException {
        SealAuthReq sealAuthReq = new SealAuthReq();
        sealAuthReq.setToken(token);
        SealAuthReq.EmployeeInfo employeeInfo = new SealAuthReq.EmployeeInfo();
        employeeInfo.setUnionId(unionId);
        sealAuthReq.setEmployeeInfo(employeeInfo);
        SealAuthReq.SealInfo sealInfo = new SealAuthReq.SealInfo();
        sealInfo.setSealId(sealId);
        sealAuthReq.setSealInfo(sealInfo);
        BaseRsp rsp = sealClient.sealAuth(sealAuthReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 签章取消授权
     */
    public void cancelSealAuth() throws ApiException {
        CancelSealAuthReq cancelSealAuthReq = new CancelSealAuthReq();
        cancelSealAuthReq.setToken(token);
        cancelSealAuthReq.setEmployeeInfo(unionId);
        cancelSealAuthReq.setSealInfo(sealId);
        BaseRsp rsp = sealClient.cancelSealAuth(cancelSealAuthReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 企业签章列表
     */
    public void companySealList() throws ApiException {
        CompanySealListReq companySealListReq = new CompanySealListReq();
        companySealListReq.setToken(token);
        companySealListReq.setSealInfo(1);
        BaseRsp<CompanySealListRsp> rsp = sealClient.companySealList(companySealListReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 企业签章详请
     */
    public void companySealDetail() throws ApiException {
        CompanySealDetailReq companySealDetailReq = new CompanySealDetailReq();
        companySealDetailReq.setToken(token);
        CompanySealDetailReq.SealInfo sealInfo = new CompanySealDetailReq.SealInfo();
        sealInfo.setSealId(sealId);
        companySealDetailReq.setSealInfo(sealInfo);
        BaseRsp<CompanySealDetailRsp> rsp = sealClient.companySealDetail(companySealDetailReq);
        CommonUtil.checkResult(rsp);
    }
}
