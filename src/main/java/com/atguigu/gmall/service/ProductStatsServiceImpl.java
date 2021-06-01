package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.ProductStats;
import com.atguigu.gmall.mapper.ProductStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 业务层加service，控制层加controller
 * 1.@Service的意义是把对象ProductStatsServiceImpl的创建交给spring boot容器
 * 2.引入ProductStatsMapper，但是是接口没有实现，但是运行的时候，框架会自动帮忙创建接口的实现类
 * 那么它框架怎样知道要创建接口的实现类呢？因为在启动的时候扫描了mapper的包@MapperScan(basePackages = "com.atguigu.gmall.mapper")
 * 创建实现类也是由接口的实现类去做的，如果要使用实现类，需要把他注入进来@Autowired
 *
 */
//@Servic把这个类交给容器管理
@Service
public class ProductStatsServiceImpl implements ProductStatsService {

    @Autowired
    ProductStatsMapper productStatsMapper;

    @Override
    public BigDecimal getGMV(int date) {
        return productStatsMapper.selectGMV(date);
    }

    @Override
    public List<ProductStats> getProductStatsByTradeMark(int date, int limit) {
        return productStatsMapper.selectProductStatusByTrademark(date,limit);
    }

    @Override
    public List<ProductStats> getProductStatsByCategory3(int date, int limit) {
        return productStatsMapper.selectProductStatusByCategory3(date,limit);
    }

    @Override
    public List<ProductStats> getProductStatsBySpu(int date, int limit) {
        return productStatsMapper.selectProductStatsBySpu(date,limit);
    }
}
