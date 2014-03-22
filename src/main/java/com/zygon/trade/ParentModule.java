
package com.zygon.trade;

import com.zygon.command.Command;
import com.zygon.command.CommandResult;
import com.zygon.schema.ConfigurationSchema;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is to abstract away the responsibilities of 
 * advertising, creating, deleting children
 *
 * @author zygon
 */
public abstract class ParentModule extends Module {

    private final Class<? extends Module> childClazz;
    private final Logger logger;
    private final ConfigurationSchema childSchema;
    
    public ParentModule(String name, Schema schema, Collection<String> supportedCommands, Class<? extends Module> childClazz) {
        super(name, schema, supportedCommands);
        
        this.childClazz = childClazz;
        this.logger = LoggerFactory.getLogger(name);
        
        Module instance = null;
        
        try {
            instance = (Module) this.childClazz.getConstructor(String.class).newInstance("foo");
        } catch (Exception e) {
            logger.error(null, e);
            throw new RuntimeException();
        }
        
        this.childSchema = instance.getSchema();
    }

    public ParentModule(String name, Schema schema, Class<? extends Module> childClazz) {
        this (name, schema, null, childClazz);
    }

    public ParentModule(String name, Class<? extends Module> childClazz) {
        this(name, null, null, childClazz);
    }
    
    /*pkg*/ Module createChild(Configuration config, boolean install) {
        
        String name = config.getValue("name");
        
        Module instance = null;
        
        try {
            instance = (Module) this.childClazz.getConstructor(String.class).newInstance(name);
        } catch (Exception e) {
            logger.error(null, e);
            throw new RuntimeException("Error creating child " + name, e);
        }
        
        if (install) {
            instance.install();
            instance.configure(config);
            
            try {
                instance.doInit();
            } catch (Throwable th) {
                throw new RuntimeException("Error initializing module " + instance.getDisplayname(), th);
            }
        }
        
        instance.setParent(this);
        
        return instance;
    }
    
    /*pkg*/ Class<? extends Module> getChildClazz() {
        return this.childClazz;
    }
    
    @Override
    public Response getOutput(Request request) {
        String output = "";
        
        if (request.isListCommandRequest()) {
            StringBuilder sb = new StringBuilder();
            
            if (this.hasSchema()) {
                sb.append(Command.EDIT);
                sb.append('\n');
                this.writeProperties(sb, this.getSchema());
                sb.append('\n');
            }
            
            sb.append(Command.CREATE);
            sb.append('\n');
            this.writeProperties(sb, this.childSchema);
            sb.append('\n');
            
            output = sb.toString();
        } else {
            return super.getOutput(request);
        }
        
        return new Response(output);
    }

    @Override
    public CommandResult process(Command command) {
        if (command.isCreateRequest()) {
            String[] cmdArgs = command.getArguments();
            
            // TBD: parse arguments into a validated Configuration object
            Configuration config = new Configuration(this.childSchema);
            // Totally ghetto for now
            config.setValue("name", "foo");
            
            try {
                Module child = this.createChild(config, true);
                this.add(child);
            } catch (Exception e) {
                return new CommandResult(false, e.getMessage());
            }
            
            return CommandResult.SUCCESS;
        } else {
            return super.process(command);
        }
    }
}
