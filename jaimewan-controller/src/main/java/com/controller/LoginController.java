package com.controller;


import com.POJO.Register;
import com.POJO.SignIn;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.service.EmailService;
import com.service.LoginService;
import com.service.RedisService;
import com.service.SMSservice;
import com.util.BeanFactory;
import com.util.ViaPictureNameCreater;
import com.util.jwtutil.Constant;
import com.util.jwtutil.JwtUtil;
import com.util.verificationCodeCreater;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author: Jaime
 * @Date: 2018/4/12 15:54
 * @Description: for Task5*
 */
@Controller
public class LoginController {

    private static final Charset CHARSET = Charset.forName("gb2312");

    @RequestMapping("/welcome")
    public String indexPage(Model model) {
        //职业展示
        LoginService loginService=new BeanFactory().getLoginService();
        model.addAttribute("goodstudent", loginService.findGood());
        model.addAttribute("numberofstudent", loginService.selectCount());
        model.addAttribute("numberofgraduate", loginService.selectCountGraduate());
        model.addAttribute("images1", loginService.findImgaes1());
        return "index";
    }

    //职业页加入tiles
    @RequestMapping(value = "/u/jobs")
    public String jobs(Model model) {
        LoginService loginService=new BeanFactory().getLoginService();
        model.addAttribute("joblist1", loginService.findJobList1());
        model.addAttribute("numberofstudent1", loginService.selectCount());
        return "jobs";
        //根据该逻辑名，找到tiles.xml中 对应的definition...获取最后的jsp
    }

    //login
    @RequestMapping("/login")//add method = RequestMethod.post
    public String loginIndex(Model model, SignIn signIn) {
        model.addAttribute("signIn", signIn);
        return "login";
    }

    @RequestMapping(value = "/register ", method = RequestMethod.GET)
    public String registerForm() {
        return "registerform";
    }


    //登陆验证+格式验证
    @RequestMapping("/clientLogin")
    public String clientLogin(@Valid SignIn signIn, BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse) {
        //格式验证通过
        LoginService loginService=new BeanFactory().getLoginService();
        RedisService redisService=new  BeanFactory().getRedisService();
        if (!bindingResult.hasErrors()) {
            SignIn signInLoginInfo = loginService.clientLogin(signIn.getAccount());
            //查询到了数据
            if (signInLoginInfo != null) {
                //账号密码匹配，登陆成功
                if (signIn.getAccount().equals(signInLoginInfo.getAccount()) &&
                        signIn.getPassword().equals(signInLoginInfo.getPassword())) {
                    try {
                        //token生成
                        String s = JSON.toJSONString(signInLoginInfo.getSign_id());
                        String token = JwtUtil.createJWT(Constant.JWT_ID, s, signInLoginInfo.getAccount(), Constant.JWT_TTL);
                        //Token放入redis；
                        redisService.hset("signIn", signInLoginInfo.getAccount(), token, 10 * 60 * 60 * 24);
                        //设置cookie，将token放入cookie中
                        Cookie cookie = new Cookie("token", token);
                        //设置cookie的时间
                        cookie.setMaxAge(6000);
                        //将cookie返回至浏览器
                        httpServletResponse.addCookie(cookie);
                        return "redirect:u/jobs";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "login";
                }
                //账号密码不匹配，登陆失败
                else {
                    return "login";
                }
            }
            //数据库中无匹配的数据
            else {
                model.addAttribute("errorlogin", "account/password error");
                return "login";
            }
        }
        //格式验证不通过
        else {
            return "login";
        }
    }

    //登出
    @RequestMapping("/signout")
    public String signOut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("token", null);
        //设置cookie的时间
        cookie.setMaxAge(0);
        //将cookie返回至浏览器
        httpServletResponse.addCookie(cookie);
        return "redirect:/welcome";
    }

    //手机号码验证输入跳转
    @RequestMapping(value = "/phoneinfo")
    public String phoneinfo() {

        return "registerform";
    }

    //手机号码验证
    @RequestMapping(value = "/phonenum", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String phonenum(String telephone) {
        SMSservice smSservice=new BeanFactory().getSmsService();
        RedisService redisService=new BeanFactory().getRedisService();
        if (!telephone.equals("") && telephone != null) {
            verificationCodeCreater codeCreater = new verificationCodeCreater();
            codeCreater.setCode();
            ViaPictureNameCreater vpname=new ViaPictureNameCreater();
            vpname.setViapicturename();
            String vcode = String.valueOf(codeCreater.getCode());
            String code = smSservice.smssend(telephone, vcode);
            if (code != null & code.equals("OK")) {
                redisService.hset("telephone", telephone, vcode, 3000);
                return JSONObject.toJSONString("发送成功");
            }
            return JSONObject.toJSONString("发送失败");
        } else {
            return JSONObject.toJSONString("未填写手机号码");
        }
    }

    //邮箱验证码发送
    @RequestMapping(value = "/emailvcode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String email(String email) throws IOException {
        EmailService emailService=new BeanFactory().getEmailService();
        RedisService redisService=new BeanFactory().getRedisService();
        verificationCodeCreater codeCreater = new verificationCodeCreater();
        codeCreater.setCode();
        int vcode = codeCreater.getCode();
        String code =emailService.send_template(email, "caoyue", vcode);
        JSONObject j = JSON.parseObject(code);
        String statusCode = j.getString("statusCode");
        if (statusCode != null & statusCode.equals("200")) {
           redisService.hset("email", email, vcode, 3000);
            return JSONObject.toJSONString("发送成功");
        } else {

            return JSONObject.toJSONString("发送失败");
        }
    }

    //注册是否成功
    @RequestMapping(value = "/infovalida", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    @ResponseBody
    public String infovalida(@Valid Register register, BindingResult bindingResult, MultipartFile file, HttpServletRequest request,
                             String vcode) {
        LoginService loginService=new BeanFactory().getLoginService();
        RedisService redisService=new BeanFactory().getRedisService();
        if (!bindingResult.hasErrors()) {
            if (loginService.clientLogin(register.getAccount()) == null) {
                if (vcode != null && !vcode.equals("")) {
                    if (!file.isEmpty()) {
                        String path = request.getSession().getServletContext().getRealPath("/files");
                        String fileName = file.getOriginalFilename();
                        System.out.println(path);
                        File targetFile = new File(path, fileName);
                        if (!targetFile.exists()) {
                            targetFile.mkdirs();
                        }
                        try {
                            file.transferTo(targetFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String s = redisService.hget("telephone", register.getTelephone());
                        boolean p = redisService.hexists("telephone", register.getTelephone());
                        if (p && s.equals(vcode)) {

                            String result = loginService.register(register, targetFile);

                            return new JSONObject().toJSONString("手机验证成功" + "=注册成功" + "url" + result);

                        } else {
                            return new JSONObject().toJSONString("手机验证失败");
                        }
                    } else return new JSONObject().toJSONString("文件未选择");
                } else return new JSONObject().toJSONString("手机验证码为空");
            }
            else return new JSONObject().toJSONString("账户名已存在");
        }
        else return bindingResult.getFieldError().getDefaultMessage();
    }
}
