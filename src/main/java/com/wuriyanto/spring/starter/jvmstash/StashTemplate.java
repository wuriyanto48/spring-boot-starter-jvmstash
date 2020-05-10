package com.wuriyanto.spring.starter.jvmstash;

import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.logging.Logger;

public class StashTemplate {

    private static final Logger LOGGER = Logger.getLogger(StashTemplate.class.getName());

    @Resource
    StashContainer stashContainer;

    public StashTemplate() {

    }

    public StashTemplate(StashContainer stashContainer) {
        Assert.notNull(stashContainer, "can't construct StashTemplate with empty stashContainer");
        this.stashContainer = stashContainer;
    }

    public void write(int b) throws IOException {
        this.stashContainer.getStash().write(b);
    }

    public void write(byte[] b) throws IOException {
        this.stashContainer.getStash().write(b);
    }
}
