package com.wuriyanto.spring.starter.jvmstash;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = StashProperties.class)
public class StashAutoConfiguration {

    private StashProperties stashProperties;

    public StashAutoConfiguration(StashProperties stashProperties) {
        this.stashProperties = stashProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public StashContainer stashContainer() {
        return new StashContainer(stashProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public StashTemplate stashTemplate() {
        return new StashTemplate();
    }
}
