package com.cls.common.services;

import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.commodity.vo.CommodityShortVO;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.stat.LogStat;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/9-22:13
 * Description：
 */
public class PublicService {

    /**
     * 包装shopInfo
     * @param commodityShortVOS
     * @param shopService
     */
    public static void wrapShopInfo(ShopServiceImpl shopService,
                                    List<CommodityShortVO> commodityShortVOS){
        List<Integer> sids = commodityShortVOS.stream().map(CommodityShortVO::getShopId).collect(Collectors.toList());
        Search search = new Search();
        search.put("id_in",sids);
        List<ShopInfo> shopInfos = shopService.queryByProperties(search,"");
        if(CollectionUtils.isEmpty(shopInfos)){
            throw new ClsException(LogStat.NO_DATA);
        }
        Map<Integer,ShopInfo> integerShopInfoMap = new HashMap<>();
        for(ShopInfo shopInfo : shopInfos){
            integerShopInfoMap.put(shopInfo.getId(), shopInfo);
        }
        for(CommodityShortVO shortVO : commodityShortVOS){
            shortVO.setShopName(integerShopInfoMap.get(shortVO.getShopId()).getShopName());
        }
    }

    /**
     * 包装销量
     * @param commodityVOS
     */
    public static void wrapSellsCount(OperationServiceImpl operationService,
                                       List<CommodityShortVO> commodityVOS){
        List<Integer> ids = commodityVOS.stream().map(CommodityShortVO::getId).collect(Collectors.toList());
        Search search = new Search();
        search.put("objId_in",ids);
        search.put("type", TYPE.commodity.code);
        search.put("behavior", BEHAVIOR.BOUGHT.code);
        List<UserOperation> userOperations = operationService.queryByProperties(search,"");
        if(CollectionUtils.isEmpty(userOperations)){
            return;
        }
        Map<Integer, Integer> commIdCountsMap = new HashMap<>();
        for(UserOperation userOperation : userOperations){
            Integer count = commIdCountsMap.get(userOperation.getObjId());
            if(count == null){
                count = 0;
            }
            commIdCountsMap.put(userOperation.getObjId(), count + userOperation.getCount());
        }
        for(CommodityShortVO commodityVO : commodityVOS){
            Integer count = commIdCountsMap.get(commodityVO.getId());
            if(count == null){
                commodityVO.setSellsCount(0);
            }else{
                commodityVO.setSellsCount(count);
            }
        }
    }
}
