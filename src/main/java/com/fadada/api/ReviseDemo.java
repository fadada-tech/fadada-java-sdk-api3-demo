package com.fadada.api;

import com.fadada.api.bean.req.account.NoticeReq;
import com.fadada.api.bean.req.revise.*;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.document.DownLoadFileRsp;
import com.fadada.api.bean.rsp.revise.CreateReviseTaskRsp;
import com.fadada.api.bean.rsp.revise.GetFillFileUrlRsp;
import com.fadada.api.bean.rsp.revise.ReviseTaskDetailRsp;
import com.fadada.api.client.ReviseTaskClient;
import com.fadada.api.exception.ApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yh128
 * @className ReviseDemo
 * @description 定稿任务Demo
 * @createTime 2020年12月1日 10:14:05
 */
public class ReviseDemo extends BaseDemo {

    private ReviseTaskClient reviseTaskClient;
    private String taskId = "定稿任务编号";
    private String templateId = "模板编号";

    public ReviseDemo(FadadaApiClient fadadaApiClient) {
        this.reviseTaskClient = new ReviseTaskClient(fadadaApiClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            ReviseDemo reviseDemo = new ReviseDemo(client);
            // 获取token
            baseDemo.getToken(client);

            // 创建定稿任务
            reviseDemo.createReviseTask();
            // 获取填充链接
            reviseDemo.getFillFileUrl();
            // 定稿任务详请
            reviseDemo.reviseTaskDetail();
            // 接口填充
            reviseDemo.saveFillValues();
            // 定稿任务撤销
            reviseDemo.cancelReviseTask();
            // 下载定稿任务
            reviseDemo.downloadReviseTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建定稿任务
     *
     * @throws ApiException
     */
    private void createReviseTask() throws ApiException {
        CreateReviseTaskReq req = new CreateReviseTaskReq();
        req.setToken(token);
        req.setTemplateId(templateId);
        req.setTaskSubject("主题");
        req.setSort(1);
        req.setFinalizeWay(1);
        req.setRedirectUrl("重定向地址");
        List<CreateReviseTaskReq.FillRoleInfo> roles = new ArrayList<>();
        CreateReviseTaskReq.FillRoleInfo fillRoleInfo = new CreateReviseTaskReq.FillRoleInfo();
        fillRoleInfo.setUnionId(unionId);
        fillRoleInfo.setRoleName("角色名称");
        NoticeReq noticeReq = new NoticeReq();
        noticeReq.setNotifyAddress("手机号码");
        noticeReq.setNotifyWay(1);
        fillRoleInfo.setNotice(noticeReq);

        CreateReviseTaskReq.FillRoleInfo fillRoleInfo1 = new CreateReviseTaskReq.FillRoleInfo();
        fillRoleInfo1.setUnionId(unionId);
        fillRoleInfo1.setRoleName("角色名称");
        NoticeReq noticeReq1 = new NoticeReq();
        noticeReq1.setNotifyAddress("手机号码");
        noticeReq1.setNotifyWay(1);
        fillRoleInfo1.setNotice(noticeReq);
        roles.add(fillRoleInfo);
        roles.add(fillRoleInfo1);
        req.setFillRoles(roles);
        BaseRsp<CreateReviseTaskRsp> rsp = reviseTaskClient.createReviseTask(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取填写链接
     *
     * @throws ApiException
     */
    private void getFillFileUrl() throws ApiException {
        GetFillFileUrlReq req = new GetFillFileUrlReq();
        req.setToken(token);
        req.setTaskId(taskId);
        req.setRoleName("角色名称");
        BaseRsp<GetFillFileUrlRsp> rsp = reviseTaskClient.getFillFileUrl(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 定稿任务详请
     *
     * @throws ApiException
     */
    private void reviseTaskDetail() throws ApiException {
        ReviseTaskDetailReq req = new ReviseTaskDetailReq();
        req.setToken(token);
        req.setTaskId(taskId);
        BaseRsp<ReviseTaskDetailRsp> rsp = reviseTaskClient.reviseTaskDetail(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 接口填充
     *
     * @throws ApiException
     */
    private void saveFillValues() throws ApiException {
        SaveFillValuesReq req = new SaveFillValuesReq();
        req.setToken(token);
        req.setTaskId(taskId);
        List<SaveFillValuesReq.RoleFillValueInfo> fillValueInfos = new ArrayList<>();
        SaveFillValuesReq.RoleFillValueInfo valueInfo = new SaveFillValuesReq.RoleFillValueInfo();
        valueInfo.setRoleName("角色名称");
        List<SaveFillValuesReq.FillValueInfo> valueInfos = new ArrayList<>();
        SaveFillValuesReq.FillValueInfo fillValueInfo = new SaveFillValuesReq.FillValueInfo();
        fillValueInfo.setFileId("填充的文件Id");
        // 填充的值
        fillValueInfo.setValues("{\"name\":\"张三\",\"address\":\"深圳\"}");
        valueInfos.add(fillValueInfo);
        valueInfo.setFillValues(valueInfos);
        fillValueInfos.add(valueInfo);
        req.setRoleFillValues(fillValueInfos);
        BaseRsp rsp = reviseTaskClient.saveFillValues(req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 定稿任务撤销
     *
     * @throws ApiException
     */
    private void cancelReviseTask() throws ApiException {
        CancelReviseTaskReq req = new CancelReviseTaskReq();
        req.setToken(token);
        req.setTaskId(taskId);
        BaseRsp rsp = reviseTaskClient.cancelReviseTask(req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 下载定稿任务
     *
     * @throws ApiException
     */
    private void downloadReviseTask() throws ApiException {
        DownloadReviseTaskReq req = new DownloadReviseTaskReq();
        req.setToken(token);
        req.setTaskId(taskId);
        req.setDownloadWay(0);
        BaseRsp<DownLoadFileRsp> rsp = reviseTaskClient.downloadReviseTask(req);
        if (rsp.isSuccess()) {
            CommonUtil.fileSink(rsp.getData().getFileBytes(), "d:\\", "定稿任务.zip");
        } else {
            log.error("下载定稿任务失败：{}", rsp);
        }
    }


}
