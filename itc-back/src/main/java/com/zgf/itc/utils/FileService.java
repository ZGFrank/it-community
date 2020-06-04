package com.zgf.itc.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zgf.itc.config.UploadProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZGF
 **/
@Component
@EnableConfigurationProperties(UploadProperties.class)
public class FileService {
    private Log log = LogFactory.getLog(FileService.class);

    @Resource
    private FastFileStorageClient storageClient;

    @Resource
    private UploadProperties prop;

    public ResponseResult upload(MultipartFile file) {
        // 1、校验文件类型
        String contentType = file.getContentType();
        /*if (!prop.getAllowTypes().contains(contentType)) {
            throw new RuntimeException("文件类型不支持");
        }*/
        // 2、校验文件内容
        try {
            assert contentType != null;
            if (contentType.contains("image")) {
                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
                    return ResponseResult.e500("上传文件有问题");
                }
            }
        } catch (IOException e) {
            log.error("校验文件内容失败....{}", e);
            return ResponseResult.e500("校验文件内容失败");
        }

        try {
            // 3、上传到FastDFS
            // 3.1、获取扩展名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 3.2、上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            System.out.println(storePath.getGroup());
            System.out.println(storePath.getPath());
            // 返回路径
            Map<String, String> map = new HashMap<>(2);
            map.put("fileName", file.getOriginalFilename());
            map.put("fileUrl", storePath.getFullPath());
            return ResponseResult.ok(map);
        } catch (IOException e) {
            log.error("【文件上传】上传文件失败！....{}", e);
            return ResponseResult.e500("上传文件失败");
        }
    }

    public boolean delete(String fileUrl) {
        if (prop.getDefaultAvatar().equals(fileUrl)) {
            return true;
        }
        try {
            String group = fileUrl.substring(0, fileUrl.indexOf("/"));
            String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
            storageClient.deleteFile(group, path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件URL
     * @return 文件字节
     * @throws IOException
     */
    public ResponseEntity<Object> download(HttpServletResponse response, String fileUrl, String oldName) throws IOException {

        try {
            try {
                //如果名字有中文 要处理编码
                oldName = URLEncoder.encode(oldName, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String group = fileUrl.substring(0, fileUrl.indexOf("/"));
            String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
            DownloadByteArray downloadByteArray = new DownloadByteArray();
            byte[] bytes = storageClient.downloadFile(group, path, downloadByteArray);
            HttpHeaders header = new HttpHeaders();
            //封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //设置下载的文件的名称
            header.setContentDispositionFormData("attachment", oldName);
            //创建ResponseEntity对象
            ResponseEntity<Object> entity =
                    new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
