
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 *
 * @author zygon
 */
public class Text extends StyledComponent {

    private final String content;
    
    public Text(Styling style, String content) {
        super(style);
        
        this.content = content;
    }

    public Text(String content) {
        this (null, content);
    }
    
    @Override
    public Response render(Request request) {
        return new Response(WebUtil.writeTag(Tags.SAMP, this.content));
    }
}
