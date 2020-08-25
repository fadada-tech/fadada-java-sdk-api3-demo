package com.fadada.api;


import com.fadada.api.bean.rsp.BaseRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author yh
 * @version 1.0.0
 * @ClassName CommonUtil.java
 * @Description 工具类
 * @Param
 * @createTime 2020年07月08日 17:28:00
 */
public class CommonUtil {

    public static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil() {
    }

    public static void checkResult(BaseRsp baseRsp) {
        if (baseRsp == null && !baseRsp.isSuccess()) {
            log.error("请求失败：{}", baseRsp);
            return;
        }
        log.info("请求结果为：{}", baseRsp);
    }

    public static void fileSink(byte[] fileBytes, String path, String fileName) {
        File f = new File(path + fileName);
        if (f.exists()) {
            f.delete();
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(f); BufferedOutputStream bw = new BufferedOutputStream(fos)) {
            bw.write(fileBytes);
        } catch (Exception e) {
            log.error("文件写入失败：{}", e);
        }
    }
}
