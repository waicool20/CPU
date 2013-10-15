package me.waicool20.cpu;

import me.waicool20.cpu.CPUModule.CPUModule;
import org.bukkit.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleDatabase {

    public static CopyOnWriteArrayList<CPUModule> ModuleDatabaseMap = new CopyOnWriteArrayList<CPUModule>();

    private static final Path configPath = Paths.get(CPU.plugin.getDataFolder().toPath() + "/ModuleDatabase.yml");
    private static final Charset charset = Charset.forName("UTF-8");

    public static void saveDefaults(){
        if(Files.notExists(configPath)){
            try{
                Files.createFile(configPath);
            } catch (IOException e) {
                CPU.logger.severe("Could not create " + CPU.plugin.getDataFolder().toPath() + "/ModuleDatabase.yml");
            }
        }
    }

    private static void save(){
        try{
            BufferedWriter bufferedWriter = Files.newBufferedWriter(configPath,charset,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
            for(CPUModule cpuModule : ModuleDatabaseMap){
                int typified = cpuModule.isTypified() ? 1:0;
                String cpuModuleLocation = cpuModule.getAttributes().getOwner()+";" +cpuModule.getID().getWorld().getName()+";" +cpuModule.getID().getBlockX()+";" +cpuModule.getID().getBlockY()+";" +cpuModule.getID().getBlockZ()+";" +typified+";";
                bufferedWriter.append(cpuModuleLocation);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            CPU.logger.severe("Could not write to " + CPU.plugin.getDataFolder().toPath() + "/ModuleDatabase.yml");
            CPU.logger.severe("Please check " + CPU.plugin.getDataFolder().toPath() + "/ModuleDatabase.yml");
        }
    }

    public static void addModule(CPUModule cpuModule){
        ModuleDatabaseMap.add(cpuModule);
        save();
    }

    public static void removeModule(CPUModule cpuModule){
        ModuleDatabaseMap.remove(cpuModule);
        save();
    }

    public static void loadModules(){
        try{
            saveDefaults();
            BufferedReader bufferedReader = Files.newBufferedReader(configPath,charset);
            String line;
            while((line = bufferedReader.readLine()) != null){
                String[] cpuModuleInfo = line.split(";");
                String owner = cpuModuleInfo[0];
                World world = getWorld(cpuModuleInfo[1]);
                if(world == null){
                    Path worldFolder = Paths.get(Bukkit.getServer().getWorldContainer().toPath() + "/" + cpuModuleInfo[1]);
                    if(Files.exists(worldFolder)){
                        Bukkit.getServer().createWorld(new WorldCreator(cpuModuleInfo[1]));
                        world = getWorld(cpuModuleInfo[1]);
                    }else{
                        CPU.logger.severe("[CPU] Could not load the world " + cpuModuleInfo[1] + " | Is it missing?");
                    }
                }
                int x = Integer.parseInt(cpuModuleInfo[2]);
                int y = Integer.parseInt(cpuModuleInfo[3]);
                int z = Integer.parseInt(cpuModuleInfo[4]);
                boolean typified = false;
                if(cpuModuleInfo.length > 5){
                    typified = (Integer.parseInt(cpuModuleInfo[5]) != 0);
                }

                CPUModule newCpuModule = new CPUModule(owner,world,x,y,z);

                newCpuModule.setTypified(typified);

                if(newCpuModule.getCore() == null || newCpuModule.getInput1() == null || newCpuModule.getInput2() == null || newCpuModule.getType() == null){
                    removeModule(newCpuModule);
                    continue;
                }
                ModuleDatabaseMap.add(newCpuModule);
            }
        } catch (IOException e) {
            CPU.logger.severe("Could not read " + CPU.plugin.getDataFolder().toPath() + "/ModuleDatabase.yml Reason: " + e + " | Files is missing?");
        }
    }

    private static World getWorld(String string){
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getName().equalsIgnoreCase(string.trim())) {
                return world;
            }
        }
        return null;
    }
}
