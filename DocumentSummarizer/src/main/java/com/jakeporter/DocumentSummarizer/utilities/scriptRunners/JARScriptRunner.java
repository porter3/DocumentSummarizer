package com.jakeporter.DocumentSummarizer.utilities.scriptRunners;

import com.jakeporter.DocumentSummarizer.exceptions.PythonScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;


public class JARScriptRunner {

    Logger logger = LoggerFactory.getLogger(JARScriptRunner.class);

    private final String pythonCmd;
    private final String script; // name of file in 'src/main/resources/', not the full path
    private File scriptFile;

    public JARScriptRunner(String pythonCmd, String script) {
        this.pythonCmd = pythonCmd;
        this.script = script;
    }

    public String runPythonScript(String text, String... processArgs) {
        logger.info("PROCESS ARGS: " + Arrays.toString(processArgs));
        String result = null;
        try {
            Process pythonProcess = initiateScriptProcess(processArgs);
            logger.info("JarScriptRunner LINE 31: Initiated script process, writing to StdIn.");
            logger.info("Process is alive: " + String.valueOf(pythonProcess.isAlive()));
            writeToStdIn(pythonProcess, text);
            logger.info("JarScriptRunner LINE 33: Wrote to StdIn.");
            result = getResultFromStdOut(pythonProcess);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PythonScriptException("Something went wrong getting the summary.");
        }
        finally {
            deleteTempFile();
        }
        return result;
    }

    private Process initiateScriptProcess(String... processArgs) throws IOException {
        this.scriptFile = writeScriptToTempFile();
        String[] allProcessArgs = consolidateProcessArgs(pythonCmd, scriptFile.getAbsolutePath(), processArgs);
        logger.info("PROCESS ARGS: " + Arrays.toString(allProcessArgs));
        ProcessBuilder processBuilder = new ProcessBuilder(allProcessArgs);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private String[] consolidateProcessArgs(String pythonCmd, String scriptPath, String[] otherArgs) {
        String[] processArgs = new String[otherArgs.length + 2];
        processArgs[0] = pythonCmd;
        processArgs[1] = scriptPath;
        for (int i = 0; i < otherArgs.length; i++) {
            processArgs[i + 2] = otherArgs[i];
        }
        return processArgs;
    }

    private void writeToStdIn(Process process, String text) throws IOException {
        OutputStream pythonStdIn = process.getOutputStream();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pythonStdIn))) {
            writer.write(text);
            writer.flush();
        }
    }

    private String getResultFromStdOut(Process process) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStream stdOut = process.getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stdOut))){
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }

    private void deleteTempFile() {
        this.scriptFile.delete();
    }

    private File writeScriptToTempFile() throws IOException {
        String[] filenameComponents = script.split("\\.");
        File scriptFile;
        try (InputStream scriptStream = JARScriptRunner.class.getClassLoader().getResourceAsStream(script)) {
            byte[] buffer = new byte[scriptStream.available()];
            scriptFile = File.createTempFile(filenameComponents[0], "." + filenameComponents[1]);
            scriptStream.read(buffer);
            try (OutputStream fileStream = new FileOutputStream(scriptFile)) {
                fileStream.write(buffer);
                fileStream.flush();
            }
        }
        return scriptFile;
    }

}
