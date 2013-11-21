
package com.zygon.trade.ui.web;

/**
 *
 * @author zygon
 */
public class WebTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        WebServer server = new JettyServer(8080);
        
        
        server.start();
        
        Thread.sleep (20000);
        
        server.stop();
    }

}
