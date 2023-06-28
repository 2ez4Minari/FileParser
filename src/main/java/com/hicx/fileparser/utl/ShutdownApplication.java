package com.hicx.fileparser.utl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class ShutdownApplication {
    private final ApplicationContext appContext;


    public void terminateFileParser() {
        System.out.println("No file path provided, terminating File Parser application.");
        // Need to call System.exit to ensure spawned batch job pod is Completed successfully.
        System.exit(SpringApplication.exit(appContext, () -> 0));
    }
}
