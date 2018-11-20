package com.judge.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Dao.PersonDao;
import com.judge.demo.Entity.BuyRecord;
import com.judge.demo.Entity.Person;
import com.judge.demo.Entity.PersonRecord;
import com.judge.demo.Entity.TransferRecord;
import com.judge.demo.Service.BuyRecordService;
import com.judge.demo.Service.PersonService;
import com.judge.demo.Service.SearchService;
import com.judge.demo.Service.TransferRecordService;
import com.judge.demo.Untils.SearchUntil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/judge/person")
public class PersonController {
    @Resource
    private TransferRecordService transferRecordService;
    @Resource
    private BuyRecordService buyRecordService;
    @Resource
    private SearchService searchService;
    @Resource
    private PersonService personService;

    /**
     * 以人为节点的转账记录查询
     *
     * @param name
     * @param pageIndex
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectTransferByName", method = RequestMethod.GET)
    public JSONObject selectTransferByName(@RequestParam String query, @RequestParam String name, @RequestParam Integer pageIndex, @RequestParam Integer pageSize, HttpServletRequest request) {
        JSONObject object = new JSONObject();
        List<String> param = new ArrayList<>();

        //添加索引关键词
        if (!query.isEmpty())
            param.add(query);
        param.add(name);

        //获取范围内
        List<Long> Ids = searchService.getTransferRecordId(param, request);
        List<TransferRecord> list = transferRecordService.selectTransferRecordByIds(Ids, (pageIndex - 1) * pageSize, pageSize);
        Integer count = transferRecordService.selecrTransferRecordCountByIds(Ids);
        object.put("list", list);
        object.put("count", count);
        return object;
    }

    @RequestMapping(value = "/selectBuyRecordByName", method = RequestMethod.GET)
    public JSONObject selectBuyrecordByName(@RequestParam String query, @RequestParam String name, @RequestParam Integer pageIndex, @RequestParam Integer pageSize, HttpServletRequest request) {
        JSONObject object = new JSONObject();
        List<String> param = new ArrayList<>();

        //添加索引关键词
        if (!query.isEmpty())
            param.add(query);
        param.add(name);

        //获取范围内
        List<Long> Ids = searchService.getBuyRecordId(param, request);
        List<BuyRecord> list = buyRecordService.selectBuyRecordByIds(Ids, (pageIndex - 1) * pageSize, pageSize);
        Integer count = buyRecordService.selecrBuyRecordCountByIds(Ids);
        object.put("list", list);
        object.put("count", count);
        return object;
    }

    @RequestMapping(value = "/personInfo", method = RequestMethod.GET)
    public JSONObject personInfo(@RequestParam String name, HttpServletRequest request) {
        JSONObject object = new JSONObject();

        List<Long> Ids = searchService.getTransferRecordId(name, request);
        List<TransferRecord> list = transferRecordService.selectAllTransferRecordByIds(Ids);
        if (personService.isSearch(name)) {
            object.put("type", "重点人员");
        } else {
            object.put("type", "普通人员");
        }
        //支付宝
        Map<String, HashSet<String>> result = new HashMap<>();
        if (list != null)
            for (TransferRecord record : list) {
                String payName = record.getTransferRecordPayName();
                String payZhifubao = record.getTransferRecordPayZhifubao();
                String payZhifubaoId = record.getTransferRecordPayZhifubaoId();

                String collectionName = record.getTransferRecordCollectionName();
                String collectionZhifubao = record.getTransferRecordCollectionZhifubao();
                String collectionZhifubaoId = record.getTransferRecordCollectionZhifubaoId();

                if (payName.equals(name)) {
                    if (payZhifubao != null && payZhifubaoId != null) {//如果没有数据为空
                        HashSet temp;
                        if (result.containsKey(payZhifubaoId)) {
                            temp = result.get(payZhifubaoId);
                        } else {
                            temp = new HashSet();
                        }
                        temp.add(payZhifubao);
                        result.put(payZhifubaoId, temp);
                    }
                }
                if (collectionName.equals(name)) {
                    if (collectionZhifubao != null && collectionZhifubaoId != null) {//如果没有数据为空
                        HashSet temp;
                        if (result.containsKey(collectionZhifubaoId)) {
                            temp = result.get(collectionZhifubaoId);
                        } else {
                            temp = new HashSet();
                        }
                        temp.add(collectionZhifubao);
                        result.put(collectionZhifubaoId, temp);
                    }
                }

            }
        object.put("zhifubao_result", result);
        List<String> phone_list = buyRecordService.selectPhoneByName(name);
        for (int i = 0; i < phone_list.size(); i++) {
            if (phone_list.get(i).equals("\\N")) {
                phone_list.remove(i);
            }
        }
        object.put("phone_list", phone_list);
        List<Long> other_Ids = searchService.getTransferRecordId(name, request);
        List<BuyRecord> account = buyRecordService.selectBuyRecordByIds(other_Ids);
        //有问题
//        List<BuyRecord> other = buyRecordService.selectBuyName(name);
        HashSet<String> account_set = new HashSet<>();

//        if (other != null)
//            for (BuyRecord record : other) {
//                account_set.add(record.getBuyRecordBuyerAccount());
//            }

        if (account != null)
            for (BuyRecord record : account) {
                if (record.getBuyRecordIndex().indexOf(name + "购物记录") != -1) {//如果索引中包含name购物记录,记录name的购买账号
                    account_set.add(record.getBuyRecordBuyerAccount());
                }
            }
        object.put("account", account_set);
        return object;
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.PUT)
    public JSONObject addName(@PathVariable String name) {
        JSONObject object = new JSONObject();
        if (personService.addName(name) != 0)
            object.put("code", 1);
        return object;
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.DELETE)
    public JSONObject deleteName(@PathVariable String name) {
        JSONObject object = new JSONObject();
        personService.deleteName(name);
        object.put("code", 1);
        return object;
    }

    @RequestMapping(value = "/keyPerson", method = RequestMethod.GET)
    public JSONObject getKeyPerson() {
        JSONObject object = new JSONObject();
        List<PersonRecord> list = personService.selectAllKeyPerson();
        object.put("list", list);
        return object;
    }

    @RequestMapping(value = "/allPeopleList", method = RequestMethod.GET)
    public JSONObject getPeopleList(@RequestParam Integer pageIndex, @RequestParam Integer pageSize, @RequestParam(defaultValue = "", required = false) String query) {
        JSONObject object = new JSONObject();
        Map<String, Integer> map;
        if (Person.flag == 0) {
            map = new TreeMap<>();
        } else {
            Person.flag = 0;
            personService.saveNameInMemory();
            map = new TreeMap<>();
        }
        if (query.equals("")) {
            for (int i = (pageIndex - 1) * pageSize, j = 0; i < Person.memory.size() && j < pageSize; i++, j++) {
                if (personService.isSearch(Person.memory.get(i)))//如果重点
                    map.put(Person.memory.get(i), 1);
                else
                    map.put(Person.memory.get(i), 0);
            }
        } else {
            for (String name : Person.memory) {
                if (name.indexOf(query) != -1) {
                    if (personService.isSearch(name))//如果重点
                        map.put(name, 1);
                    else
                        map.put(name, 0);
                }
            }
        }
        if(map.containsKey(""))
            map.remove("");
        Integer count = Person.memory.size();
        List<JSONObject> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            JSONObject object1 = new JSONObject();
            object1.put("name", entry.getKey());
            object1.put("type", entry.getValue());
            list.add(object1);
        }
        object.put("list", list);
        object.put("count", count);
        return object;
    }

    @RequestMapping(value = "/keyPerson", method = RequestMethod.PUT)
    public JSONObject updateKeyPerson(@RequestBody PersonRecord personRecord) {
        JSONObject object = new JSONObject();
        personService.updatePerson(personRecord);
        object.put("code", 1);
        return object;
    }

    @RequestMapping(value = "/keyPerson/{name}", method = RequestMethod.GET)
    public JSONObject getPersonRecordByname(@PathVariable String name) {
        JSONObject object = new JSONObject();
        PersonRecord record = personService.getPersonRecordByName(name);
        object.put("addForm", record);
        return object;
    }
}
