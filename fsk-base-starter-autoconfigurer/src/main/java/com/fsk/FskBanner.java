package com.fsk;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/3/1
 * Describe: FskBanner.
 */
@Slf4j
public class FskBanner implements Banner {

    public static Logger logger = LoggerFactory.getLogger(FskBanner.class);
    private static final String[] BANNER_DEFAULT = new String[]{" ____  ____  __ _        ____   __   ____  ____", "(  __)/ ___)(  / ) ___  (  _ \\ / _\\ / ___)(  __)", " ) _) \\___ \\ )  ( (___)  ) _ (/    \\\\___ \\ ) _)", "(__)  (____/(__\\_)      (____/\\_/\\_/(____/(____)"};
    private static final String[] BANNER_OTHER = new String[]{"███████╗███████╗██╗  ██╗     ██████╗  █████╗ ███████╗███████╗","██╔════╝██╔════╝██║ ██╔╝     ██╔══██╗██╔══██╗██╔════╝██╔════╝","█████╗  ███████╗█████╔╝█████╗██████╔╝███████║███████╗█████╗  ","██╔══╝  ╚════██║██╔═██╗╚════╝██╔══██╗██╔══██║╚════██║██╔══╝  ","██║     ███████║██║  ██╗     ██████╔╝██║  ██║███████║███████╗","╚═╝     ╚══════╝╚═╝  ╚═╝     ╚═════╝ ╚═╝  ╚═╝╚══════╝╚══════╝"};
    private static final String[] BANNER_OTHER1 = new String[]{" (                                  "," )\\ )      )      (                 ","(()/(   ( /(    ( )\\     )      (   "," /(_)|  )\\())__ )((_) ( /( (   ))\\  ","(_))_)\\((_)\\___((_)_  )(_)))\\ /((_) ","| |_((_) |(_)   | _ )((_)_((_|_))   ","| __(_-< / /    | _ \\/ _` (_-< -_)  ","|_| /__/_\\_\\    |___/\\__,_/__|___|  "};
    private static final String FSK_BASE_FRAMEWORK = " :: FBFA :: ";
    private static final int STRAP_LINE_SIZE = 42;

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println(AnsiOutput.toString(AnsiColor.WHITE, ">>>>> Welcome to FSK Base Framework <<<<<", AnsiColor.DEFAULT));
        out.println("-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-");
        for (String var : BANNER_DEFAULT) {
            out.println(AnsiOutput.toString(AnsiColor.GREEN, var, AnsiColor.DEFAULT));
        }

        String version = FskApplication.FRAMEWORK_VERSION;
        version = version != null ? " ( v" + version + " )" : "";
        StringBuilder padding = new StringBuilder();

        while (padding.length() < STRAP_LINE_SIZE - (version.length() + FSK_BASE_FRAMEWORK.length())) {
            padding.append(" ");
        }

        out.println(AnsiOutput.toString(new Object[]{AnsiColor.GREEN, FSK_BASE_FRAMEWORK, AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, version}));
        out.println("-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            log.info("Banner running Interrupted");
        }
    }
}
