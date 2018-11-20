package com.judge.demo.Service;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.TransferRecord;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TransferRecordService {
    JSONObject resolveData(List<MultipartFile> files, HttpServletRequest request);
    List<TransferRecord> selectAllTransferRecord(Integer pageIndex, Integer pageSize);
    List<TransferRecord> selectAllTransferRecord(Integer pageIndex, Integer pageSize,Date beginDate,Date endDate);

    List<TransferRecord> selectTransferRecordByIds(List<Long> Ids,Integer pageIndex,Integer pageSize,Date beginDate,Date endDate);
    List<TransferRecord> selectTransferRecordByIds(List<Long> Ids,Integer pageIndex,Integer pageSize);
    Integer selectAllTransferRecordCount();
    Integer selectAllTransferRecordCount(Date beginDate,Date endDate);

    Integer selecrTransferRecordCountByIds(List<Long> Ids,Date beginDate,Date endDate);
    Integer selecrTransferRecordCountByIds(List<Long> Ids);
    List<TransferRecord> selectAllTransferRecordByIds(List<Long> Ids);
    List<TransferRecord> selectAllTransferRecord();

    /**
     * 根据两个人名获取相关记录
     */
    List<TransferRecord> selectTransferRecordByName(String name1,String name2);

    //节点查询
    //转账记录 条数 金额 大小

    /**
     * 根据条件查询符合的人名列表
     * @param num
     * @param fee
     * @return
     */
    Set<String> selectName(Integer num, Double fee);

    //根据人名查询对应的人名列表
    Set<String> selectRelativeName(String name);

    Integer currentCount();
    //根据人名获取记录
    List<TransferRecord> getTransferListByName(Set<String> set);
    //获取到有多个支付宝id的用户名称列表
    List<String> getMulitiZhifubaoNameList();
    List<String> getMulitiZhifubaoNameList(Integer pageIndex,Integer pageSize,String query);
    Integer getMulitiZhifubaoNameListCount(String query);
    //根据名字获取对应的支付宝id
    Set<String> getZhifubaoIdsByName(String name);
    //获取到指定日期分页查询记录
    List<TransferRecord> getDateBetween(Date beginDate,Date endDate,Integer pageIndex,Integer pageSize);

    //根据姓名获取转入转出数量
    Map<String,Integer> getInOut(String name);

    Map<String,Double> getIntOutAll(Date beginDate, Date endDate);

    Map<String,Double> getIntOutAll(List<Long>Ids,Date beginDate, Date endDate);

    List<TransferRecord> getExceptRecord(Double minFee,Double maxFee);
    //获取异常资金流
    List<TransferRecord> getExceptRecord(Double minFee,Double maxFee,Integer pageIndex,Integer pageSize);
    //获取异常资金流数量
    Integer getExceptRecordCount(Double minFee,Double maxFee);
//    //根据id 日期 存储记录
//    void saveRecordByIdsAndDate(List<Long> Ids,Date beginDate,Date endDate);
//    //根据日期存储记录
//    void saveRecordByDate(Date beginDate,Date endDate);
}
