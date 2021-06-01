package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.VisitorStats;
import com.atguigu.gmall.mapper.VisitorStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//todo @Service的意义是把对象ProductStatsServiceImpl的创建交给spring boot容器
// 引入ProductStatsMapper，但是是接口没有实现，但是运行的时候，框架会自动帮忙创建接口的实现类
@Service
public class VisitorStatsServiceImpl implements VisitorStatsService {

    @Autowired
    VisitorStatsMapper visitorStatsMapper;

    @Override
    public List<VisitorStats> getVisitorStatsByNew(int date) {
        return visitorStatsMapper.selectVisitStatsByNew(date);
    }

    @Override
    public List<VisitorStats> getVisitorStatsByHour(int date) {
        return visitorStatsMapper.selectVisitStatsByHr(date);
    }
}
