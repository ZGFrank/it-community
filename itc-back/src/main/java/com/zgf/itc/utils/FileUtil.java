package com.zgf.itc.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZGF
 */
public class FileUtil {

    public static ResponseResult upload(MultipartFile file,String judge,String type,Integer account){
        if (file.isEmpty()) {
            return ResponseResult.error("文件为空");
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();

        String path = "G:/it-community/upload/";
        Calendar instance = Calendar.getInstance();
        String purl = judge + "/" + account + "/" + type + "/" + instance.get(Calendar.YEAR) + "_" + instance.get(Calendar.MONTH) + System.currentTimeMillis() + "_" + fileName;
        path = path + purl;
        File dest = new File(path);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest); //保存文件
            purl = "/upload/" + purl;
            Map<String, String> data = new HashMap<>(2);
            data.put("src", purl);
            data.put("title", fileName);
            return ResponseResult.ok("上传成功", data);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return ResponseResult.error("保存失败");
        }
    }

    public static void deleteFileUsePath(String path) {
        String realPath="G:/it-community"+path;
        //根据文件
        File file=new File(realPath);
        if(file.exists()) {
            file.delete();
        }
    }

}
