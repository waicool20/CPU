package me.waicool20.cpu;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.lang.reflect.Method;
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
        startUpdates();
        startMetrics();
        addNTBat();
    }

    @Override
    public void onDisable() {
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            cpu.removeNTBat();
        }
        CPUDatabase.CPUDatabaseMap.clear();
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
        Bukkit.getServer().getPluginManager().registerEvents(new CraftingAndRecipes(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CPUChangeListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EatsRedstoneApple(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TypifierClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
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

    private void startUpdates() {
        bukkitScheduler.scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                if (CPUPlugin.plugin.getConfig().getBoolean("disabled")) {
                    return;
                }
                for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                    cpu.getType().updatePower();
                    cpu.updateSpawnBat();
                }
            }
        }, 0, 2);
    }

    private void startMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
            logger.info("[CPU] Metrics started!");
        } catch (IOException e) {
            logger.severe("[CPU] Could not send metrics!");
        }
    }

    private void addNTBat() {
        try {
            @SuppressWarnings("rawtypes")
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;

            Method a = net.minecraft.server.v1_6_R3.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);

            a.invoke(a, NameTagBat.class, "Bat", 65);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
