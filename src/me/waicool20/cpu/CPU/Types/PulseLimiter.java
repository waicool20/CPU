package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PulseLimiter extends Type {
    boolean state = false;

    public PulseLimiter(CPU cpu) {
        CPU = cpu;
        setName("PulseLimiter");
    }


    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, redR, null, null, null, null,
                redW, redW, redR, redR, GOLD, redR, redR, redW, redW,
                redW, null, null, null, NPIS, null, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (CPU.getInput1().isPowered() || CPU.getInput2().isPowered()) {
            if (state) return;
            state = true;
            if (CPUPlugin.bukkitScheduler.isQueued(on)) return;
            CPU.getOutput().setPower(true);
            on = CPUPlugin.bukkitScheduler.scheduleSyncDelayedTask(CPUPlugin.plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    CPU.getOutput().setPower(false);
                }
            }, CPU.getDelay());
        } else {
            CPUPlugin.bukkitScheduler.cancelTask(on);
            CPU.getOutput().setPower(false);
            state = false;
        }
    }
}
