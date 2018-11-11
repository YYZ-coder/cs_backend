package com.cls.common.base;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.stat.LogStat;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/3/29-23:39
 * Description：
 *
 */
public class BaseController {


    /**
     *
     * @return
     */
    protected Integer getRID(){
        Integer rid = (Integer)WebSession.getVal("rid");
        if(!checkInteger(rid)){
            throw new ClsException(LogStat.LOG_FAILED);
        }
        return rid;
    }

    /**
     *
     * @return
     */
    protected Integer getUID(){
        Integer cuid = (Integer) WebSession.getVal("cuid");
        if(!checkInteger(cuid)){
            throw new ClsException(LogStat.NO_LOGIN);
        }
        return cuid;
    }

    /**
     *
     * @param i
     * @return
     */
    protected boolean checkInteger(Integer i){
        if(i == null || i.equals(0)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 异常
     * @param jsonBackData
     * @param e
     */
    protected void wrapException(JsonBackData jsonBackData ,Throwable e){
        if(e instanceof ClsException){
            jsonBackData.setResult(((ClsException) e).getCode());
        }else{
            jsonBackData.setResult(4000);
        }
        jsonBackData.setSuccess(false);
        jsonBackData.setBackData(e.getMessage());
    }

}
