package com.jobstore.jobstore.utill;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AwsUtill {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷


    // S3 파일 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {

        // MultipartFile -> File
        File file = new File(multipartFile.getOriginalFilename());
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // S3에 저장할 파일명
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getName();
        // S3에 파일 업로드
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        System.out.println("url" + uploadImageUrl);

        return uploadImageUrl;
    }

    /* 2. 파일 삭제 */
    public void delete (String url) {
        try {
            // deleteObject(버킷명, 키값)으로 객체 삭제
            amazonS3Client.deleteObject( new DeleteObjectRequest(bucket, url));
        } catch (AmazonServiceException e) {

        }
    }
}
