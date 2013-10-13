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
            } else if(subCommand.equalsIgnoreCase("info")){
                return info(sender, cmd, label, args);
            } else if(subCommand.equalsIgnoreCase("disable")){
                return disable(sender, cmd, label, args);
            } else if(subCommand.equalsIgnoreCase("enable")){
                return enable(sender, cmd, label, args);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid Command!");
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

    private boolean info(CommandSender sender,Command cmd,String label,String[] args){
        if(sender.hasPermission("cpu.toggleinfo") || sender.hasPermission("cpu.*")){
            if(args.length < 2){
                sender.sendMessage(ChatColor.RED + "Too little arguments! Usage:/cpu info [on/off]");
                return true;
            }
            if(args.length > 2){
                sender.sendMessage(ChatColor.RED + "Too many arguments! Usage:/cpu info [on/off]");
                return true;
            }
            if(args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("on")){
                if(args[1].equalsIgnoreCase("off")){
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
            }else{
                sender.sendMessage(ChatColor.RED + "Invalid argument! Usage:/cpu info [on/off]");
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
