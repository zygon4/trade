
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
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
    private final Footer pageFooter = new Footer("Author: David Charubini");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        
        Map<String, Object> status = new HashMap<>();
        status.put(Request.STATUS, null);
        Request req = new Request(status);
        
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        
        sb.append("<head>\n");
        sb.append(this.pageHeader.render(req).getOutput());
        sb.append("</head>\n");
        
        sb.append("<body>\n");
        
        // TODO: stuff
        
        sb.append("</body>\n");
        
        sb.append(this.pageFooter.render(req).getOutput());
        sb.append("</html>");
        
        response.getWriter().println(sb.toString());
    }
}
