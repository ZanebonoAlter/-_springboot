package com.judge.demo.Service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

/*
具体见DianPuService
 */
public interface RecordService {
    public JSONObject resolveData(MultipartFile file);
}
