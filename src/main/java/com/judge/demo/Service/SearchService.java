package com.judge.demo.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SearchService {

    List<Long> getBuyRecordId(String query, HttpServletRequest request);
    List<Long> getTransferRecordId(String query,HttpServletRequest request);
    List<Long> getTransferRecordId(List<String> query,HttpServletRequest request);
    List<Long> getBuyRecordId(List<String> query,HttpServletRequest request);
}
