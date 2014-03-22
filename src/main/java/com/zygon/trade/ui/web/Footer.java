
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 *
 * @author zygon
 */
public class Footer implements Component {

    private final String footer;

    public Footer(String footer) {
        this.footer = footer;
    }
    
    @Override
    public Response render(Request request) {
        return new Response(WebUtil.writeTag("footer", this.footer));
    }
}
