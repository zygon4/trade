
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 *
 * @author zygon
 */
public class Container implements Component {

    private final Component component;

    public Container(Component component) {
        this.component = component;
    }
    
    @Override
    public Response render(Request request) {
        Response resp = this.component.render(request);
        String result = WebUtil.writeTag(Tags.DIV, "container", resp.getOutput());
        return new Response(result);
    }
}
