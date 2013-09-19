package ChatManager.Commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

public class CommandController implements CommandExecutor{
	
	/**
	 * @author AmoebaMan
	 * 
	 * All credits go to AmoebaMan for this handling of Commands via Annotations; It's an easier implementation, and allows a skip of the verifications
	 * that normally come with something like this
	 */
    private final static HashMap<Command, Object> handlers = new HashMap<Command, Object>();
    private final static HashMap<Command, Method> methods = new HashMap<Command, Method>();
    private final static HashMap<String, SubCommand> subCommands = new HashMap<String, SubCommand>();
    private final static HashMap<String, Object> subHandlers = new HashMap<String, Object>();
    private final static HashMap<String, Method> subMethods = new HashMap<String, Method>();

    /**
     * Registers all command handlers and subcommand handlers in a class, matching them with their corresponding commands and subcommands registered to the specified plugin.
     * @param plugin The plugin whose commands will be considered for registration
     * @param handler An instance of the class whose methods will be considered for registration
     */
    public static void registerCommands(JavaPlugin plugin, Object handler){

        for(Method method : handler.getClass().getMethods()){
            Class<?>[] params = method.getParameterTypes();
            if(params.length == 2 && CommandSender.class.isAssignableFrom(params[0]) && String[].class.equals(params[1])){

                if(isCommandHandler(method)){
                    CommandHandler annotation = method.getAnnotation(CommandHandler.class);
                    if(plugin.getCommand(annotation.name()) != null){
                        plugin.getCommand(annotation.name()).setExecutor(new CommandController());
                        if(!(annotation.aliases().equals(new String[]{""})))
                            plugin.getCommand(annotation.name()).setAliases(Lists.newArrayList(annotation.aliases()));
                        if(!annotation.description().equals(""))
                            plugin.getCommand(annotation.name()).setDescription(annotation.description());
                        if(!annotation.usage().equals(""))
                            plugin.getCommand(annotation.name()).setUsage(annotation.usage());
                        if(!annotation.permission().equals(""))
                            plugin.getCommand(annotation.name()).setPermission(annotation.permission());
                        if(!annotation.permissionMessage().equals(""))
                            plugin.getCommand(annotation.name()).setPermissionMessage(ChatColor.RED + annotation.permissionMessage());
                        handlers.put(plugin.getCommand(annotation.name()), handler);
                        methods.put(plugin.getCommand(annotation.name()), method);
                    }
                }

                if(isSubCommandHandler(method)){
                    SubCommandHandler annotation = method.getAnnotation(SubCommandHandler.class);
                    if(plugin.getCommand(annotation.parent()) != null){
                        plugin.getCommand(annotation.parent()).setExecutor(new CommandController());
                        SubCommand subCommand = new SubCommand(plugin.getCommand(annotation.parent()), annotation.name());
                        subCommand.permission = annotation.permission();
                        subCommand.permissionMessage = annotation.permissionMessage();
                        subCommands.put(subCommand.toString(), subCommand);
                        subHandlers.put(subCommand.toString(), handler);
                        subMethods.put(subCommand.toString(), method);
                    }
                }
            }
        }
    }

    /**
     * @author AmoebaMan
     * An annotation interface that may be attached to a method to designate it as a command handler.
     * When registering a handler with this class, only methods marked with this annotation will be considered for command registration.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface CommandHandler {
        String name();
        String[] aliases() default { "" };
        String description() default "";
        String usage() default "";
        String permission() default "";
        String permissionMessage() default "[Vaecon Network] You do not have permission for this command, if you believe this is an error please fill out a bug report on our forums.";
    }

    /**
     * Tests if a method is a command handler
     */
    private static boolean isCommandHandler(Method method){
        return method.getAnnotation(CommandHandler.class) != null;
    }

    /**
     * @author AmoebaMan
     * An annotation interface that may be attached to a method to designate it as a subcommand handler.
     * When registering a handler with this class, only methods marked with this annotation will be considered for subcommand registration.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface SubCommandHandler {
        String parent();
        String name();
        String permission() default "";
        String permissionMessage() default "[Vaecon Network] You do not have permission for this command, if you believe this is an error please fill out a bug report on our forums.";
    }

    /**
     * Tests if a method is a subcommand handler
     */
    private static boolean isSubCommandHandler(Method method){
        return method.getAnnotation(SubCommandHandler.class) != null;
    }

