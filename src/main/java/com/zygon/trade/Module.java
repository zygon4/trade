/**
 * 
 */

package com.zygon.trade;

import com.zygon.configuration.Configurable;
import com.google.common.base.Preconditions;
import com.zygon.configuration.Configuration;
import com.google.common.collect.Lists;
import com.zygon.command.CommandProcessor;
import com.zygon.command.CommandResult;
import com.zygon.command.Command;
import com.zygon.schema.parse.ConfigurationSchema;
import com.zygon.schema.parse.JSONSchemaParser;
import com.zygon.schema.parse.SchemaParser;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Module is intended to be a user facing element in the system. They hold
 * status information and any controllers that an operator might want to use
 * to interact with the underlying functionality.
 *
 * @author zygon
 */
public abstract class Module implements OutputProvider, CommandProcessor, Installable, Configurable {

    /*pkg*/ static  Module createModule(String clazz, String name) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        
        Class<Module> cls = (Class<Module>) Class.forName(clazz);
        Constructor<Module> constructor = null;
        Module newInstance = null;
        
        if (name != null) {
            try {
                constructor = cls.getConstructor(String.class);
                newInstance = constructor.newInstance(name);
            } catch (NoSuchMethodException nsme) {
                // try again with parent module constructor signature

                constructor = cls.getConstructor();
                newInstance = constructor.newInstance();
            }
        } else {
            constructor = cls.getConstructor();
            newInstance = constructor.newInstance();
        }
        
