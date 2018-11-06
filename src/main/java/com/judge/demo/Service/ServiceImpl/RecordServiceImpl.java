package com.judge.demo.Service.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Entity.AliDianpu;
import com.judge.demo.Entity.Record;
import com.judge.demo.Service.RecordService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Override
    public JSONObject resolveData(MultipartFile file) {
        JSONObject object = new JSONObject();
        List<Record> list = new ArrayList<>();
        boolean notNull = false;
        List<AliDianpu> userList = new ArrayList<AliDianpu>();
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
        Record record;
        for (int r = 1; r <= sheet.getLastRowNum(); r++){
            record = new Record();
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            String record_sender_zhifubao_account = row.getCell(0).getStringCellValue();
            String record_sender_zhifubao_account_id = row.getCell(1).getStringCellValue();
            String record_sender_phone = row.getCell(2).getStringCellValue();
            String record_sender_name = row.getCell(3).getStringCellValue();;
            String record_sender_indentity_id = row.getCell(4).getStringCellValue();
            String record_sender_last_login_city=row.getCell(5).getStringCellValue();
            String record_sender_often_login_city = row.getCell(6).getStringCellValue();
            String record_sender_taobao_account = row.getCell(7).getStringCellValue();
            String record_sender_taobao_account_id = row.getCell(8).getStringCellValue();
            String record_receiver_zhifubao_account = row.getCell(9).getStringCellValue();
            String record_receiver_zhifubao_account_id = row.getCell(10).getStringCellValue();
            String record_receiver_phone = row.getCell(11).getStringCellValue();
            String record_receiver_name = row.getCell(12).getStringCellValue();
            String record_receiver_identity_id = row.getCell(13).getStringCellValue();
            String record_receiver_last_login_city = row.getCell(14).getStringCellValue();
            String record_receiver_often_login_city = row.getCell(15).getStringCellValue();
            String record_receiver_taobao_account = row.getCell(16).getStringCellValue();
            String record_receiver_taobao_account_id = row.getCell(17).getStringCellValue();
            String record_gmt_create = row.getCell(18).getStringCellValue();
            String record_transfer_title=row.getCell(19).getStringCellValue();
            String record_suspicion_transfer_title=row.getCell(20).getStringCellValue();
            String record_transfer_fee = row.getCell(21).getStringCellValue();
            String record_feather = row.getCell(22).getStringCellValue();
            String record_collection_company = row.getCell(23).getStringCellValue();
            String record_receiver_account = row.getCell(24).getStringCellValue();
            String record_sender_gmt_last_visit = row.getCell(25).getStringCellValue();
            String reocrd_receiver_gmt_last_visit = row.getCell(26).getStringCellValue();
            String record_sender_default_address = row.getCell(27).getStringCellValue();
            String record_sender_latest_address = row.getCell(28).getStringCellValue();
            String record_sender_latest_phone = row.getCell(29).getStringCellValue();
            String record_sender_latest_receiver = row.getCell(29).getStringCellValue();
            String record_receiver_default_address = row.getCell(30).getStringCellValue();
            String record_receiver_latest_address = row.getCell(31).getStringCellValue();
            String record_receiver_latest_phone = row.getCell(32).getStringCellValue();
            String record_receiver_latest_receiver = row.getCell(33).getStringCellValue();
            String record_all =record_sender_zhifubao_account+record_sender_zhifubao_account_id+record_sender_phone+record_sender_name+record_sender_indentity_id
                    +record_sender_last_login_city+record_sender_often_login_city+record_sender_taobao_account+record_sender_taobao_account_id+record_receiver_zhifubao_account
                    +record_receiver_zhifubao_account_id+record_receiver_phone+record_receiver_name+record_receiver_identity_id+record_receiver_last_login_city+record_receiver_often_login_city
                    +record_receiver_taobao_account+record_receiver_taobao_account_id+record_gmt_create+record_transfer_title+record_suspicion_transfer_title+record_transfer_fee+record_feather
                    +record_collection_company+record_receiver_account+record_sender_gmt_last_visit+reocrd_receiver_gmt_last_visit+record_sender_default_address+record_sender_latest_address
                    +record_sender_latest_phone+record_sender_latest_receiver+record_receiver_default_address+record_receiver_latest_address+record_receiver_latest_phone+record_receiver_latest_receiver;

            record.setRecordSenderZhifubaoAccount(record_sender_zhifubao_account);
            record.setRecordSenderZhifubaoAccountId(record_sender_zhifubao_account_id);
            record.setRecordSenderPhone(record_sender_phone);
            record.setRecordSenderName(record_sender_name);
            record.setRecordSenderIdentityId(record_sender_indentity_id);
            record.setRecordSenderLastLoginCity(record_sender_last_login_city);
            record.setRecordSenderOftenLoginCity(record_sender_often_login_city);
            record.setRecordSenderTaobaoAccount(record_sender_taobao_account);
            record.setRecordSenderTaobaoAccountId(record_sender_taobao_account_id);
            record.setRecordReceiverZhifubaoAccount(record_receiver_zhifubao_account);
        }

        return null;
    }
}
