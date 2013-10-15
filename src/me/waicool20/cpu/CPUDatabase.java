package me.waicool20.cpu;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CopyOnWriteArrayList;

public class CPUDatabase {

    public static CopyOnWriteArrayList<CPU> CPUDatabaseMap = new CopyOnWriteArrayList<CPU>();

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

    private static void save() {
        try {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(configPath, charset, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            for (CPU cpu : CPUDatabaseMap) {
                int typified = cpu.isTypified() ? 1 : 0;
                String cpuModuleLocation = cpu.getAttributes().getOwner() + ";" + cpu.getID().getWorld().getName() + ";" + cpu.getID().getBlockX() + ";" + cpu.getID().getBlockY() + ";" + cpu.getID().getBlockZ() + ";" + typified + ";";
                bufferedWriter.append(cpuModuleLocation);
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
        save();
    }

    public static void removeCPU(CPU cpu) {
        CPUDatabaseMap.remove(cpu);
        save();
    }

    public static void loadCPUs() {
        try {
            saveDefaults();
            BufferedReader bufferedReader = Files.newBufferedReader(configPath, charset);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] cpuModuleInfo = line.split(";");
                String owner = cpuModuleInfo[0];
                World world = getWorld(cpuModuleInfo[1]);
                if (world == null) {
                    Path worldFolder = Paths.get(Bukkit.getServer().getWorldContainer().toPath() + "/" + cpuModuleInfo[1]);
                    if (Files.exists(worldFolder)) {
                        Bukkit.getServer().createWorld(new WorldCreator(cpuModuleInfo[1]));
                        world = getWorld(cpuModuleInfo[1]);
                    } else {
                        CPUPlugin.logger.severe("[CPU] Could not load the world " + cpuModuleInfo[1] + " | Is it missing?");
                    }
                }
                int x = Integer.parseInt(cpuModuleInfo[2]);
                int y = Integer.parseInt(cpuModuleInfo[3]);
                int z = Integer.parseInt(cpuModuleInfo[4]);
                boolean typified = false;
                if (cpuModuleInfo.length > 5) {
                    typified = (Integer.parseInt(cpuModuleInfo[5]) != 0);
                }

                CPU newCpu = new CPU(owner, world, x, y, z);

                newCpu.setTypified(typified);

                if (newCpu.getCore() == null || newCpu.getInput1() == null || newCpu.getInput2() == null || newCpu.getType() == null) {
                    removeCPU(newCpu);
                    continue;
                }
                CPUDatabaseMap.add(newCpu);
            }
        } catch (IOException e) {
            CPUPlugin.logger.severe("Could not read " + CPUPlugin.plugin.getDataFolder().toPath() + "/CPUDatabase.yml Reason: " + e + " | Files is missing?");
        }
        CPUPlugin.logger.info("[CPU] Loaded " + CPUDatabaseMap.size() + " CPUs!");
    }

    private static World getWorld(String string) {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getName().equalsIgnoreCase(string.trim())) {
                return world;
            }
        }
        return null;
    }
}
