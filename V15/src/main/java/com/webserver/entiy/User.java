package com.webserver.entiy;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String nickname;
    private int age;

    public User(){}

    public User(String username, String password,String nickname,int age){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getNickname(){
        return nickname;
    }
    public int getAge(){
        return age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}
