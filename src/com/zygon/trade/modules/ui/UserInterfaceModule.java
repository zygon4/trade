/**
 * 
 */

package com.zygon.trade.modules.ui;

import com.zygon.trade.ParentModule;

/**
 * @author zygon
 *
 */
public class UserInterfaceModule extends ParentModule {

    public static String ID = "ui";
    
    public UserInterfaceModule() {
        super(ID, WebConsole.class);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
