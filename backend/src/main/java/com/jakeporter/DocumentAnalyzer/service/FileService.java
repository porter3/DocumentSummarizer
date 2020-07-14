package com.jakeporter.DocumentAnalyzer.service;

import com.jakeporter.DocumentAnalyzer.exceptions.FileDeletionException;
import com.jakeporter.DocumentAnalyzer.exceptions.FileStorageException;
import com.jakeporter.DocumentAnalyzer.exceptions.UnsupportedFileFormatException;
import com.jakeporter.DocumentAnalyzer.utilities.summarizers.DocumentSummarizer;
import com.jakeporter.DocumentAnalyzer.utilities.summarizers.PythonSummarizer;
import com.jakeporter.DocumentAnalyzer.utilities.textExtractors.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // value can be set by upload.dir in application.properties - defaults to user.home if not defined
    @Value("${upload.dir:${user.home}}")
    public String uploadDirectory;

    public FileType uploadFile(MultipartFile file) throws FileStorageException {
        FileType fileType;
        try {
            fileType = getFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
            logger.info("File type: " + fileType);
            Path copyLocation = getFileLocation(file);
            logger.info("Upload directory: " + uploadDirectory);
            // copy the file's input stream to the path and replace any file with the same name
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Error storing " + file.getOriginalFilename() + ".");
        } catch (UnsupportedFileFormatException e) {
            throw new UnsupportedFileFormatException("File format not supported.");
        }
        return fileType;
    }

    public String summarize(MultipartFile file, FileType fileType) {
        String summary = "";
        try {
            FileTextExtractor extractor = FileTextExtractorFactory.getExtractor(fileType);
            DocumentSummarizer summarizer = new PythonSummarizer(extractor);
            summary = summarizer.summarizeDocument(file);
            logger.info("Summary: " + summary);
        } catch (Throwable e) {
        } finally {
            deleteFile(getFileLocation(file));
        }
        return summary;
    }

    public String summarize(String text) throws IOException {
        DocumentSummarizer summarizer = new PythonSummarizer();
        return summarizer.summarizeDocument(text);
    }

    // create absolute path of the file and normalize it for different OSs
    private Path getFileLocation(MultipartFile file) {
        return Paths.get(uploadDirectory + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
    }

    private FileType getFileType(String fileExtension) {
        switch (fileExtension) {
            case "doc":
                return FileType.DOC;
            case "docx":
                return FileType.DOCX;
            case "pdf":
                return FileType.PDF;
            case "txt":
                return FileType.TXT;
            default:
                throw new UnsupportedFileFormatException();
        }
    }

    private void deleteFile(Path filePath) throws FileDeletionException {
        try{
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileDeletionException("Error deleting file from " + filePath.toString() + ".");
        }
    }
}
