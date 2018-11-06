package com.judge.demo.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/*
这里是文件服务接口
用于接受excel文件，存到本地
对excel文件路径进行区分，不同类型文件存不同地方
 */
public interface FileService {
    public File uploadFile(MultipartFile file);
}
