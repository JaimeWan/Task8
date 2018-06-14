package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;

/**
 * @Author: Jaime
 * @Date: 2018/5/29 1:37
 * @Description: *
 */
public class rmitest {

    public static void main(String[] args) {

        System.out.println("over");
        new ClassPathXmlApplicationContext("spring/*");
        System.out.println("service1");
    }
}
