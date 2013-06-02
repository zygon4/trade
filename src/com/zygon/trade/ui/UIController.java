/**
 * 
 */

package com.zygon.trade.ui;

import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import com.zygon.trade.Module;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class UIController {

    private final Module root;
    private final Module[] modules;
    
    private Shell shell = null;

    public UIController(Module root, Module[] modules) {
        this.root = root;
        this.modules = modules;
    }

    private void buildNavigationTree (List<NavigationNode> children, Module[] modules) {
        if (modules != null) {
            for (Module module : modules) {
                if (module.getModules() == null) {
                    children.add(new NavigationNode(module, null));
                } else {
                    this.buildNavigationTree(children, module.getModules());
                }
            }
        }
    }
    
    private static final String PROMPT = ">";
    
    public void initialize() throws IOException {
        if (this.shell != null) {
            throw new IllegalStateException();
        }
        
        List<NavigationNode> nodes = new ArrayList<>();
        this.buildNavigationTree(nodes, this.modules);
        NavigationNode root = new NavigationNode(this.root, nodes.toArray(new NavigationNode[nodes.size()]));
        
        this.shell = ShellFactory.createConsoleShell(PROMPT, "Stella", root);
        try {
        this.shell.commandLoop();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
    
    public void uninitialize() {
        // TBD: force exit?
        this.shell = null;
    }
}
