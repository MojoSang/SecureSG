package com.zhong.rm;

import redis.clients.jedis.Jedis;

import java.beans.Transient;
import java.util.*;

public class RedisWrapper {
    /**
     * 获得Jedis对象
     *
     * @return
     */
    public static Jedis getJedisObject() {
        //连接本地的 Redis 服务
        Jedis jedis = RedisPool.getJedis();
        //System.out.println("连接成功");
        //查看服务是否运行
        if ("PONG".equals(jedis.ping())) {
            return jedis;
        } else {
            return null;
        }
    }

    /**
     * 获得第begin 到 第end个关键词的记录<br>
     * 在函数的内部获得连接池的资源，使用完毕之后直接释放<br></>
     *
     * @param begin 起始，含，从0开始
     * @param end 结束，不含，从0开始
     * @return 获得begin到end的关键词-文件名的记录
     */
    public static Map<String,Collection<String>> getRecords(int begin,int end){
        //获取jedis对象
        Jedis jedis = getJedisObject();
        //返回列表 key 中指定区间内的元素
        List<String> allKeys = jedis.lrange("allkeys",begin,end-1);
        Map<String,Collection<String>> res = new HashMap<>();
        for(String key : allKeys){
            System.out.println(key);
            //返回集合中的所有的成员
            Set<String> filenames = jedis.smembers(key);
            res.put(key,filenames);
        }
        System.out.println(res.size());
        //释放资源
        RedisPool.returnResource(jedis);
        System.out.println("加载数据成功，已经加载 "+(end-begin)+" 个关键词的记录");
        return res;
    }

}