package com.cls.common.search;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-20:38
 * Description：
 */
public class Search extends HashMap<String, Object> {

    public Search(){
        super();
    }

    public Search put(String k, Object v){
        super.put(k,v);
        return this;
    }
}
