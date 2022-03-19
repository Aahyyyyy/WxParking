package com.zjut.lsw.util;
/**
 * show PictureUtil的工具类，用来随机生成photo名称
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 05
 * param  无参数
 *  @return 字符串
 * */

import java.util.Random;

public class PictureUtil {
    public static String  getRandomFileName(){
        //随机生成一个字符串
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
