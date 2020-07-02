package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class DOCXTextExtractor implements FileTextExtractor {

    @Override
    public String extractText(MultipartFile file) throws IOException {
        XWPFWordExtractor extractor = new XWPFWordExtractor(new XWPFDocument(file.getInputStream()));
        return extractor.getText();
    }
}
