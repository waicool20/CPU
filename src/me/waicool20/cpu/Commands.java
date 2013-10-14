package me.waicool20.cpu;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{

    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
        if(label.equalsIgnoreCase("cpu")){
            if(args.length == 0){
                sendPluginInfo(sender);
                return true;
            }
            String subCommand = args[0];
            if(subCommand.equalsIgnoreCase("reload")){
                return reload(sender, cmd, label, args);
            } else if(subCommand.equalsIgnoreCase("toggleinfo")){
                return toggleinfo(sender, cmd, label, args);
            } else if(subCommand.equalsIgnoreCase("disable")){
                return disable(sender, cmd, label, args);
            } else if(subCommand.equalsIgnoreCase("enable")){
                return enable(sender, cmd, label, args);
            } else if(subCommand.equalsIgnoreCase("help")){
                return sendHelp(sender, cmd, label, args);
            } else{
                sender.sendMessage(ChatColor.RED + "Invalid Command! Use \"/cpu help\" for more help!");
            }
        }else if(label.equalsIgnoreCase("typifier")){
            if(args.length != 0){
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
            }
            if(sender instanceof Player){
                ((Player) sender).getInventory().addItem(CraftingAndRecipes.typifier());
                return true;
            }
            sender.sendMessage(ChatColor.RED + "[CPU] Only players can get a typifier!");
        }
        return false;
    }

    private boolean sendHelp(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + "CPU Commands:" + ChatColor.YELLOW + "----");
        sender.sendMessage(ChatColor.GOLD + "/cpu" + ChatColor.WHITE + ": Shows plugin version and number of activated CPUs");
        sender.sendMessage(ChatColor.GOLD + "/cpu help" + ChatColor.WHITE + ": Shows this help page!");
        sender.sendMessage(ChatColor.GOLD + "/cpu enable" + ChatColor.WHITE + ": Enable CPU redstone updates");
        sender.sendMessage(ChatColor.GOLD + "/cpu disable" + ChatColor.WHITE + ": Disable CPU redstone updates");
        sender.sendMessage(ChatColor.GOLD + "/cpu reload" + ChatColor.WHITE + ": Reload config files");
        sender.sendMessage(ChatColor.GOLD + "/cpu toggleinfo" + ChatColor.WHITE + ": Toggles CPU creation info");
        return true;
    }

    private boolean reload(CommandSender sender,Command cmd,String label,String[] args){
        if(sender.hasPermission("cpu.reload") || sender.hasPermission("cpu.*")){
            CPU.plugin.reloadConfig();
            ModuleDatabase.ModuleDatabaseMap.clear();
            ModuleDatabase.loadModules();
            sender.sendMessage(ChatColor.GREEN + "CPU has been reloaded!!!");
            sender.sendMessage(ChatColor.GREEN + "Active Modules: " + ModuleDatabase.ModuleDatabaseMap.size());
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return false;
    }

    private boolean toggleinfo(CommandSender sender,Command cmd,String label,String[] args){
        if(sender.hasPermission("cpu.toggleinfo") || sender.hasPermission("cpu.*")){
            if(CPU.plugin.getConfig().getBoolean("send-info")){
                CPU.plugin.getConfig().set("send-info", false);
                CPU.plugin.saveConfig();
                sender.sendMessage("Module info will " + ChatColor.GREEN + "NOT BE" + ChatColor.WHITE + " shown when you create a module for the first time!");
                return true;
            } else{
                CPU.plugin.getConfig().set("send-info", true);
                CPU.plugin.saveConfig();
                sender.sendMessage("Module info will " + ChatColor.GREEN + "BE" + ChatColor.WHITE + " shown when you create a module for the first time!");
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return false;
    }

    private boolean disable(CommandSender sender,Command cmd,String label,String[] args){
        if(sender.hasPermission("cpu.disable")|| sender.hasPermission("cpu.*")){
            if(CPU.plugin.getConfig().getBoolean("Disabled")){
                sender.sendMessage(ChatColor.RED + "CPU is already disabled!");
                return true;
            }
            CPU.plugin.getConfig().set("Disabled",true);
            CPU.plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "CPU has been disabled!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return true;
    }

    private boolean enable(CommandSender sender,Command cmd,String label,String[] args){
        if(sender.hasPermission("cpu.disable") || sender.hasPermission("cpu.*")){
            if(!CPU.plugin.getConfig().getBoolean("Disabled")){
                sender.sendMessage(ChatColor.RED + "CPU is already enabled!");
                return true;
            }
            CPU.plugin.getConfig().set("Disabled", false);
            CPU.plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "CPU has been enabled!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return false;
    }

    private void sendPluginInfo(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "------ CPU Plugin Info ------");
        sender.sendMessage(ChatColor.GREEN + "Version: " + CPU.pdfFile.getVersion());
        sender.sendMessage(ChatColor.GREEN + "Active Modules: " + ModuleDatabase.ModuleDatabaseMap.size());
    }
}
