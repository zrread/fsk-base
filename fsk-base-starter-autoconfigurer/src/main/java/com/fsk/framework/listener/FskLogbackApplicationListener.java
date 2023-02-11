package com.fsk.framework.listener;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.fsk.framework.utils.LogUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 用于对Logback进行初始化。由于需要对Logback参数进行动态修改，所以在Logback中必须需要将所有Appender打开，否则将会在运行时
 * 无法获取Appender，但又有一部分Appender不需要增添，所以在该类中将其去除。为了满足该需求，将Appender的配置放在了bootstrap.yml
 * 中。例如：
 * log:
 * appender:
 * STDOUT: true  # 表示打开STDOUT的Appender
 * FILE: false # 表示不写入配置文件
 * <p>
 * 配置好后，程序会在启动时打印少量日志，当执行到该监听器后，就会根据配置停止日志打印。
 */
@Slf4j
public class FskLogbackApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent> {


    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        String hasStdoutLog = event.getApplicationContext().getEnvironment().getProperty("log.file.appender.STDOUT", "true");
        String hasFileLog = event.getApplicationContext().getEnvironment().getProperty("log.file.appender.FILE", "true");
        String fileLocation = event.getApplicationContext().getEnvironment().getProperty("log.file.location", "true");
        String maxFileSize = event.getApplicationContext().getEnvironment().getProperty("log.file.maxFileSize", "true");
        String maxHistory = event.getApplicationContext().getEnvironment().getProperty("log.file.maxHistory", "true");
        String totalSizeCap = event.getApplicationContext().getEnvironment().getProperty("log.file.totalSizeCap", "true");

        FskRollingFileAppender fileAppender = FskRollingFileAppender.builder()
                .file(fileLocation)
                .build();
        FskRollingPolicy rollingPolicy = FskRollingPolicy.builder()
                .fileNamePattern("server.%d{yyyy-MM-dd}.%i.log")
                .maxFileSize(maxFileSize)
                .maxHistory(maxHistory)
                .totalSizeCap(totalSizeCap)
                .build();
        this.initRollingPolicy(fileAppender, rollingPolicy);

        if (hasStdoutLog.toLowerCase().contains("false") || hasStdoutLog.toLowerCase().contains("stop")) {
            log.info("停止打印STDOUT控制日志");
            LogUtils.stopLogConsole();

        }

        if (hasFileLog.toLowerCase().contains("false") || hasFileLog.toLowerCase().contains("stop")) {
            log.info("停止打印FILE文件日志");
            LogUtils.stopLogFile();
        }
    }

    private void initRollingPolicy(FskRollingFileAppender appender, FskRollingPolicy policy) {
        RollingFileAppender fileAppender = LogUtils.getFileAppender();
        fileAppender.setFile(appender.getFile());

        SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy = (SizeAndTimeBasedRollingPolicy)fileAppender.getRollingPolicy();
        sizeAndTimeBasedRollingPolicy.setFileNamePattern(policy.getFileNamePattern());
        sizeAndTimeBasedRollingPolicy.setMaxFileSize(FileSize.valueOf(policy.getMaxFileSize()));
        sizeAndTimeBasedRollingPolicy.setMaxHistory(Integer.parseInt(policy.getMaxHistory()));
        sizeAndTimeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf(policy.getTotalSizeCap()));
        fileAppender.setRollingPolicy(sizeAndTimeBasedRollingPolicy);

        fileAppender.stop();
        sizeAndTimeBasedRollingPolicy.start();
        fileAppender.start();
    }

    @Builder
    @Getter
    @Setter
    public static class FskRollingFileAppender {
        private String file;
    }

    @Builder
    @Getter
    @Setter
    public static class FskRollingPolicy {
        private String fileNamePattern;
        private String maxFileSize;
        private String maxHistory;
        private String totalSizeCap;
    }
}
