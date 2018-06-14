package com.service;

import com.aliyun.oss.ClientException;

/**
 * @Author: Jaime
 * @Date: 2018/5/19 22:20
 * @Description: *
 */
public interface SMSservice {
    public String smssend(String telephone, String vcode) ;
}
