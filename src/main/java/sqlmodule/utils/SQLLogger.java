package sqlmodule.utils;

import lombok.experimental.UtilityClass;

import java.util.logging.Logger;

@UtilityClass
public class SQLLogger {

    private final static Logger logger = Logger.getLogger(SQLLogger.class.getName());

    public static void info(String message){
        logger.info("[SQL] " + message);
    }

    public static void severe(String message) {
        logger.severe("[SQL] " + message);
    }

    public static void warning(String message){
        logger.warning("[SQL] " + message);
    }

    public static void debug(String message){
        logger.info("[SQL] [DEBUG] " + message);
    }

}
