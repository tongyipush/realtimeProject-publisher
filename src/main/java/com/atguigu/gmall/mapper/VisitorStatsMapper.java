package com.atguigu.gmall.mapper;

import com.atguigu.gmall.bean.VisitorStats;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VisitorStatsMapper {

    //todo 新老访客统计
    @Select("select is_new,sum(uv_ct) uv_ct,sum(pv_ct) pv_ct,sum(sv_ct) sv_ct,sum(uj_ct) uj_ct" +
            ",sum(dur_sum) dur_sum from visitor_stats_2021 where toYYYYMMDD(stt)=#{date}" +
            " group by is_new")
    List<VisitorStats> selectVisitStatsByNew(int date);

    //todo 分时流量统计

    @Select("select sum(if(visitor_stats_2021.is_new='1',visitor_stats_2021.uv_ct,0)) new_uv," +
            "toHour(stt) hr,sum(visitor_stats_2021.uv_ct) uv_ct,sum(visitor_stats_2021.pv_ct) " +
            "pv_ct,sum(visitor_stats_2021.uj_ct) uj_ct from visitor_stats_2021 where" +
            " toYYYYMMDD(stt)=#{date} group by toHour(stt)")
    List<VisitorStats> selectVisitStatsByHr(int date);
}
