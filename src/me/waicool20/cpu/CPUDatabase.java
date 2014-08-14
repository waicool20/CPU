package me.waicool20.cpu;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPU.Types.Type;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.LivingEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class CPUDatabase {

    public static CopyOnWriteArrayList<CPU> CPUDatabaseMap = new CopyOnWriteArrayList<CPU>();
    public static ArrayList<LivingEntity> NTBats = new ArrayList<LivingEntity>();

    private static final Path configPath = Paths.get(CPUPlugin.plugin.getDataFolder().toPath() + "/CPUDatabase.yml");
    private static final Charset charset = Charset.forName("UTF-8");

    public static void saveDefaults() {
        if (Files.notExists(configPath)) {
            try {
                Files.createFile(configPath);
            } catch (IOException e) {
                CPUPlugin.logger.severe("Could not create " + CPUPlugin.plugin.getDataFolder().toPath() + "/CPUDatabase.yml");
            }
        }
    }

    public static void save() {
        try {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(configPath, charset, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            for (CPU cpu : CPUDatabaseMap) {
                String cpuLocation = getCPUInfo(cpu);
                bufferedWriter.append(cpuLocation);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            CPUPlugin.logger.severe("Could not write to " + CPUPlugin.plugin.getDataFolder().toPath() + "/CPUDatabase.yml");
            CPUPlugin.logger.severe("Please check " + CPUPlugin.plugin.getDataFolder().toPath() + "/CPUDatabase.yml");
        }
    }

    public static void addCPU(CPU cpu) {
        CPUDatabaseMap.add(cpu);
        if (CPUPlugin.plugin.getConfig().getBoolean("guardians")) cpu.spawnNTBat();
        NTBats.add(cpu.getNTBat());
        save();
    }

    public static void removeCPU(CPU cpu) {
        CPUDatabaseMap.remove(cpu);
        NTBats.remove(cpu.getNTBat());
        cpu.removeNTBat();
        save();
    }

    public static void loadCPUs() {
        try {
            saveDefaults();
            BufferedReader bufferedReader = Files.newBufferedReader(configPath, charset);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] cpuInfo = line.split(";");
                if (cpuInfo.length < 5) return;
                // Load owner
                String owner = cpuInfo[0];

                // Load World
                World world = Bukkit.getServer().getWorld(cpuInfo[1]);
                if (world == null) {
                    Path worldFolder = Paths.get(Bukkit.getServer().getWorldContainer().toPath() + "/" + cpuInfo[1]);
                    if (Files.exists(worldFolder)) {
                        Bukkit.getServer().createWorld(new WorldCreator(cpuInfo[1]));
                        world = Bukkit.getServer().getWorld(cpuInfo[1]);
                    } else {
                        CPUPlugin.logger.severe("[CPU] Could not load the world " + cpuInfo[1] + " | Is it missing?");
                    }
                }
                // Load XYZ
                int x = Integer.parseInt(cpuInfo[2]);
                int y = Integer.parseInt(cpuInfo[3]);
                int z = Integer.parseInt(cpuInfo[4]);

                // Load typified, Wireless ID and Type
                boolean typified = false;
                String WirelessID = null;
                String CPUType = null;
                if (cpuInfo.length > 5) {
                    typified = Boolean.parseBoolean(cpuInfo[5]);
                }
                if(cpuInfo.length > 6){
                    if (!cpuInfo[6].equals("null")) WirelessID = cpuInfo[6];
                }
                if(cpuInfo.length > 7){
                    if (!cpuInfo[7].equals("null")) CPUType = cpuInfo[7];
                }


                CPU loadedCPU = new CPU(owner, world, x, y, z);

                if (WirelessID != null) {
                    loadedCPU.getAttributes().setWirelessID(WirelessID);
                } else {
                    loadedCPU.getAttributes().setWirelessID("0");
                }

                if (CPUType != null) {
                    for (Type types : Type.getTypes(loadedCPU)) {
                        if (CPUType.equals(types.getName())) {
                            loadedCPU.setType(types);
                        }
                    }
                } else {
                    removeCPU(loadedCPU);
                    continue;
                }

                loadedCPU.setTypified(typified);

                if (loadedCPU.getCore() == null || loadedCPU.getInput1() == null || loadedCPU.getInput2() == null || loadedCPU.getType() == null) {
                    removeCPU(loadedCPU);
                    continue;
                }
                CPUDatabaseMap.add(loadedCPU);
            }
            if (CPUPlugin.plugin.getConfig().getBoolean("guardians")) spawnNTBats();
        } catch (IOException e) {
            CPUPlugin.logger.severe("Could not read " + CPUPlugin.plugin.getDataFolder().toPath() + "/CPUDatabase.yml Reason: " + e + " | Files is missing?");
        }
        CPUPlugin.logger.info("[CPU] Loaded " + CPUDatabaseMap.size() + " CPUs!");
    }

    private static String getCPUInfo(CPU cpu) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : cpu.toStorageFormat()) {
            stringBuilder.append(string);
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    private static void spawnNTBats() {
        CPUPlugin.bukkitScheduler.scheduleSyncDelayedTask(CPUPlugin.plugin, new Runnable() {
            @Override
            public void run() {
                for (CPU cpu : CPUDatabaseMap) {
                    cpu.spawnNTBat();
                    NTBats.add(cpu.getNTBat());
                }
            }
        }, 20);
    }
}
