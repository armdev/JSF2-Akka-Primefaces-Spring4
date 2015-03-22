package com.progress.backend.connections;

import com.mongodb.MongoOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoOptionsFactory {
    private static final Logger logger = LoggerFactory.getLogger(MongoOptionsFactory.class);

    private MongoOptions defaults;
    private int connectionsPerHost;
    private int connectionTimeout;
    private int maxWaitTime;
    private int threadsAllowedToBlockForConnectionMultiplier;
    private boolean autoConnectRetry;
    private int socketTimeOut;

   
    public MongoOptionsFactory() {
        defaults = new MongoOptions();
    }

 
    public MongoOptions createMongoOptions() {
        MongoOptions options = new MongoOptions();
        options.connectionsPerHost = getConnectionsPerHost();
        options.connectTimeout = getConnectionTimeout();
        options.maxWaitTime = getMaxWaitTime();
        options.threadsAllowedToBlockForConnectionMultiplier = getThreadsAllowedToBlockForConnectionMultiplier();
        options.autoConnectRetry = isAutoConnectRetry();
        options.socketTimeout = getSocketTimeOut();
       if (logger.isDebugEnabled()) {
            logger.info("Mongo Options");
            logger.info("Connections per host :{}", options.connectionsPerHost);
            logger.info("Connection timeout : {}", options.connectTimeout);
            logger.info("Max wait timeout : {}", options.maxWaitTime);
            logger.info("Threads allowed to block : {}", options.threadsAllowedToBlockForConnectionMultiplier);
            logger.info("Autoconnect retry : {}", options.autoConnectRetry);
            logger.info("Socket timeout : {}", options.socketTimeout);
        }
        return options;
    }

    /**
     * Getter for the AutoConnectRetry property.
     *
     * @return true if autoConnectRetry is true
     */
    public boolean isAutoConnectRetry() {
        return (autoConnectRetry) || defaults.autoConnectRetry;
    }

    /**
     * Setter for AutoConnectRetry.
     *
     * @param autoConnectRetry true if must try to auto reconnect
     */
    public void setAutoConnectRetry(boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }

 
    public int getConnectionsPerHost() {
        return (connectionsPerHost > 0) ? connectionsPerHost : defaults.connectionsPerHost;
    }

  
    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

 
    public int getConnectionTimeout() {
        return (connectionTimeout > 0) ? connectionTimeout : defaults.connectTimeout;
    }

    
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

  
    public int getMaxWaitTime() {
        return (maxWaitTime > 0) ? maxWaitTime : defaults.maxWaitTime;
    }

   
    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

   
    public int getSocketTimeOut() {
        return (socketTimeOut > 0) ? socketTimeOut : defaults.socketTimeout;
    }

   
    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

   
    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return (threadsAllowedToBlockForConnectionMultiplier > 0)
                ? threadsAllowedToBlockForConnectionMultiplier
                : defaults.threadsAllowedToBlockForConnectionMultiplier;
    }

    
    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }
}
