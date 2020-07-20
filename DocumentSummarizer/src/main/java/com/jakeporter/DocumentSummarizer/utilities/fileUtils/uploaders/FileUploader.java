package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    public String uploadFile(MultipartFile file);
}
