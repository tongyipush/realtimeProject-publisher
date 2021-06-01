package com.atguigu.gmall.controller;


import ch.qos.logback.core.joran.conditional.ElseAction;
import com.atguigu.gmall.bean.KeywordStats;
import com.atguigu.gmall.bean.ProductStats;
import com.atguigu.gmall.bean.ProvinceStats;
import com.atguigu.gmall.bean.VisitorStats;
import com.atguigu.gmall.service.KeyWordStatsService;
import com.atguigu.gmall.service.ProductStatsService;
import com.atguigu.gmall.service.ProvinceStatsService;
import com.atguigu.gmall.service.VisitorStatsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * controller主要作用，接收请求，响应
 *
 *1.RestController表示当前的组件交给容器去管理；如果方法的返回值是字符串的话，不是做页面跳转，直接返回JSON格式
 *
 */
@RestController

//@Target({ElementType.TYPE, ElementType.METHOD})表示方法可以加在方法上边，类上边
// 放在类上其实大部分是为了做区分的
@RequestMapping("/api/sugar")
public class SugarController {
    //注入
    @Autowired
    ProductStatsService productStatsService;

    @Autowired
    ProvinceStatsService provinceStatsService;

    @Autowired
    VisitorStatsService visitorStatsService;

    @Autowired
    KeyWordStatsService keyWordStatsService;

    //表示接收请求
    @RequestMapping("/gmv")
    //int不能接收空值，但是Interger可以接收空值，如果传空值的话就默认按照0处理
    //@RequestParam(value = "date",defaultValue = "0") Integer date传的值是字符串，但是底层会帮忙转换成Int类型
    public String getGMV(@RequestParam(value = "date",defaultValue = "0") Integer date){

        if (date == 0) date = now();

        BigDecimal gmv = productStatsService.getGMV(date);
        String josn ="{\"status\": 0,\"data\": "+gmv+"}";
        return josn;

    }

    private int now() {

        //toYYYYMMDD(stt)=#{date},clickhouse中toYYYYMMDD()方法返回的整数类型，因此传入的是Integar
        String yyyyMMdd = DateFormatUtils.format(new Date(), "yyyyMMdd");
        return Integer.valueOf(yyyyMMdd);
    }

    //获取品牌销售排行
    // 使用Integer的原因能够接收空值
    @RequestMapping("/trademark")
    public String getProductStatsByTradeMark(
            //接收string类型，但是底层会将string类型的转换为int类型
            @RequestParam(value = "date",defaultValue = "0") Integer date,
            @RequestParam(value = "limit",defaultValue = "10") Integer limit
    ){
        if (date==0) date = now();
        List<ProductStats> listProductStatsByTradeMark = productStatsService.getProductStatsByTradeMark(date, limit);

        List<String> tradeMarksList = new ArrayList<>();
        List<BigDecimal> valuesList = new ArrayList<>();
        for (ProductStats productStats : listProductStatsByTradeMark) {
            tradeMarksList.add(productStats.getTm_name());
            valuesList.add(productStats.getOrder_amount());
        }
        String json ="{\"status\": 0,\"data\": {\"categories\": [\""+StringUtils.join(tradeMarksList,"\",\"")+"\"],\"series\": [{\"data\": ["+StringUtils.join(valuesList,",")+"]}]}}";
        return json;
    }

    //todo 处理品类销售占比
    @RequestMapping("/category3")
    public String getProductStatsByCategory3(
            @RequestParam(value = "date",defaultValue = "0") Integer date,
            @RequestParam(value = "limit",defaultValue = "5") Integer limit){
        if (date==0) date = now();

        List<ProductStats> Category3List = productStatsService.getProductStatsByCategory3(date, limit);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"status\": 0,\"data\": [");
        int i=0;
        for (ProductStats productStats : Category3List) {
            if (i++>0){
                stringBuilder.append(",");
            }
            stringBuilder.append("{\"name\": \""+productStats.getCategory3_name()+"\",\"value\": "+productStats.getOrder_amount()+"}");
        }
        stringBuilder.append("]}");

