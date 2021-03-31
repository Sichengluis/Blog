package com.scluis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Sichengluis on 2021/2/18 11:58
 */
public class fileUtil {
    private static final Logger logger= LoggerFactory.getLogger(fileUtil.class);
    /**
     * 功能描述: 获取文件后缀名
     * @Param: [fileName 文件名]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/18 12:02
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 功能描述: 生成新的文件名
     * @Param: [fileOriginName 原文件名]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/18 12:03
     */
    public static String getFileName(String fileOriginName){
        return getUUID() + getSuffix(fileOriginName);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 功能描述: 处理文件上传
     * @Param: [file  文件, path 文件存储路径, fileName 原文件名]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/18 12:03
     */
    public static String save(MultipartFile file, String path, String fileName){
        logger.info("首图上传失败测试-进入save方法");
        String newFileName = getFileName(fileName);
        logger.info("首图上传失败测试-得到newFileName，newFileName为："+newFileName);
        // 生成新的文件名
        String realPath = path + newFileName;
        logger.info("首图上传失败测试-得到realPath，realPath为："+realPath);

        //使用原文件名
//        String realPath = path + "/" + fileName;

        File dest = new File(realPath);
        logger.info("首图上传失败测试-创建dest");
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            logger.info("首图上传失败测试-父路径不存在，创建相关文件夹");
            dest.getParentFile().mkdirs();
            logger.info("首图上传失败测试-创建完成");
        }

        try {
            //保存文件
            logger.info("首图上传失败测试-开始保存文件");
            file.transferTo(dest);
            logger.info("首图上传失败测试-保存完成");
            return newFileName;
        } catch (Exception e) {
            logger.info("首图上传失败测试-save出现异常，异常信息为："+e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
