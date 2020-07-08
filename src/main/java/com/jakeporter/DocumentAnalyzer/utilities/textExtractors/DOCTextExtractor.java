package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

import com.jakeporter.DocumentAnalyzer.exceptions.DOCIssueException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class DOCTextExtractor implements FileTextExtractor {

    static final String DOC_EXCEPTION_MESSAGE = "Something went wrong processing the word document.";

    @Override
    public String extractText(MultipartFile file) {
        HWPFDocument hwpfDocument;
        WordExtractor extractor;
        InputStream stream = null;
        String extractedText;
        try {
            stream = file.getInputStream();
            hwpfDocument = new HWPFDocument(stream);
            extractor = new WordExtractor(hwpfDocument);
            extractedText = extractor.getText();
            stream.close();
        } catch (IOException e) {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException f) {
                throw new DOCIssueException(DOC_EXCEPTION_MESSAGE);
            }
            throw new DOCIssueException(DOC_EXCEPTION_MESSAGE);
        }
        return extractedText;
    }
}
