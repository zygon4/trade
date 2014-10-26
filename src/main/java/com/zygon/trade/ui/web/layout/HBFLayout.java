
package com.zygon.trade.ui.web.layout;

import com.zygon.trade.Request;
import com.zygon.trade.Response;
import com.zygon.trade.ui.web.Component;
import com.zygon.trade.ui.web.Tags;
import com.zygon.trade.ui.web.WebUtil;

/**
 *
 * @author zygon
 */
public class HBFLayout implements Component {

    private final Component header;
    private final Component body;
    private final Component footer;

    public HBFLayout(Component header, Component content, Component footer) {
        this.header = header;
        this.body = content;
        this.footer = footer;
    }

    @Override
    public Response render(Request request) {
        StringBuilder sb = new StringBuilder();
        
        String headerContent = this.header.render(request).getOutput();
        sb.append(WebUtil.writeTag(Tags.DIV, "hbf-header", headerContent));
        
        String bodyContent = this.body.render(request).getOutput();
        sb.append(WebUtil.writeTag(Tags.DIV, "hbf-content", bodyContent));
        
        String footerContent = this.footer.render(request).getOutput();
        sb.append(WebUtil.writeTag(Tags.DIV, "hbf-footer", footerContent));
        
        return new Response(sb.toString());
    }
}
