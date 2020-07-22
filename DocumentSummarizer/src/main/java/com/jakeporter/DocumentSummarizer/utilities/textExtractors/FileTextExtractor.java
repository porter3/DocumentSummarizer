package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileTextExtractor {

    public String extractTextFromStream(InputStream inputStream);
}
