
package com.zygon.trade;

import com.zygon.command.CommandProcessor;
import com.zygon.command.CommandResult;
import com.zygon.command.Command;
import com.zygon.schema.ConfigurationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class ConfigurationCommandProcessor implements CommandProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationCommandProcessor.class);
    
    private final CommandProcessor cmdProcessor;
    private final ConfigurationSchema schema;
    private final ConfigurationManager configurationManager;

    public ConfigurationCommandProcessor(CommandProcessor cmdProcessor, ConfigurationSchema schema, ConfigurationManager configurationManager) {
        this.cmdProcessor = cmdProcessor;
        this.schema = schema;
        this.configurationManager = configurationManager;
    }
    
    private CommandResult handleEditRequest(ConfigurationSchema schema, String[] arguments) {
        // TODO:
        return CommandResult.SUCCESS;
    }
    
    @Override
    public CommandResult process(Command command) {
        CommandResult result = null;
        
        if (command.isEditRequest()) {
            if (this.schema != null) {
                if (command.hasArguments()) {
                    result = this.handleEditRequest(this.schema, command.getArguments());
                } else {
                    logger.error("Unable to process create request. No arguments provided.");
                    throw new IllegalArgumentException("Unable to process edit request. No arguments provided.");
                }
                
            } else {
                logger.error("Unable to process edit request. No schema.");
                throw new IllegalArgumentException("Unable to process edit request. No schema.");
            }
        } else if (command.isCreateRequest()) {
            
            // TODO
            
//            if (this.childSchema != null) {
//                
//                
//            } else {
//                logger.error("Unable to process create request. No child schema.");
//                throw new IllegalArgumentException("Unable to process create request. No child schema.");
//            }
        } else {
            result = this.cmdProcessor.process(command);
        }
        
        return result;
    }
}
