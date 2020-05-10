package com.wuriyanto.spring.starter.jvmstash;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Wuriyanto 2020.
 * Stash class, represent OutputStream writer
 * this class will override all write method from OutputStream to write data into logstash
 */
public class Stash extends OutputStream {

    private static final Logger LOGGER = Logger.getLogger(Stash.class.getName());

    // socket represent socket client
    private Socket socket;

    // writer represent io writer
    private DataOutputStream writer;

    // reader represent io reader
    // not yet useful right now,
    // but in the future maybe we need read the reply from server
    private DataInputStream reader;

    private Boolean closed;

    private StashSocketFactory stashSocketFactory;

    private Stash(Builder builder) throws StashException {

        if (!builder.secure) {
            this.stashSocketFactory = new DefaultStashSocketFactory(builder.host, builder.port,
                    false, builder.connectionTimeout, builder.readTimeout, null);
        } else {
            try {
                KeyStore ks = KeyStore.getInstance("PKCS12");
                InputStream keyIn = builder.keyStoreIs;
                ks.load(keyIn, builder.keyStorePassword.toCharArray());

                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                kmf.init(ks, builder.keyStorePassword.toCharArray());

                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(ks);

                // Create a SSLSocketFactory that allows for self signed certs
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                SSLSocketFactory ssf = ctx.getSocketFactory();
                this.stashSocketFactory = new DefaultStashSocketFactory(builder.host, builder.port,
                        builder.secure, builder.connectionTimeout, builder.readTimeout, ssf);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException(e.getMessage());
            } catch (CertificateException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException("certificate error " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException("algorithm error " + e.getMessage());
            } catch (KeyStoreException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException("keystore error " + e.getMessage());
            } catch (KeyManagementException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException("key management error " + e.getMessage());
            } catch (UnrecoverableKeyException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException("unrecoverable key error " + e.getMessage());
            }
        }

    }

    public void connect() throws StashException {
        if (!isConnected()) {
            try {
                this.socket = this.stashSocketFactory.create();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException(e.getMessage());
            }

            try {
                LOGGER.log(Level.INFO, "logstash connected");

                this.writer = new DataOutputStream(socket.getOutputStream());
                this.reader = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
                throw new StashException(e.getMessage());
            }
        }
    }

    @Override
    public void write(int b) throws IOException {
        if (socket.isConnected()) {
            this.writer.write(b);
            this.writer.write(Constant.CRLF);

            //buffer might be full so flush now
            this.writer.flush();
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (socket.isConnected()) {
            this.writer.write(b);
            this.writer.write(Constant.CRLF);

            //buffer might be full so flush now
            this.writer.flush();
        }
    }

    public void disconnect() throws IOException {
        if (isConnected()) {
            LOGGER.log(Level.INFO, "logstash disconnected");
            this.socket.close();
            this.writer.close();
            this.reader.close();
            //super.close();
        }
    }

    public boolean isConnected() {
        return this.socket != null && this.socket.isBound() && !this.socket.isClosed() && this.socket.isConnected()
                && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown();
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    public static class Builder {
        private String host;
        private Integer port;
        private Boolean secure;
        private InputStream keyStoreIs;
        private String keyStorePassword;
        private Integer connectionTimeout;
        private Integer readTimeout;

        public Builder() {
            // default host
            this.host = Constant.DEFAULT_HOST;

            // logstash default port
            this.port = Constant.DEFAULT_PORT;

            // connection timeout when trying to connect to logstash server
            this.connectionTimeout = Constant.DEFAULT_TIMEOUT;

            // connection timeout when trying to read from logstash server
            this.readTimeout = Constant.DEFAULT_TIMEOUT;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setSecure(Boolean secure) {
            this.secure = secure;
            return this;
        }

        public Builder setKeyStoreIs(InputStream keyStoreIs) {
            this.keyStoreIs = keyStoreIs;
            return this;
        }

        public Builder setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
            return this;
        }

        public Builder setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setReadTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Stash build() throws StashException {
            return new Stash(this);
        }
    }
}
