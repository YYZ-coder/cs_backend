package com.cls.common.exception;

import com.cls.common.stat.LogStat;
import lombok.Data;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/3/25-15:35
 * Description：
 *      自定义异常
 */
@Data
public class ClsException extends RuntimeException{

    private Integer code;

    private String message;

    public ClsException(LogStat logStat){
        super(logStat.message);
        this.message = logStat.message;
        this.code = logStat.code;
    }

    public ClsException(LogStat logStat,String message){
        super(message);
        this.code = logStat.code;
        this.message = message;
    }

}
