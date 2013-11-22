
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 *
 * @author zygon
 */
public class Header implements Component {

    private final String header;

    public Header(String header) {
        this.header = header;
    }
    
    @Override
    public Response render(Request request) {
        return new Response(WebUtil.writeTag("header", WebUtil.writeTag("h1", this.header)));
    }
}
