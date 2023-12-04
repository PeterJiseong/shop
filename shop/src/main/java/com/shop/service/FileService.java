package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile (String uploadPath, String originalFileName,
                              byte[] fileData) throws Exception{
        System.err.println("Service.FileService.uploadFile");
        //고유한 파일 이름 생성
        //uuid : 범용으로 사용되는 고유 식별자
        //128bit, 16진수(총 32자)
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString()+extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;

    }

    public void deleteFile(String filePath) throws Exception{
        System.err.println("Service.ItemImgService.deleteFile");
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
