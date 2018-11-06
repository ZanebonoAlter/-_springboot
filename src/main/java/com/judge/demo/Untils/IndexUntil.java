package com.judge.demo.Untils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexUntil {
    //这个是写进索引的实例
    private IndexWriter writer;

    public IndexWriter getWriter() {
        return writer;
    }

    public IndexUntil(String indexDir) throws IOException {
        //获取索引所在目录
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        //中文分词器
        Analyzer analyzer = new SmartChineseAnalyzer();
        //配置中写明使用的分词器
        IndexWriterConfig config=new IndexWriterConfig(analyzer);
        //将配置以及路径写进实例中
        writer = new IndexWriter(directory, config);
    }

    //关闭写索引
    public void close() throws IOException {
        writer.close();
    }
}
