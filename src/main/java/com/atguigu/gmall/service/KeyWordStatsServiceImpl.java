package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.KeywordStats;
import com.atguigu.gmall.mapper.KeyWordStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//todo 放入容器中
@Service
public class KeyWordStatsServiceImpl implements KeyWordStatsService {

    @Autowired
    KeyWordStatsMapper keyWordStatsMapper;
    @Override
    public List<KeywordStats> getKeyWordStatsByKeyWord(int date, int limit) {
        return keyWordStatsMapper.selectKeyWordStats(date,limit);
    }
}
