package com.DAO;

import com.POJO.*;
import java.util.List;

public interface LoginMapper {
    //task-4
    //优秀学员
    List<GoodStudent> findGood();
    //在线学习人数
    int selectCount();
    //毕业学员人数
    int selectCountGraduate();
    //查找页面前三张图片
    List<Images1> findImgaes1();
    //查找第一种职业的数据 test3页面中使用
    List<JobList> findJobList1();
    //登陆账户验证
    SignIn clientLogin(String account);
    SignIn clientLogin2(String account);
    //注册功能
    int register(Register register);
}
