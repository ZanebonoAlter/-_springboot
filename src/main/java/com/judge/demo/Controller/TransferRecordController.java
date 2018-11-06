package com.judge.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.Person;
import com.judge.demo.Entity.TransferRecord;
import com.judge.demo.Service.GraphService;
import com.judge.demo.Service.SearchService;
import com.judge.demo.Service.TransferRecordService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/judge/transferrecord")
public class TransferRecordController {
    @Resource
    private TransferRecordService transferRecordService;
    @Resource
    private SearchService searchService;
    @Resource
    private GraphService graphService;

    /**
     * 文件上传接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JSONObject Upload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        JSONObject object = transferRecordService.resolveData(files, request);
        Person.flag = 1;
        TransferRecord.transfer_flag = 1;
        TransferRecord.all_transfer_flag = 0;
        return object;
    }

    /**
     * 测试用，查询索引，返回Id
     *
     * @param request
     * @param query
     * @return
     */
    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public JSONObject Search(HttpServletRequest request, @PathVariable("query") String query) {
        JSONObject object = new JSONObject();
        List<Long> result = searchService.getTransferRecordId(query, request);
        object.put("result", result);
        return object;
    }

    /**
     * 查询所有结果
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/select/selectAll/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public JSONObject selectAll(@PathVariable Integer pageIndex, @PathVariable Integer pageSize, @RequestParam(defaultValue = "1970/1/1") Date beginDate,
                                @RequestParam(defaultValue = "2099/12/30") Date endDate) {
        JSONObject object = new JSONObject();
        List<TransferRecord> list = transferRecordService.selectAllTransferRecord((pageIndex - 1) * pageSize, pageSize, beginDate, endDate);
        Integer count = 0;
        Map<String, Double> map = new TreeMap<>();
        if (TransferRecord.all_transfer_flag == 1) {
            count = transferRecordService.selectAllTransferRecordCount(beginDate, endDate);
            map = transferRecordService.getIntOutAll(beginDate, endDate);
            TransferRecord.all_transfer_InOut = map;
            TransferRecord.all_count = count;
            TransferRecord.all_transfer_flag = 0;
        } else {
            count = TransferRecord.all_count;
            map = TransferRecord.all_transfer_InOut;
        }
//        Integer total = count / pageSize;
//        if (count % pageSize != 0)
//            total++;
        object.put("code", 1);
        object.put("list", list);
//        object.put("total", total);
        object.put("count", count);
        object.put("in", map.get("in"));
        object.put("out", map.get("out"));
        return object;
    }

    /**
     * 根据字段查询索引结果
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @param request
     * @return
     */
    @RequestMapping(value = "/select/selectSearch/{pageIndex}/{pageSize}/{query}", method = RequestMethod.GET)
    public JSONObject selectBySearch(@PathVariable Integer pageIndex, @PathVariable Integer pageSize, @PathVariable String query, @RequestParam(defaultValue = "1970/1/1") Date beginDate,
                                     @RequestParam(defaultValue = "2099/12/30") Date endDate, HttpServletRequest request) {
        JSONObject object = new JSONObject();
        List<Long> Ids = searchService.getTransferRecordId(query, request);
        List<TransferRecord> list = transferRecordService.selectTransferRecordByIds(Ids, (pageIndex - 1) * pageSize, pageSize, beginDate, endDate);
        Integer count = 0;
        Map<String, Double> map;
        if (query.equals(TransferRecord.transfer_query) && beginDate.equals(TransferRecord.transfer_beginDate) && endDate.equals(TransferRecord.transfer_endDate) && TransferRecord.transfer_flag == 0) {
            count = TransferRecord.count;
            map = TransferRecord.transfer_InOut;
        } else {
            count = transferRecordService.selecrTransferRecordCountByIds(Ids, beginDate, endDate);
            TransferRecord.count = count;
            map = transferRecordService.getIntOutAll(Ids, beginDate, endDate);
            TransferRecord.transfer_InOut = map;

            TransferRecord.transfer_flag = 0;
            TransferRecord.transfer_query = query;
            TransferRecord.transfer_beginDate = beginDate;
            TransferRecord.transfer_endDate = endDate;
        }
//        Integer total = count / pageSize;
//
//        if (count % pageSize != 0)
//            total++;
        object.put("code", 1);
        object.put("list", list);
//        object.put("total", total);
        object.put("count", count);
        if(map==null||!map.containsKey("in"))
            object.put("in", 0);
        else
            object.put("in", map.get("in"));
        if (map==null||!map.containsKey("out"))
            object.put("out", 0);
        else
            object.put("out",map.get("out"));
        return object;
    }

