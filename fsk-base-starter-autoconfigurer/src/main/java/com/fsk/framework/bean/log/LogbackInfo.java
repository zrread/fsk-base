package com.fsk.framework.bean.log;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogbackInfo {

    private String consoleLogStatus;
    private String fileLogStatus;
    private String filepath;
    private String fileName;
    private String filePattern;
    private String maxFileSize;
    private Integer maxHistory;
    private String fskLogLevel;
    private String rootLogLevel;

}
