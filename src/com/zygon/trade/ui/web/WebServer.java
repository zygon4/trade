
package com.zygon.trade.ui.web;

/**
 *
 * @author zygon
 */
public class WebServer {

    private final int port;

    public WebServer(int port) {
        this.port = port;
    }

    protected void error(String msg, Throwable th) {
        // TBD:
    }
    
    public final int getPort() {
        return this.port;
    }
    
    public void start() {
        
    }
    
    public void stop() {
        
    }
}
