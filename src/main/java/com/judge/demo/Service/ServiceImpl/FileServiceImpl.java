package com.judge.demo.Service.ServiceImpl;

import com.judge.demo.Service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    public File uploadFile(MultipartFile file){
        if(file.isEmpty()){
            return null;
        }
        String name = file.getOriginalFilename();
        int size = (int)file.getSize();
        System.out.println(name + "-->" + size);

        String path = "D:/temp";
        File dest = new File(path+"/"+name);
        //父目录判断
        if(!dest.getParentFile().exists())
            dest.getParentFile().mkdir();
        try {
            file.transferTo(dest);
            return dest;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
