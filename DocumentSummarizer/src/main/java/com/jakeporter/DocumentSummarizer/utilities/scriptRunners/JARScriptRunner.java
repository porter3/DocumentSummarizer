package com.jakeporter.DocumentSummarizer.utilities.scriptRunners;

import com.jakeporter.DocumentSummarizer.exceptions.PythonScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class JARScriptRunner {

    private final String pythonCmd;
    private final String script; // name of file in 'src/main/resources/', not the full path
    private File scriptFile;
    private static final String EXCEPTION_MSG = "Something went wrong in getting the summary.";

    public JARScriptRunner(String pythonCmd, String script) {
        this.pythonCmd = pythonCmd;
        this.script = script;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public String runPythonScript(String text, String... processArgs) {
        String result = null;
        try {
            Process pythonProcess = initiateScriptProcess(processArgs);
            writeToStdIn(pythonProcess, text);
            result = getResultFromStdOut(pythonProcess);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PythonScriptException(EXCEPTION_MSG);
        } finally {
            deleteTempFile();
        }
        return result;
    }

    private Process initiateScriptProcess(String... processArgs) {
        this.scriptFile = writeScriptToTempFile();
        String[] allProcessArgs = consolidateProcessArgs(pythonCmd, scriptFile.getAbsolutePath(), processArgs);
        ProcessBuilder processBuilder = new ProcessBuilder(allProcessArgs);
        processBuilder.redirectErrorStream(true);
        Process pythonProcess;
        try {
            pythonProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new PythonScriptException(EXCEPTION_MSG);
        }
        return pythonProcess;
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

    private void writeToStdIn(Process process, String text) {
        OutputStream pythonStdIn = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pythonStdIn));
        try {
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            try {
                writer.close();
            } catch (IOException f) {
                f.printStackTrace();
                throw new PythonScriptException(EXCEPTION_MSG);
            }
            e.printStackTrace();
            throw new PythonScriptException(EXCEPTION_MSG);
        }
    }

    private String getResultFromStdOut(Process process) {
        StringBuilder builder = new StringBuilder();
        InputStream stdOut = process.getInputStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stdOut));
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException f) {
                f.printStackTrace();
                throw new PythonScriptException(EXCEPTION_MSG);
            }
            e.printStackTrace();
            throw new PythonScriptException(EXCEPTION_MSG);
        }
        return builder.toString();
    }

    private void deleteTempFile() {
        this.scriptFile.delete();
    }

    private File writeScriptToTempFile() {
        InputStream scriptStream = JARScriptRunner.class.getClassLoader().getResourceAsStream(script);
        String[] filenameComponents = script.split("\\.");
        File scriptFile;
        OutputStream fileStream = null;
        try {
            scriptFile = File.createTempFile(filenameComponents[0], filenameComponents[1]);
            byte[] buffer = new byte[scriptStream.available()];
            scriptStream.read(buffer);
            scriptStream.close();
            fileStream = new FileOutputStream(scriptFile);
            fileStream.write(buffer);
            fileStream.flush();
            fileStream.close();
        } catch (IOException e) {
            try {
                fileStream.close();
            } catch (IOException f) {
                f.printStackTrace();
                throw new PythonScriptException(EXCEPTION_MSG);
            }
            e.printStackTrace();
            throw new PythonScriptException(EXCEPTION_MSG);
        }
        return scriptFile;
    }

}
