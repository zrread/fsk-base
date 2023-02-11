package com.fsk.framework.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import com.fsk.framework.bean.log.LogbackInfo;
import com.fsk.framework.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class LogUtils {

    private static LoggerContext getLoggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    private static Logger getRootLogger() {
        LoggerContext loggerContext = getLoggerContext();
        return loggerContext.getLogger("ROOT");
    }

    private static Logger getFskLogger() {
        LoggerContext loggerContext = getLoggerContext();
        return loggerContext.getLogger("com.fsk");
    }

    private static Appender getConsoleAppender() {
        return getRootLogger().getAppender("STDOUT");
    }

    public static RollingFileAppender getFileAppender() {
        Appender appender = getRootLogger().getAppender("FILE");

        if (appender == null) {
            String errorInfo = "File Appender is Null!";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }

        if (!(appender instanceof RollingFileAppender)) {
            String errorInfo = "Log Appender is not RollingFileAppender! Cannot update logback parameters!";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }

        return (RollingFileAppender) appender;
    }

    private static TimeBasedRollingPolicy getTriggeringPolicy() {
        TriggeringPolicy triggeringPolicy = getFileAppender().getTriggeringPolicy();

        if (!(triggeringPolicy instanceof TimeBasedRollingPolicy)) {
            String errorInfo = "Log TriggeringPolicy is not TimeBasedRollingPolicy! Cannot update logback parameters!";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }

        return (TimeBasedRollingPolicy) triggeringPolicy;

    }

    private static SizeAndTimeBasedFNATP getTimeBasedFileNamingAndTriggeringPolicy() {
        TimeBasedFileNamingAndTriggeringPolicy timeBasedFileNamingAndTriggeringPolicy = getTriggeringPolicy().getTimeBasedFileNamingAndTriggeringPolicy();

        if (!(timeBasedFileNamingAndTriggeringPolicy instanceof SizeAndTimeBasedFNATP)) {
            String errorInfo = "Log TimeBasedFileNamingAndTriggeringPolicy is not SizeAndTimeBasedFNATP! Cannot update logback parameters!";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }

        return (SizeAndTimeBasedFNATP) timeBasedFileNamingAndTriggeringPolicy;
    }

    public static void updateMaxHistory(Integer maxHistory) {
        if (maxHistory == null) {
            return;
        }

        TimeBasedRollingPolicy timeBasedRollingPolicy = getTriggeringPolicy();
        timeBasedRollingPolicy.setMaxHistory(maxHistory);
        timeBasedRollingPolicy.start();
    }

    public static void updateMaxFileSize(String maxFileSize) {
        if (StringUtils.isBlank(maxFileSize)) {
            return;
        }

        SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = getTimeBasedFileNamingAndTriggeringPolicy();
        sizeAndTimeBasedFNATP.setMaxFileSize(FileSize.valueOf(maxFileSize));
        sizeAndTimeBasedFNATP.start();
    }

    public static void updateRootLogLevel(String logLevel) {
        if (StringUtils.isBlank(logLevel)) {
            return;
        }

        getRootLogger().setLevel(Level.valueOf(logLevel));
    }

    public static void updateFskLogLevel(String logLevel) {
        if (StringUtils.isBlank(logLevel)) {
            return;
        }

        getFskLogger().setLevel(Level.valueOf(logLevel));
    }

    public static void updateLogFilepath(String filepath) {
        if (filepath == null) {
            return;
        }

        if (StringUtils.isBlank(filepath)) {
            filepath = "./";
        }

        if (!filepath.endsWith("/")) {
            filepath = filepath + "/";
        }

        if (!Files.exists(Paths.get(filepath))) {
            try {
                Files.createDirectory(Paths.get(filepath));
            } catch (IOException e) {
                throw new BizException("创建目录" + filepath + "失败，请确认路径是否正确，且具备权限");
            }
        }

        RollingFileAppender appender = getFileAppender();
        TimeBasedRollingPolicy rollingPolicy = getTriggeringPolicy();
        appender.setFile(filepath + Paths.get(appender.getFile()).getFileName().toString());

        rollingPolicy.setFileNamePattern(filepath + Paths.get(rollingPolicy.getFileNamePattern()).getFileName().toString());

        appender.stop();
        appender.getRollingPolicy().start();
        appender.getTriggeringPolicy().start();
        appender.start();
    }

    public static void startLogConsole() {
        Appender appender = getConsoleAppender();
        appender.start();
    }

    public static void stopLogConsole() {
        Appender appender = getConsoleAppender();
        appender.stop();
    }

    public static void startLogFile() {
        RollingFileAppender appender = getFileAppender();
        appender.stop();
        appender.getRollingPolicy().start();
        appender.getTriggeringPolicy().start();
        appender.start();
    }

    public static void stopLogFile() {
        Appender appender = getFileAppender();
        appender.stop();
    }

    public static LogbackInfo getLogbackInfo() {
        Appender logAppender = getConsoleAppender();
        RollingFileAppender fileAppender = getFileAppender();

        LogbackInfo logbackInfo = new LogbackInfo();
        logbackInfo.setConsoleLogStatus(logAppender.isStarted() ? "started" : "stopped");
        logbackInfo.setFileLogStatus(fileAppender.isStarted() ? "started" : "stopped");
        logbackInfo.setFileName(fileAppender.getFile());
        logbackInfo.setFilePattern(getTriggeringPolicy().getFileNamePattern());
        logbackInfo.setMaxHistory(getTriggeringPolicy().getMaxHistory());
        logbackInfo.setFskLogLevel(getFskLogger().getLevel().toString());
        logbackInfo.setRootLogLevel(getRootLogger().getLevel().toString());

        SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = getTimeBasedFileNamingAndTriggeringPolicy();
        Field maxFileSizeField = ReflectionUtils.findField(SizeAndTimeBasedFNATP.class, "maxFileSize");
        maxFileSizeField.setAccessible(true);
        Object fileSize = ReflectionUtils.getField(maxFileSizeField, sizeAndTimeBasedFNATP);
        if (fileSize != null) {
            logbackInfo.setMaxFileSize(fileSize.toString());
        }

        return logbackInfo;
    }

    public static LogbackInfo updateLogbackInfo(LogbackInfo logbackInfo) {
        String consoleLogStatus = logbackInfo.getConsoleLogStatus();
        if (consoleLogStatus != null && consoleLogStatus.toLowerCase().contains("stop")) {
            stopLogConsole();
        }
        if (consoleLogStatus != null && consoleLogStatus.toLowerCase().contains("start")) {
            startLogConsole();
        }


        String fileLogStatus = logbackInfo.getFileLogStatus();
        if (fileLogStatus != null && fileLogStatus.toLowerCase().contains("stop")) {
            stopLogFile();
        }
        if (fileLogStatus != null && fileLogStatus.toLowerCase().contains("start")) {
            startLogFile();
        }

        updateLogFilepath(logbackInfo.getFilepath());

        updateFskLogLevel(logbackInfo.getFskLogLevel());

        updateRootLogLevel(logbackInfo.getRootLogLevel());

        updateMaxFileSize(logbackInfo.getMaxFileSize());

        updateMaxHistory(logbackInfo.getMaxHistory());

        return getLogbackInfo();
    }

}

