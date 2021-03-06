
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 *
 * @author zygon
 */
public class Footer implements Component {

    private final String content;

    public Footer(String content) {
        this.content = content;
    }
    
    @Override
    public Response render(Request request) {
        return new Response(WebUtil.writeTag(Tags.FOOTER, this.content));
    }
}
