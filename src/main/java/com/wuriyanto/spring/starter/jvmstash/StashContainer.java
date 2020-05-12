package com.wuriyanto.spring.starter.jvmstash;

import com.wuriyanto.jvmstash.Stash;
import com.wuriyanto.jvmstash.StashException;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StashContainer implements SmartLifecycle {

    private static final Logger LOGGER = Logger.getLogger(StashContainer.class.getName());

    private StashProperties stashProperties;

    private Stash stash;

    private volatile boolean running;

    public StashContainer(StashProperties stashProperties) {
        this.stashProperties = stashProperties;
    }

    @Override
    public void start() {
        LOGGER.log(Level.INFO, "start stash spring starter {}", stashProperties);

        try {
            stash = stashProperties.getStashBuilder().build();
        } catch (StashException e) {
            e.printStackTrace();
        }

        try {
            stash.connect();
        } catch (StashException e) {
            e.printStackTrace();
        }

        this.running = true;
    }

    @Override
    public void stop() {
        LOGGER.log(Level.INFO, "stop stash spring starter");
        if (stash != null) {
            try {
                stash.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
    }

    public Stash getStash() {
        return stash;
    }
}
