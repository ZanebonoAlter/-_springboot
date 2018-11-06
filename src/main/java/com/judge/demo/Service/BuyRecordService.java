package com.judge.demo.Service;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.BuyRecord;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BuyRecordService {
    JSONObject resolveData(List<MultipartFile> files, HttpServletRequest request);
    List<BuyRecord> selectAllBuyRecord(Integer pageIndex,Integer pageSize);
    List<BuyRecord> selectBuyRecordByIds(List<Long> Ids,Integer pageIndex,Integer pageSize);
    Integer selectAllBuyRecordCount();
    Integer selecrBuyRecordCountByIds(List<Long> Ids);

    //根据名字寻找手机号
    List<String> selectPhoneByName(String name);
    //根据Id列表查找所有购买记录
    List<BuyRecord> selectBuyRecordByIds(List<Long> Ids);

    Integer currenctCount();

    //根据名称查找
    List<BuyRecord> selectBuyName(String name);
}
