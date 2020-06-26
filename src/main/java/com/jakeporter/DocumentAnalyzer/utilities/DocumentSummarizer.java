package com.jakeporter.DocumentAnalyzer.utilities;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class DocumentSummarizer {

    private String extractText(MultipartFile file) throws IOException {
        XWPFWordExtractor extractor = new XWPFWordExtractor(new XWPFDocument(file.getInputStream()));
        return extractor.getText();
    }

    protected abstract String computeSummary(String text) throws IOException;

    public String summarizeDocument(MultipartFile file) throws IOException {
        String text = extractText(file);
        return computeSummary(text);
    }
}
