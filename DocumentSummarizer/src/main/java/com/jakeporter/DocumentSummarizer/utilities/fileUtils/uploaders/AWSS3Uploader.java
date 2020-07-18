package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("AWSUploader")
public class AWSS3Uploader implements FileUploader {

    @Override
    public void uploadFile(MultipartFile file) {

    }
}
