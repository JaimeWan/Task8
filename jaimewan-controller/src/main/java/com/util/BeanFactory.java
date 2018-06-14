package com.util;

import com.service.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: Jaime
 * @Date: 2018/6/2 9:47
 * @Description: *
 */

public class BeanFactory {
   private LoginService loginService;
   private OssService ossService;
    private EmailService emailService;
   private RedisService redisService;
   private SMSservice smsService;
   private static ApplicationContext ctx= new ClassPathXmlApplicationContext("spring/rmi.xml");
    public LoginService getLoginService() {
        int a= (int) ((Math.random()*999999)%2);
        if (a==0){
            LoginService loginService= (LoginService) ctx.getBean("clientRmiTest1");
            System.out.println("s1");
            return loginService;
        }
      else {
            loginService= (LoginService) ctx.getBean("clientRmiTest12");
            System.out.println("s2");
            return loginService;
        }
    }
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public OssService getOssService() {
        int a= (int) ((Math.random()*999999)%2);
        if (a==0){
            ossService= (OssService) ctx.getBean("clientRmiTest2");
            return ossService;
        }
        else {
            ossService = (OssService) ctx.getBean("clientRmiTest22");
            return ossService;
        }
    }

    public void setOssService(OssService ossService) {
        this.ossService = ossService;
    }

    public EmailService getEmailService() {
        int a= (int) ((Math.random()*999999)%2);
        if (a==0){
            emailService= (EmailService) ctx.getBean("clientRmiTest3");
            return emailService;
        }
        else {
            emailService= (EmailService) ctx.getBean("clientRmiTest32");
            return emailService;
        }
    }

    public void setEmailService(EmailService emailUtil) {
        this.emailService = emailUtil;
    }

    public RedisService getRedisService() {
        int a= (int) ((Math.random()*999999)%2);
        if (a==0){
            redisService= (RedisService) ctx.getBean("clientRmiTest4");
            return redisService;
        }
        else {
            redisService= (RedisService) ctx.getBean("clientRmiTest42");
            return redisService;
        }
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public SMSservice getSmsService() {
        int a= (int) ((Math.random()*999999)%2);
        if (a==0){
            smsService= (SMSservice) ctx.getBean("clientRmiTest5");
            return smsService;
        }
        else {
            smsService= (SMSservice) ctx.getBean("clientRmiTest52");
            return smsService;
        }
    }

    public void setSmsService(SMSservice smsService) {
        this.smsService = smsService;
    }
}
