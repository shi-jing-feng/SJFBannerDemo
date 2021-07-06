package com.shijingfeng.sjf_banner.util;

/**
 * Function: 转换工具类
 * Author: ShiJingFeng
 * Date: 2019/11/8 13:36
 * Description:
 * Author: ShiJingFeng
 */
public class CastUtil {

    /**
     * 用于消除类型装换警告
     * @param obj 原类型数据
     * @param <T> 泛型
     * @return 装换类型后的数据
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

}