    /**
     * A class for representing subcommands
     */
    private static class SubCommand{
        public final Command superCommand;
        public final String subCommand;
        public String permission;
        public String permissionMessage;
        public SubCommand(Command superCommand, String subCommand){
            this.superCommand = superCommand;
            this.subCommand = subCommand.toLowerCase();
        }
        public boolean equals(Object x){ return toString().equals(x.toString()); }
        public String toString(){ return (superCommand.getName() + " " + subCommand).trim(); }
    }

    /**
     * This is the method that "officially" processes commands, but in reality it will always delegate responsibility to the handlers and methods assigned to the command or subcommand
     * Beyond checking permissions, checking player/console sending, and invoking handlers and methods, this method does not actually act on the commands
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        /*
         * If a subcommand may be present...
         */
        if(args.length > 0){
            /*
             * Get the subcommand given and the handler and method attached to it
             */
            SubCommand subCommand = new SubCommand(command, args[0]);
            subCommand = subCommands.get(subCommand.toString());
            /*
             * If and only if the subcommand actually exists...
             */
            if(subCommand != null){
                Object subHandler = subHandlers.get(subCommand.toString());
                Method subMethod = subMethods.get(subCommand.toString());
                /*
                 * If and only if both handler and method exist...
                 */
                if(subHandler != null && subMethod != null){
                    /*
                     * Reorder the arguments so we don't resend the subcommand
                     */
                    String[] subArgs = new String[args.length - 1];
                    for(int i = 1; i < args.length; i++)
                        subArgs[i - 1] = args[i];
                    /*
                     * If the method requires a player and the subcommand wasn't sent by one, don't continue
                     */
                    if(subMethod.getParameterTypes()[0].equals(Player.class) && !(sender instanceof Player)){
                        sender.sendMessage(ChatColor.RED + "This command requires a player sender");
                        return true;
                    }
                    /*
                     * If the method requires a console and the subcommand wasn't sent by one, don't continue
                     */
                    if(subMethod.getParameterTypes()[0].equals(ConsoleCommandSender.class) && !(sender instanceof ConsoleCommandSender)){
                        sender.sendMessage(ChatColor.RED + "This command requires a console sender");
                        return true;
                    }
                    /*
                     * If a permission is attached to this subcommand and the sender doens't have it, don't continue
                     */
                    if(!subCommand.permission.isEmpty() && !sender.hasPermission(subCommand.permission)){
                        sender.sendMessage(ChatColor.RED + subCommand.permissionMessage);
                        return true;
                    }
                    /*
                     * Try to process the command
                     */
                    try{ subMethod.invoke(subHandler, sender, args); }
                    catch(Exception e){
                        sender.sendMessage(ChatColor.RED + "[Vaecon Network] There was an error while processing this command; Please check the syntax used, and if it persists fill out a bug report on our forums.");
                        e.printStackTrace(); 
                    }
                    return true;
                }
            }
        }
        /*
         * If a subcommand was successfully executed, the command will not reach this point
         * Get the handler and method attached to this command
         */
        Object handler = handlers.get(command);
        Method method = methods.get(command);
        /*
         * If and only if both handler and method exist...
         */
        if(handler != null && method != null){
            /*
             * If the method requires a player and the command wasn't sent by one, don't continue
             */
            if(method.getParameterTypes()[0].equals(Player.class) && !(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "This command requires a player sender");
                return true;
            }
            /*
             * If the method requires a console and the command wasn't sent by one, don't continue
             */
            if(method.getParameterTypes()[0].equals(ConsoleCommandSender.class) && !(sender instanceof ConsoleCommandSender)){
                sender.sendMessage(ChatColor.RED + "This command requires a console sender");
                return true;
            }
            /*
             * Try to process the command
             */
            try{ method.invoke(handler, sender, args); }
            catch(Exception e){
                sender.sendMessage(ChatColor.RED + "[Vaecon Network] There was an error while processing this command; Please check the syntax used, and if it persists fill out a bug report on our forums.");
                e.printStackTrace(); 
            }
        }
        /*
         * Otherwise we have to fake not recognising the command
         */
        else
            sender.sendMessage("Unknown command. Type \"help\" for help.");

        return true;
    }

}