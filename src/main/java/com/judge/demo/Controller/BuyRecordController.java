package com.judge.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.BuyRecord;
import com.judge.demo.Entity.Person;
import com.judge.demo.Entity.TransferRecord;
import com.judge.demo.Service.BuyRecordService;
import com.judge.demo.Service.SearchService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/judge/buyrecord")
public class BuyRecordController {
    @Resource
    private BuyRecordService buyRecordService;
    @Resource
    private SearchService searchService;

    @RequestMapping(value="/upload",method = RequestMethod.POST)
    public JSONObject Upload(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        JSONObject object = buyRecordService.resolveData(files,request);
        Person.flag=1;
        TransferRecord.transfer_flag=1;
        return object;
    }
    @RequestMapping(value = "/search/{query}",method = RequestMethod.GET)
    public JSONObject Search(HttpServletRequest request, @PathVariable("query")String query){
        JSONObject object = new JSONObject();
        System.out.println(query);
        List<Long> result = searchService.getBuyRecordId(query,request);
        object.put("result",result);
        return object;
    }
    @RequestMapping(value="/select/selectAll/{pageIndex}/{pageSize}",method = RequestMethod.GET)
    public JSONObject selectAll(@PathVariable Integer pageIndex,@PathVariable Integer pageSize){
        JSONObject object = new JSONObject();
        List<BuyRecord> list = buyRecordService.selectAllBuyRecord((pageIndex-1)*pageSize,pageSize);
        Integer count = buyRecordService.selectAllBuyRecordCount();
        Integer total = count/pageSize;
        if(count%pageSize!=0)
            total++;
        object.put("code",1);
        object.put("list",list);
        object.put("total",total);
        object.put("count",count);
        return object;
    }
    @RequestMapping(value="/select/selectSearch/{pageIndex}/{pageSize}/{query}",method = RequestMethod.GET)
    public JSONObject selectBySearch(@PathVariable Integer pageIndex,@PathVariable Integer pageSize,@PathVariable String query,HttpServletRequest request){
        JSONObject object = new JSONObject();
        List<Long> Ids = searchService.getBuyRecordId(query,request);
        List<BuyRecord> list = buyRecordService.selectBuyRecordByIds(Ids,(pageIndex-1)*pageSize,pageSize);
        Integer count = buyRecordService.selecrBuyRecordCountByIds(Ids);
        Integer total = count/pageSize;
        if(count%pageSize!=0)
            total++;
        object.put("code",1);
        object.put("list",list);
        object.put("total",total);
        object.put("count",count);
        return object;
    }

    @RequestMapping(value = "/currentCount",method = RequestMethod.GET)
    public JSONObject currentCount(){
        JSONObject object = new JSONObject();
        Integer count = buyRecordService.currenctCount();
        object.put("count",count);
        return object;
    }
}
