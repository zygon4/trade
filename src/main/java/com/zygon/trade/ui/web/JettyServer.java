
package com.zygon.trade.ui.web;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

/**
 *
 * @author zygon
 */
public class JettyServer extends WebServer {

    private static final boolean REQUEST_LOGGER = true;
    
    private static SecurityHandler basicAuth(String username, String password, String realm) {

        HashLoginService loginService = new HashLoginService();
        loginService.putUser(username, Credential.getCredential(password), new String[]{"user"});
        loginService.setName(realm);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");

        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName(realm);
        csh.addConstraintMapping(cm);
        csh.setLoginService(loginService);

        return csh;
    }
    
    private final Server server;
    
    public JettyServer(int port) {
        super(port);
        
        this.server = new Server();
        
        // Connector(s)
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setOutputBufferSize(32768);
        ServerConnector http = new ServerConnector(this.server, new HttpConnectionFactory(httpConfig));        
        http.setPort(port);
        http.setIdleTimeout(30000);
        
        this.server.setConnectors(new Connector[] { http });
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        
        // web resources
        ResourceHandler resources = new ResourceHandler();
//        resources.setResourceBase("");
        
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resources, context});
        
        this.server.setHandler(handlers);
        
        
        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/");
        
//        String homePath = System.getProperty("user.home");
//        String pwdPath = System.getProperty("user.dir");

        
        // Lastly, the default servlet for root content (always needed, to satisfy servlet spec)
        // It is important that this is last.
//        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
//        holderPwd.setInitParameter("resourceBase",pwdPath);
//        holderPwd.setInitParameter("dirAllowed","true");
//        context.addServlet(holderPwd,"/");
        
//        String jettyHome = System.getProperty("jetty.home","./web");
        
    }

    @Override
    public void start() {
        super.start();
        
        if (!this.server.isRunning() && !this.server.isStarting() && !this.server.isStarted()) {
            try {
                this.server.start();
            } catch (Exception e) {
                this.error(null, e);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        
        if (this.server.isRunning()) {
            try {
                this.server.stop();
            } catch (Exception e) {
                this.error(null, e);
            }
            
            try {
                this.server.join();
            } catch (InterruptedException ie) {
                this.error(null, ie);
            }
        }
    }

    private String getStatus() {
        if (this.server.isStarted()) {
            return "Started";
        } else if (this.server.isStarting()) {
            return "Starting";
        } else if (this.server.isRunning()) {
            return "Running";
        } else if (this.server.isStopping()) {
            return "Stopping";
        } else if (this.server.isStopped()) {
            return "Stopped";
        } else if (this.server.isFailed()) {
            return "Failed";
        }
        
        return "Unknown";
    }
    
    public void writeStatus(StringBuilder sb) {
        sb.append(this.getStatus());
    }
}
