package com.judge.demo.Service;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.AliDianpu;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/*
对阿里店铺数据提供的接口服务
主要是
1.从excel文件中获取数据
2.将数据进行总和处理，写进index字段，尝试写进数据库，如果成功，对该字段建立本地索引（index和id记录）
3.提供索引搜索服务
4.基于索引的搜索和其他算法需求
 */
public interface DianPuService {
    /*
    从excel获取店铺数据
     */
    public JSONObject resolveData(MultipartFile file);
}
