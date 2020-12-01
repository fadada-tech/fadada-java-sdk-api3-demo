package com.fadada.api;

import com.fadada.api.bean.req.issp.DownloadFileReq;
import com.fadada.api.bean.req.issp.UploadFileReq;
import com.fadada.api.bean.rsp.BaseRsp;
import com.fadada.api.bean.rsp.document.DownLoadFileRsp;
import com.fadada.api.bean.rsp.issp.UploadFileRsp;
import com.fadada.api.client.IsspLocalClient;
import com.fadada.api.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yh128
 * @className IsspLocalDemo
 * @description IsspLocalDemo
 * @createTime 2020年12月1日 09:49:17
 */
public class IsspLocalDemo {

    public static Logger log = LoggerFactory.getLogger(IsspLocalDemo.class);
    private String fileUuid = "文件uuid";
    private String fileName = "文件名称";
    private static String isspServerUrl = "issp服务请求地址";

    private static IsspLocalClient isspLocalClient;

    public static void main(String[] args) throws ApiException {
        isspLocalClient = new IsspLocalClient(isspServerUrl);
        IsspLocalDemo isspLocalDemo = new IsspLocalDemo();
        isspLocalDemo.uploadFile();
        isspLocalDemo.downloadFile();
    }


    /**
     * 文件上传
     */
    public void uploadFile() throws ApiException {
        UploadFileReq uploadFileReq = new UploadFileReq();
        uploadFileReq.setFileType(1);
        String path = getClass().getClassLoader().getResource("sampleContract.pdf").getFile();
        File file = new File(path);
        BaseRsp<UploadFileRsp> rsp = isspLocalClient.uploadFile(file, uploadFileReq);
        System.out.println(rsp);
    }

    /**
     * 文件下载
     */
    public void downloadFile() throws ApiException {
        DownloadFileReq downloadFileReq = new DownloadFileReq();
        DownloadFileReq.FileInfo fileInfo = new DownloadFileReq.FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setFileType(2);
        fileInfo.setFileUuid(fileUuid);
        List<DownloadFileReq.FileInfo> list = new ArrayList<>();
        list.add(fileInfo);
        downloadFileReq.setFileInfos(list);
        BaseRsp<DownLoadFileRsp> rsp = isspLocalClient.downloadFile(downloadFileReq);
        if (rsp.isSuccess()) {
            String path = getClass().getClassLoader().getResource("").getPath();
            CommonUtil.fileSink(rsp.getData().getFileBytes(), path, "文件.zip");
        } else {
            log.error("ISSP下载文件失败：{}", rsp.toString());
        }
    }


}
