package com.atguigu.gmall.mapper;

import com.atguigu.gmall.bean.KeywordStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface KeyWordStatsMapper {

    //todo 因为热词在，搜索，加购，下单，点击里边的价值是不一样的，所以应该按照评分进行乘积
    // 进行乘积获取响应的评分
    @Select("select keyword,sum(keyword_stats_2021.ct * multiIf(source='SEARCH',10,source='ORDER',3,source='CART',2,source='CLICK',1,0)) ct from keyword_stats_2021 where toYYYYMMDD(stt)=#{date} group by keyword order by ct desc limit #{limit};")
    List<KeywordStats> selectKeyWordStats(@Param("date") int date,@Param("limit") int limit);
}
