package com.judge.demo.Service.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Dao.TransferRecordDao;
import com.judge.demo.Dao.TransferRecordMapper;
import com.judge.demo.Entity.BuyRecord;
import com.judge.demo.Entity.TransferRecord;
import com.judge.demo.Entity.TransferRecordExample;
import com.judge.demo.Service.TransferRecordService;
import com.judge.demo.Untils.IndexUntil;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransferRecordServiceImpl implements TransferRecordService {
    @Resource
    private TransferRecordMapper transferRecordMapper;
    @Resource
    private TransferRecordDao transferRecordDao;
    @Value("${lucene-index}")
    private String path;

    @Override
    public JSONObject resolveData(List<MultipartFile> files, HttpServletRequest request) {
        JSONObject object = new JSONObject();

        String error_format_message = "";
        Integer effective_num = 0;//有效加载数据
        Integer repeat_num = 0;//重复数据
        Integer code = 1;//加载情况，1为正常，0为有错误
        List<TransferRecord> list = new ArrayList<>();//所有加载成功的数据
        List<TransferRecord> result = new ArrayList<>();//有效数据
        for (MultipartFile file : files) {
            if (files.isEmpty()) {
                error_format_message += file.getOriginalFilename() + "文件不应该为空\n";
                code = 0;//表示有错误
                //object.put("code",0);//表示有错误
                object.put("error_format_message", error_format_message);
                continue;
            }
            /*
            判断文件格式
             */
            String fileName = file.getOriginalFilename();
            if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
                code = 0;
                error_format_message += fileName + "格式不正确\n";
                object.put("error_format_message", error_format_message);
                return object;
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
            TransferRecord transferRecord;
            for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                transferRecord = new TransferRecord();
                DecimalFormat df = new DecimalFormat("#");
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                String transfer_record_flow_id = "";
                Date transfer_record_create_time = null;
                Date transfer_record_pay_time = null;
                Double transfer_record_pay_fee = 0.0;
                String transfer_record_pay_zhifubao = "";
                String transfer_record_pay_zhifubao_id = "";
                String transfer_record_pay_name = "";
                String transfer_record_collection_zhifubao = "";
                String transfer_record_collection_zhifubao_id = "";
                String transfer_record_collection_name = "";
                String transfer_record_remark = "";
                String transfer_record_flow = "";
                String transfer_record_status = "";
                String transfer_record_product_name = "";
                String transfer_record_index = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                RowUntil until = new RowUntil();

                transfer_record_flow_id = until.RowString(row, 0);
                //空记录结束
                if (transfer_record_flow_id == null || transfer_record_flow_id.equals("")) {

                } else if (transfer_record_flow_id.equals("流水号")) {
                    continue;
                }

                transfer_record_create_time = until.RowDate(row, 1);
                String s_transfer_record_create_time = "";
                if (transfer_record_create_time != null) {
                    s_transfer_record_create_time = sdf.format(transfer_record_create_time);
                }
                transfer_record_pay_time = until.RowDate(row, 2);
                String s_transfer_record_pay_time = "";
                if (transfer_record_pay_time != null) {
                    s_transfer_record_pay_time = sdf.format(transfer_record_pay_time);
                }
                transfer_record_pay_fee = until.RowDouble(row, 3);
                transfer_record_pay_zhifubao = until.RowString(row, 4);
                transfer_record_pay_zhifubao_id = until.RowString(row, 5);
                transfer_record_pay_name = until.RowString(row, 6);
                transfer_record_collection_zhifubao = until.RowString(row, 7);
                transfer_record_collection_zhifubao_id = until.RowString(row, 8);
                transfer_record_collection_name = until.RowString(row, 9);
                transfer_record_remark = until.RowString(row, 10);
                transfer_record_flow = until.RowString(row, 11);
                transfer_record_status = until.RowString(row, 12);
                transfer_record_product_name = until.RowString(row, 13);

                transfer_record_index = transfer_record_flow_id + "," + s_transfer_record_create_time + "," + s_transfer_record_pay_time + "," + transfer_record_pay_fee + ","
                        + transfer_record_pay_zhifubao + "," + transfer_record_pay_zhifubao_id + "," + transfer_record_pay_name + "," + transfer_record_collection_zhifubao + "," + transfer_record_collection_zhifubao_id + ","
                        + transfer_record_collection_name + "," + transfer_record_remark + "," + transfer_record_flow + "," + transfer_record_status + "," + transfer_record_product_name + "," + fileName;

                transferRecord.setTransferRecordFlowId(transfer_record_flow_id);
                transferRecord.setTransferRecordCreateTime(transfer_record_create_time);
                transferRecord.setTransferRecordPayTime(transfer_record_pay_time);
                transferRecord.setTransferRecordPayFee(transfer_record_pay_fee);
                transferRecord.setTransferRecordPayZhifubao(transfer_record_pay_zhifubao);
                transferRecord.setTransferRecordPayZhifubaoId(transfer_record_pay_zhifubao_id);
                transferRecord.setTransferRecordPayName(transfer_record_pay_name);
                transferRecord.setTransferRecordCollectionZhifubao(transfer_record_collection_zhifubao);
                transferRecord.setTransferRecordCollectionZhifubaoId(transfer_record_collection_zhifubao_id);
                transferRecord.setTransferRecordCollectionName(transfer_record_collection_name);
                transferRecord.setTransferRecordRemark(transfer_record_remark);
                transferRecord.setTransferRecordFlow(transfer_record_flow);
                transferRecord.setTransferRecordStatus(transfer_record_status);
                transferRecord.setTransferRecordProductName(transfer_record_product_name);
                transferRecord.setTransferRecordIndex(transfer_record_index);

                list.add(transferRecord);
            }
        }
        object.put("effect_size", "加载有效格式数据:" + list.size() + "条");
        //以上所有文件读取完毕
        for (TransferRecord record : list) {
            int flag = transferRecordDao.addTransferRecord(record);
            if (flag == 1) {
                result.add(record);
            } else {
                repeat_num++;
            }
        }
        object.put("uneffect_size", "重复数据共:" + repeat_num + "条");
        object.put("new_size", "新增数据共:" + result.size() + "条");
        //result为新增数据
         /*
        建立索引
         */
        synchronized (this) {
            try {
//            System.out.println(request.getSession().getServletContext().getRealPath("/"));
                IndexUntil indexUntil = new IndexUntil(path);
                for (TransferRecord record : result) {
                    Document document = new Document();
                    document.add(new TextField("transfer_record_index", record.getTransferRecordIndex(), Field.Store.YES));
                    document.add(new TextField("transfer_record_id", record.getTransferRecordId() + "", Field.Store.YES));
                    indexUntil.getWriter().addDocument(document);
                }
                indexUntil.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        object.put("code", code);
        return object;
    }

    @Override
    public List<TransferRecord> selectAllTransferRecord(Integer pageIndex, Integer pageSize) {
        TransferRecordExample example = new TransferRecordExample();
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        example.setOrderByClause("transfer_record_create_time desc");
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public List<TransferRecord> selectAllTransferRecord(Integer pageIndex, Integer pageSize, Date beginDate, Date endDate) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayTimeBetween(beginDate, endDate);
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        example.setOrderByClause("transfer_record_create_time desc");
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public List<TransferRecord> selectTransferRecordByIds(List<Long> Ids, Integer pageIndex, Integer pageSize, Date beginDate, Date endDate) {
        if(Ids==null||Ids.isEmpty())
            return null;
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        example.setOrderByClause("transfer_record_create_time desc");
        criteria.andTransferRecordIdIn(Ids);
        criteria.andTransferRecordPayTimeBetween(beginDate, endDate);
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public List<TransferRecord> selectTransferRecordByIds(List<Long> Ids, Integer pageIndex, Integer pageSize) {
        if(Ids==null||Ids.isEmpty())
            return null;
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        example.setOrderByClause("transfer_record_create_time desc");
        criteria.andTransferRecordIdIn(Ids);
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public Integer selectAllTransferRecordCount() {
        TransferRecordExample example = new TransferRecordExample();
        return transferRecordMapper.selectByExample(example).size();
    }

    @Override
    public Integer selectAllTransferRecordCount(Date beginDate, Date endDate) {

        return transferRecordDao.selectAllTransferRecordCountByDate(beginDate,endDate);
    }

    @Override
    public Integer selecrTransferRecordCountByIds(List<Long> Ids, Date beginDate, Date endDate) {
        if(Ids==null||Ids.isEmpty())
            return 0;
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayTimeBetween(beginDate, endDate);
        criteria.andTransferRecordIdIn(Ids);
        return transferRecordMapper.selectByExample(example).size();
    }

    @Override
    public Integer selecrTransferRecordCountByIds(List<Long> Ids) {
        if(Ids==null||Ids.isEmpty())
            return 0;
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordIdIn(Ids);
        return transferRecordMapper.selectByExample(example).size();
    }

    @Override
    public List<TransferRecord> selectAllTransferRecordByIds(List<Long> Ids) {
        if(Ids==null||Ids.isEmpty())
            return null;
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("transfer_record_create_time desc");
        criteria.andTransferRecordIdIn(Ids);
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public List<TransferRecord> selectAllTransferRecord() {
        TransferRecordExample example = new TransferRecordExample();
        example.setOrderByClause("transfer_record_create_time desc");
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public List<TransferRecord> selectTransferRecordByName(String name1, String name2) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayNameEqualTo(name1);
        criteria.andTransferRecordCollectionNameEqualTo(name2);

        TransferRecordExample example1 = new TransferRecordExample();
        TransferRecordExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andTransferRecordPayNameEqualTo(name2);
        criteria1.andTransferRecordCollectionNameEqualTo(name1);

        List<TransferRecord> list1 = transferRecordMapper.selectByExample(example);
        List<TransferRecord> list2 = transferRecordMapper.selectByExample(example1);
        list1.addAll(list2);
        return list1;
    }

    @Override
    public Set<String> selectName(Integer num, Double fee) {

        List<Map<String, Object>> payList = transferRecordMapper.selectiveGroupByPayName(fee);
        List<Map<String, Object>> collectionList = transferRecordMapper.selectiveGroupByCollectionName(fee);
        Set<String> result = new TreeSet<>();
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < payList.size(); i++) {
            String name = payList.get(i).get("transfer_record_pay_name").toString();
            Integer count = Integer.parseInt(payList.get(i).get("count(*)").toString());
            if (!map.containsKey(name)) {
                map.put(name, count);
            } else {
                Integer number = map.get(name) + count;
                map.put(name, number);
            }
        }
        for (int i = 0; i < collectionList.size(); i++) {
            String name = collectionList.get(i).get("transfer_record_collection_name").toString();
            Integer count = Integer.parseInt(collectionList.get(i).get("count(*)").toString());
            if (!map.containsKey(name)) {
                map.put(name, count);
            } else {
                Integer number = map.get(name) + count;
                map.put(name, number);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= num)
                result.add(entry.getKey());
        }
        return result;
    }

    @Override
    public Set<String> selectRelativeName(String name) {
//        List<String> payName = transferRecordDao.selectPayNameByCollectionName(name);
//        List<String> collcetionName = transferRecordDao.selectCollectionameByPayName(name);
//        Set<String> result = new HashSet<>();

//        result.addAll(payName);
//        result.addAll(collcetionName);
//        result.remove(name);
        return transferRecordMapper.selectRelativeNameList(name);
    }

    @Override
    public Integer currentCount() {
        return transferRecordDao.selectAllTransferRecordCount();
    }

    @Override
    public List<TransferRecord> getTransferListByName(Set<String> set) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        List<String> list = new ArrayList<>();
        for (String s : set)
            list.add(s);
        criteria.andTransferRecordPayNameIn(list);
        List<TransferRecord> recordList_1 = transferRecordMapper.selectByExample(example);

        TransferRecordExample example1 = new TransferRecordExample();
        TransferRecordExample.Criteria criteria1 = example1.createCriteria();
        criteria.andTransferRecordCollectionNameIn(list);
        List<TransferRecord> recordList_2 = transferRecordMapper.selectByExample(example1);

        Set<TransferRecord> result = new HashSet<>();
        result.addAll(recordList_1);
        result.addAll(recordList_2);

        List<TransferRecord> final_record = new ArrayList<>();
        final_record.addAll(result);

        return final_record;
    }

    @Override
    public List<String> getMulitiZhifubaoNameList() {
        List<Map<String, Object>> payList = transferRecordMapper.selectNameListGroupByPayName();
        List<Map<String, Object>> collectionList = transferRecordMapper.selectNameListGroupByCollectionName();
        Map<String, Set<String>> map = new TreeMap<>();
        for (int i = 0; i < payList.size(); i++) {
            String transfer_record_pay_zhifubao_id = payList.get(i).get("transfer_record_pay_zhifubao").toString();
            String transfer_record_pay_name = payList.get(i).get("transfer_record_pay_name").toString();

            Set<String> set = new TreeSet<>();
            if (!map.containsKey(transfer_record_pay_name)) {//如果没有
                set.add(transfer_record_pay_zhifubao_id);
                map.put(transfer_record_pay_name, set);
            } else {
                set = map.get(transfer_record_pay_name);
                set.add(transfer_record_pay_zhifubao_id);
                map.put(transfer_record_pay_name, set);
            }

        }
        for (int i = 0; i < collectionList.size(); i++) {
            String id = collectionList.get(i).get("transfer_record_collection_zhifubao").toString();
            String name = collectionList.get(i).get("transfer_record_collection_name").toString();

            Set<String> set = new TreeSet<>();
            if (!map.containsKey(name)) {//如果没有
                set.add(id);
                map.put(name, set);
            } else {
                set = map.get(name);
                set.add(id);
                map.put(name, set);
            }
        }

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    @Override
    public List<String> getMulitiZhifubaoNameList(Integer pageIndex, Integer pageSize, String query) {
        List<Map<String,String>> result = transferRecordMapper.selectMutiZhifubaoWithName(pageIndex,pageSize,"%"+query+"%");
        List<String> nameList = new ArrayList<>();
        for (Map<String,String> record:result){
            nameList.add(record.get("name"));
        }
        return nameList;
    }

    @Override
    public Integer getMulitiZhifubaoNameListCount(String query) {
        return transferRecordMapper.selectMutiZhifubaoCountWithName("%"+query+"%");
    }

    @Override
    public Set<String> getZhifubaoIdsByName(String name) {
        return transferRecordMapper.getZhifubaoIdsByName(name);
    }

    @Override
    public List<TransferRecord> getDateBetween(Date beginDate, Date endDate, Integer pageIndex, Integer pageSize) {
        TransferRecordExample example = new TransferRecordExample();
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayTimeBetween(beginDate, endDate);
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public Map<String, Integer> getInOut(String name) {
        Integer In = transferRecordMapper.selectCountIn();
        Integer Out = transferRecordMapper.selectCountOut();
        Map<String, Integer> result = new HashMap<>();
        result.put("In", In);
        result.put("Out", Out);
        return result;
    }

    @Override
    public Map<String, Double> getIntOutAll(Date beginDate, Date endDate) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayTimeBetween(beginDate, endDate);

        Double out = 0.0;
        Double in = 0.0;
        List<TransferRecord> list = transferRecordMapper.selectByExample(example);
        for (TransferRecord record : list) {
            if (record.getTransferRecordFlow().equals("转出")) {
                out += record.getTransferRecordPayFee();
            } else {
                in += record.getTransferRecordPayFee();
            }
        }
        Map<String, Double> map = new HashMap<>();
        map.put("in", in);
        map.put("out", out);
        return map;
    }

    @Override
    public Map<String, Double> getIntOutAll(List<Long> Ids, Date beginDate, Date endDate) {
        if(Ids==null||Ids.isEmpty())
            return null;
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayTimeBetween(beginDate, endDate);

        criteria.andTransferRecordIdIn(Ids);
        Double out = 0.0;
        Double in = 0.0;
        List<TransferRecord> list = transferRecordMapper.selectByExample(example);
        for (TransferRecord record : list) {
            if (record.getTransferRecordFlow().equals("转出")) {
                out += record.getTransferRecordPayFee();
            } else {
                in += record.getTransferRecordPayFee();
            }
        }
        Map<String, Double> map = new HashMap<>();
        map.put("in", in);
        map.put("out", out);
        return map;
    }

    @Override
    public List<TransferRecord> getExceptRecord(Double minFee, Double maxFee) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayFeeBetween(minFee, maxFee);
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public List<TransferRecord> getExceptRecord(Double minFee, Double maxFee, Integer pageIndex, Integer pageSize) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayFeeBetween(minFee,maxFee);
        example.setLimitStart(pageIndex);
        example.setLimitEnd(pageSize);
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    public Integer getExceptRecordCount(Double minFee, Double maxFee) {
        return transferRecordDao.getExceptRecordCount(minFee,maxFee);
    }

//    @Override
//    public void saveRecordByIdsAndDate(List<Long> Ids, Date beginDate, Date endDate) {
//
//    }
//
//    @Override
//    public void saveRecordByDate(Date beginDate, Date endDate) {
//
//    }
}
