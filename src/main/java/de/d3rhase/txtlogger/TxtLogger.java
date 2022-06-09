package de.d3rhase.txtlogger;

import de.d3rhase.constants.LogColors;
import de.d3rhase.interfaces.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Objects;

public class TxtLogger implements Logger {

    private static final String DEFAULT_LOG_DIR = "logs";
    private static final String DEFAULT_LOG_NAME = "TOPIC";
    private static final boolean DEFAULT_DELETE_EXISTING_LOGS = false;

    private final LinkedList<String> log_queue;
    private boolean writing_to_log_file;
    private String path;
    private String logDir;


    public TxtLogger(String logTitle, boolean deleteExistingLogs) {
        this(logTitle, deleteExistingLogs, DEFAULT_LOG_DIR);
    }

    public TxtLogger(String logTitle) {
        this(logTitle, DEFAULT_DELETE_EXISTING_LOGS, DEFAULT_LOG_DIR);
    }

    public TxtLogger(String logTitle, boolean deleteExistingLogs, String logDir) {
        this.log_queue = new LinkedList<>();
        this.writing_to_log_file = false;
        this.logDir = logDir;

        String date = getDateTime();
        date = date.substring(0,23);

        path = (this.logDir + "/" + logTitle + "_log-" + date + ".txt");

        if (!Files.exists(Path.of(this.logDir))) {
            try {
                new File(this.logDir).mkdir();
            } catch (SecurityException ignored) {
            }
        }

        if (deleteExistingLogs) {
            File dir = new File(this.logDir);
            try {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.getName().contains(logTitle + "_log")) {
                        file.delete();
                    }
                }
            } catch (NullPointerException | SecurityException ignored) {
            }
        }

        if (Files.exists(Path.of(this.path))) {
            try {
                PrintWriter writer = new PrintWriter(this.path);
                writer.print("");
                writer.close();
            } catch (SecurityException | FileNotFoundException ignored) {
            }
        }

        try {
            File logFile = new File(this.path);
            FileWriter fileWriter = new FileWriter(this.path);
            fileWriter.flush();
            fileWriter.write("---- LOG - " + logTitle + " - Date: " + date + " ----\n\n\n");
            fileWriter.close();
        } catch (IOException ignored) {
        }

        //this.testLogger(); // Test
    }

    private void append(String entry) {
        synchronized (this.log_queue) {
            this.log_queue.add(entry);
        }
        Thread writeThread = getWriteThread();
        if (!this.writing_to_log_file) {
            writeThread.start();
        }
    }

    private Thread getWriteThread() {
        return new Thread(() -> {
            this.writing_to_log_file = true;
            try {
                synchronized (this.log_queue) {
                    FileWriter fileWriter = new FileWriter(this.path, true);
                    for (String entry : this.log_queue) {
                        fileWriter.write(entry + "\n");
                    }
                    fileWriter.close();
                    this.log_queue.clear();
                }
            } catch (IOException ignored) {
            }
            this.writing_to_log_file = false;
        });
    }

    private static String getDateTime() {
        LocalDateTime time = LocalDateTime.now();
        String rawTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String[] splitTime = rawTime.split("T");
        rawTime = (splitTime[0] + "__" + splitTime[1]);
        splitTime = rawTime.split(":");
        rawTime = (splitTime[0] + "-" + splitTime[1] + "-" + splitTime[2]);
        return rawTime;
    }

    public void ok(String module, String text, boolean printToConsole) {
        String entry = (getDateTime() + " - " + module + " - " + text);
        this.append("OK - " + entry);
        if (printToConsole) {
            System.out.println(LogColors.OKGREEN + LogColors.BOLD + "OK" + LogColors.ENDC + " - " + entry);
        }
    }

    public void ok(String module, String text) {
        this.ok(module, text, true);
    }

    public void info(String module, String text, boolean printToConsole) {
        String entry = (getDateTime() + " - " + module + " - " + text);
        this.append("INFO - " + entry);
        if (printToConsole) {
            System.out.println(LogColors.INFO + LogColors.BOLD + "INFO" + LogColors.ENDC + " - " + entry);
        }
    }

    public void info(String module, String text) {
        this.info(module, text, true);
    }

    public void debug(String module, String text, boolean printToConsole) {
        String entry = (getDateTime() + " - " + module + " - " + text);
        this.append("DEBUG - " + entry);
        if (printToConsole) {
            System.out.println(LogColors.OKCYAN + LogColors.BOLD + "DEBUG" + LogColors.ENDC + " - " + entry);
        }
    }

    public void debug(String module, String text) {
        this.debug(module, text, true);
    }

    public void warning(String module, String text, boolean printToConsole) {
        String entry = (getDateTime() + " - " + module + " - " + text);
        this.append("WARNING - " + entry);
        if (printToConsole) {
            System.out.println(LogColors.WARNING + LogColors.BOLD + "WARNING" + LogColors.ENDC + " - " + entry);
        }
    }

    public void warning(String module, String text) {
        this.warning(module, text, true);
    }

    public void error(String module, String text, boolean printToConsole) {
        String entry = (getDateTime() + " - " + module + " - " + text);
        this.append("ERROR - " + entry);
        if (printToConsole) {
            System.out.println(LogColors.FAIL + LogColors.BOLD + "ERROR" + LogColors.ENDC + LogColors.FAIL + " - " + entry);
        }
    }

    public void error(String module, String text) {
        this.error(module, text, true);
    }

    @Override
    public void saveLog() {

    }

    private void testLogger() {
        this.ok("TEST", "Text");
        this.info("TEST", "Text");
        this.debug("TEST", "Text");
        this.warning("TEST", "Text");
        this.error("TEST", "Text");
    }

}
