package me.waicool20.cpu;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;

class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("cpu")) {
            if (args.length == 0) {
                if (sender.hasPermission("cpu.command.status")) {
                    sendPluginInfo(sender);
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
                sender.sendMessage(ChatColor.RED + "[CPU] \"/cpu help\" for help!");
                return true;
            }
            String subCommand = args[0];
            if (subCommand.equalsIgnoreCase("reload")) {
                return reload(sender, cmd, label, args);
            } else if (subCommand.equalsIgnoreCase("toggleinfo")) {
                return toggleinfo(sender, cmd, label, args);
            } else if (subCommand.equalsIgnoreCase("disable")) {
                return disable(sender, cmd, label, args);
            } else if (subCommand.equalsIgnoreCase("enable")) {
                return enable(sender, cmd, label, args);
            } else if (subCommand.equalsIgnoreCase("help")) {
                return sendHelp(sender, cmd, label, args);
            } else if (subCommand.equalsIgnoreCase("gettp")) {
                return getTP(sender, cmd, label, args);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid Command! Use \"/cpu help\" for more help!");
            }
        } else if (label.equalsIgnoreCase("typifier")) {
            if (args.length != 0) {
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
            }
            if (sender instanceof Player) {
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
        sender.sendMessage(ChatColor.GOLD + "/cpu gettp" + ChatColor.WHITE + ": Get a Destination book with your current location");
        return true;
    }

    private boolean reload(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("cpu.command.reload")) {
            CPUPlugin.plugin.reloadConfig();
            CPUDatabase.CPUDatabaseMap.clear();
            CPUDatabase.loadCPUs();
            sender.sendMessage(ChatColor.GREEN + "CPU has been reloaded!!!");
            sender.sendMessage(ChatColor.GREEN + "Active CPUs : " + CPUDatabase.CPUDatabaseMap.size());
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return true;
    }

    private boolean toggleinfo(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("cpu.command.toggleinfo")) {
            if (CPUPlugin.plugin.getConfig().getBoolean("send-info")) {
                CPUPlugin.plugin.getConfig().set("send-info", false);
                CPUPlugin.plugin.saveConfig();
                sender.sendMessage("CPU info will " + ChatColor.GREEN + "NOT BE" + ChatColor.WHITE + " shown when you create a CPU for the first time!");
                return true;
            } else {
                CPUPlugin.plugin.getConfig().set("send-info", true);
                CPUPlugin.plugin.saveConfig();
                sender.sendMessage("CPU info will " + ChatColor.GREEN + "BE" + ChatColor.WHITE + " shown when you create a CPU for the first time!");
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return true;
    }

    private boolean disable(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("cpu.command.disable")) {
            if (CPUPlugin.plugin.getConfig().getBoolean("disabled")) {
                sender.sendMessage(ChatColor.RED + "[CPU] CPU is already disabled!");
                return true;
            }
            CPUPlugin.plugin.getConfig().set("disabled", true);
            CPUPlugin.plugin.saveConfig();
            for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                cpu.getType().disable();
            }
            sender.sendMessage(ChatColor.GREEN + "[CPU] CPU has been disabled!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return true;
    }

    private boolean enable(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("cpu.command.enable")) {
            if (!CPUPlugin.plugin.getConfig().getBoolean("disabled")) {
                sender.sendMessage(ChatColor.RED + "[CPU] CPU is already enabled!");
                return true;
            }
            CPUPlugin.plugin.getConfig().set("disabled", false);
            CPUPlugin.plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "[CPU] CPU has been enabled!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
        return true;
    }

    private boolean getTP(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("cpu.command.gettp")) {
                Player player = (Player) sender;
                Location location = player.getLocation();
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookMeta = (BookMeta) book.getItemMeta();

                bookMeta.setTitle("Destination");
                bookMeta.setPages(Arrays.asList(location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch()));
                book.setItemMeta(bookMeta);
                player.getInventory().addItem(book);
                return true;
            }
            sender.sendMessage(ChatColor.RED + "[CPU] You do not have enough permission to run this command!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "[CPU] Only players can get the teleport coordinates!");
        return true;
    }

    private void sendPluginInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_GREEN + "------ " + ChatColor.GREEN + "CPU Plugin Info" + ChatColor.DARK_GREEN + " ------");
        sender.sendMessage("Version: " + ChatColor.AQUA + CPUPlugin.pdfFile.getVersion());
        sender.sendMessage("Active CPUs: " + ChatColor.AQUA + CPUDatabase.CPUDatabaseMap.size());
        if (CPUPlugin.plugin.getConfig().getBoolean("notify-updates") && UpdateChecker.getInstance().NewUpdateAvailable() && (sender.hasPermission("cpu.notifyupdate"))) {
            sender.sendMessage(ChatColor.GREEN + "New update available: " + ChatColor.AQUA + UpdateChecker.getInstance().getLatestVersion());
            sender.sendMessage(ChatColor.GREEN + "Go get it at: " + ChatColor.AQUA + UpdateChecker.getInstance().getDlLink());
        } else {
            sender.sendMessage(ChatColor.GREEN + "You have the latest version!");
        }
    }
}