        return stringBuilder.toString();
    }

    //todo 处理热门商品销售排行
    @RequestMapping("/spu")
    public Object getProductStatsBySpu(
            //使用Integer可以获取空值数据，如果int只能接收非空值，不能访问路径时不传值
            @RequestParam(value = "date",defaultValue = "0") Integer date,
            @RequestParam(value = "limit",defaultValue = "15") Integer limit) {
        if (date == 0) date = now();
        HashMap resMap = new HashMap();
        resMap.put("status",0);
        HashMap dataMap = new HashMap();
        List columnsList = new ArrayList();
        List rowsList = new ArrayList();

        HashMap columnMap1 = new HashMap();
        columnMap1.put("name","商品名称");
        columnMap1.put("id","spu_name");
        columnsList.add(columnMap1);

        HashMap columnMap2 = new HashMap();
        columnMap2.put("name","销售金额");
        columnMap2.put("id","order_amount");
        columnsList.add(columnMap2);

        HashMap columnMap3 = new HashMap();
        columnMap3.put("name","点击次数");
        columnMap3.put("id","order_ct");
        columnsList.add(columnMap3);




        List<ProductStats> spuList = productStatsService.getProductStatsBySpu(date, limit);
        for (ProductStats productStats : spuList) {
            HashMap rowsMap = new HashMap();
            rowsMap.put("spu_name",productStats.getSpu_name());
            rowsMap.put("order_amount",productStats.getOrder_amount());
            rowsMap.put("order_ct",productStats.getOrder_ct());

            rowsList.add(rowsMap);

        }


        dataMap.put("columns",columnsList);
        dataMap.put("rows",rowsList);
        resMap.put("data",dataMap);

        return resMap;

    }

    //todo 处理地区交易金额

    @RequestMapping("/province")
    public Map getProvinceStats(@RequestParam(value = "date",defaultValue = "0") Integer date){

        if (date == 0) date = null;
        HashMap resMap = new HashMap();
        resMap.put("status",0);

        HashMap dataMap = new HashMap();
        ArrayList mapDataList = new ArrayList();

        List<ProvinceStats> provinceStatList = provinceStatsService.getProvinceStats(date);
        for (ProvinceStats provinceStats : provinceStatList) {
            HashMap hashMap = new HashMap();
            hashMap.put("name",provinceStats.getProvince_name());
            hashMap.put("value",provinceStats.getOrder_amount());
            mapDataList.add(hashMap);

        }

        dataMap.put("mapData",mapDataList);

        resMap.put("data",dataMap);

    return resMap;
    }

    //todo 获取新老用户流量统计
    @RequestMapping("/visitor")
    public String getVisitorStatsByNew(@RequestParam(value = "date",defaultValue = "0") Integer date){

        List<VisitorStats> visitorStatsList = visitorStatsService.getVisitorStatsByNew(date);

        //todo 防止空指针，赋值new VisitorStats()
        // 如果赋值null,可能会报空指针
   VisitorStats newVisitorStats = new VisitorStats();
        VisitorStats oldVisitorStats = new VisitorStats();

        for (VisitorStats visitorStats : visitorStatsList) {
            if (visitorStats.getIs_new().equals("1")){
                newVisitorStats=visitorStats;
            }else {
                oldVisitorStats = visitorStats;
            }
        }

        String json ="{" +
                "  \"status\": 0," +
                "  \"data\": {" +
                "    \"columns\": [" +
                "      {" +
                "        \"name\": \"类别\"," +
                "        \"id\": \"type\"" +
                "      }," +
                "      {" +
                "        \"name\": \"新用户\"," +
                "        \"id\": \"new\"" +
                "      }," +
                "      {" +
                "        \"name\": \"老用户\"," +
                "        \"id\": \"old\"" +
                "      }" +
                "    ]," +
                "    \"rows\": [" +
                "      {" +
                "        \"type\": \"用户数(人)\"," +
                "        \"new\": "+newVisitorStats.getUv_ct()+"," +
                "        \"old\": "+oldVisitorStats.getUv_ct() +
                "      }," +
                "      {" +
                "        \"type\": \"总访问页面(次)\"," +
                "        \"new\": "+newVisitorStats.getPv_ct()+"," +
                "        \"old\": "+oldVisitorStats.getPv_ct()+
                "      }," +
                "      {" +
                "        \"type\": \"跳出率(%)\"," +
                "        \"new\": "+newVisitorStats.getUjRate()+"," +
                "        \"old\": "+oldVisitorStats.getUjRate()+
                "      }," +
                "      {" +
                "        \"type\": \"平均在线时长(秒)\"," +
                "        \"new\": "+newVisitorStats.getDurPerSv()+"," +
                "        \"old\": "+oldVisitorStats.getDurPerSv() +
                "      }," +
                "      {" +
                "        \"type\": \"平均访问页面数(人次)\"," +
                "        \"new\": "+newVisitorStats.getPvPerSv()+"," +
                "        \"old\": " +oldVisitorStats.getDurPerSv()+
                "      }" +
                "    ]" +
                "  }" +
                "}";
        return json;
    }

    //todo 分时流量统计
    @RequestMapping("/hour")
    public String getVisitorStatsByHour(@RequestParam(value = "date",defaultValue = "0") Integer date){

        List<VisitorStats> visitorStatsList = visitorStatsService.getVisitorStatsByHour(date);

        VisitorStats[] arrVisitorStats = new VisitorStats[24];

        for (VisitorStats visitorStats : visitorStatsList) {

            //todo 定义一个数组，是什么时间段产生的数据，就对应的放到哪个数组下表里边
            arrVisitorStats[visitorStats.getHr()] = visitorStats;

        }
        List<String> hrList=new ArrayList<>();
        ArrayList uvList = new ArrayList();
        ArrayList pvList = new ArrayList();
        ArrayList newUjList = new ArrayList();
        for (int i = 0; i <= 23; i++) {
            VisitorStats visitorStats = arrVisitorStats[i];
            if (visitorStats!=null){
                uvList.add(visitorStats.getUv_ct());
                pvList.add(visitorStats.getPv_ct());
                newUjList.add(visitorStats.getNew_uv());
            }else{
                uvList.add(0L);
                pvList.add(0L);
                newUjList.add(0L);
            }
            //todo 使用两位表示，如果是一位，前边需要补0
            hrList.add(String.format("%2d",i));
        }

        String json ="{\"status\": 0,\"data\": {\"categories\": ["+StringUtils.join(hrList,",")+"]," +
                "\"series\": [{\"name\": \"uv\",\"data\": ["+StringUtils.join(uvList,",")+"]},{\"name\": \"pv\",\"data\": " +
                "["+StringUtils.join(StringUtils.join(pvList,","))+"]},{\"name\": \"新用户数\",\"data\": ["+StringUtils.join(newUjList,",")+"]}]}}";

        return json;

    }

    //todo 热词字符云
    @RequestMapping("/keyword")
    public String getKeyWordStats(
            @RequestParam(value = "date",defaultValue = "0") Integer date,
            @RequestParam(value = "limit",defaultValue = "30") Integer limit){

        List<KeywordStats> keyWordStatsList = keyWordStatsService.getKeyWordStatsByKeyWord(date, limit);


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"status\": 0,\"data\": [");
        int i =0;
        for (KeywordStats keywordStats : keyWordStatsList) {
            if (i++>0){
                stringBuilder.append(",");
            }
            stringBuilder.append("{\"name\": \""+keywordStats.getKeyword()+"\",\"value\": "+keywordStats.getCt()+"}");
        }

        stringBuilder.append("]}");

        return stringBuilder.toString();

    }

}