    @RequestMapping(value = "/graph", method = RequestMethod.GET)
    public JSONObject getGraphConfig(@RequestParam(required = false, defaultValue = "") List<String> query, HttpServletRequest request) {
        JSONObject object;
        if (!query.isEmpty()) {
            List<Long> Ids = searchService.getTransferRecordId(query, request);
            List<TransferRecord> list = transferRecordService.selectAllTransferRecordByIds(Ids);
            object = graphService.graphArray(list, query);
        } else {
            List<TransferRecord> list = transferRecordService.selectAllTransferRecord();
            object = graphService.graphArray(list, query);
        }
        return object;
    }

    @RequestMapping(value = "/peopleDetail", method = RequestMethod.GET)
    public JSONObject peopleDetail(@RequestParam String name1, @RequestParam String name2) {
        JSONObject object = new JSONObject();
        System.out.println(name1);
        System.out.println(name2);
        List<TransferRecord> list = transferRecordService.selectTransferRecordByName(name1, name2);
        object.put("list", list);
        return object;
    }

    /**
     * 条件查询，第一层节点判断
     *
     * @param num
     * @param fee
     * @return
     */
    @RequestMapping(value = "/firstFloor", method = RequestMethod.GET)
    public JSONObject selectivePeopleFirstFloor(@RequestParam(defaultValue = "0", required = false) Integer num, @RequestParam(defaultValue = "0", required = false) Double fee) {
        JSONObject object = new JSONObject();
        Set<String> nodeList = transferRecordService.selectName(num, fee);
        //关联算法
        Map<String, Set<String>> all = new HashMap<>();

        for (String s : nodeList) {
            Set<String> set = transferRecordService.selectRelativeName(s);
            for (String temp : nodeList) {//不包含一层节点嫌疑人
                if (set.contains(temp))
                    set.remove(temp);
            }
            all.put(s, set);
        }

        TreeSet<String> list = new TreeSet<>();
        for (Map.Entry<String, Set<String>> entry1 : all.entrySet()) {
            for (Map.Entry<String, Set<String>> entry2 : all.entrySet()) {
                //如果是自己
                if (entry1.getKey().equals(entry2.getKey()))
                    continue;
                for (String s : entry1.getValue()) {//对第一个集合所有关联人进行遍历
                    if (entry2.getValue().contains(s)) {//如果在第二个集合的关联人内找到
                        list.add(s);//添加嫌疑人
                        break;
                    }
                }
            }
        }
        JSONArray node = new JSONArray();
        for (String s : nodeList) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            node.add(temp);
        }
        object.put("node", node);
        JSONArray first_list = new JSONArray();
        for (String s : list) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            first_list.add(temp);
        }
        object.put("first_list", first_list);

        Map<String, Set<String>> map = new HashMap<>();//这个应该是筛选后的结果
        for (String name : nodeList) {//对每个自定义名字遍历
            Set<String> set = transferRecordService.selectRelativeName(name);//获取到所有关系
            Set<String> result = new HashSet<>();//筛选后的关系集合
            for (String keyName : nodeList) {
                if (set.contains(keyName))//如果name的关系集中有重点人的关系，那么把该重点人加入到关系集合中
                    result.add(keyName);
            }
            map.put(name, result);//筛选后的节点关系
        }

        for (String s : list) {//所有一级
            Set<String> allRelative = transferRecordService.selectRelativeName(s);
            Set<String> result = new HashSet<>();//筛选
            for (String name : nodeList) {
                if (allRelative.contains(name)) {
                    result.add(name);
                }
            }
            for (String first : list) {//如果和一级之间有关系
                if (allRelative.contains(first)) {
                    result.add(first);
                }
            }
            map.put(s, result);
        }
        JSONObject graph = graphService.graphWith(map);
        object.put("graph", graph);
        return object;
    }

    /**
     * 查询条件 二层挖掘
     *
     * @param num
     * @param fee
     * @return
     */
    @RequestMapping(value = "/secondFloor", method = RequestMethod.GET)
    public JSONObject selectivePeopleSecondFloor(@RequestParam(defaultValue = "0", required = false) Integer num, @RequestParam(defaultValue = "0", required = false) Double fee) {
        JSONObject object = new JSONObject();
        //新算法
        //获取所有人的关联人set集合
//        Map<String, Set<String>> new_map = new HashMap<>();
//        List<TransferRecord> temp_list = transferRecordService.selectAllTransferRecord();
//        List<String> nameList = new ArrayList<>();
        Set<String> secondFloor = new TreeSet<>();
//        for (TransferRecord transferRecord : temp_list) {
//            if (!nameList.contains(transferRecord.getTransferRecordPayName()))
//                nameList.add(transferRecord.getTransferRecordPayName());
//            if (!nameList.contains(transferRecord.getTransferRecordCollectionName()))
//                nameList.add(transferRecord.getTransferRecordCollectionName());
//        }

        //第一层 节点姓名，关联人集合<关联人姓名，与关联人相关的集合>
        Set<String> suspects = transferRecordService.selectName(num, fee);
        //开始查找nodeList对应的关系人集合
        Map<String, Set<String>> first = new TreeMap<>();
        Set<String> haveJudged = new HashSet<>();//已经判断过的第二层姓名，
        for (String name : suspects) {
            Set<String> new_set = transferRecordService.selectRelativeName(name);//获取对应关系
            for (String name1 : suspects) {//去除根节点关系
                new_set.remove(name1);
            }
            first.put(name, new_set);//将每个人以及其对应的关系列表放入,去除和其他根节点关系,放入的是根节点和第一层人物关系集合

        }
        //二层节点去除根节点关系和父节点关系
        for (Map.Entry<String, Set<String>> entry : first.entrySet()) {
            for (String name : entry.getValue()) {//获取到第二层节点人名
                Set<String> new_set = transferRecordService.selectRelativeName(name);//获取到所有关系

                if (new_set.contains(entry.getKey())) {//去除父节点
                    new_set.remove(entry.getKey());
                }
                for (String name2 : suspects) {
                    if (new_set.contains(name2)) {//去除根节点
                        new_set.remove(name2);
                    }
                }
                //new_set :当前一层节点拥有的所有关系，去除了根节点和父节点,即当前一层节点对应的所有二层节点信息
                for (Map.Entry<String, Set<String>> entry1 : first.entrySet()) {
                    if (entry.getKey().equals(entry1.getKey()))//和自己以外的名称比对
                        continue;
                    //已经比对过的不比对
                    if (haveJudged.contains(entry1.getKey()))
                        continue;
                    for (String name1 : entry1.getValue()) {//获取到了其他根节点第一层节点的所有名称
                        if (new_set.contains(name1))//如果有关系
                        {
                            secondFloor.add(name1);//关系是相互的
                            secondFloor.add(name);//增加当前的一层节点名称
                        }
                    }
                }
            }
            haveJudged.add(entry.getKey());
        }


        JSONArray node = new JSONArray();
        for (String s : suspects) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            node.add(temp);
        }
        object.put("node", node);
        JSONArray second_list = new JSONArray();
        for (String s : secondFloor) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            second_list.add(temp);
        }
        object.put("second_list", second_list);


        //custom全是重点人
        Map<String, Set<String>> map = new HashMap<>();//这个应该是筛选后的结果
        for (String name : suspects) {//对每个自定义名字遍历
            Set<String> set = transferRecordService.selectRelativeName(name);//获取到所有关系
            Set<String> result = new HashSet<>();//筛选后的关系集合
            for (String keyName : suspects) {
                if (set.contains(keyName))//如果name的关系集中有重点人的关系，那么把该重点人加入到关系集合中
                    result.add(keyName);
            }
            map.put(name, result);//筛选后的节点关系
        }
        //custom:所有重点人，secondFloor所有二级
        for (String s : secondFloor) {//所有二级
            Set<String> allRelative = transferRecordService.selectRelativeName(s);
            Set<String> result = new HashSet<>();//筛选
            for (String name : suspects) {
                if (allRelative.contains(name)) {
                    result.add(name);
                }
            }
            for (String second : secondFloor) {//如果和二级之间有关系
                if (allRelative.contains(second)) {
                    result.add(second);
                }
            }
            map.put(s, result);
        }
        JSONObject graph = graphService.graphWith(map);
        object.put("graph", graph);
        return object;
    }

    @RequestMapping(value = "/currentCount", method = RequestMethod.GET)
    public JSONObject currentCount() {
        JSONObject object = new JSONObject();
        Integer count = transferRecordService.currentCount();
        object.put("count", count);
        return object;
    }

    @RequestMapping(value = "/getMuliZhifubao", method = RequestMethod.GET)
    public JSONObject getMuliZhifubao(@RequestParam Integer pageIndex, @RequestParam Integer pageSize, @RequestParam Integer needCount, @RequestParam String query) {
        JSONObject object = new JSONObject();
        List<String> list = new ArrayList<>();
//        if (Person.muti_flag == 1) {
//            Person.muti_flag = 0;
//            list = transferRecordService.getMulitiZhifubaoNameList();
//            Person.mutiZhifubaoName = list;
//        } else {
//            list = Person.mutiZhifubaoName;
//        }
        list = transferRecordService.getMulitiZhifubaoNameList((pageIndex - 1) * pageSize, pageSize, query);
        Integer count = -1;
        if (needCount == 1)
            count = transferRecordService.getMulitiZhifubaoNameListCount(query);
        JSONArray result_list = new JSONArray();
        for (String s : list) {
            JSONObject temp = new JSONObject();
            temp.put("pName", s);
            result_list.add(temp);
        }
        object.put("list", result_list);
        object.put("count", count);
        return object;
    }

    @RequestMapping(value = "/queryGraph", method = RequestMethod.GET)
    public JSONObject queryGraphNO(@RequestParam List<String> custom) {
        JSONObject object = new JSONObject();
        Map<String, Set<String>> map = new HashMap<>();//这个应该是筛选后的结果
        if (custom == null)
            return object;
        //custom全是重点人
        for (String name : custom) {//对每个自定义名字遍历
            Set<String> set = transferRecordService.selectRelativeName(name);//获取到所有关系
            Set<String> result = new HashSet<>();//筛选后的关系集合
            for (String keyName : custom) {
                if (set.contains(keyName))//如果name的关系集中有重点人的关系，那么把该重点人加入到关系集合中
                    result.add(keyName);
            }
            map.put(name, result);//筛选后的节点关系
        }
        JSONObject graph = graphService.graphWith(map);
        object.put("graph", graph);
        return object;
    }

    @RequestMapping(value = "/queryGraphFirst", method = RequestMethod.GET)
    public JSONObject queryGraph(@RequestParam List<String> custom, @RequestParam(defaultValue = "2") Integer number) {
        JSONObject object = new JSONObject();
        Map<String, Set<String>> map = new HashMap<>();//这个应该是筛选后的结果
        if (custom == null)
            return object;
        //custom全是重点人
        for (String name : custom) {//对每个自定义名字遍历
            Set<String> set = transferRecordService.selectRelativeName(name);//获取到所有关系
            Set<String> result = new HashSet<>();//筛选后的关系集合
            for (String keyName : custom) {
                if (set.contains(keyName))//如果name的关系集中有重点人的关系，那么把该重点人加入到关系集合中
                    result.add(keyName);
            }
            map.put(name, result);//筛选后的节点关系
        }
        //获取一层筛选结果
        //关联算法
        Map<String, Set<String>> all = new HashMap<>();

        for (String s : custom) {
            Set<String> set = transferRecordService.selectRelativeName(s);
            for (String temp : custom) {//不包含一层节点嫌疑人
                if (set.contains(temp))
                    set.remove(temp);
            }
            all.put(s, set);
        }

        TreeSet<String> list = new TreeSet<>();
        for (Map.Entry<String, Set<String>> entry1 : all.entrySet()) {
            for (Map.Entry<String, Set<String>> entry2 : all.entrySet()) {
                //如果是自己
                if (entry1.getKey().equals(entry2.getKey()))
                    continue;
                for (String s : entry1.getValue()) {//对第一个集合所有关联人进行遍历
                    if (entry2.getValue().contains(s)) {//如果在第二个集合的关联人内找到
                        list.add(s);//添加嫌疑人
                        break;
                    }
                }
            }
        }
        Set<String> result_name = new HashSet<>();//筛选
        for (String s : list) {//所有一级
            Set<String> allRelative = transferRecordService.selectRelativeName(s);
            Set<String> result = new HashSet<>();//筛选
            int count = 0;
            for (String name : custom) {
                if (allRelative.contains(name)) {
                    count++;
                    result.add(name);
                }
            }
            for (String first : list) {//如果和一级之间有关系
                if (allRelative.contains(first)) {
                    result.add(first);
                }
            }
            if (count >= number) {
                map.put(s, result);
                result_name.add(s);//把筛选的一级人员加进来
            }
        }
        //map:最终的关系了
        JSONObject graph = graphService.graphWith(map);
        object.put("graph", graph);
        JSONArray node = new JSONArray();
        for (String s : custom) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            node.add(temp);
        }
        object.put("node", node);
        JSONArray first_list = new JSONArray();
        for (String s : result_name) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            first_list.add(temp);
        }
        object.put("first_list", first_list);
        return object;
    }

    @RequestMapping(value = "/queryGraphSecond", method = RequestMethod.GET)
    public JSONObject queryGraphSecond(@RequestParam List<String> custom) {
        JSONObject object = new JSONObject();
        //新算法
        //获取所有人的关联人set集合
//        Map<String, Set<String>> new_map = new HashMap<>();
//        List<TransferRecord> temp_list = transferRecordService.selectAllTransferRecord();
//        List<String> nameList = new ArrayList<>();
        Set<String> secondFloor = new TreeSet<>();
        //所有人的名字
//        for (TransferRecord transferRecord : temp_list) {
//            if (!nameList.contains(transferRecord.getTransferRecordPayName()))
//                nameList.add(transferRecord.getTransferRecordPayName());
//            if (!nameList.contains(transferRecord.getTransferRecordCollectionName()))
//                nameList.add(transferRecord.getTransferRecordCollectionName());
//        }

        //第一层 节点姓名，关联人集合<关联人姓名，与关联人相关的集合>
        Set<String> suspects = new HashSet<>();
        for (String name : custom)
            suspects.add(name);

        //可以改掉
//        for (String name : nameList) {//将每个姓名以及对应的关联列表加入map中
//            Set<String> connlist = transferRecordService.selectRelativeName(name);
//            for (String name1 : suspects) {
//                connlist.remove(name1);
//            }
//            new_map.put(name,connlist);
//        }
        //new_map:所有人，以及其对应的关系人列表
        //nodeList:有问题的人的名字集合
        //开始查找nodeList对应的关系人集合
        Map<String, Set<String>> first = new TreeMap<>();
        for (String name : suspects) {
            Set<String> new_set = transferRecordService.selectRelativeName(name);//获取对应关系
            for (String name1 : suspects) {//去除根节点关系
                new_set.remove(name1);
            }
            first.put(name, new_set);//将每个人以及其对应的关系列表放入,去除和其他根节点关系,放入的是根节点和第一层人物关系集合
        }
        Set<String> haveJudged = new HashSet<>();//已经判断过的第二层姓名，
        //二层节点去除根节点关系和父节点关系
        for (Map.Entry<String, Set<String>> entry : first.entrySet()) {
            for (String name : entry.getValue()) {//获取到第二层节点人名
                Set<String> new_set = transferRecordService.selectRelativeName(name);//获取到该人名所有关系

                if (new_set.contains(entry.getKey())) {//去除父节点
                    new_set.remove(entry.getKey());
                }
                for (String name2 : suspects) {
                    if (new_set.contains(name2)) {//去除根节点
                        new_set.remove(name2);
                    }
                }
                //new_set :当前一层节点拥有的所有关系，去除了根节点和父节点,即当前一层节点对应的所有二层节点信息
                for (Map.Entry<String, Set<String>> entry1 : first.entrySet()) {
                    if (entry.getKey().equals(entry1.getKey()))//和自己以外的名称比对
                        continue;
                    //已经比对过的不比对
                    if (haveJudged.contains(entry1.getKey()))
                        continue;
                    for (String name1 : entry1.getValue()) {//获取到了其他根节点第一层节点的所有名称
                        if (new_set.contains(name1))//如果有关系
                        {
                            secondFloor.add(name1);//关系是相互的
                            secondFloor.add(name);//增加当前的一层节点名称
                        }
                    }
                }
            }
            haveJudged.add(entry.getKey());
        }
        Map<String, Set<String>> map = new HashMap<>();//这个应该是筛选后的结果
        if (custom == null)
            return object;
        //custom全是重点人
        for (String name : custom) {//对每个自定义名字遍历
            Set<String> set = transferRecordService.selectRelativeName(name);//获取到所有关系
            Set<String> result = new HashSet<>();//筛选后的关系集合
            for (String keyName : custom) {
                if (set.contains(keyName))//如果name的关系集中有重点人的关系，那么把该重点人加入到关系集合中
                    result.add(keyName);
            }
            map.put(name, result);//筛选后的节点关系
        }
        //custom:所有重点人，secondFloor所有二级
        for (String s : secondFloor) {//所有二级
            Set<String> allRelative = transferRecordService.selectRelativeName(s);
            Set<String> result = new HashSet<>();//筛选
            for (String name : custom) {
                if (allRelative.contains(name)) {
                    result.add(name);
                }
            }
            for (String second : secondFloor) {//如果和二级之间有关系
                if (allRelative.contains(second)) {
                    result.add(second);
                }
            }
            map.put(s, result);
        }
        JSONObject graph = graphService.graphWith(map);
        object.put("graph", graph);
        JSONArray node = new JSONArray();
        for (String s : custom) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            node.add(temp);
        }
        object.put("node", node);
        JSONArray second_list = new JSONArray();
        for (String s : secondFloor) {
            JSONObject temp = new JSONObject();
            temp.put("nodeName", s);
            second_list.add(temp);
        }
        object.put("second_list", second_list);
        return object;
    }

    @RequestMapping(value = "/queryDate", method = RequestMethod.GET)
    public JSONObject queryDate(@RequestParam Date beginDate, @RequestParam Date endDate, Integer pageIndex, Integer pageSize) {
        JSONObject object = new JSONObject();
        List<TransferRecord> list = transferRecordService.getDateBetween(beginDate, endDate, (pageIndex - 1) * pageSize, pageSize);
        object.put("list", list);
        return object;
    }


    @RequestMapping(value = "/exceptionRecord", method = RequestMethod.GET)
    public JSONObject exceptionFee(@RequestParam(defaultValue = "0") Double minFee, @RequestParam(defaultValue = "99999") Double maxFee, Integer pageIndex, Integer pageSize) {
        JSONObject object = new JSONObject();
        List<TransferRecord> list = transferRecordService.getExceptRecord(minFee, maxFee, pageIndex, pageSize);
        Integer count = transferRecordService.getExceptRecordCount(minFee, maxFee);
        object.put("list", list);
        object.put("count", count);
        return object;
    }

    @RequestMapping(value = "/onePersonGraph", method = RequestMethod.GET)
    public JSONObject getOnePersonGraph(@RequestParam String name, @RequestParam Integer level) {
        JSONObject object = new JSONObject();
        Set<String> relative = transferRecordService.selectRelativeName(name);
//        Map<String,Set<String>> map = new HashMap<>();
//        map.put(name,relative);
        JSONObject graph = graphService.graphExtend(name, relative, level);
        object.put("graph", graph);
        object.put("list", relative);
        return object;
    }
}
