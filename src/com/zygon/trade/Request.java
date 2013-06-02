/**
 * 
 */

package com.zygon.trade;

import java.util.Map;

/**
 *
 * @author zygon
 */
public final class Request {
    
    public static final String CMD = "_cmd";
    public static final String ARGS = "_args";
    public static final String LC = "_lc";
    
    private final Map<String, Object> input;

    public Request(Map<String, Object> input) {
        this.input = input;
    }

    public Object get(String key) {
        return this.input.get(key);
    }
    
    public String[] getArgs() {
        return (String[]) this.input.get(ARGS);
    }
    
    public String getCommand() {
        return (String) this.input.get(CMD);
    }
    
    public boolean isCommand(String cmd) {
        return ((String) this.input.get(CMD)).startsWith(cmd);
    }
    
    public boolean isCommandRequest() {
        return this.input.containsKey(CMD);
    }

    public boolean isListCommandRequest() {
        return this.input.containsKey(LC);
    }
}
