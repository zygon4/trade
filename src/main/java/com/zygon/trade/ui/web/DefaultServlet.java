
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.ui.web.layout.HBFLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zygon
 */
public class DefaultServlet extends HttpServlet {

    private final Header pageHeader = new Header("Stella");
    private final Text content = new Text("This is the main content");
    private final Footer pageFooter = new Footer("Author: David Charubini");
    
    private final HBFLayout hbfLayout = new HBFLayout(pageHeader, content, pageFooter);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        
        Map<String, Object> status = new HashMap<>();
        status.put(Request.STATUS, null);
        Request req = new Request(status);
        
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        
        sb.append("<body>\n");
        
        sb.append(this.hbfLayout.render(req).getOutput());
        
        sb.append("</body>\n");
        
        sb.append("</html>");
        
        response.getWriter().println(sb.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("service");
        this.doGet(req, resp);
    }
    
    
    
}
