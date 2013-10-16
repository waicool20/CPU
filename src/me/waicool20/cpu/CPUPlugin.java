package me.waicool20.cpu;

import me.waicool20.cpu.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public class CPUPlugin extends JavaPlugin {
    public static JavaPlugin plugin;
    public static PluginDescriptionFile pdfFile;
    public static BukkitScheduler bukkitScheduler;
    public static Logger logger;

    @Override
    public void onEnable() {
        initVariables();
        registerListeners();
        registerCommands();
        setupConfig();
        CraftingAndRecipes.addRecipes();
        checkForUpdates();
    }

    private void checkForUpdates() {
        if (CPUPlugin.plugin.getConfig().getBoolean("notify-updates")) {
            if (UpdateChecker.getInstance().NewUpdateAvailable()) {
                CPUPlugin.logger.info("[CPU] New update available: " + UpdateChecker.getInstance().getLatestVersion() + "! Go get it at " + UpdateChecker.getInstance().getDlLink());
            }
        }
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new CreateCPUListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RedstoneUpdatesListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftingAndRecipes(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CPUBreakListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EatsRedstoneApple(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TypifierClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }

    private void registerCommands() {
        this.getCommand("cpu").setExecutor(new Commands());
        this.getCommand("typifier").setExecutor(new Commands());
    }

    private void initVariables() {
        plugin = this;
        pdfFile = this.getDescription();
        bukkitScheduler = Bukkit.getServer().getScheduler();
        logger = Bukkit.getServer().getLogger();
    }

    private void setupConfig() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.getConfig().set("Disabled", null);
        this.saveConfig();
        CPUDatabase.saveDefaults();
        CPUDatabase.loadCPUs();
    }
}
