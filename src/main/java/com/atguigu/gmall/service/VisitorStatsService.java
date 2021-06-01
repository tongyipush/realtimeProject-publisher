package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.VisitorStats;

import java.util.List;

public interface VisitorStatsService {

    List<VisitorStats> getVisitorStatsByNew(int date);

    List<VisitorStats> getVisitorStatsByHour(int date);
}
