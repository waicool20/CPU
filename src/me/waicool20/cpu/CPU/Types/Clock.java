package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPUPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Clock extends Type {
    private boolean powered = false;

    public Clock(me.waicool20.cpu.CPU.CPU cpu) {
        CPU = cpu;
        setName("Clock");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {   null, null, null, redR, GOLD, redR, null, null, null,
                                        redW, redW, redW, redR, CLCK, redR, redW, redW, redW,
                                        redW, null, null, redR, redR, redR, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if(CPU.getInput1().isPowered() || CPU.getInput2().isPowered()){
            if(CPUPlugin.bukkitScheduler.isQueued(on)) return;
            on = CPUPlugin.bukkitScheduler.scheduleSyncRepeatingTask(CPUPlugin.plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    if(powered){
                        powered = false;
                        CPU.getOutput().setPower(false);
                    } else {
                        powered = true;
                        CPU.getOutput().setPower(true);
                    }
                }
            },0,CPU.getDelay());
        } else {
            if(CPUPlugin.bukkitScheduler.isQueued(on)) CPUPlugin.bukkitScheduler.cancelTask(on);
            CPU.getOutput().setPower(false);
            powered = false;
        }
    }
}
