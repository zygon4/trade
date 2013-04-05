/**
 * 
 */

package com.zygon.exchange;

/**
 *
 * @author zygon
 * 
 * This is to gently abstract away threads and thread options.
 * 
 * TBD: punt on this and use the Quartz scheduler instead
 */
@Deprecated
public abstract class EThread extends Thread {

    protected EThread(String name) {
        super(name);
        super.setDaemon(true);
    }

    protected abstract void doRun();
    
    @Override
    public final void run() {
        
        // TBD trace
        
        doRun();
        
        // TBD trace
    }
}
