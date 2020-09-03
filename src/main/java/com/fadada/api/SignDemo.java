package com.fadada.api;

import com.fadada.api.bean.req.sign.*;
import com.fadada.api.bean.req.sign.file.*;
import com.fadada.api.bean.req.sign.template.CreateByDraftIdReq;
import com.fadada.api.bean.req.sign.template.TemplateSignerReq;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.document.UploadFileRsp;
import com.fadada.api.bean.rsp.sign.*;
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
    private String mobile = "手机号码";

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件ID闯将签署任务
     *
     * @return
     * @throws ApiException
     */
    public FileSignTaskRsp createSignTaskByFileId()
            throws ApiException {
        SigntaskSenderReq sender = new SigntaskSenderReq();

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
        BaseRsp<FileSignTaskRsp> rsp = signTaskClient.createSignTaskByFileId(token, signTaskReq);
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
        BaseRsp<GetSignUrlRsp> rsp = signTaskClient.getSignUrl(token, req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 基于模板创建签署任务
     */
    public TemplateSignTaskRsp createByTemplate() throws ApiException {
        CreateByDraftIdReq templateSignTaskReq = new CreateByDraftIdReq();
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

        BaseRsp<TemplateSignTaskRsp> rsp = signTaskClient.createSignTaskByDraftId(token, templateSignTaskReq);
        CommonUtil.checkResult(rsp);
        return rsp.getData();
    }

    /**
     * 撤销签署任务
     */
    public void cancelSignTask() throws ApiException {
        CancelSignTaskReq cancelSignTaskReq = new CancelSignTaskReq();
        cancelSignTaskReq.setRemark("由于乙方某某原因，撤销该任务");
        cancelSignTaskReq.setTaskId(taskId);
        BaseRsp rsp = signTaskClient.cancelSignTask(token, cancelSignTaskReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取签署任务发起链接 签署任务status为create调用该方法
     */
    public void getSentUrl() throws ApiException {
        GetSentUrlReq getSentUrlReq = new GetSentUrlReq();
        getSentUrlReq.setTaskId(taskId);
        BaseRsp<GetSentUrlRsp> rsp = signTaskClient.getSentUrl(token, getSentUrlReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取预览链接
     */
    public void getSignPreviewUrl() throws ApiException {
        GetSignPreviewUrlReq getSignPreviewUrlReq = new GetSignPreviewUrlReq();
        getSignPreviewUrlReq.setTaskId(taskId);
        BaseRsp<GetSignPreviewUrlRsp> rsp = signTaskClient.getSignPreviewUrl(token, getSignPreviewUrlReq);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 催签
     *
     * @throws ApiException
     */
    public void urgeSign() throws ApiException {
        UrgeSignReq urgeSignReq = new UrgeSignReq();
        urgeSignReq.setTaskId(taskId);
        BaseRsp rsp = signTaskClient.urgeSign(token, urgeSignReq);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 获取签署任务详请
     *
     * @throws ApiException
     */
    public void getTaskDetailByTaskId() throws ApiException {
        GetTaskDetailReq getTaskDetailReq = new GetTaskDetailReq();
        getTaskDetailReq.setTaskId(taskId);
        getTaskDetailReq.setUnionId(unionId);
        BaseRsp<GetTaskDetailsRes> rsp = signTaskClient.getTaskDetailByTaskId(token, getTaskDetailReq);
        CommonUtil.checkResult(rsp);
    }


}
