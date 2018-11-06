package com.judge.demo.Service.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Dao.DianpuDao;
import com.judge.demo.Entity.AliDianpu;
import com.judge.demo.Service.DianPuService;
import com.judge.demo.Untils.MapSort;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class DianPuServiceImpl implements DianPuService {

    @Resource
    private DianpuDao dianpuDao;

    @Override
    public JSONObject resolveData(MultipartFile file) {
        JSONObject object = new JSONObject();
        List<AliDianpu> list = new ArrayList<>();
        boolean notNull = false;
        String fileName = file.getName();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            object.put("code",0);
            object.put("msg","格式不正确");
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
            if(sheet!=null){
                notNull = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        数据导入
         */
        AliDianpu aliDianpu;
        //第一行标题不要，从第二行开始
        for (int r = 1; r <= sheet.getLastRowNum(); r++){
            aliDianpu=new AliDianpu();
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            String id = row.getCell(0).getStringCellValue();
            String buy_ni = row.getCell(1).getStringCellValue();
            String sell_ni = row.getCell(2).getStringCellValue();
            String money = row.getCell(3).getStringCellValue();
            String title = row.getCell(4).getStringCellValue();
            String num =row.getCell(5).getStringCellValue();
            String buy_name = row.getCell(6).getStringCellValue();
            String buy_phone = row.getCell(7).getStringCellValue();
            String buy_shen = row.getCell(8).getStringCellValue();
            String buy_city = row.getCell(9).getStringCellValue();
            String buy_region = row.getCell(10).getStringCellValue();
            String buy_addres = row.getCell(11).getStringCellValue();
            String c_date = row.getCell(12).getStringCellValue();
            String index = id+buy_ni+sell_ni+money+title+num+buy_name+buy_phone+buy_shen+buy_city+buy_region+buy_addres+c_date;
            /*
            尝试做格式转换
             */
            //SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date new_c_date;
            Double new_money;
            Integer new_num;
            //转换错误直接下一次
            try {
                 new_c_date = sDateFormat.parse(c_date);
                 new_money=Double.parseDouble(money);
                 new_num = Integer.parseInt(num);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            aliDianpu.setId(id);
            aliDianpu.setBuyNi(buy_ni);
            aliDianpu.setSellNi(sell_ni);
            aliDianpu.setMoney(new_money);
            aliDianpu.setTitle(title);
            aliDianpu.setNum(new_num);
            aliDianpu.setBuyName(buy_name);
            aliDianpu.setBuyPhone(buy_phone);
            aliDianpu.setBuyShen(buy_shen);
            aliDianpu.setBuyCity(buy_city);
            aliDianpu.setBuyRegion(buy_region);
            aliDianpu.setBuyAddres(buy_addres);
            aliDianpu.setcDate(new_c_date);
            aliDianpu.setIndex(index);
            list.add(aliDianpu);
        }
        object.put("code",1);
        object.put("msg","加载成功");
        object.put("effect_size","加载有效格式数据:"+list.size()+"条");
        /*
        做插入操作
         */
        //有效记录集合
        List<AliDianpu> result = new ArrayList<>();
        //重复记录数量
        int count = 0;
        for(AliDianpu dianpu:list){
            int flag=dianpuDao.addrecord(dianpu);
            if(flag==1){//没有重复记录
                result.add(dianpu);
            }else if(flag==0){
                count++;
            }
        }
        object.put("uneffect_size","重复数据共:"+count+"条");
        //记录每个人记录有多少条
        Map<String,Integer> record = new HashMap<>();
        for(AliDianpu dianpu:result){
            if(record.containsKey(dianpu.getBuyName())){
                //数量加1
                record.put(dianpu.getBuyName(), (record.get(dianpu.getBuyName())+1) );
            }else{
                record.put(dianpu.getBuyName(),1);
            }
        }
        //按照数量降序排序
        record=MapSort.sortMapByValue(record);

        StringBuffer result_msg=new StringBuffer();
        for (Map.Entry<String, Integer> entry : record.entrySet()) {
            result_msg.append("名称:"+entry.getKey()+";记录:"+entry.getValue()+"条\n");
        }
        String s_msg = result_msg.toString();
        //有效的每个人的记录条数
        object.put("detail",s_msg);
        return object;
    }
}
