/**
 * 
 */

package com.zygon.trade.ui;

import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import com.google.common.collect.Lists;
import com.zygon.trade.ConfigurationCommandProcessor;
import com.zygon.configuration.ConfigurationManager;
import com.zygon.trade.Module;
import com.zygon.trade.OutputProviderImpl;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author zygon
 */
public class UIController {

    private final Module root;
    private final ConfigurationManager configurationManager;
    
    private Shell shell = null;

    // Not crazy about passing through the Configuration Manager but i don't
    // want each Module to have a reference to it.
    public UIController(Module root, ConfigurationManager configurationManager) {
        this.root = root;
        this.configurationManager = configurationManager;
    }

    private List<NavigationNode> buildNavigationTree(Module[] modules) {
        
        List<NavigationNode> nodes = Lists.newArrayList();
        
        if (modules != null) {
            for (Module module : modules) {
                
                NavigationNode[] children = null;
                
                if (module.getModules() != null && module.getModules().length > 0) {
                    List<NavigationNode> childrenNodes = buildNavigationTree(module.getModules());
                    children = childrenNodes.toArray(new NavigationNode[childrenNodes.size()]);
                }
                
                nodes.add(new NavigationNode(
                        // Here we are injecting some additional functionality to keep the configuration
                        // code outside of the Module implementation.
                        new OutputProviderImpl(module, 
                                               new ConfigurationCommandProcessor(module, module, 
                                                                                 this.configurationManager)), children)
                        );
            }
        }
        
        return nodes;
    }
    
    private static final String PROMPT = ">";
    
    public void initialize() throws IOException {
        if (this.shell != null) {
            throw new IllegalStateException();
        }
        
        List<NavigationNode> nodes = this.buildNavigationTree(this.root.getModules());
        
        NavigationNode root = new NavigationNode(this.root, nodes.toArray(new NavigationNode[nodes.size()]));
        
        this.shell = ShellFactory.createConsoleShell(PROMPT, "Stella", root);
        
        // spawn a cheesy thread to man the command loop
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    shell.commandLoop();
                } catch (IOException io) {
                    // TODO: log? remove Cliche completely?
                    io.printStackTrace();
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }
    
    public void uninitialize() {
        // TBD: force exit?
        this.shell = null;
    }
}
