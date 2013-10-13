package me.waicool20.cpu.CPUModule;

import me.waicool20.cpu.CPU;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Output {
    private CPUModule cpuModule;
    private Block block;

    public Output(CPUModule cpuModule,Block block) {
        this.cpuModule = cpuModule;
        this.block = block;
    }

    public void setPower(final boolean powered,int delay){
        CPU.bukkitScheduler.scheduleSyncDelayedTask(CPU.plugin, new BukkitRunnable() {
            @Override
            public void run() {
                if(powered){
                    block.setType(Material.REDSTONE_BLOCK);
                }else {
                    block.setType(Material.AIR);
                }
            }
        },0);
    }

    public Block getBlock() {
        return block;
    }

    public boolean getPower(){
        return block.getType() == Material.REDSTONE_BLOCK;
    }
}
