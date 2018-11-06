package com.judge.demo.Untils;

import org.apache.poi.ss.usermodel.Row;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RowUntil {

    public String RowString(Row row,int index){
        DecimalFormat df=new DecimalFormat("#");
        if(row.getCell((index))==null)
            return null;
        switch (row.getCell(index).getCellTypeEnum()){
            case STRING:
                return row.getCell(index).getStringCellValue();
            case NUMERIC:
                return df.format(row.getCell(index).getNumericCellValue());
            default:
                return row.getCell(index).toString();
        }
    }

    public Date RowDate(Row row,int index){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if(row.getCell((index))==null)
            return null;
        switch (row.getCell(index).getCellTypeEnum()){
            case NUMERIC:
                return row.getCell(index).getDateCellValue();
            case STRING:
                try {
                    return sdf.parse(row.getCell(index).getStringCellValue());
                } catch (ParseException e) {
//                    System.out.println("不是-的日期格式");
                    try {
                        return sdf2.parse(row.getCell(index).getStringCellValue());
                    } catch (ParseException e1) {
//                        System.out.println("错误的日期格式");
                        return null;
                    }
                }
            default:
                return null;
        }
    }

    public int RowInt(Row row,int index){
        if(row.getCell((index))==null)
            return 0;
        switch (row.getCell(index).getCellTypeEnum()){
            case NUMERIC:
                return (int)row.getCell(index).getNumericCellValue();
            default:
                return 0;
        }
    }

    public double RowDouble(Row row,int index){
        if(row.getCell((index))==null)
            return 0.0;
        switch (row.getCell(index).getCellTypeEnum()){
            case NUMERIC:
                return row.getCell(index).getNumericCellValue();
            case STRING:
                return Double.parseDouble(row.getCell(index).getStringCellValue());
            default:
                return 0.0;
        }
    }

}
