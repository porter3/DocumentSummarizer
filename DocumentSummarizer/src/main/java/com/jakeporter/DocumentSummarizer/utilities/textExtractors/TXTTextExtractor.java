package com.jakeporter.DocumentSummarizer.utilities.textExtractors;

import com.jakeporter.DocumentSummarizer.exceptions.TXTIssueException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TXTTextExtractor implements FileTextExtractor {

    private static final String TXT_EXCEPTION_MESSAGE = "Something went wrong processing the text file.";

    @Override
    public String extractText(MultipartFile file) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            try{
                reader.close();
            } catch (IOException f) {
                throw new TXTIssueException(TXT_EXCEPTION_MESSAGE);
            }
            throw new TXTIssueException(TXT_EXCEPTION_MESSAGE);
        }
        return builder.toString();
    }
}
