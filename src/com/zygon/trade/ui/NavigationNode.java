/**
 * 
 */

package com.zygon.trade.ui;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellDependent;
import asg.cliche.ShellFactory;
import com.zygon.trade.OutputProvider;
import com.zygon.trade.Request;
import com.zygon.trade.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Going to try and keep the Cliche library out of the public eye.
 *
 * @author zygon
 */
public class NavigationNode implements ShellDependent {

    // Other potential goodies from Cliche
    //    @Command(description="Command description")
//public int someCommand(
//    @Param(name="param1", description="Description of param1")
//        int param1,
//    @Param(name="param2", description="Description of param2")
//        int param2) {
//    . . .
//}
    
//    @Command(name="list", abbreviation="ls")
//public List listFiles() { . . . }
    
    private final OutputProvider outputProvider;
    private final NavigationNode[] children;
    
    private NavigationNode parent;
    private Shell theShell;

    public NavigationNode(OutputProvider outputProvider, NavigationNode[] children) {
        this.outputProvider = outputProvider;
        this.children = children;
        if (children != null) {
            for (NavigationNode child : this.children) {
                child.setParent(this);
            }
        }
    }

    @Command(description="Execute a command")
    public void cmd (@Param(name="command", description="The command to execute") String cmd, String ...arguments) {
        
        Map<String, Object> command = new HashMap<>();
        command.put(Request.CMD, cmd);
        if (arguments != null) {
            command.put(Request.ARGS, arguments);
        }
        Request request = new Request(command);
        
        Object output = this.outputProvider.getOutput(request);
        if (output != null) {
            System.out.println(output);
        }
    }
    
    @Command(description="Execute a command")
    public void cmd (@Param(name="command", description="The command to execute") String cmd) {
        this.cmd(cmd, (String[]) null);
    }
            
    protected NavigationNode getParent() {
        return this.parent;
    }
    
    protected String getNavigationElement() {
        return this.outputProvider.getNavigationElementToken();
    }
    
    @Command
    public String getName() {
        return this.outputProvider.getDisplayname();
    }
    
    /*pkg*/ NavigationNode getRoot() {
        NavigationNode node = this;
        
        while (node.getParent() != null) {
            node = node.getParent();
        }
        
        return node;
    }
    
    @Command
    public void cd(String path) throws IOException {
        if (path.equals("..")) {
            if (this.getParent() != null) {
                this.parent.theShell.commandLoop();
            }
        } else {
            NavigationNode childTree = null;
            
            if (this.children != null) {
                for (NavigationNode child : this.children) {
                    if (child.getName().equals(path)) {
                        childTree = child;
                        break;
                    }
                }
            }
            
            if (childTree == null) {
                System.out.println("Unknown path : \"" + path + "\"");
            } else {
                ShellFactory.createSubshell(childTree.getNavigationElement(), this.theShell, childTree.getName(), childTree).commandLoop();
            }
        }
    }
    
    @Command(description="List available commands")
    public void lc() {
        Map<String, Object> lc = new HashMap<>();
        lc.put(Request.LC, null);
        Request request = new Request(lc);
        Response output = this.outputProvider.getOutput(request);
        
        System.out.println(output);
    }
    
    @Command 
    public void ls() {
        
        if (this.parent != null) {
            System.out.println(" - ..");
        }
        
        if (this.children != null) {
            for (NavigationNode child : this.children) {
                System.out.println(" - " + child.getName());
            }
        }
    }
    
    @Override
    public void cliSetShell(Shell shell) {
        this.theShell = shell;
    }
    
    private void setParent(NavigationNode parent) {
        this.parent = parent;
    }
}
