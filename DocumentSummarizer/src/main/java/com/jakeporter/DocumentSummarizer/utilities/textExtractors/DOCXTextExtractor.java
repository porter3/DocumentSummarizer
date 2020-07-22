package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.DOCXIssueException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DOCXTextExtractor implements FileTextExtractor {

    static final String DOCX_EXCEPTION_MESSAGE = "Something went wrong processing the word document.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        XWPFDocument xwpfDocument;
        XWPFWordExtractor extractor;
        String extractedText;
        try {
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
