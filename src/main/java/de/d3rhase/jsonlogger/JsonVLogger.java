package de.d3rhase.jsonlogger;

import de.d3rhase.constants.LogColors;
import de.d3rhase.constants.LogTypes;
import de.d3rhase.interfaces.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;

public class JsonVLogger extends Logger {

    private final JSONArray logQueue;
    private boolean writingToLogFile;
    private String path;
    private String logDir;
    private String logTitle;
    private String logCategory;


    public JsonVLogger(String logTitle, boolean deleteExistingLogs) {
        this(logTitle, deleteExistingLogs, DEFAULT_LOG_DIR);
    }

    public JsonVLogger(String logTitle) {
        this(logTitle, DEFAULT_DELETE_EXISTING_LOGS, DEFAULT_LOG_DIR);
    }

    public JsonVLogger(String logTitle, boolean deleteExistingLogs, String logDir) {
        this.logQueue = new JSONArray();
        this.writingToLogFile = false;
        this.logDir = validateDir(logDir);
        this.logTitle = logTitle;
        this.logCategory = "_log-";

        String date = getDate();

        this.path = (this.logDir + logTitle + this.logCategory + date + ".json");

        createLogDirectory(this.logDir);

        deleteExistingLogs(this.logDir, this.logTitle, this.logCategory, deleteExistingLogs);

        this.append(createJsonTitleEntry(logTitle, "JsonLOG", date));

        //this.testLogger(); // Test
    }

    private void append(JSONObject entry) {
        synchronized (this.logQueue) {
            this.logQueue.put(entry);
        }
    }

    private Thread getWriteThread() {
        return new Thread(() -> {
            if (!this.writingToLogFile) {
                this.writingToLogFile = true;
                try {
                    synchronized (this.logQueue) {
                        PrintWriter fileWriter = new PrintWriter(this.path);
                        fileWriter.write(this.logQueue.toString(4));
                        fileWriter.close();
                    }
                } catch (IOException ignored) {
                }
                this.writingToLogFile = false;
            }
        });
    }

    public void ok(String module, String text, boolean printToConsole) {
        LocalDateTime dateTime = getDateTime();
        this.append(createJsonEntry(module, text, LogTypes.OK, dateTime));
        printConsoleEntry(createConsoleEntry(module, text, LogTypes.OK, dateTime), printToConsole);
    }

    public void ok(String module, String text) {
        this.ok(module, text, true);
    }

    public void info(String module, String text, boolean printToConsole) {
        LocalDateTime dateTime = getDateTime();
        this.append(createJsonEntry(module, text, LogTypes.INFO, dateTime));
        printConsoleEntry(createConsoleEntry(module, text, LogTypes.INFO, dateTime), printToConsole);
    }

    public void info(String module, String text) {
        this.info(module, text, true);
    }

    public void debug(String module, String text, boolean printToConsole) {
        LocalDateTime dateTime = getDateTime();
        this.append(createJsonEntry(module, text, LogTypes.DEBUG, dateTime));
        printConsoleEntry(createConsoleEntry(module, text, LogTypes.DEBUG, dateTime), printToConsole);
    }

    public void debug(String module, String text) {
        this.debug(module, text, true);
    }

    public void warning(String module, String text, boolean printToConsole) {
        LocalDateTime dateTime = getDateTime();
        this.append(createJsonEntry(module, text, LogTypes.WARNING, dateTime));
        printConsoleEntry(createConsoleEntry(module, text, LogTypes.WARNING, dateTime), printToConsole);
    }

    public void warning(String module, String text) {
        this.warning(module, text, true);
    }

    public void error(String module, String text, boolean printToConsole) {
        LocalDateTime dateTime = getDateTime();
        this.append(createJsonEntry(module, text, LogTypes.ERROR, dateTime));
        printConsoleEntry(createConsoleEntry(module, text, LogTypes.ERROR, dateTime), printToConsole);
    }

    public void error(String module, String text) {
        this.error(module, text, true);
    }

    public void saveLog(){
        Thread writeThread = getWriteThread();
        writeThread.start();

    }

    private void testLogger() {
        this.ok("TEST", "Text");
        this.info("TEST", "Text");
        this.debug("TEST", "Text");
        this.warning("TEST", "Text");
        this.error("TEST", "Text");
    }
}
