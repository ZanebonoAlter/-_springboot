package com.judge.demo.Service;

import com.judge.demo.Entity.BuyRecord;
import com.judge.demo.Entity.PersonRecord;
import com.judge.demo.Entity.TransferRecord;

import java.util.List;
import java.util.Map;

/*
具体见DianPuService
 */
public interface PersonService {
    Map<String,String> selectZhifubaoByName(String name);
    List<String> selectPhoneByName(String name);
    /*
    判断是否在名单上
     */
    Boolean isSearch(String name);

    //增加到名单上
    Integer addName(String name);
    //从名单上删除
    void deleteName(String name);

    /**
     * 获取重点人员名单列表，因为数量较少，不做分页
     * @return
     */
    List<PersonRecord> selectAllKeyPerson();

    /**
     * 分页获取所有的人员和对应的是否是重点人标志（0不是，1是）
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Map<String,Integer> selectAllPerson(Integer pageIndex,Integer pageSize);

    /**
     * 获取人单数量
     * @return
     */
    Integer selectAllPersonCount();

    /**
     * 直接更新记录
     * @param personRecord
     */
    void updatePerson(PersonRecord personRecord);

    /**
     * 根据姓名获取记录
     * @param name
     * @return
     */
    PersonRecord getPersonRecordByName(String name);

    /**
     * 根据查询字段模糊查询姓名
     * @param query
     * @return
     */
    Map<String,Integer> getPersonList(String query);

    void saveNameInMemory();
}
