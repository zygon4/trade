
package com.zygon.trade;

/**
 *
 * @author zygon
 */
public class CommandResult {

    public static final CommandResult SUCCESS = new CommandResult(true);
    
    private final boolean successful;
    private final String message;

    public CommandResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public CommandResult(boolean successful) {
        this(successful, null);
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isSuccessful() {
        return this.successful;
    }
}
