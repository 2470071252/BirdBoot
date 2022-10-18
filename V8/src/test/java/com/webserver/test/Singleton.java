package com.webserver.test;

/**
 * 单例模式 java23种设计模式之一
 * 使用单例模式定义的类，全局只能有一个实例
 *
 */
public class Singleton {
    // 2 定义一个私有的，静态的当前类属性并初始化（这个过程等于就实例化了一次对象）
    private static Singleton instance = new Singleton();
    // 1 私有构造器，杜绝外界通过new来创建实例
    private  Singleton(){

    }
    // 3 定义一个公开的静态方法，可以获取当前类的实例（外界通过该方法始终拿到同一个对象）
     public static Singleton getInstance(){
        return instance;
    }
}
