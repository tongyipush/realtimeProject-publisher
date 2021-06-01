package com.atguigu.gmall.mapper;

import com.atguigu.gmall.bean.ProvinceStats;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface ProvinceStatsMapper {

    @Select("select province_name,sum(order_amount) order_amount from province_stats_2021 where toYYYYMMDD(stt)=#{date}" +
            " group by province_id,province_name")
    List<ProvinceStats> selectProvinceStats(int date);
}
