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
    public static final String CREATE = "_create";
    public static final String EDIT = "_edit";
    public static final String ARGS = "_args";
    public static final String LC = "_lc";
    public static final String STATUS = "_s";
    
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
    
    public String getCommandName() {
        return (String) this.input.get(CMD);
    }
    
    public boolean hasArguments() {
        return this.input.containsKey(ARGS);
    }
    
    public boolean isCommand(String cmd) {
        return cmd.startsWith(((String) this.input.get(CMD)));
    }
    
    public boolean isCommandRequest() {
        return this.input.containsKey(CMD);
    }

    public boolean isCreateRequest() {
        return this.input.containsKey(CREATE);
    }
    
    public boolean isEditRequest() {
        return this.input.containsKey(EDIT);
    }
    
    public boolean isListCommandRequest() {
        return this.input.containsKey(LC);
    }
    
    public boolean isStatusRequest() {
        return this.input.containsKey(STATUS);
    }
}
