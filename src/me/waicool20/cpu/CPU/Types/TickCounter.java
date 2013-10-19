package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPUPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TickCounter {
    private int task;
    private int ticks = 0;

    public void startCounting() {
        task = CPUPlugin.bukkitScheduler.scheduleSyncRepeatingTask(CPUPlugin.plugin, new BukkitRunnable() {
            @Override
            public void run() {
                ticks++;
            }
        }, 0, 1);
    }

    public boolean isRunning() {
        return CPUPlugin.bukkitScheduler.isCurrentlyRunning(task);
    }

    public void stopCounting() {
        CPUPlugin.bukkitScheduler.cancelTask(task);
    }

    public int getCountedTicks() {
        return ticks;
    }

    public void reset() {
        ticks = 0;
    }
}
