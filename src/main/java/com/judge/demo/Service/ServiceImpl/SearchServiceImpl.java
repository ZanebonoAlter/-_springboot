package com.judge.demo.Service.ServiceImpl;

import com.judge.demo.Service.SearchService;
import com.judge.demo.Untils.SearchUntil;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class SearchServiceImpl implements SearchService {
    @Value("${lucene-index}")
    private String path;
    /**
     * 可以改善，返回实体
     * @param query
     * @param request
     * @return
     */
    @Override
    public List<Long> getBuyRecordId(String query, HttpServletRequest request) {
        //String path = request.getSession().getServletContext().getRealPath("/");

        List<Long> result=new ArrayList<>();
        try {
            result = (new SearchUntil().search(path,query,"buy_record_index","buy_record_id"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Long> getTransferRecordId(String query, HttpServletRequest request) {
        List<Long> result=new ArrayList<>();
        try {
            result = (new SearchUntil().search(path,query,"transfer_record_index","transfer_record_id"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Long> getTransferRecordId(List<String> query, HttpServletRequest request) {
        List<Long> result=new ArrayList<>();
        try {
            result = (new SearchUntil().searchList(path,query,"transfer_record_index","transfer_record_id"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Long> getBuyRecordId(List<String> query, HttpServletRequest request) {
        List<Long> result=new ArrayList<>();
        try {
            result = (new SearchUntil().searchList(path,query,"buy_record_index","buy_record_id"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return result;
    }
}
