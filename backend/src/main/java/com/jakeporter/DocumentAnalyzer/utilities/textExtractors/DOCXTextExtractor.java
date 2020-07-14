package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

import com.jakeporter.DocumentAnalyzer.exceptions.DOCXIssueException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class DOCXTextExtractor implements FileTextExtractor {

    static final String DOCX_EXCEPTION_MESSAGE = "Something went wrong processing the word document.";

    @Override
    public String extractText(MultipartFile file) {
        XWPFDocument xwpfDocument;
        XWPFWordExtractor extractor;
        InputStream stream = null;
        String extractedText;
        try {
            stream = file.getInputStream();
            xwpfDocument = new XWPFDocument(stream);
            extractor = new XWPFWordExtractor(xwpfDocument);
            extractedText = extractor.getText();
            stream.close();
        } catch (Exception e) { // is generic since the two constructors above can throw a variety of exceptions
            e.printStackTrace();
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException f) {
                throw new DOCXIssueException(DOCX_EXCEPTION_MESSAGE);
            }
            throw new DOCXIssueException(DOCX_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}
