package com.jakeporter.DocumentSummarizer.utilities.scriptRunners;

import com.jakeporter.DocumentSummarizer.exceptions.PythonScriptException;

import java.io.*;

// TODO: make this class more generic and have a subclass extend more specific functionality
public class JARScriptRunner {

    private final String pythonCmd;
    private final String script;

    private File scriptFile;

    public JARScriptRunner(String pythonCmd, String script) {
        this.pythonCmd = pythonCmd;
        this.script = script;
    }

    public String runPythonScript(String text, String summaryDelimiter) {
        String result = null;
        try {
            Process pythonProcess = initiateScriptProcess(summaryDelimiter);
            writeToStdIn(pythonProcess, text);
            result = getResultFromStdOut(pythonProcess);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTempFile();
        }
        return result;
    }

    private Process initiateScriptProcess(String summaryDelimiter) {
        this.scriptFile = writeScriptToTempFile();
        ProcessBuilder processBuilder = new ProcessBuilder(pythonCmd, scriptFile.getAbsolutePath(), summaryDelimiter);
        processBuilder.redirectErrorStream(true);
        Process pythonProcess;
        try {
            pythonProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new PythonScriptException("Something went wrong running the Python script.");
        }
        return pythonProcess;
    }

    private void writeToStdIn(Process process, String text) {
        OutputStream pythonStdIn = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pythonStdIn));
        try {
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new PythonScriptException("Something went wrong in getting the summary.");
        }
    }

    private String getResultFromStdOut(Process process) {
        final String READING_EXCEPTION_MESSAGE = "Something went wrong in getting the summary.";
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
                throw new PythonScriptException(READING_EXCEPTION_MESSAGE);
            }
            e.printStackTrace();
            throw new PythonScriptException(READING_EXCEPTION_MESSAGE);
        }
        return builder.toString();
    }

    private void deleteTempFile() {
        this.scriptFile.delete();
    }

    private File writeScriptToTempFile() {
        InputStream scriptStream = JARScriptRunner.class.getClassLoader().getResourceAsStream(script);
        File scriptFile;
        try {
            scriptFile = File.createTempFile("textSummarizer", ".py");
            byte[] buffer = new byte[scriptStream.available()];
            scriptStream.read(buffer);
            scriptStream.close();
            OutputStream fileStream = new FileOutputStream(scriptFile);
            fileStream.write(buffer);
            fileStream.flush();
            fileStream.close();
        } catch (IOException e) {
            throw new PythonScriptException("Something went wrong.");
        }
        return scriptFile;
    }

}
