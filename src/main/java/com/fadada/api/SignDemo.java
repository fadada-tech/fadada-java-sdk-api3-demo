package com.fadada.api;

import com.fadada.api.bean.req.sign.*;
import com.fadada.api.bean.req.sign.batch.*;
import com.fadada.api.bean.req.sign.draft.CreateTaskByDraftIdReq;
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
import com.fadada.api.utils.string.StringUtil;

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

    private String taskId = "签署任务编号";
    private String fileId = "文件编号";
    private String draftId = "模板编号";
    private String sealId = "印章编号";
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
            // 依据原始文件创建签署任务
            signDemo.createTaskByFile();

            // 根据草稿Id创建签署任务
            signDemo.createSignTaskByDraftId();

            // 解锁签署任务
            signDemo.unlock();

            // 获取快捷签署链接
            signDemo.getQuickSignUrl();

            // ------- 批量任务------
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


    /**
     * 根据草稿id创建签署任务
     *
     * @throws ApiException
     */
    public void createSignTaskByDraftId()
            throws ApiException {
        CreateTaskByDraftIdReq req = new CreateTaskByDraftIdReq();
        req.setToken(token);
        req.setDraftId(draftId);
        req.setSort(2);
        req.setTaskSubject("签署任务主题");
        req.setAutoArchive(1);
        req.setStatus("sent");

        CreateTaskByDraftIdReq.CreateTaskSignerInfo createTaskSignerInfo =
                new CreateTaskByDraftIdReq.CreateTaskSignerInfo();
        createTaskSignerInfo.setTemplateRoleName("角色名称");
        createTaskSignerInfo.setSignOrder(1);
        ExternalSignerReq externalSignerReq = new ExternalSignerReq();
        externalSignerReq.setMobile("手机号码");
        externalSignerReq.setPersonName("个人名称");
        createTaskSignerInfo.setExternalSigner(externalSignerReq);
        CreateTaskByDraftIdReq.SignerInfo signerInfo = new CreateTaskByDraftIdReq.SignerInfo();
        CreateTaskByDraftIdReq.SignatoryInfo signatoryInfo = new CreateTaskByDraftIdReq.SignatoryInfo();
        signatoryInfo.setSignerId(unionId);
        signerInfo.setSignatory(signatoryInfo);
        createTaskSignerInfo.setSigner(signerInfo);
        List<CreateTaskByDraftIdReq.CreateTaskSignerInfo> infos = new ArrayList<>();
        infos.add(createTaskSignerInfo);
        req.setSigners(infos);
        BaseRsp<CreateTaskByDraftIdRsp> rsp = signTaskClient.createTaskByDraftId(req);
        CommonUtil.checkResult(rsp);
    }


    public void createTaskByFile() throws ApiException {
        CreateTaskByFileReq req = new CreateTaskByFileReq();
        req.setToken(token);
        req.setTaskSubject("签署任务主题");
        req.setSort(2);
        req.setStatus("sent");

        CreateTaskByFileReq.SignerInfo signerInfo1 = new CreateTaskByFileReq.SignerInfo();
        //应用内部签署方对象1  个人手动签署, 设置签署文件对应签署坐标
        signerInfo1.setSigner(getSignerReq("个人unionId",
                null, 0, null, null, null));

        signerInfo1.setSignRegions(getSignRegion(1, null, 0, 600D, 600D,
                "文件编号"));


        CreateTaskByFileReq.SignerInfo signerInfo2 = new CreateTaskByFileReq.SignerInfo();
        //应用内部企业自动签署的场景， companyUnionId必须是平台方企业，或者，作过授权自动签署给平台方的企业
        signerInfo2.setSigner(getSignerReq(null,
                "企业unionId", 1, null, null, null));
        signerInfo2.setSignRegions(getSignRegion(1, null, 0, 300D, 300D,
                "文件编号"));

        CreateTaskByFileReq.SignerInfo signerInfo3 = new CreateTaskByFileReq.SignerInfo();
        //应用内部企业手动签署的场景
        signerInfo3.setSigner(getSignerReq(null,
                "企业unionId", 1, null, null, null));
        signerInfo3.setSignRegions(getSignRegion(1, null, 0, 900D, 900D,
                "文件编号"));


        //签署列表
        List<CreateTaskByFileReq.SignerInfo> signers = new ArrayList<>();
        signers.add(signerInfo1);
        signers.add(signerInfo2);
        signers.add(signerInfo3);
        req.setSigners(signers);

        //所有的签署文件集合
        List<FileReq> fileReqs = new ArrayList<>();
        FileReq fileReq = new FileReq();
        fileReq.setFileId("文件编号");
        fileReqs.add(fileReq);
        req.setFiles(fileReqs);

        BaseRsp<CreateTaskByFileRsp> rsp = signTaskClient.createTaskByFile(req);
        CommonUtil.checkResult(rsp);

    }

    /**
     * 解锁签署任务
     *
     * @throws ApiException
     */
    public void unlock() throws ApiException {
        UnlockReq req = new UnlockReq();
        req.setToken(token);
        req.setTaskId(taskId);
        List<UnlockReq.UnlockSignerInfo> signers = new ArrayList<>();
        UnlockReq.UnlockSignerInfo signerInfo = new UnlockReq.UnlockSignerInfo();
        UnlockReq.Signer signer = new UnlockReq.Signer();
        SignatoryReq signatoryReq = new SignatoryReq();
        signatoryReq.setSignerId(unionId);
        signer.setSignatory(signatoryReq);
        signerInfo.setSigner(signer);
        signers.add(signerInfo);
        req.setSigners(signers);
        BaseRsp<List<UnlockRsp>> rsp = signTaskClient.unlock(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 获取快捷签署链接
     *
     * @throws ApiException
     */
    public void getQuickSignUrl() throws ApiException {
        GetQuickSignUrlReq req = new GetQuickSignUrlReq();
        req.setToken(token);
        req.setMobile("手机号码");
        req.setTaskId(taskId);
        BaseRsp<GetQuickSignUrlRsp> rsp = signTaskClient.getQuickSignUrl(req);
        CommonUtil.checkResult(rsp);
    }

    public SignerReqV2 getSignerReq(String personUnionId, String companyUnionId,
                                    Integer signWay, Integer signIntendWay,
                                    String personSealId, String companySealId) {

        SignerReqV2 signerReq1 = new SignerReqV2();
        if (StringUtil.isNotBlank(personUnionId)) {
            SignatoryReq signatory = new SignatoryReq();
            signatory.setSignerId(personUnionId);
            if (StringUtil.isNotBlank(personSealId)) {
                SealReq sealReq = new SealReq();
                sealReq.setSealId(personSealId);
                signatory.setSeal(sealReq);
            }
            signerReq1.setSignatory(signatory);
        }
        if (StringUtil.isNotBlank(companyUnionId)) {
            CorpReq corp = new CorpReq();
            corp.setCorpId(companyUnionId);
            if (StringUtil.isNotBlank(companySealId)) {
                SealReq sealReq = new SealReq();
                sealReq.setSealId(companySealId);
                corp.setSeal(sealReq);
            }
            signerReq1.setCorp(corp);
        }
        if (signWay != null || signIntendWay != null) {
            SignActionReq signAction = new SignActionReq();
            signAction.setSignIntendWay(signIntendWay);
            signAction.setSignWay(signWay);
            signerReq1.setSignAction(signAction);
        }

        return signerReq1;
    }

    private List<CreateTaskByFileReq.SignRegionInfo> getSignRegion(int type, String keyWord, int p, double x, double y, String fileId) {
        List<CreateTaskByFileReq.SignRegionInfo> signRegionsReq = new ArrayList<>();
        CreateTaskByFileReq.SignRegionInfo signRegionReq = new CreateTaskByFileReq.SignRegionInfo();
        List<SignHereReq> signHereReqs = new ArrayList<>();
        SignHereReq signHereReq = new SignHereReq();
        if (type == 1) {
            signHereReq.setPageNumber(p);
            signHereReq.setXCoordinate(x);
            signHereReq.setYCoordinate(y);
        } else {
            signHereReq.setKeyWord(keyWord);
        }
        signHereReqs.add(signHereReq);
        signRegionReq.setSignHeres(signHereReqs);
        signRegionReq.setFileId(fileId);
        signRegionReq.setType(type);
        signRegionsReq.add(signRegionReq);
        return signRegionsReq;
    }


}
