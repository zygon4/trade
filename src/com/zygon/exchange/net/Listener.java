/**
 * 
 */

package com.zygon.exchange.net;

import com.zygon.exchange.EThread;
import com.zygon.exchange.modules.feed.FListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * More of a transfer point than a listener proper.
 * 
 * @author zygon
 */
public class Listener extends EThread implements FListener {

    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final byte[] buffer = new byte[65535];
    
    
    private volatile boolean running = true;
    
    public Listener(String name, InputStream inputStream, OutputStream outputStream) {
        super("Listener-" + name);
        
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    
    @Override
    public void doRun() {
        int read = 0;
        while (this.running) {
            try {
                
                read = this.inputStream.read(this.buffer);
                
                if (read > 0) {
                    this.outputStream.write(this.buffer, 0, read);
                } else if (read == -1) {
                    this.running = false;
                }
                
            } catch (IOException io) {
                // ignore, consider tracing
            } catch (Throwable th) {
                // ignore, consider tracing
            }
        }
    }
    
    @Override
    public final void doStop() {
        this.running = false;
    }

    @Override
    public void doStart() {
        if (!this.running) {
            throw new IllegalStateException("cannot be restarted");
        }
        this.start();
    }
}
