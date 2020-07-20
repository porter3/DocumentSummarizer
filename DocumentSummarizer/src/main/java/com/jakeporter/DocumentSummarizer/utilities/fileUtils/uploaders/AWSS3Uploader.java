package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jakeporter.DocumentSummarizer.exceptions.FileUploaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Component("AWSUploader")
public class AWSS3Uploader implements FileUploader {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private AmazonS3 s3Client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    private static final String FILE_UPLOADER_EXCEPTION_MSG = "Something went wrong in uploading the file.";

    @PostConstruct
    private void initializeAWSCredentials() {
        logger.info("endpointUrl: " + endpointUrl + " - bucketName: " + bucketName);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3Client.builder()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Override
    public String uploadFile(MultipartFile mpFile) {
        String fileUrl = null;
        File file = null;
        try {
            file = convertMultipartFileToFile(mpFile);
            String fileName = generateFileName(mpFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileToS3Bucket(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploaderException(FILE_UPLOADER_EXCEPTION_MSG);
        } finally {
            file.delete();
        }
        return fileUrl;
    }

    private File convertMultipartFileToFile(MultipartFile mpFile) {
        File convertedFile = new File(mpFile.getOriginalFilename());
        try {
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(mpFile.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploaderException(FILE_UPLOADER_EXCEPTION_MSG);
        }
        return convertedFile;
    }

    private String generateFileName(MultipartFile file) {
        return new Date().getTime() + "-" + file.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        // TODO: try changing CannedAccessControlList.PublicRead to ....PrivateRead after verifying it works
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
