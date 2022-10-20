package com.webserver.test;

import java.util.Arrays;

/**
 * String重载的split方法
 * String[] split(String s, limit l)
 */
public class SplitDemo {
    public static void main(String[] args) {
        String line = "1=2=3=34=4=5====";
        // 忽略后面所有拆分出来的空字符串
        String[] data = line.split("=");
        System.out.println(Arrays.toString(data));    // [1, 2, 3, 34, 4, 5]

        //limit:2 表示仅拆分出两项
        data = line.split("=",2);
        System.out.println(Arrays.toString(data));    // [1, 2=3=34=4=5====]

        data = line.split("=",3);
        System.out.println(Arrays.toString(data));    // [1, 2, 3=34=4=5====]

        // 当limit值大于可拆分项时，仅保留可拆分项
        data = line.split("=",100);
        System.out.println(Arrays.toString(data));    // [1, 2, 3, 34, 4, 5, , , , ]

        data = line.split("=",0);
        System.out.println(Arrays.toString(data));    // [1, 2, 3, 34, 4, 5]

        // limit:为负数则表示尽拆，没有省略和保留
        data = line.split("=",-1);
        System.out.println(Arrays.toString(data));    // [1, 2, 3, 34, 4, 5, , , , ]
    }
}
