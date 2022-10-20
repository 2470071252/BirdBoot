package com.webserver.contorller;

import com.webserver.entiy.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;

/**
 * 当前类用于处理与用户相关操作
 */
public class UserController {
    private static File userDir;
    static {
        userDir = new File("./user");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理用户登录！！！");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+","+password);


    }

    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理用户注册！！！");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String age = request.getParameter("age");
        int intAge = Integer.parseInt(age);
        System.out.println(username+","+password+","+nickname+","+age);
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                nickname == null || nickname.trim().isEmpty() ||
                !age.matches("[0-9]+")
        ) {
            // 希望用户看到reg_info_error.html
            response.sendRedirect("/reg_info_error.html");
        }
        User user = new User(username, password, nickname,intAge);
        File file = new File(userDir,username+".obj");
        if (file.exists()) {
            response.sendRedirect("/reg_user_exists.html");
            return;
        }
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
            response.sendRedirect("/reg_success.html");

    }
}
