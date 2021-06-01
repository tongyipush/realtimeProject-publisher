package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.ProductStats;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


public interface ProductStatsService {
    //获取某一天的交易额
    public BigDecimal getGMV(int date);

    //按照品牌获取交易排行
    List<ProductStats> getProductStatsByTradeMark(int date,int limit);

    //按照品类获取交易排行
    List<ProductStats> getProductStatsByCategory3(int date,int limit);

    //按照spu获取销售排行
    List<ProductStats> getProductStatsBySpu(int date,int limit);
}
