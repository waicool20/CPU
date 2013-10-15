package me.waicool20.cpu.CPU;

import me.waicool20.cpu.CPUPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Output {
    private final Block block;

    public Output(Block block) {
        this.block = block;
    }

    public void setPower(final boolean powered, int delay) {
        CPUPlugin.bukkitScheduler.scheduleSyncDelayedTask(CPUPlugin.plugin, new BukkitRunnable() {
            @Override
            public void run() {
                if (powered) {
                    block.setType(Material.REDSTONE_BLOCK);
                } else {
                    block.setType(Material.AIR);
                }
            }
        }, 0);
    }

    public Block getBlock() {
        return block;
    }

    public boolean getPower() {
        return block.getType() == Material.REDSTONE_BLOCK;
    }
}
