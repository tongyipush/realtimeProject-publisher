package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.KeywordStats;

import java.util.List;

public interface KeyWordStatsService {

    List<KeywordStats> getKeyWordStatsByKeyWord(int date,int limit);
}
