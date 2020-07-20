package com.jakeporter.DocumentSummarizer.utilities.fileUtils.removers;

import java.nio.file.Path;

public interface FileRemover {

    public void deleteFile(Path filePath);
}
