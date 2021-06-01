package com.atguigu.gmall.mapper;

import com.atguigu.gmall.bean.ProductStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * 如果想要定义一个接口，按照大家以前的实现，写接口的实现类，定义类去实现接口
 * 但是当前的映射层mapper没有用自己来实现，我们使用一个框架MyBatis
 * MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射
 * MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis
 * 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO。
 * MyBatis属于ORM框架=> 把对象关系型数据库映射起来
 */
public interface ProductStatsMapper {

    //todo 统计每天订单总金额
    @Select("select sum(order_amount) order_amount from product_stats_2021 where toYYYYMMDD(stt)=#{date}")
    BigDecimal selectGMV(int date);

    //todo 统计品牌销售排行
    @Select("select tm_name,sum(order_amount) order_amount from product_stats_2021 where toYYYYMMDD(stt)=#{date}" +
            " group by tm_id,tm_name having order_amount>0 order by order_amount desc limit #{limit}")
    //todo 如果两个参数，那么需要增加参数注释辨别
    List<ProductStats> selectProductStatusByTrademark(@Param("date") int date,@Param("limit") int limit);

    //todo 统计品类销售占比
    @Select("select category3_name,sum(order_amount) order_amount from product_stats_2021 where toYYYYMMDD(stt)=#{date}" +
            " group by category3_id,category3_name having order_amount>0 order by order_amount desc limit #{limit}")
    List<ProductStats> selectProductStatusByCategory3(@Param("date") int date,@Param("limit") int limit);

    //todo 处理热门商品销售排行

    @Select("select spu_name,sum(order_amount) order_amount,sum(order_ct) order_ct from product_stats_2021 where toYYYYMMDD(stt)=#{date}" +
            " group by spu_id,spu_name having order_amount>0 order by order_amount desc limit #{limit}")
    List<ProductStats> selectProductStatsBySpu(@Param("date") int date,@Param("limit") int limit);
}
