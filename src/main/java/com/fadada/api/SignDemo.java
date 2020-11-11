package com.fadada.api;

import com.fadada.api.bean.req.sign.*;
import com.fadada.api.bean.req.sign.batch.*;
import com.fadada.api.bean.req.sign.file.*;
import com.fadada.api.bean.req.sign.template.CreateByDraftIdReq;
import com.fadada.api.bean.req.sign.template.ExternalSigner;
import com.fadada.api.bean.req.sign.template.TemplateSignerReq;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.document.UploadFileRsp;
import com.fadada.api.bean.rsp.sign.*;
import com.fadada.api.bean.rsp.sign.batch.BatchCreateByDraftIdRsp;
import com.fadada.api.bean.rsp.sign.batch.BatchGetSignUrlRsp;
import com.fadada.api.bean.rsp.sign.batch.BatchGetSigntasksByBatchNoRsp;
import com.fadada.api.client.SignTaskClient;
import com.fadada.api.exception.ApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yh
 * @version 1.0.0
 * @ClassName SignDemo.java
 * @Description 签署任务接口使用demo
 * @Param
 * @createTime 2020年07月06日 15:36:00
 */
public class SignDemo extends BaseDemo {

    private String taskId = "任务Id";
    private String fileId = "文件ID";
    private String draftId = "模板ID";
    private String sealId = "印章ID";
    private String mobile = "手机号码";
    private String batchNo = "签署批次号值";

    private SignTaskClient signTaskClient;

