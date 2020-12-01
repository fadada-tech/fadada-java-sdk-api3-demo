package com.fadada.api;

import com.fadada.api.bean.req.issp.SyncContractFileInfoReq;
import com.fadada.api.bean.req.issp.SyncTemplateFileInfoReq;
import com.fadada.api.bean.req.issp.WitnessReq;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.issp.SyncContractFileInfoRsp;
import com.fadada.api.bean.rsp.issp.SyncTemplateFileInfoRsp;
import com.fadada.api.client.IsspApiClient;
import com.fadada.api.exception.ApiException;

/**
 * @author yh128
 * @className IsspApiDemo
 * @description IsspApiDemo
 * @createTime 2020年12月1日 09:49:17
 */
public class IsspApiDemo extends BaseDemo {

    private String templateId = "模板编号";
    private String fileUuid = "文件uuid";
    private String fileName = "文件名称";
    private String taskId = "签署任务编号";

    private IsspApiClient isspApiClient;

    public IsspApiDemo(FadadaApiClient fadadaApiClient) {
        this.isspApiClient = new IsspApiClient(fadadaApiClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            IsspApiDemo isspApiDemo = new IsspApiDemo(client);
            // 获取token
            baseDemo.getToken(client);

            // 同步合同文件信息
            isspApiDemo.syncContractFileInfo();
            // 同步模板文件信息
            isspApiDemo.syncTemplateFileInfo();
            // 合同文件归档
            isspApiDemo.witness();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 同步模板文件数据信息
     */
    public void syncTemplateFileInfo() throws ApiException {
        SyncTemplateFileInfoReq req = new SyncTemplateFileInfoReq();
        req.setToken(token);
        req.setFileName(fileName);
        req.setFileType(2);
        req.setFileUuid(fileUuid);
        req.setTemplateId(templateId);
        req.setDeleteStatus(2);
        BaseRsp<SyncTemplateFileInfoRsp> rsp = isspApiClient.syncTemplateFileInfo(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 同步合同文件数据信息
     */
    public void syncContractFileInfo() throws ApiException {
        SyncContractFileInfoReq req = new SyncContractFileInfoReq();
        req.setToken(token);
        req.setFileName(fileName);
        req.setFileType(1);
        req.setFileUuid(fileUuid);
        req.setDeleteStatus(2);
        BaseRsp<SyncContractFileInfoRsp> rsp = isspApiClient.syncContractFileInfo(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 合同文件归档
     */
    public void witness() throws ApiException {
        WitnessReq req = new WitnessReq();
        req.setToken(token);
        req.setTaskId(taskId);
        BaseRsp rsp = isspApiClient.witness(req);
        CommonUtil.checkResult(rsp);
    }


}
