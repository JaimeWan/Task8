package com.service;

import com.POJO.Email;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Jaime
 * @Date: 2018/5/31 2:23
 * @Description: *
 */
public interface EmailService {
    String convert(List<Email> dataList);
    String send_template(String address, String username, int vcode) throws ClientProtocolException, IOException;
}
