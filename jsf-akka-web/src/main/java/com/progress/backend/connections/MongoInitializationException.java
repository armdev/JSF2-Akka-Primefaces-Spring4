
package com.progress.backend.connections;

/**
 *
 * @author armen
 */
public class MongoInitializationException extends RuntimeException {

    private static final long serialVersionUID = -1099192821808240472L;

   
    public MongoInitializationException(String message) {
        super(message);
    }
  
    public MongoInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}