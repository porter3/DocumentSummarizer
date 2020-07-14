package com.jakeporter.DocumentAnalyzer.utilities.textExtractors;

import com.jakeporter.DocumentAnalyzer.exceptions.TXTIssueException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TXTTextExtractor implements FileTextExtractor {

    private static final String TXT_EXCEPTION_MESSAGE = "Something went wrong processing the text file.";

    @Override
    public String extractText(MultipartFile file) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        try {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            reader.close();
            throw new TXTIssueException(TXT_EXCEPTION_MESSAGE);
        }
        return builder.toString();
    }
}
