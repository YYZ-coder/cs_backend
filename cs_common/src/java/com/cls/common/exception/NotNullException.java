package com.cls.common.exception;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/2-23:47
 * Description：
 *      参数为空异常
 */
public class NotNullException extends RuntimeException{

    public NotNullException(String message, Class clazz){
        super(message + " in class:" + clazz.toString());
    }
}
