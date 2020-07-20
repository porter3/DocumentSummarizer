package com.jakeporter.DocumentSummarizer.utilities.fileUtils.removers;

import com.jakeporter.DocumentSummarizer.exceptions.FileDeletionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component("local")
public class LocalRemover implements FileRemover {

    @Override
    public void deleteFile(Path filePath) {
        try{
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileDeletionException("Error deleting file from " + filePath.toString() + ".");
        }
    }
}
