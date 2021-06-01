package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.ProvinceStats;
import com.atguigu.gmall.mapper.ProvinceStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceStatsServiceImpl implements ProvinceStatsService {

    //注入，使用容器管理
    @Autowired
    ProvinceStatsMapper provinceStatsMapper;
    @Override
    public List<ProvinceStats> getProvinceStats(int date) {
        return provinceStatsMapper.selectProvinceStats(date);
    }
}
