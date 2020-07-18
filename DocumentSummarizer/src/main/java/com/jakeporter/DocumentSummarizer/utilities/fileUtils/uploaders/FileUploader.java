package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    public void uploadFile(MultipartFile file);
}
