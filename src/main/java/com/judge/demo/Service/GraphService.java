package com.judge.demo.Service;


import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.TransferRecord;

import javax.naming.Name;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GraphService {
    JSONObject graphArray(List<TransferRecord> list,List<String> query);

    /**
     *
     * @param nameMap 名字，以及该名字对应的关系范围
     * @return
     */
    JSONObject graphWith(Map<String, Set<String>> nameMap);

    JSONObject graphExtend(String name,Set<String> nameSet,Integer level);
}
