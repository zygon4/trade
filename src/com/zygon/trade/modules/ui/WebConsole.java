
package com.zygon.trade.modules.ui;

import com.zygon.trade.Configuration;
import com.zygon.trade.Module;
import com.zygon.trade.ui.web.JettyServer;

/**
 *
 * @author zygon
 */
public class WebConsole extends Module {

    private int port = -1;
    private JettyServer server = null;
    
    public WebConsole(String name) {
        // TODO: schema that contains port/host information
        super(name, null, null);
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        int port = Integer.parseInt(configuration.getValue("port"));
        
        if (this.server == null) {
            this.port = port;
            this.server = new JettyServer(this.port);
        } else {
            if (port != this.port) {
                this.server.stop();
                this.server = new JettyServer(port);
                this.server.start();
                this.port = port;
            }
        }
    }

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        sb.append("Port - ").append(this.port).append(" - ");
        this.server.writeStatus(sb);
    }

    @Override
    public void initialize() {
        this.server.start();
    }

    @Override
    public void uninitialize() {
        this.server.stop();
    }
}
