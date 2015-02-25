
package com.zygon.trade;

import com.zygon.configuration.Configurable;
import com.zygon.command.CommandProcessor;
import com.zygon.command.CommandResult;
import com.zygon.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercept configuration related requests.
 *
 * @author zygon
 */
public class ConfigurationCommandProcessor implements CommandProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationCommandProcessor.class);
    
    private final CommandProcessor cmdProcessor;
    private final Configurable configurable;

    public ConfigurationCommandProcessor(CommandProcessor cmdProcessor, Configurable configurable) {
        this.cmdProcessor = cmdProcessor;
        this.configurable = configurable;
    }
    
    private CommandResult handleCreateRequest(CommandProcessor cmdProcessor, Command command) {
        // TODO: 
        
        CommandResult result = cmdProcessor.process(command);
        
        return result;
    }
    
    private CommandResult handleEditRequest(Configurable configurable, String[] arguments) {
        
        /*
         * TODO:
         * - Convert args into something more tangible
         * - Validate versus the config element
         * - 
         */
        
//        this.configurable.c
        // TODO:
        return CommandResult.SUCCESS;
    }
    
    @Override
    public CommandResult process(Command command) {
        CommandResult result = null;
        
        if (command.isEditRequest()) {
            
            if (this.configurable != null) {
                
                if (command.hasArguments()) {
                    result = this.handleEditRequest(this.configurable, command.getArguments());
                } else {
                    logger.error("Unable to process create request. No arguments provided.");
                    throw new IllegalArgumentException("Unable to process edit request. No arguments provided.");
                }
                
            } else {
                logger.error("Unable to process edit request. No schema.");
                throw new IllegalArgumentException("Unable to process edit request. No schema.");
            }
            
        } else if (command.isCreateRequest()) {
            
            if (this.cmdProcessor instanceof ParentModule) {
                result = ((ParentModule) this.cmdProcessor).process(command);
            } else {
                logger.error("Unable to process create request. Not a parent.");
                throw new IllegalArgumentException("Unable to process create request. Not a parent.");
            }
            
        } else {
            result = this.cmdProcessor.process(command);
        }
        
        return result;
    }
}
