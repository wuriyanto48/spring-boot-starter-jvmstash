package com.wuriyanto.spring.starter.jvmstash;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@ConfigurationProperties(prefix = "stash")
public class StashProperties implements Cloneable {

    private String host = null;

    private Integer port = null;

    private Boolean secure;

    private String keyStorePath;

    private String keyStorePassword;

    private Integer connectionTimeout = null;

    private Integer readTimeout = null;

    public Stash.Builder getStashBuilder() {
        Stash.Builder builder = new Stash.Builder();
        builder.setSecure(secure);

        if (host != null) {
            builder.setHost(host);
        } else {
            builder.setHost(Constant.DEFAULT_HOST);
        }

        if (port != null) {
            builder.setPort(port);
        } else {
            builder.setPort(Constant.DEFAULT_PORT);
        }

        if (keyStorePath != null) {
            InputStream keyStore = null;
            try {
                keyStore = new ClassPathResource(keyStorePath).getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.setKeyStoreIs(keyStore);
        }

        if (keyStorePassword != null) {
            builder.setKeyStorePassword(keyStorePassword);
        }

        if (connectionTimeout != null) {
            builder.setConnectionTimeout(connectionTimeout);
        } else {
            builder.setConnectionTimeout(Constant.DEFAULT_TIMEOUT);
        }

        if (readTimeout != null) {
            builder.setReadTimeout(readTimeout);
        } else {
            builder.setReadTimeout(Constant.DEFAULT_TIMEOUT);
        }

        return builder;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    @Override
    protected StashProperties clone() throws CloneNotSupportedException {
        return (StashProperties) super.clone();
    }

    @Override
    public String toString() {
        return "StashProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", secure=" + secure +
                ", connectionTimeout=" + connectionTimeout +
                ", readTimeout=" + readTimeout +
                '}';
    }
}