        return newInstance;
    }
    
    /*pkg*/ static  Module createModule(String clazz) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        return createModule(clazz, null);
    }
    
    // TBD: unique identifier using the name
    private final String name;
    private final Logger logger;
    private final ConfigurationSchema schema; 
    private final Set<String> commands = new HashSet<>();
    private final SchemaRenderer schemaRender = new SchemaRenderer();
    private final List<Module> modules = Lists.newArrayList();
     
    private Module parent = null;
    private Configuration configuration; // is it easier to set a config once
                                         // and mutate it - or keep resetting it?
    /*pkg*/ Module(String name, Schema schema, Collection<String> supportedCommands, SchemaParser schemaParser) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(this.name);
        
        try {
            
            if (schema != null) {
                this.schema = schema.getConfigurationSchema() != null ? schema.getConfigurationSchema() : 
                        schemaParser.parse(schema.getSchemaResource(), this.getClass().getResourceAsStream(schema.getSchemaResource()));
            } else {
                this.schema = null;
            }
            
        } catch (IOException io) {
            // TBD: what to do? This is fatal..
            this.logger.error(null, io);
            throw new RuntimeException();
        }
        
        if (supportedCommands != null) {
            for (String cmdName : supportedCommands) {
                this.commands.add(cmdName);
            }
        }
        
        // for now lets add whatever modules are given - soon remove the
        // getModule[] method.
        Module[] modules = this.getModules();
        if (modules != null && modules.length != 0) {
            this.modules.addAll(Arrays.asList(this.getModules()));
        }
    }    
    
    public Module(String name, Schema schema, Collection<String> supportedCommands) {
        this(name, schema, supportedCommands, new JSONSchemaParser());
    }
    
    protected Module(String name, Schema schema) {
        this(name, schema, null);
    }
    
    protected Module(String name) {
        this(name, null);
    }

    /*pkg*/ void add(Module module) {
        this.modules.add(module);
    }
    
    @Override
    public void configure(Configuration configuration) {
        Preconditions.checkState(this.hasSchema());
        
        // TODO: inspect properties and take action probably before setting the in memory config.  
        // TODO: Consider making a protected "doConfiguration" to enforce proper usage by children classes.
        // TODO: Check if the configuration name and (possibly) version matches our own.
        this.configuration = configuration;
    }
    
    /*pkg*/ void doHook() {
        
        this.logger.info("Hooking module {}", this.name);
        
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.doHook();
            }
        }
        
        this.hook();
    }
    
    /*pkg*/ void doInit() {
        
        Module[] modules = this.getModules();
        
        this.logger.info("Initializing module {}", this.name);
        
        boolean initialized = false;
        
        try {
            this.initialize();
            initialized = true;
        } catch (Exception e) {
            this.logger.error("Exception occurred while initializing module " + this.name, e);
            e.printStackTrace();
        }
        
        if (initialized) {
            if (modules != null) {
                for (Module child : this.getModules()) {
                    child.doInit();
                }
            }
        }
    }
    
    // TODO: only install if required
    /*pkg*/ void doInstall() {
        logger.info("Installing " + this.getDisplayname());
        boolean installed = false;

        try {
            this.install();
            installed = true;
        } catch (Throwable th) {
            this.logger.error("Exception occurred while installing module " + this.name, th);
            th.printStackTrace();
        }

        // TODO: persist meta information

        if (installed) {
            Module[] modules = this.getModules();
            if (modules != null) {
                for (Module child : this.getModules()) {
                    child.doInstall();
                }
            }
        }

        logger.info("Installation of " + this.getDisplayname() + " complete");
    }
    
    /*pkg*/ void doUnHook() {
        this.logger.info("Unhooking module {}", this.name);
        Module[] modules = this.getModules();
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.doUnHook();
            }
        }
        
        this.unhook();
    }
    
    /*pkg*/ void doUninit() {
        
        this.logger.info("Unintializing module {}", this.name);
        
        Module[] modules = this.getModules();
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.doUninit();
            }
        }
        
        this.uninitialize();
    }

    protected void doWriteStatus(StringBuilder sb) {
        // If children have additional info to write out
    }

    public Configuration getConfiguration() {
        // TBD: return default config if nothing is set.
        return this.configuration;
    }

    protected final Logger getLogger() {
        return this.logger;
    }
    
    private Module getModule(Module module, String id) {
        if (module.name.equals(id)) {
            return module;
        } else {
            Module[] children = module.getModules();
            if (children != null) {
                Module mod = null;
                for (Module m : children) {
                    mod = getModule(m, id);
                    if (mod != null) {
                        break;
                    }
                }
                
                return mod;
            }
        }
        
        return null;
    }
    
    protected Module getModule(String id) {
        Module root = this.getRoot();
        return this.getModule(root, id);
    }
    
    protected <T extends Module> Collection<T> getChildrenModules() {
        Collection<T> modules = Lists.newArrayList();
        
        for (Module mod : this.getModules()) {
            T module = (T) mod;
            modules.add(module);
        }
        
        return modules;
    }
    
    public Module[] getModules() {
        return this.modules.toArray(new Module[this.modules.size()]);
    }
    
    @Override
    public final String getDisplayname() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public InstallableMetaData getInstallableMetaData() {
        return InstallableMetaDataHelper.createServerMetaProperties(this.getId(), this.getConfiguration(), this.getClass().getCanonicalName());
    }

    @Override
    public final String getNavigationElementToken() {
        return this.name.toLowerCase().replace(" ", "");
    }
    
    @Override
    public Response getOutput(Request request) {
        String output = "";
        
        if (request.isCommandRequest()) {
            String commandName = request.getCommandName();
            
            if (this.commands.contains(commandName)) {
                Command command = new Command(commandName, request.getArgs());
                CommandResult result = this.process(command);
                if (!result.isSuccessful()) {
                    String outputMessage = "Command failed due to: " + result.getMessage();
                    logger.debug(outputMessage);
                    output = outputMessage;
                }
            } else {
                String outputMessage = "No command found for: " +request.getCommandName();
                logger.debug(outputMessage);
                output = outputMessage;
            }
            
        } else if (request.isListCommandRequest()) {
            StringBuilder sb = new StringBuilder();
            
            if (this.hasSchema()) {
                sb.append(Command.EDIT);
                sb.append('\n');
                this.writeProperties(sb, this.schema);
                sb.append('\n');
            }
            
            output = sb.toString();
        } else if (request.isStatusRequest()) {
            StringBuilder sb = new StringBuilder();
            this.writeStatus(sb);
            output = sb.toString();
        }
        
        return new Response(output);
    }
    
    public Module getParent() {
        return this.parent;
    }
    
    public final Module getRoot() {
        Module node = this;
        
        while (node.parent != null) {
            node = node.parent;
        }
        
        return node;
    }

    @Override
    public final ConfigurationSchema getSchema() {
        return this.schema;
    }
    
    protected final boolean hasSchema() {
        return this.getSchema() != null;
    }
    
    /**
     * This gives a module a time to collaborate with other modules before
     * initialization occurs.  Collaboration may include registrations, etc.
     */
    protected void hook() {
        // nothing by default
    }
    
    public abstract void initialize();

    public void install() {
        
    }

    @Override
    public CommandResult process(Command command) {
        return CommandResult.SUCCESS;
    }
    
    /*pkg*/ void setParents () {
        Module[] modules = this.getModules();
        
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.setParent(this);
            }
        }
    }
    
    /*pkg*/ void setParent(Module parent) {
        this.parent = parent;
    }

    protected void unhook() {
        // nothing by default
    }
    
    public abstract void uninitialize();

    public void uninstall() {
        logger.info("Uninstalling " + this.getDisplayname());
        
        // TODO: remove meta information
        
        logger.info("Uninstallation of " + this.getDisplayname() + " complete");
    }
    
    // This is silly direct-to-string writing - this needs to be extracted else
    // where.
    private void writeStatus (StringBuilder sb) {
        
        sb.append("== Status for [").append(this.getDisplayname()).append("] ==").append('\n');
        
        if (this.hasSchema()) {
            Configuration config = getConfiguration();

            Map<String, String> configValues = config.getValues();
            
            for (Map.Entry<String, String> configEntry : configValues.entrySet()) {
                sb.append("  # ").append(configEntry.getKey()).append(": ").append(configEntry.getValue()).append('\n');
            }
        }
        
        this.doWriteStatus(sb);
    }
    
    protected void writeProperties (StringBuilder sb, ConfigurationSchema schema) {
        this.schemaRender.render(sb, schema.getElement());
    }
}
