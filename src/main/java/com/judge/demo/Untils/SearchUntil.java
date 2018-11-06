package com.judge.demo.Untils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchUntil {
    /**
     * 查询索引专用，返回List<Long>
     * @param indexDir 索引所在目录
     * @param q 查询关键字
     * @param search_filed 查询域
     * @param search_index 查询结果域(比如说id)
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InvalidTokenOffsetsException
     */
    public List<Long> search(String indexDir, String q,String search_filed,String search_index) throws IOException, ParseException, InvalidTokenOffsetsException {
        List<Long> result = new ArrayList<>();
        //获取路径
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        //读取所有索引
        IndexReader reader = DirectoryReader.open(directory);
        //建立索引查询器
        IndexSearcher searcher = new IndexSearcher(reader);
        //中文分词器
        Analyzer analyzer=new SmartChineseAnalyzer();
        //建立查询解析器
        /**
         * 第一个参数是要查询的字段；
         * 第二个参数是分析器Analyzer
         * */
        QueryParser parser = new QueryParser(search_filed,analyzer);
        //转换成查询
        Query query = parser.parse(q);
        //Term t = new Term("content", q);
        //Query query2 = new TermQuery(t);

        //开始查询
        /**
         * 第一个参数是通过传过来的参数来查找得到的query;
         * 第二个参数是要出查询的行数
         */
        TopDocs topDocs = searcher.search(query, 500000);
        //TopDocs topDocs2 = searcher.search(query2, 10);

        //遍历topDocs
        /**
         * ScoreDoc:是代表一个结果的相关度得分与文档编号等信息的对象。
         * scoreDocs:代表文件的数组
         * @throws Exception
         * */
        for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
            //获取文档
            Document document = searcher.doc(scoreDoc.doc);
            if(document.get(search_filed).indexOf(q)==-1)
                continue;
            result.add(Long.parseLong(document.get(search_index)));
        }
        reader.close();
        return result;
    }

    public List<Long> searchList(String indexDir, List<String> q,String search_filed,String search_index) throws IOException, ParseException, InvalidTokenOffsetsException {
        List<Long> result = new ArrayList<>();
        //获取路径
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        //读取所有索引
        IndexReader reader = DirectoryReader.open(directory);
        //建立索引查询器
        IndexSearcher searcher = new IndexSearcher(reader);
        //中文分词器
        Analyzer analyzer=new SmartChineseAnalyzer();
        //建立查询解析器
        /**
         * 第一个参数是要查询的字段；
         * 第二个参数是分析器Analyzer
         * */
        QueryParser parser = new QueryParser(search_filed,analyzer);
        String query_s="";
        for (String s:
             q) {
            query_s+=s;
        }

        //转换成查询
        Query query = parser.parse(query_s);
        //Term t = new Term("content", q);
        //Query query2 = new TermQuery(t);

        //开始查询
        /**
         * 第一个参数是通过传过来的参数来查找得到的query;
         * 第二个参数是要出查询的行数
         */
        TopDocs topDocs = searcher.search(query, 500000);
        //TopDocs topDocs2 = searcher.search(query2, 10);

        //遍历topDocs
        /**
         * ScoreDoc:是代表一个结果的相关度得分与文档编号等信息的对象。
         * scoreDocs:代表文件的数组
         * @throws Exception
         * */
        for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
            //获取文档
            Document document = searcher.doc(scoreDoc.doc);
            //没有在字段中完整出现的话，去做下一个（因为相关度的关系，可能三个字的名词有两个匹配比两个字名词全匹配到匹配程度一样）
            int flag=0;
            for (String s:
                 q) {
                if(document.get(search_filed).indexOf(s)!=-1){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                continue;
            }
            result.add(Long.parseLong(document.get(search_index)));
        }
        reader.close();
        return result;
    }
}
