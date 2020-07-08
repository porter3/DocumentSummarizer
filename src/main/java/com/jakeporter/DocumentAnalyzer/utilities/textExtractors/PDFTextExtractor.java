package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

import com.jakeporter.DocumentAnalyzer.exceptions.PDFIssueException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PDFTextExtractor implements FileTextExtractor {

    @Override
    public String extractText(MultipartFile file) throws IOException {
        PDFParser parser;
        COSDocument cosDocument = null;
        PDDocument pdDocument = null;
        String extractedText = null;
        try {
            parser = new PDFParser(new RandomAccessBufferedFileInputStream(file.getInputStream()));
            // parses the stream and populates the nested COSDocument instance, closes stream when done
            parser.parse();
            cosDocument = parser.getDocument();
            pdDocument = new PDDocument(cosDocument);
            extractedText = new PDFTextStripper().getText(pdDocument);
            cosDocument.close();
            pdDocument.close();
        } catch (IOException e) {
            try {
                if (cosDocument != null) {
                    cosDocument.close();
                }
                if (pdDocument != null) {
                    pdDocument.close();
                }
            } catch (IOException f) {
                throw new PDFIssueException("Something went wrong processing the PDF file.");
            }
        }
        return extractedText;
    }
}
