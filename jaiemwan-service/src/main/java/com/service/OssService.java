package com.service;

import com.aliyun.oss.OSSClient;

import java.io.File;
import java.io.InputStream;

/**
 * @Author: Jaime
 * @Date: 2018/5/31 2:16
 * @Description: *
 */
public interface OssService {
    String uploadObjectToOSS(OSSClient client, File file, String bucketName, String diskName);
    InputStream getOSS2InputStream(OSSClient client, String bucketName, String diskName, String key);
    void deleteFile(OSSClient client, String bucketName, String diskName, String key);
    String getContentType(String fileName);
}
