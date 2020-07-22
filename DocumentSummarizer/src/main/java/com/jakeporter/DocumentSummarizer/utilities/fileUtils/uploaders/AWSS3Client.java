package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.jakeporter.DocumentSummarizer.exceptions.AWSException;
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

    public InputStream getS3ObjectInputStream(String fileUrl) {
        final String AWS_EXCEPTION_MESSAGE = "Something went wrong in retrieving the file.";
        S3Object s3Object = null;
        logger.info("fileUrl: " + fileUrl);
        try {
            String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileKey));
        } catch (Exception e) {
            if (s3Object != null) {
                try {
                    s3Object.close();
                } catch (IOException f) {
                    f.printStackTrace();
                    throw new AWSException(AWS_EXCEPTION_MESSAGE);
                }
            }
            e.printStackTrace();
            throw new AWSException(AWS_EXCEPTION_MESSAGE);
        }
        return s3Object.getObjectContent();
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
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
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.Private));
    }

}
