
package com.zygon.trade;

import com.zygon.command.CommandProcessor;
import com.zygon.command.CommandResult;
import com.zygon.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * TBD: better name
 * The purpose of this class is to intercept configuration requests and 
 * provide them to the configuration manager.
 */
public class OutputProviderImpl implements OutputProvider {

    private static final Logger logger = LoggerFactory.getLogger(OutputProviderImpl.class);
    
    private final OutputProvider outputProvider;
    private final CommandProcessor cmdProcessor;

    public OutputProviderImpl(OutputProvider outputProvider, CommandProcessor cmdProcessor) {
        this.outputProvider = outputProvider;
        this.cmdProcessor = cmdProcessor;
    }

    @Override
    public String getDisplayname() {
        return this.outputProvider.getDisplayname();
    }

    @Override
    public String getNavigationElementToken() {
        return this.outputProvider.getNavigationElementToken();
    }

    @Override
    public Response getOutput(Request request) {
        
        if (request.isCommandRequest()) {
            String commandName = request.getCommandName();
            
            if (request.isCreateRequest() || request.isEditRequest()) {
                
                Command command = new Command(commandName, request.getArgs());
                CommandResult result = this.cmdProcessor.process(command);
                
                if (!result.isSuccessful()) {
                    String outputMessage = "Command failed due to: " + result.getMessage();
                    logger.debug(outputMessage);
                }
            }
        }
        
        return this.outputProvider.getOutput(request);
    }
}
