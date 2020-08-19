package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.jakeporter.DocumentSummarizer.exceptions.FileUploaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;

@Component("awsUploader")
public class AWSS3Client {

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

    private static final String EXCEPTION_MSG = "Something went wrong in uploading the file.";

    @PostConstruct
    private void initializeAWSCredentials() {
        logger.info("endpointUrl: " + endpointUrl + " - bucketName: " + bucketName);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3Client.builder()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public String uploadFile(MultipartFile mpFile) throws IOException {
        String fileUrl = null;
        File file = null;
        // caller throws custom exception
        try {
            file = convertMultipartFileToFile(mpFile);
            String fileName = generateFileName(mpFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileToS3Bucket(fileName, file);
        } finally {
            file.delete();
        }
        return fileUrl;
    }

    public InputStream getS3ObjectInputStream(String fileUrl) {
        String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileKey));
        try (s3Object) {
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploaderException(EXCEPTION_MSG);
        }
        return s3Object.getObjectContent();
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
    }

    private File convertMultipartFileToFile(MultipartFile mpFile) throws IOException {
        File convertedFile = new File(mpFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(mpFile.getBytes());
        }
        return convertedFile;
    }

    private String generateFileName(MultipartFile file) {
        return new Date().getTime() + "-" + file.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.Private));
    }

}
