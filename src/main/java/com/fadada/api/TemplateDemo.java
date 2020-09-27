package com.fadada.api;

import com.fadada.api.bean.req.document.TemplateFileReq;
import com.fadada.api.bean.req.template.*;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.document.DownLoadFileRsp;
import com.fadada.api.bean.rsp.template.*;
import com.fadada.api.client.TemplateClient;
import com.fadada.api.exception.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yh
 * @className TemplateDemo
 * @description 模板使用demo
 * @createTime 2020年7月31日 14:37:20
 */
public class TemplateDemo extends BaseDemo {
    private String templateId = null;
    private String fileId = "模板文件ID";

    private TemplateClient templateClient;

    public TemplateDemo(DefaultFadadaApiClient templateClient) {
        this.templateClient = new TemplateClient(templateClient);
    }

    public static void main(String[] args) {
        try {
            BaseDemo baseDemo = new BaseDemo();
            DefaultFadadaApiClient client = baseDemo.getClient();
            TemplateDemo templateDemo = new TemplateDemo(client);
            // 获取token
            baseDemo.getToken(client);
            // 上传企业模板文件附件
            templateDemo.uploadCompanyTemplateFile();
            // 修改企业模板信息
//            templateDemo.updateCompanyTemplate();
            // 获取企业模板控件维护页面url
//            templateDemo.getEditCompanyTemplateUrl();
            // 删除企业模板文件附件
//            templateDemo.delCompanyTemplateFile();
            // 获取企业模板列表
//            templateDemo.queryCompanyTemplateList();
            // 下载企业模板文件
//            templateDemo.downloadCompanyTemplateFile();
            // 获取模板详请
//            templateDemo.getTemplateDetailById();
            // 模板填充
//            templateDemo.createByTemplate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传企业模板文件附件
     */
    public void uploadCompanyTemplateFile() throws ApiException {
        UploadCompanyTemplateFileReq req = new UploadCompanyTemplateFileReq();
        req.setFileType(1, templateId);
        String path = getClass().getClassLoader().getResource("sampleContract.pdf").getFile();
        File file = new File(path);
        BaseRsp<UploadCompanyTemplateFileRsp> rsp = templateClient.uploadCompanyTemplateFile(token, req, file);
        CommonUtil.checkResult(rsp);
        templateId = rsp.getData().getTemplateId();
    }

    /**
     * 修改企业模板信息
     */
    public void updateCompanyTemplate() throws ApiException {
        UpdateCompanyTemplateReq req = new UpdateCompanyTemplateReq();
        UpdateCompanyTemplateReq.TemplateInfo templateInfo = new UpdateCompanyTemplateReq.TemplateInfo();
        templateInfo.setTemplateName("房屋租赁合同");
        templateInfo.setTemplateRemark("房屋租赁使用合同模板");
        templateInfo.setTemplateId(templateId);
        templateInfo.setSortType(1);
        UpdateCompanyTemplateReq.Target target = new UpdateCompanyTemplateReq.Target();
        target.setRoleName("租户");
        target.setRoleType(1);
        List<UpdateCompanyTemplateReq.Target> lists = new ArrayList<>();
        lists.add(target);
        templateInfo.setTargets(lists);
        req.setTemplateInfo(templateInfo);
        BaseRsp<UpdateCompanyTemplateRsp> rsp = templateClient.updateCompanyTemplate(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 获取企业模板控件维护页面url
     */
    public void getEditCompanyTemplateUrl() throws ApiException {
        GetEditCompanyTemplateUrlReq req = new GetEditCompanyTemplateUrlReq();
        req.setTemplateId(templateId);
        BaseRsp<GetEditCompanyTemplateUrlRsp> rsp = templateClient.getEditCompanyTemplateUrl(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 删除企业模板文件附件
     */
    public void delCompanyTemplateFile() throws ApiException {
        DelCompanyTemplateFileReq req = new DelCompanyTemplateFileReq();
        req.setTemplateIdAndFileId(templateId, fileId);
        BaseRsp rsp = templateClient.delCompanyTemplateFile(token, req);
        CommonUtil.checkResult(rsp);
    }

    /**
     * 企业模板列表查询
     */
    public void queryCompanyTemplateList() throws ApiException {
        QueryCompanyTemplateListReq req = new QueryCompanyTemplateListReq();
        QueryCompanyTemplateListReq.QueryInfo queryInfo = new QueryCompanyTemplateListReq.QueryInfo();
        queryInfo.setCurrentPageNo(1);
        queryInfo.setPageSize(10);
        req.setQueryInfo(queryInfo);
        BaseRsp<QueryCompanyTemplateListRsp> rsp = templateClient.queryCompanyTemplateList(token, req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 模板文件下载
     */
    public void downloadCompanyTemplateFile() throws ApiException {
        DownloadCompanyTemplateFileReq req = new DownloadCompanyTemplateFileReq();
        DownloadCompanyTemplateFileReq.TemplateInfo templateInfo = new DownloadCompanyTemplateFileReq.TemplateInfo();
        templateInfo.setTemplateId(templateId);
        req.setTemplateInfo(templateInfo);
        BaseRsp<DownLoadFileRsp> rsp = templateClient.downloadCompanyTemplateFile(token, req);
        if (rsp.isSuccess()) {
            String path = getClass().getClassLoader().getResource("").getPath();
            CommonUtil.fileSink(rsp.getData().getFileBytes(), path, "模板文件.zip");
        } else {
            log.error("模板文件下载失败：{}", rsp.toString());
        }
    }

    /**
     * 获取模板详请
     *
     * @throws ApiException
     */
    public void getTemplateDetailById() throws ApiException {
        GetTemplateDetailByIdReq req = new GetTemplateDetailByIdReq();
        req.setTemplateId(templateId);
        BaseRsp<GetTemplateDetailByIdRsp> rsp = templateClient.getTemplateDetailById(token, req);
        CommonUtil.checkResult(rsp);
    }


    /**
     * 模板填充接口
     *
     * @throws ApiException
     */
    public void createByTemplate() throws ApiException {
        CreateByTemplateReq req = new CreateByTemplateReq();
        req.setTemplateId(templateId);
        List<TemplateFileReq> templateFiles = new ArrayList<>();

        TemplateFileReq t1 = new TemplateFileReq();
        Map<String, String> f1 = new HashMap<>();
        f1.put("姓名", "张三");
        f1.put("性别", "女");

        t1.setTemplateFileId(fileId);
        t1.setDocumentFileName("第1份文档");
        t1.setFormFields(f1);
        templateFiles.add(t1);

        TemplateFileReq t2 = new TemplateFileReq();
        Map<String, String> f2 = new HashMap<>();
        f2.put("姓名", "张三");
        f2.put("性别", "男");
        t2.setTemplateFileId(fileId);
        t2.setDocumentFileName("第2份文档");
        t2.setFormFields(f2);

        templateFiles.add(t2);
        req.setTemplateFiles(templateFiles);
        BaseRsp<DraftRsp> rsp = templateClient.createByTemplate(token, req);
        CommonUtil.checkResult(rsp);
    }

}
