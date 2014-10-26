
package com.zygon.trade.ui.web;

import com.zygon.trade.Request;
import com.zygon.trade.Response;

/**
 *
 * @author zygon
 */
public class StyledComponent implements Component {

    private final Styling style;

    public StyledComponent (Styling style) {
        this.style = style;
    }
    
    protected Styling getStyling() {
        return this.style;
    }

    @Override
    public Response render(Request request) {
        // TBD
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