    public SignDemo(FadadaApiClient fadadaApiClient) {
        this.signTaskClient = new SignTaskClient(fadadaApiClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            // 客户端初始化
            DefaultFadadaApiClient fadadaApiClient = baseDemo.getClient();
            SignDemo signDemo = new SignDemo(fadadaApiClient);

            DocDemo docDemo = new DocDemo(fadadaApiClient);
            // 获取token
            baseDemo.getToken(fadadaApiClient);

            // 第一步 上传文件
            UploadFileRsp uploadFileRsp = docDemo.uploadFile();
            signDemo.fileId = uploadFileRsp.getFileId();
            // 第二步 根据文件ID创建签署任务
            FileSignTaskRsp signTaskByFileId = signDemo.createSignTaskByFileId();
            signDemo.taskId = signTaskByFileId.getTaskId();
            // 第三步创建签署任务时候status为create调用获取设置签章位置链接，status为sent直接调用第四步
            signDemo.getSentUrl();
            // 第四步获取签署链接
            signDemo.getSignUrl();
            signDemo.mobile = "";
            signDemo.draftId = "";
            // 根据模板Id创建签署任务
            signDemo.createByTemplate();
            // 取消签署任务
            signDemo.cancelSignTask();
            // 催签
            signDemo.urgeSign();
            // 获取预览链接
            signDemo.getSignPreviewUrl();
            // 获取签署详请
            signDemo.getTaskDetailByTaskId();
            //依据原始文件创建签署任务
            signDemo.createTaskByFile();


            // 创建批次号批量任务
            signDemo.batchCreateByDraftId();
            // 批次号批量任务新增
            signDemo.batchAddByDraftId();
            // 批次号批量任务 发起
            signDemo.batchSent();
            // 根据批次号获取签署任务
            signDemo.batchGetSigntasksByBatchNo();
            // 批次号获取签署链接
            signDemo.batchGetSignUrl();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件ID创建签署任务
     *
     * @return
     * @throws ApiException
     */
    public FileSignTaskRsp createSignTaskByFileId()
            throws ApiException {
        List<SignerReq> signerReqs = new ArrayList();
        SignerReq signerReq = new SignerReq();
        signerReq.setAuthorizedUnionId(null);
        signerReq.setUnionId(unionId);
        signerReq.setSignOrder(203);


        List<FileSignReq> fileSignReqs = new ArrayList();
        List<SignHereReq> signHereReqs = new ArrayList();
        SignHereReq signHereReq = new SignHereReq();
        signHereReq.setPageNumber(1);
        signHereReq.setXCoordinate(160.6);
        signHereReq.setYCoordinate(160.6);
        signHereReqs.add(signHereReq);

        signerReq.setFileSigns(fileSignReqs);
        signerReqs.add(signerReq);


        List<FileReq> fileReqs = new ArrayList();
        FileReq fileReq = new FileReq();
        fileReq.setFileId(fileId);
        fileReqs.add(fileReq);


        FileSignTaskReq signTaskReq = new FileSignTaskReq();
        signTaskReq.setAutoArchive(2);
        signTaskReq.setSender(null);
        signTaskReq.setSort(1);
        //create 创建，sent 发起
        signTaskReq.setStatus("create");
        signTaskReq.setTaskSubject("房屋租赁合同");
        signTaskReq.setAutoArchive(0);

        signTaskReq.setSigners(signerReqs);
        signTaskReq.setFiles(fileReqs);
        signTaskReq.setToken(token);
        BaseRsp<FileSignTaskRsp> rsp = signTaskClient.createSignTaskByFileId(signTaskReq);
        CommonUtil.checkResult(rsp);

        return rsp.getData();
    }


    /**
     * 获取签署链接
     *
     * @throws ApiException
     */
    public void getSignUrl() throws ApiException {
        GetSignUrlReq req = new GetSignUrlReq();
        req.setUnionId(unionId);
        req.setRedirectUrl("http://www.fadada.com");
        req.setTaskId(taskId);
        req.setToken(token);
        BaseRsp<GetSignUrlRsp> rsp = signTaskClient.getSignUrl(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 基于模板创建签署任务
     */
    public TemplateSignTaskRsp createByTemplate() throws ApiException {
        CreateByDraftIdReq templateSignTaskReq = new CreateByDraftIdReq();
        templateSignTaskReq.setToken(token);
        templateSignTaskReq.setTaskSubject("房屋租赁合同");
        templateSignTaskReq.setSort(1);
        templateSignTaskReq.setStatus("sent");
        templateSignTaskReq.setDraftId(draftId);
        List<TemplateSignerReq> signers = new ArrayList<>();
        TemplateSignerReq signerReq1 = new TemplateSignerReq();
        signerReq1.setUnionId(unionId);
        signerReq1.setTemplateRoleName("租客");
        signerReq1.setSignOrder(112);
        NoticeReq noticeReq1 = new NoticeReq();
        noticeReq1.setNotifyWay(1);
        noticeReq1.setNotifyAddress(mobile);
        signerReq1.setNotice(noticeReq1);
        signers.add(signerReq1);
        templateSignTaskReq.setSigners(signers);

        BaseRsp<TemplateSignTaskRsp> rsp = signTaskClient.createSignTaskByDraftId(templateSignTaskReq);
        CommonUtil.checkResult(rsp);
        return rsp.getData();
    }

    /**
     * 撤销签署任务
     */
    public void cancelSignTask() throws ApiException {
        CancelSignTaskReq cancelSignTaskReq = new CancelSignTaskReq();
        cancelSignTaskReq.setToken(token);
        cancelSignTaskReq.setRemark("由于乙方某某原因，撤销该任务");
        cancelSignTaskReq.setTaskId(taskId);
        BaseRsp rsp = signTaskClient.cancelSignTask(cancelSignTaskReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取签署任务发起链接 签署任务status为create调用该方法
     */
    public void getSentUrl() throws ApiException {
        GetSentUrlReq getSentUrlReq = new GetSentUrlReq();
        getSentUrlReq.setToken(token);
        getSentUrlReq.setTaskId(taskId);
        BaseRsp<GetSentUrlRsp> rsp = signTaskClient.getSentUrl(getSentUrlReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取预览链接
     */
    public void getSignPreviewUrl() throws ApiException {
        GetSignPreviewUrlReq getSignPreviewUrlReq = new GetSignPreviewUrlReq();
        getSignPreviewUrlReq.setToken(token);
        getSignPreviewUrlReq.setTaskId(taskId);
        BaseRsp<GetSignPreviewUrlRsp> rsp = signTaskClient.getSignPreviewUrl(getSignPreviewUrlReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 催签
     *
     * @throws ApiException
     */
    public void urgeSign() throws ApiException {
        UrgeSignReq urgeSignReq = new UrgeSignReq();
        urgeSignReq.setToken(token);
        urgeSignReq.setTaskId(taskId);
        BaseRsp rsp = signTaskClient.urgeSign(urgeSignReq);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 获取签署任务详请
     *
     * @throws ApiException
     */
    public void getTaskDetailByTaskId() throws ApiException {
        GetTaskDetailReq getTaskDetailReq = new GetTaskDetailReq();
        getTaskDetailReq.setToken(token);
        getTaskDetailReq.setTaskId(taskId);
        getTaskDetailReq.setUnionId(unionId);
        BaseRsp<GetTaskDetailsRes> rsp = signTaskClient.getTaskDetailByTaskId(getTaskDetailReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 依据原始文件创建签署任务
     *
     * @throws ApiException
     */
    public void createTaskByFile() throws ApiException {
        CreateTaskByFileReq req = new CreateTaskByFileReq();
        req.setToken(token);
        req.setTaskSubject("房屋租赁");
        req.setSort(1);
        req.setStatus("sent");


        CreateTaskByFileReq.TaskSenderInfo sender = new CreateTaskByFileReq.TaskSenderInfo();
        sender.setUnionId(unionId);
        req.setSender(sender);

        CreateTaskByFileReq.SignerInfo signerInfo = new CreateTaskByFileReq.SignerInfo();
        CreateTaskByFileReq.ExternalSignerReq externalSignerReq = new CreateTaskByFileReq.ExternalSignerReq();
        externalSignerReq.setMobile(mobile);
        externalSignerReq.setPersonName("用户名称");
        signerInfo.setExternalSigner(externalSignerReq);

        List<CreateTaskByFileReq.SignRegionInfo> signRegions = new ArrayList<>();
        CreateTaskByFileReq.SignRegionInfo signRegionInfo = new CreateTaskByFileReq.SignRegionInfo();
        signRegionInfo.setFileId(fileId);

        List<SignHereReq> signHeres = new ArrayList<>();
        SignHereReq signHereReq = new SignHereReq();
        signHereReq.setPageNumber(0);
        signHereReq.setXCoordinate(234.8);
        signHereReq.setYCoordinate(786.2);
        signHeres.add(signHereReq);
        signRegionInfo.setSignHeres(signHeres);
        signRegions.add(signRegionInfo);

        signerInfo.setSignRegions(signRegions);

        List<CreateTaskByFileReq.SignerInfo> signers = new ArrayList<>();
        signers.add(signerInfo);
        req.setSigners(signers);

        List<FileReq> fileReqs = new ArrayList<>();
        FileReq fileReq = new FileReq();
        fileReq.setFileId(fileId);
        fileReqs.add(fileReq);
        req.setFiles(fileReqs);

        BaseRsp<CreateTaskByFileRsp> rsp = signTaskClient.createTaskByFile(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 创建批次号批量任务
     *
     * @throws ApiException
     */
    public void batchCreateByDraftId() throws ApiException {
        BatchCreateByDraftIdReq req = new BatchCreateByDraftIdReq();
        req.setToken(token);
        BatchCreateByDraftIdReq.SenderInfo sender = new BatchCreateByDraftIdReq.SenderInfo();
        sender.setSignIntendWay(2);
        sender.setUnionId(unionId);
        req.setSender(sender);
        List<BatchDraftIdSigntaskInfoReq> lists = new ArrayList<>();
        BatchDraftIdSigntaskInfoReq ba1 = new BatchDraftIdSigntaskInfoReq();
        ba1.setDraftId(draftId);
        TemplateSenderReq senderReq = new TemplateSenderReq();
        senderReq.setSignWay(1);
        senderReq.setSealId(sealId);
        senderReq.setTemplateRoleName("发起方");
        ba1.setSender(senderReq);
        BatchTemplateSignerReq batchTemplateSignerReq = new BatchTemplateSignerReq();
        ExternalSigner ext = new ExternalSigner();
        ext.setMobile(mobile);
        ext.setPersonName("用户姓名");
        batchTemplateSignerReq.setExternalSigner(ext);
        batchTemplateSignerReq.setSignOrder(200);
        batchTemplateSignerReq.setTemplateRoleName("DBA");
        List<BatchTemplateSignerReq> signers = new ArrayList<>();
        signers.add(batchTemplateSignerReq);

        ba1.setSigners(signers);
        ba1.setSort(1);
        ba1.setTaskSubject("测试合同");
        lists.add(ba1);
        req.setSigntasks(lists);
        BaseRsp<BatchCreateByDraftIdRsp> rsp = signTaskClient.batchCreateByDraftId(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 批次号批量任务新增
     *
     * @throws ApiException
     */
    public void batchAddByDraftId() throws ApiException {
        BatchAddByDraftIdReq req = new BatchAddByDraftIdReq();
        req.setToken(token);
        req.setBatchNo(batchNo);
        List<BatchDraftIdSigntaskInfoReq> lists = new ArrayList<>();
        BatchDraftIdSigntaskInfoReq ba1 = new BatchDraftIdSigntaskInfoReq();
        ba1.setDraftId(draftId);
        TemplateSenderReq senderReq = new TemplateSenderReq();
        senderReq.setSignWay(1);
        senderReq.setSealId(sealId);
        senderReq.setTemplateRoleName("发起方");
        senderReq.setSignOrder(52);
        ba1.setSender(senderReq);
        BatchTemplateSignerReq batchTemplateSignerReq = new BatchTemplateSignerReq();
        ExternalSigner ext = new ExternalSigner();
        ext.setMobile(mobile);
        ext.setPersonName("小辉");
        batchTemplateSignerReq.setExternalSigner(ext);
        batchTemplateSignerReq.setSignOrder(200);
        batchTemplateSignerReq.setTemplateRoleName("DBA");
        List<BatchTemplateSignerReq> signers = new ArrayList<>();
        signers.add(batchTemplateSignerReq);

        ba1.setSigners(signers);
        ba1.setSort(1);
        ba1.setTaskSubject("批量合同");


        lists.add(ba1);
        lists.add(ba1);
        lists.add(ba1);
        req.setSigntasks(lists);
        BaseRsp<BatchCreateByDraftIdRsp> rsp = signTaskClient.batchAddByDraftId(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 批次号下签署任务发起
     *
     * @throws ApiException
     */
    public void batchSent() throws ApiException {
        BatchSentReq req = new BatchSentReq();
        req.setToken(token);
        req.setBatchNo(batchNo);
        BaseRsp<BatchCreateByDraftIdRsp> rsp = signTaskClient.batchSent(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 批次号获取签署链接
     *
     * @throws ApiException
     */
    public void batchGetSignUrl() throws ApiException {
        BatchGetSignUrlReq req = new BatchGetSignUrlReq();
        req.setToken(token);
        req.setBatchNo(batchNo);
        BaseRsp<BatchGetSignUrlRsp> rsp = signTaskClient.batchGetSignUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 根据批次号获取签署任务
     *
     * @throws ApiException
     */
    public void batchGetSigntasksByBatchNo() throws ApiException {
        BatchGetSigntasksByBatchNoReq req = new BatchGetSigntasksByBatchNoReq();
        req.setToken(token);
        req.setBatchNo(batchNo);
        BaseRsp<BatchGetSigntasksByBatchNoRsp> rsp = signTaskClient.batchGetSigntasksByBatchNo(req);
        CommonUtil.checkResult(rsp);
    }


}
