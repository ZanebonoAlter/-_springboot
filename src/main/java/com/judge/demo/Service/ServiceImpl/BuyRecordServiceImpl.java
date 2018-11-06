package com.judge.demo.Service.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Dao.BuyRecordDao;
import com.judge.demo.Dao.BuyRecordMapper;
import com.judge.demo.Entity.BuyRecord;
import com.judge.demo.Entity.BuyRecordExample;
import com.judge.demo.Service.BuyRecordService;
import com.judge.demo.Untils.IndexUntil;
import com.judge.demo.Untils.MapSort;
import com.judge.demo.Untils.RowUntil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BuyRecordServiceImpl implements BuyRecordService {
    @Resource
    private BuyRecordMapper buyRecordMapper;
    @Resource
    private BuyRecordDao buyRecordDao;
    @Value("${lucene-index}")
    private String path;

    public JSONObject resolveData(List<MultipartFile> files, HttpServletRequest request) {
        JSONObject object = new JSONObject();
        List<BuyRecord> list = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                object.put("code", -1);
                object.put("msg", "文件不为空！");
                continue;
            }
            String fileName = file.getOriginalFilename();
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                object.put("code", 0);
                object.put("msg", "格式不正确");
                continue;
            }
            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            InputStream is = null;
            Workbook wb = null;
            Sheet sheet = null;
            try {
                is = file.getInputStream();
                if (isExcel2003) {
                    wb = new HSSFWorkbook(is);
                } else {
                    wb = new XSSFWorkbook(is);
                }
                sheet = wb.getSheetAt(0);
                if (sheet != null) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        /*
        数据获取
         */
            BuyRecord buyRecord;
            for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                buyRecord = new BuyRecord();
                //格式转换
                DecimalFormat df = new DecimalFormat("#");
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                RowUntil until = new RowUntil();
            /*
            为了保证程序运行不出错，需要对每个字段转换时进行类型判断
             */
                String buy_record_order_id = "";
                buy_record_order_id = until.RowString(row, 0);
                //空数据继续
                if (buy_record_order_id == null || buy_record_order_id.equals("")) {

                } else if (buy_record_order_id.equals("订单编号")) {
                    continue;
                }

                Double buy_record_order_fee = 0.0;
                buy_record_order_fee = until.RowDouble(row, 1);

                String buy_record_buyer_account = "";
                buy_record_buyer_account = until.RowString(row, 2);

                String buy_record_seller_account = "";
                buy_record_seller_account = until.RowString(row, 3);

                String buy_record_goods_title = "";
                buy_record_goods_title = until.RowString(row, 4);

                Double buy_record_goods_price = 0.0;
                buy_record_goods_price = until.RowDouble(row, 5);

                int buy_record_goods_num = 0;
                buy_record_goods_num = until.RowInt(row, 6);

                String buy_record_address = "";
                buy_record_address = until.RowString(row, 7);

                int buy_record_status = 0;
                buy_record_status = until.RowInt(row, 8);

                Date buy_record_time = null;
                String s_buy_record_time = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                buy_record_time = until.RowDate(row, 9);
                if (buy_record_time != null)
                    s_buy_record_time = sdf.format(buy_record_time);

                String buy_record_receiver_name = "";
                buy_record_receiver_name = until.RowString(row, 10);


                String buy_record_receiver_phone = "";
                buy_record_receiver_phone = until.RowString(row, 11);

                String buy_record_index = buy_record_order_id + "," + buy_record_order_fee + "," + buy_record_buyer_account + "," + buy_record_seller_account + "," + buy_record_goods_title + ","
                        + buy_record_goods_price + "," + buy_record_goods_num + "," + buy_record_address + "," + buy_record_status + "," + s_buy_record_time + "," + buy_record_receiver_name + "," + buy_record_receiver_phone + ","
                        + fileName;

//            Double new_buy_record_order_fee;
//            Double new_buy_record_goods_price;
//            int new_buy_record_goods_num;
//            int new_buy_record_status;
//            Date new_buy_record_time=null;
            /*
            尝试做格式转换
             */
                //SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
                //判断数字类型
//            if(buy_record_order_fee.equals("")||buy_record_order_fee.equals("\\N")){
//                new_buy_record_order_fee=0.0;
//            }else{
//                new_buy_record_order_fee = Double.parseDouble(buy_record_order_fee);
//            }
//            if(buy_record_goods_price.equals("")||buy_record_goods_price.equals("\\N")){
//                new_buy_record_goods_price=0.0;
//            }else{
//                new_buy_record_goods_price = Double.parseDouble(buy_record_goods_price);
//            }
//            if(buy_record_goods_num.equals("")||buy_record_goods_num.equals("\\N")){
//                new_buy_record_goods_num=0;
//            }else{
//                new_buy_record_goods_num = Integer.parseInt(buy_record_goods_num);
//            }
//            if(buy_record_status.equals("")||buy_record_status.equals("\\N")){
//                new_buy_record_status=0;
//            }else{
//                new_buy_record_status = Integer.parseInt(buy_record_status);
//            }
                //如果日期不为空或者\N
//            if(buy_record_time.equals("\\N")||buy_record_time.equals("")){
//
//            }else{
//                try {
//                    new_buy_record_time=sDateFormat.parse(buy_record_time);
//                } catch (ParseException e) {
//                    //如果格式不对进行下一个数据
//                    e.printStackTrace();
//                    continue;
//                }
//            }
                //数据注入
                buyRecord.setBuyRecordOrderId(buy_record_order_id);
                buyRecord.setBuyRecordOrderFee(buy_record_order_fee);
                buyRecord.setBuyRecordBuyerAccount(buy_record_buyer_account);
                buyRecord.setBuyRecordSellerAccount(buy_record_seller_account);
                buyRecord.setBuyRecordGoodsTitle(buy_record_goods_title);
                buyRecord.setBuyRecordGoodsPrice(buy_record_goods_price);
                buyRecord.setBuyRecordGoodsNum(buy_record_goods_num);
                buyRecord.setBuyRecordAddress(buy_record_address);
                buyRecord.setBuyRecordStatus(buy_record_status);
                buyRecord.setBuyRecordTime(buy_record_time);
                buyRecord.setBuyRecordReceiverName(buy_record_receiver_name);
                buyRecord.setBuyRecordReceiverPhone(buy_record_receiver_phone);
                buyRecord.setBuyRecordIndex(buy_record_index);
                list.add(buyRecord);
            }
        }
        object.put("code", 1);
        object.put("msg", "加载成功");
        object.put("effect_size", "加载有效格式数据:" + list.size() + "条");
          /*
        做插入操作
         */
        //有效记录集合
        List<BuyRecord> result = new ArrayList<>();
        //重复记录数量
        int count = 0;
        for (BuyRecord record :
                list) {
            int flag = buyRecordDao.addBuyRecord(record);
            if (flag == 1) {//成功插入
                result.add(record);
            } else {
                count++;
            }
        }
        object.put("uneffect_size", "重复数据共:" + count + "条");
        object.put("new_size", "新增数据共:" + result.size() + "条");
        /*
        建立索引
         */
        synchronized (this) {
            try {
//            System.out.println(request.getSession().getServletContext().getRealPath("/"));
                IndexUntil indexUntil = new IndexUntil(path);
                for (BuyRecord record : result) {
                    Document document = new Document();
                    document.add(new TextField("buy_record_index", record.getBuyRecordIndex(), Field.Store.YES));
                    document.add(new TextField("buy_record_id", record.getBuyRecordId() + "", Field.Store.YES));
                    indexUntil.getWriter().addDocument(document);
                }
                indexUntil.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
        记录标明,不要了
         */
//        Map<String,Integer> map = new HashMap<>();
//        for(BuyRecord record:result){
//            //买家\t卖家\t收货人
//            String key = "买家账号:"+record.getBuyRecordBuyerAccount()+"\t卖家账号:"+record.getBuyRecordSellerAccount()+"\t收件人:"+record.getBuyRecordReceiverName();
//            if(map.containsKey(key)){
//                int num = map.get(key)+1;
//                map.put(key,num);
//            }else{
//                map.put(key,1);
//            }
//        }
//        //按照数量降序排序
//        map= MapSort.sortMapByValue(map);
//        StringBuffer result_msg=new StringBuffer();
//        map.forEach((k, v) -> result_msg.append("重点人员"+k+"\t记录条数"+v+"条\n"));
//        String s_msg = result_msg.toString();
//        object.put("detail",s_msg);
        return object;
    }

    @Override
    public List<BuyRecord> selectAllBuyRecord(Integer pageIndex, Integer pageSize) {
        BuyRecordExample example = new BuyRecordExample();
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        example.setOrderByClause("buy_record_time desc");
        return buyRecordMapper.selectByExample(example);
    }

    @Override
    public List<BuyRecord> selectBuyRecordByIds(List<Long> Ids, Integer pageIndex, Integer pageSize) {
        if(Ids==null||Ids.isEmpty())
            return null;
        BuyRecordExample example = new BuyRecordExample();
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        example.setOrderByClause("buy_record_time desc");
        BuyRecordExample.Criteria criteria = example.createCriteria();
        criteria.andBuyRecordIdIn(Ids);
        return buyRecordMapper.selectByExample(example);
    }

    @Override
    public Integer selectAllBuyRecordCount() {
        BuyRecordExample example = new BuyRecordExample();
        return buyRecordMapper.selectByExample(example).size();
    }

    @Override
    public Integer selecrBuyRecordCountByIds(List<Long> Ids) {
        if(Ids==null||Ids.isEmpty())
            return 0;
        BuyRecordExample example = new BuyRecordExample();
        BuyRecordExample.Criteria criteria = example.createCriteria();
        criteria.andBuyRecordIdIn(Ids);
        return buyRecordMapper.selectByExample(example).size();
    }

    @Override
    public List<String> selectPhoneByName(String name) {
        return buyRecordDao.selecrPhoneByName(name);
    }

    @Override
    public List<BuyRecord> selectBuyRecordByIds(List<Long> Ids) {
        if(Ids==null||Ids.isEmpty())
            return null;
        BuyRecordExample example = new BuyRecordExample();
        BuyRecordExample.Criteria criteria = example.createCriteria();
        criteria.andBuyRecordIdIn(Ids);
        example.setOrderByClause("buy_record_time desc");
        return buyRecordMapper.selectByExample(example);
    }

    @Override
    public Integer currenctCount() {
        return buyRecordDao.currentCount();
    }

    @Override
    public List<BuyRecord> selectBuyName(String name) {
        BuyRecordExample example = new BuyRecordExample();
        BuyRecordExample.Criteria criteria = example.createCriteria();
        criteria.andBuyRecordReceiverNameEqualTo(name);
        return buyRecordMapper.selectByExample(example);
    }
}
