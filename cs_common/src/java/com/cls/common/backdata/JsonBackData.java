package com.cls.common.backdata;

import lombok.Data;

import javax.json.Json;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2017/12/12-21:24
 * Description：
 *  规范返回的JsonData
 */
@Data
public class JsonBackData implements Serializable{

    /** 是否成功 */
    private boolean success;

    /** 返回的数据对象 */
    private Object backData;

    /** 返回结果状态 */
    private Integer result;

    public JsonBackData() {
    }

    public JsonBackData(boolean success, Integer result){
        this.success = success;
        this.result = result;
    }

    public JsonBackData(Object backData){
        this.backData = backData;
    }

    public JsonBackData(boolean success,
                        Object backData,
                        Integer result) {
        this.success = success;
        this.backData = backData;
        this.result = result;
    }

}
