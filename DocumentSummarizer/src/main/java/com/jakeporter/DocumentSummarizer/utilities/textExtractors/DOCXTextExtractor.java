package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.TextExtractorException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

public class DOCXTextExtractor implements FileTextExtractor {

    private static final String DOCX_EXCEPTION_MESSAGE = "Something went wrong processing the word document.";

    @Override
    public String extractTextFromStream(InputStream stream) {
        String extractedText;
        try (stream) {
            XWPFDocument xwpfDocument = new XWPFDocument(stream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xwpfDocument);
            extractedText = extractor.getText();
        } catch (Exception e) { // is generic since the two constructors above can throw a variety of exceptions
            throw new TextExtractorException(DOCX_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}