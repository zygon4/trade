
package com.zygon.exchange;

import org.apache.commons.daemon.Daemon;

/**
 *
 * @author zygon
 */
public class Main {

    public static void main(String[] args) throws Exception {
        
        Daemon daemonService = new Service();
        
        daemonService.init(null);
        
        try {
            daemonService.start();
            
            Thread.sleep (6000000);
            
            daemonService.stop();
        } finally {
            daemonService.destroy();
        }
    }
}
