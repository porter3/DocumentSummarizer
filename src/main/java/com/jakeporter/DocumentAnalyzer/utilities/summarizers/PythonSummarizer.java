package com.jakeporter.DocumentAnalyzer.utilities.summarizers;

import com.jakeporter.DocumentAnalyzer.utilities.textExtractors.FileTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Arrays;


public class PythonSummarizer extends DocumentSummarizer {

    private final String SCRIPT = "textSummarizer.py";

    public PythonSummarizer(){ super(); }

    public PythonSummarizer(FileTextExtractor extractor) {
        super(extractor);
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected String computeSummary(String[] textChunks) throws IOException {
        return runPythonScript(textChunks);
    }

    private String runPythonScript(String[] textChunks) throws IOException {
        // filePath starts with "/C:/..." - the first forward slash needs to go for it to work
        String filePath = this.getClass().getClassLoader().getResource(SCRIPT).getPath().substring(1);
        logger.info("Script path: " + filePath);
        String[] processArgs = makeProcessArgs(textChunks, "python", filePath);
        ProcessBuilder processBuilder = new ProcessBuilder(processArgs);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return readResult(process);
    }

    private String[] makeProcessArgs(String[] textChunks, String... argStrings) {
        String[] processArgs = new String[argStrings.length + textChunks.length];
        // argument strings that aren't in an array will be first
        int argsIndex = 0;
        for (int i = 0; i < argStrings.length; i++) {
            processArgs[argsIndex] = argStrings[i];
            argsIndex++;
        }
        for (int i = 0; i < textChunks.length; i++) {
            processArgs[argsIndex] = textChunks[i];
            argsIndex++;
        }
        return processArgs;
    }

}
