package com.judge.demo.Service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Dao.PersonDao;
import com.judge.demo.Dao.PersonRecordMapper;
import com.judge.demo.Dao.TransferRecordMapper;
import com.judge.demo.Entity.TransferRecord;
import com.judge.demo.Service.GraphService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GraphServiceImpl implements GraphService {
    @Resource
    private PersonDao personDao;
    @Resource
    private TransferRecordMapper transferRecordMapper;

    private Map<String, HashSet<String>> graphSet(Map<String, HashSet<String>> data,String name,String add1,String add2){
        HashSet<String> temp;
        if(!data.containsKey(name)){
             temp = new HashSet<>();
        }else{
            temp=data.get(name);
        }
//        for(String s:temp){
//            if(s.indexOf(add1)!=-1||s.indexOf(add2)!=-1){
//                return data;
//            }
//        }
        if(add1==null||add2==null||add1.equals("")||add2.equals("")){
            return data;
        }
        temp.add("支付宝账号:"+add1+"\n"+"支付宝账号Id:"+add2+"\n");
        data.put(name,temp);
        return data;
    }
    @Override
    public JSONObject graphArray(List<TransferRecord> list,List<String> query) {
        JSONObject object = new JSONObject();
        if (list==null)
            return null;
        Map<String, HashSet<String>> Node_zhifubao = new HashMap<>();
        //<"source,target",count>
        Map<String,Integer> links_map = new HashMap<>();

        JSONArray links = new JSONArray();

        //首先获取关系
        for(TransferRecord record:list) {
            String payName = record.getTransferRecordPayName();
            String payZhifubao = record.getTransferRecordPayZhifubao();
            String payZhifubaoId = record.getTransferRecordPayZhifubaoId();

            String collectionName = record.getTransferRecordCollectionName();
            String collectionZhifubao = record.getTransferRecordCollectionZhifubao();
            String collectionZhifubaoId = record.getTransferRecordCollectionZhifubaoId();

            Double fee = record.getTransferRecordPayFee();

            Node_zhifubao=graphSet(Node_zhifubao,payName,payZhifubao,payZhifubaoId);
            Node_zhifubao=graphSet(Node_zhifubao,collectionName,collectionZhifubao,collectionZhifubaoId);
            //判断是否在已有的双向关系中存在
            //记录数量
            String Link_Name = payName+","+collectionName;
            String Link_judge = collectionName+","+payName;
            if(links_map.containsKey(Link_Name)){
                Integer count = links_map.get(Link_Name)+1;
                links_map.put(Link_Name,count);
            }else if(links_map.containsKey(Link_judge)){
                Integer count = links_map.get(Link_judge)+1;
                links_map.put(Link_judge,count);
            }else{
                links_map.put(Link_Name,1);
            }
        }

        for(Map.Entry<String,Integer> entry:links_map.entrySet()){
            String[] temp = entry.getKey().split(",");
            String payName = temp[0];
            String collectionName =temp[1];

            JSONObject link = new JSONObject();
            link.put("source",payName);
            link.put("target",collectionName);
            link.put("value",entry.getValue());

            JSONObject label = new JSONObject();
            JSONObject normal = new JSONObject();
            JSONObject textStyle = new JSONObject();
            textStyle.put("fontSize",30);
            normal.put("show",true);
            normal.put("position","middle");
            normal.put("color","black");
            normal.put("formatter",entry.getValue()+"条");
            label.put("normal",normal);
//            link.put("label",label);

            JSONObject lineStyle = new JSONObject();
            JSONObject lineStyle_normal = new JSONObject();

//            if(entry.getValue()>=5){
//                lineStyle_normal.put("width",10);
//                lineStyle_normal.put("color","yellow");
//            }
//            if(payName.equals(collectionName)){
//                lineStyle_normal.put("color","black");
//            }
            lineStyle.put("normal",lineStyle_normal);
            link.put("lineStyle",lineStyle);

//            String[] symbol = new String[2];
//            symbol[0]="circle";
//            symbol[1]="arrow";
//            link.put("symbol",symbol);

            links.add(link);
        }
        object.put("links",links);
        //然后写入节点
        JSONArray Nodes = new JSONArray();
        for(Map.Entry<String, HashSet<String>> entry:Node_zhifubao.entrySet()){
            JSONObject node = new JSONObject();
            node.put("name",entry.getKey());
            node.put("value",0);
            node.put("symbolSize",15);
            /*
            测试
             */
            if(personDao.isSearch(entry.getKey())!=0){
                node.put("category",0);
            } else if(query.contains(entry.getKey())){
                node.put("category",1);
            } else{
                node.put("category",2);
            }
            //end

            JSONObject tooltip = new JSONObject();
            JSONObject textStyle = new JSONObject();
            textStyle.put("align","left");
            tooltip.put("trigger","item");
            tooltip.put("textStyle",textStyle);
            String detail ="姓名:"+entry.getKey()+"\n";
            for(String s:entry.getValue()){
                detail+=s;
            }
            tooltip.put("formatter",detail);
            tooltip.put("extraCssText","white-space:pre-wrap");
//            node.put("tooltip",tooltip);

            JSONObject label = new JSONObject();
            JSONObject label_normal = new JSONObject();
            label_normal.put("show",true);
            label_normal.put("position","top");
            label_normal.put("fontSize",30);
            label_normal.put("formatter",detail);
            label.put("normal",label_normal);
            node.put("label",label);


            Nodes.add(node);
        }
        object.put("data",Nodes);
        return object;
    }

    @Override
    public JSONObject graphWith(Map<String, Set<String>> nameMap) {
        JSONObject object = new JSONObject();
        //说明：set是已经做好的关系
        //先做一个关系去重
        Set<String> first = new HashSet<>();//第一步，先获取参数所有人互相之间关系，source，target
        for (Map.Entry<String,Set<String>>entry:nameMap.entrySet()){
            for(String realtiveName:entry.getValue()){
                first.add(entry.getKey()+","+realtiveName);
            }
        }
        Set<String> set = new HashSet<>();
        //然后对关系set<source，target>去重
        for (String info:first){
            String[] temp = info.split(",");
            String source = temp[0];
            String target = temp[1];
            if(set.contains(source+","+target)||set.contains(target+","+source)){
                continue;
            }
            //遇到null就跳过
            if(source.equals("null")||target.equals("null"))
                continue;
            set.add(source+","+target);
        }
        //制作关系的集合
        JSONArray links = new JSONArray();
        for(String relationship:set){
            String[] temp = relationship.split(",");
            String payName = temp[0];
            String collectionName =temp[1];

            JSONObject link = new JSONObject();
            link.put("source",payName);
            link.put("target",collectionName);
            link.put("flag",0);//原生字段
            //link.put("value",entry.getValue());

            JSONObject lineStyle = new JSONObject();
            JSONObject lineStyle_normal = new JSONObject();

            lineStyle.put("normal",lineStyle_normal);
//            link.put("lineStyle",lineStyle);

            links.add(link);

        }
        object.put("links",links);
        Set<String> nameSet = new HashSet<>();
        //获取所有人名字
        for (String name:first){
            String[] temp=name.split(",");
            nameSet.add(temp[0]);
            nameSet.add(temp[1]);
        }
        //记录节点,因为排版问题，把支付宝信息去掉了
        JSONArray Nodes = new JSONArray();
        for (String name:nameSet){
            JSONObject node = new JSONObject();
            node.put("flag",0);
            node.put("extend",0);
            node.put("name",name);
            Integer in = transferRecordMapper.selectCountInByName(name);
            if(in==null)
                in=0;
            Integer out = transferRecordMapper.selectCountOutByName(name);
            if(out==null)
                out=0;
            Integer value = in+out;
            node.put("value",value);
            if (value>100)
                node.put("symbolSize",80);
            else if(value>20)
                node.put("symbolSize",50);
            else
                node.put("symbolSize",20);
            /*
            测试
             */
            if(personDao.isSearch(name)!=0){
                node.put("category",0);
            } else{
                node.put("category",1);
            }
            //end

            JSONObject tooltip = new JSONObject();
            JSONObject textStyle = new JSONObject();
            textStyle.put("align","left");
            tooltip.put("trigger","item");
            tooltip.put("textStyle",textStyle);
            String detail =name;
//            for(String s:entry.getValue()){
//                detail+=s;
//            }
//            tooltip.put("formatter",detail);
//            tooltip.put("extraCssText","white-space:pre-wrap");
//            node.put("tooltip",tooltip);

            JSONObject label = new JSONObject();
            JSONObject label_normal = new JSONObject();
            label_normal.put("show",true);
            label_normal.put("position","top");
            label_normal.put("fontSize",20);
            label_normal.put("formatter",detail);
            label.put("normal",label_normal);
            node.put("label",label);


            Nodes.add(node);
        }
        object.put("data",Nodes);
        return object;
    }

    @Override
    public JSONObject graphExtend(String name,Set<String> nameRelative,Integer level){
        JSONObject object = new JSONObject();
        //制作关系的集合
        JSONArray links = new JSONArray();
        for(String relationship:nameRelative){

            JSONObject link = new JSONObject();
            link.put("source",name);
            link.put("target",relationship);
            link.put("flag",(1+level)+","+name);//新增字段,为当前层级=传进来的层级+1，后面接的名字是上一层人物的名字

            //link.put("value",entry.getValue());

            JSONObject lineStyle = new JSONObject();
            JSONObject lineStyle_normal = new JSONObject();

            lineStyle.put("normal",lineStyle_normal);
//            link.put("lineStyle",lineStyle);

            links.add(link);

        }
        object.put("links",links);
        Set<String> nameSet = new HashSet<>();
        //获取所有人名字
        for (String name_temp:nameRelative){
            nameSet.add(name_temp);
        }
        nameSet.add(name);
        //记录节点,因为排版问题，把支付宝信息去掉了
        JSONArray Nodes = new JSONArray();
        for (String name_temp:nameSet){
            if(name_temp.equals(name))
                continue;
            JSONObject node = new JSONObject();
            node.put("flag",(1+level)+","+name);//当前层级，上一层级人名
            node.put("extend",0);//0表示尚未扩展，1表示扩展
            node.put("name",name_temp);
            Integer in = transferRecordMapper.selectCountInByName(name_temp);
            if(in==null)
                in=0;
            Integer out = transferRecordMapper.selectCountOutByName(name_temp);
            if(out==null)
                out=0;
            Integer value = in+out;
            node.put("value",value);
            if (value>100)
                node.put("symbolSize",80);
            else if(value>20)
                node.put("symbolSize",50);
            else
                node.put("symbolSize",20);
            /*
            测试
             */
            if(personDao.isSearch(name_temp)!=0){
                node.put("category",0);
            }else{
                node.put("category",2);
            }
            //end

            JSONObject tooltip = new JSONObject();
            JSONObject textStyle = new JSONObject();
            textStyle.put("align","left");
            tooltip.put("trigger","item");
            tooltip.put("textStyle",textStyle);
            String detail =name_temp;
//            for(String s:entry.getValue()){
//                detail+=s;
//            }
//            tooltip.put("formatter",detail);
//            tooltip.put("extraCssText","white-space:pre-wrap");
//            node.put("tooltip",tooltip);

            JSONObject label = new JSONObject();
            JSONObject label_normal = new JSONObject();
            label_normal.put("show",true);
            label_normal.put("position","top");
            label_normal.put("fontSize",20);
            label_normal.put("formatter",detail);
            label.put("normal",label_normal);
            node.put("label",label);


            Nodes.add(node);
        }
        object.put("data",Nodes);
        return object;
    }
}
