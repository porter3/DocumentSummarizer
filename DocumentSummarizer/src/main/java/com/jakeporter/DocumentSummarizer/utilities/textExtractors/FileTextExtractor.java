package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileTextExtractor {

    public String extractText(MultipartFile file) throws IOException;
}
