
package com.zygon.trade;

/**
 *
 * @author zygon
 */
public class Command {
    
    public static final String EDIT = "edit";
    public static final String CREATE = "create";

    private final String name;
    private final String[] arguments;

    public Command(String name, String[] arguments) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return this.name;
    }
    
    public boolean hasArguments() {
        return this.arguments != null;
    }
    
    /*pkg*/ boolean isCreateRequest() {
        return this.name.equals(CREATE);
    }
    
    /*pkg*/ boolean isEditRequest() {
        return this.name.equals(EDIT);
    }
}
