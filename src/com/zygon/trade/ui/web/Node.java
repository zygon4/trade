
package com.zygon.trade.ui.web;

import com.zygon.trade.OutputProvider;
import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 * A Node represents an element in the system that corresponds to some unit of
 * functionality.  This might be backed by a Module or Module facade, etc.
 *
 * @author zygon
 */
public class Node implements Component {

    private final OutputProvider outputProvider;

    public Node(OutputProvider outputProvider) {
        this.outputProvider = outputProvider;
    }

    @Override
    public Response render(Request request) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(WebUtil.writeTag("h6", this.outputProvider.getDisplayname()));
        sb.append(this.outputProvider.getOutput(request).getOutput());
        
        return new Response(sb.toString());
    }
}
