package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class DOCTextExtractor implements FileTextExtractor{
    @Override
    public String extractText(MultipartFile file) throws IOException {
        HWPFDocument doc = new HWPFDocument(file.getInputStream());
        WordExtractor extractor = new WordExtractor(doc);
        return extractor.getText();
    }
}
