package com.jakeporter.DocumentSummarizer.utilities.fileUtils.removers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component("awsRemover")
public class AWSS3Remover implements FileRemover {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void deleteFile(Path filePath) {
        String fileUrl = filePath.toString();
        String fileName = fileUrl.toString().substring(fileUrl.lastIndexOf("/") + 1);
        logger.info("FileUrl: " + fileUrl);
        logger.info("fileName: " + fileName);

    }
}
