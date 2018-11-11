package com.cls.common.utils;

import java.util.*;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/1-22:37
 * Description：
 */
public class SortUtil {

    //升序排列
    private static Comparator<Map.Entry<String, Integer>> valueComparatorAsc = new Comparator<Map.Entry<String,Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                           Map.Entry<String, Integer> o2) {
            return o1.getValue()-o2.getValue();
        }
    };

    //降序排列
    private static Comparator<Map.Entry<String, Integer>> valueComparatorDesc = new Comparator<Map.Entry<String,Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                           Map.Entry<String, Integer> o2) {
            return o2.getValue()-o1.getValue();
        }
    };

    /**
     * map按照value排序
     * @param map
     * @return
     */
    public static List sortMapByVal(Map map){
        // map转换成list进行排序
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list,valueComparatorDesc);
        return list;
    }
}
