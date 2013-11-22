/**
 * 
 */

package com.zygon.trade.modules.ui;

import com.zygon.trade.Module;
import com.zygon.trade.ui.UIController;
import java.io.IOException;

/**
 * So this CLI module is (hopefully) deprecated.
 *
 * @author zygon
 */
public class CLIModule extends Module {

    private UIController controller = null;

    public CLIModule(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        
        this.controller = new UIController(this.getRoot(), null); // null for now
        
        try {
            this.controller.initialize();
        } catch (IOException io) {
            // no recovery right now if the (present) UI does not come up - so
            // chuck runtime.. if this was recoverable (port conflict, etc)
            // then we would be justified in removing this throw.
            throw new RuntimeException(io);
        }
    }

    @Override
    public void uninitialize() {
        this.controller.uninitialize();
        this.controller = null;
    }
}
