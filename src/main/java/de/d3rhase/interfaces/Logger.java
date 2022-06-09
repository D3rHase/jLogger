package de.d3rhase.interfaces;

import de.d3rhase.constants.LogColors;

public interface Logger {

    void ok(String module, String text, boolean printToConsole);
    void ok(String module, String text);

    void info(String module, String text, boolean printToConsole);
    void info(String module, String text);

    void debug(String module, String text, boolean printToConsole);
    void debug(String module, String text);

    void warning(String module, String text, boolean printToConsole);
    void warning(String module, String text);

    void error(String module, String text, boolean printToConsole);
    void error(String module, String text);

    void saveLog();
}
