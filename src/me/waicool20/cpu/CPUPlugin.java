package me.waicool20.cpu;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class CPUPlugin extends JavaPlugin {
    public static JavaPlugin plugin;
    public static PluginDescriptionFile pdfFile;
    public static BukkitScheduler bukkitScheduler;
    public static Logger logger;
    public static File file;

    @Override
    public void onEnable() {
        initVariables();
        registerListeners();
        registerCommands();
        setupConfig();
        CleanupInventories();
        CraftingAndRecipes.addRecipes();
        checkForUpdates();
        startUpdates();
        startMetrics();
        CustomEntityType.registerEntities();
        //addNTBat();
    }

    @Override
    public void onDisable() {
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            cpu.removeNTBat();
        }
        CPUDatabase.CPUDatabaseMap.clear();
        CustomEntityType.unregisterEntities();
    }

    private void CleanupInventories() {
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            cpu.getCore().getInventory().setContents(cpu.getType().typeInventory());
        }
    }

    private void checkForUpdates() {
        if (CPUPlugin.plugin.getConfig().getBoolean("notify-updates")) {
            Updater updater = new Updater(CPUPlugin.plugin, 66380, CPUPlugin.file, Updater.UpdateType.NO_DOWNLOAD, true);
            if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
                logger.info("[CPU] New update available: " + updater.getLatestName() + "!");
                logger.info("Go get it at " + updater.getLatestFileLink());
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
        Bukkit.getServer().getPluginManager().registerEvents(new ItemSorterEvent(), this);
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
        file = this.getFile();
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
                for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                    if (!CPUPlugin.plugin.getConfig().getBoolean("disabled")) cpu.getType().updatePower();
                    if (plugin.getConfig().getBoolean("guardians")) cpu.updateSpawnBat();
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

            Method a = net.minecraft.server.v1_7_R3.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);

            a.invoke(null, NameTagBat.class, "Bat", 65);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
