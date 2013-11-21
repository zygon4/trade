
package com.zygon.trade.ui.web;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

/**
 *
 * @author zygon
 */
public class JettyServer extends WebServer {

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
        
        this.server = new Server(port);
        
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(DefaultServlet.class.getCanonicalName(), "/*");
        this.server.setHandler(handler);
        
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setSecurityHandler(basicAuth("zygon", "foobar", "admin"));
        context.setContextPath("/");
        server.setHandler(context);
        
        // I think this could replace the need for a web.xml - which I'd like
//        WebAppContext webapp = new WebAppContext();
//        webapp.setContextPath("/");
//        webapp.setWar("../../tests/test-webapps/test-jetty-webapp/target/test-jetty-webapp-9.0.0-SNAPSHOT.war");
//        server.setHandler(webapp);
        
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
}
