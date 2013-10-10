
package com.zygon.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class ConfigurationCommandProcessor implements CommandProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationCommandProcessor.class);
    
    private final CommandProcessor cmdProcessor;
    private final Schema schema;
    private final Schema childSchema;
    private final ConfigurationManager configurationManager;

    public ConfigurationCommandProcessor(CommandProcessor cmdProcessor, Schema schema, Schema childSchema, ConfigurationManager configurationManager) {
        this.cmdProcessor = cmdProcessor;
        this.schema = schema;
        this.childSchema = childSchema;
        this.configurationManager = configurationManager;
    }
    
    @Override
    public CommandResult process(Command command) {
        CommandResult result = null;
        
        if (command.isEditRequest()) {
            if (this.schema != null) {
                if (command.hasArguments()) {
                    
                    // TODO: link up the args with the schema
                    
                } else {
                    // do we care?
                }
                
            } else {
                logger.error("Unable to process edit request. No schema.");
                throw new IllegalArgumentException("Unable to process edit request. No schema.");
            }
        } else if (command.isCreateRequest()) {
            if (this.childSchema != null) {
                
                // TODO
            } else {
                logger.error("Unable to process create request. No child schema.");
                throw new IllegalArgumentException("Unable to process create request. No child schema.");
            }
        } else {
            result = this.cmdProcessor.process(command);
        }
        
        return result;
    }
}
