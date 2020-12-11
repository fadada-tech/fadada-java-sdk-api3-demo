package com.fadada.api;

import com.fadada.api.bean.req.document.*;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.document.DownLoadFileRsp;
import com.fadada.api.bean.rsp.document.LookUpCoordinatesRsp;
import com.fadada.api.bean.rsp.document.UploadFileRsp;
import com.fadada.api.bean.rsp.document.VerifySignatureRsp;
import com.fadada.api.client.DocumentClient;
import com.fadada.api.exception.ApiException;

import java.io.File;


/**
 * @author yh
 * @version 1.0.0
 * @ClassName DocDemo.java
 * @Description 文档接口使用demo
 * @Param
 * @createTime 2020年07月06日 15:36:00
 */
public class DocDemo extends BaseDemo {

    private String taskId = "签署任务Id";
    private String draftId = "草稿编号";
    private String fileId = "文件Id";
    public DocumentClient documentClient;

    public DocDemo(FadadaApiClient fadadaApiClient) {
        this.documentClient = new DocumentClient(fadadaApiClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            DocDemo docDemo = new DocDemo(client);
            // 获取token
            baseDemo.getToken(client);

            // 合同文件上传
            docDemo.uploadFile();
            // 下载签署文件
            docDemo.getBySignFileId();
            // 下载合同草稿文件
            docDemo.getByDraftFileId();
            // 关键字查询坐标
            docDemo.lookUpCoordinates();
            // 合同文件验签
            docDemo.verifySignature();
            // 合同技术报告下载
            docDemo.contractReportDownload();
            // 公证处保全报告下载
            docDemo.downloadEvidenceReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传文件
     */
    public UploadFileRsp uploadFile() throws ApiException {
        UploadFileReq uploadFileReq = new UploadFileReq();
        uploadFileReq.setToken(token);
        uploadFileReq.setFileType(1);

        String path = getClass().getClassLoader().getResource("sampleContract.pdf").getFile();
        File file = new File(path);
        BaseRsp<UploadFileRsp> rsp = documentClient.uploadFile(uploadFileReq, file);
        CommonUtil.checkResult(rsp);
        return rsp.getData();
    }


    /**
     * 下载签署文件
     */
    public void getBySignFileId() throws ApiException {
        GetBySignFileIdReq req = new GetBySignFileIdReq();
        req.setToken(token);
        req.setTaskId(taskId);
        req.setSignFileId(fileId);
        BaseRsp<DownLoadFileRsp> rsp = documentClient.getBySignFileId(req);
        if (rsp.isSuccess()) {
            String path = getClass().getClassLoader().getResource("").getPath();
            CommonUtil.fileSink(rsp.getData().getFileBytes(), path, "合同.zip");
        } else {
            log.error("根据签署文件ID获取文件失败结果：{}", rsp.toString());
        }
    }

    /**
     * 下载合同草稿文件
     *
     * @throws ApiException
     */
    public void getByDraftFileId() throws ApiException {
        GetByDraftIdReq req = new GetByDraftIdReq();
        req.setToken(token);
        req.setDraftFileId("");
        req.setDraftId(draftId);
        BaseRsp<DownLoadFileRsp> rsp = documentClient.getByDraftId(req);
        if (rsp.isSuccess()) {
            String path = getClass().getClassLoader().getResource("").getPath();
            CommonUtil.fileSink(rsp.getData().getFileBytes(), path, "文件.zip");
        } else {
            log.error("根据草稿ID获取文件失败结果：{}", rsp);
        }
    }


    /**
     * 关键字查询坐标
     *
     * @throws ApiException
     */
    public void lookUpCoordinates() throws ApiException {
        LookUpCoordinatesReq req = new LookUpCoordinatesReq();
        req.setToken(token);
        LookUpCoordinatesReq.QueryInfo queryInfo = new LookUpCoordinatesReq.QueryInfo();
        queryInfo.setFileId(fileId);
        queryInfo.setKeyword("房屋");
        queryInfo.setPageNumber(0);
        queryInfo.setKeywordStrategy(0);
        req.setQueryInfo(queryInfo);
        BaseRsp<LookUpCoordinatesRsp> rsp = documentClient.lookUpCoordinates(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 合同文件验签
     *
     * @throws ApiException
     */
    public void verifySignature() throws ApiException {
        VerifySignatureReq req = new VerifySignatureReq();
        req.setToken(token);
        String path = getClass().getClassLoader().getResource("sampleContract.pdf").getFile();
        File file = new File(path);
        BaseRsp<VerifySignatureRsp> rsp = documentClient.verifySignature(req, file);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 合同技术报告下载
     *
     * @throws ApiException
     */
    public void contractReportDownload() throws ApiException {
        ContractReportDownloadReq req = new ContractReportDownloadReq();
        req.setToken(token);
        ContractReportDownloadReq.ContractInfo contractInfo = new ContractReportDownloadReq.ContractInfo();
        contractInfo.setTaskId(taskId);
        req.setContractInfo(contractInfo);
        BaseRsp<DownLoadFileRsp> rsp = documentClient.contractReportDownload(req);
        if (rsp.isSuccess()) {
            String path = getClass().getClassLoader().getResource("").getPath();
            CommonUtil.fileSink(rsp.getData().getFileBytes(), path, "技术报告.zip");
        } else {
            log.error("技术报告下载失败：{}", rsp.toString());
        }
    }

    /**
     * 保全报告下载
     *
     * @throws ApiException
     */
    public void downloadEvidenceReport() throws ApiException {
        DownloadEvidenceReportReq req = new DownloadEvidenceReportReq();
        req.setToken(token);
        DownloadEvidenceReportReq.QueryInfo queryInfo = new DownloadEvidenceReportReq.QueryInfo();
        queryInfo.setTaskId(taskId);
        queryInfo.setType(3);
        req.setQueryInfo(queryInfo);
        BaseRsp<DownLoadFileRsp> rsp = documentClient.downloadEvidenceReport(req);
        if (rsp.isSuccess()) {
            String path = getClass().getClassLoader().getResource("").getPath();
            CommonUtil.fileSink(rsp.getData().getFileBytes(), path, "公证处保全报告.zip");
        } else {
            log.error("公证处保全报告下载失败：{}", rsp.toString());
        }
    }
}
