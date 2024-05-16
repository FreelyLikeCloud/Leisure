package com.gjj.springbootdemo.controller;

import cn.hutool.core.io.FileUtil;
import com.gjj.springbootdemo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
@ResponseBody
@RequestMapping("/file")
public class FileController {
    @Value("${ip:localhost}")
    String ip;
    @Value("${server.port}")
    String port;
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";

    @PostMapping ("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extName = FileUtil.extName(originalFilename);//文件名后缀 png
        File parentFile = new File(ROOT_PATH);//文件存储目录
        if(!parentFile.exists()){
            parentFile.mkdirs();// 如果当前文件的父级目录不存在，就创建
        }
        if(FileUtil.exist(ROOT_PATH + File.separator + originalFilename)){
            originalFilename = System.currentTimeMillis() + "_" + mainName
                    +"." + extName;
        }
        File saveFile = new File(ROOT_PATH + File.separator
                + originalFilename);
        file.transferTo(saveFile);//存储文件到本地的磁盘上面
        String  url = "http://" + ip + ":" + port
                + "/file/download/" + originalFilename;
        return Result.success(url);
    }
    @RequestMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String filePath = ROOT_PATH + File.separator + fileName;
        if(!FileUtil.exist(filePath)) {
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
