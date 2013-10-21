package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUPlugin;
import org.bukkit.inventory.ItemStack;

public class NAND extends Type {

    public NAND(CPU cpu) {
        CPU = cpu;
        setName("NAND");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, redT, null, null, null, null,
                redW, redW, redR, redW, redW, redW, redR, redW, redW,
                redW, null, null, null, null, null, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (!(CPU.getInput1().isPowered() && CPU.getInput2().isPowered())) {
            if (powered) return;
            if (CPUPlugin.bukkitScheduler.isQueued(on)) return;
            if (CPU.getDelay() == 0) {
                on = CPUPlugin.bukkitScheduler.runTaskLater(CPUPlugin.plugin, new PowerOn(), 0).getTaskId();
                return;
            }
            on = CPUPlugin.bukkitScheduler.runTaskLater(CPUPlugin.plugin, new PowerOn(), CPU.getDelay()).getTaskId();
        } else {
            if (!powered) return;
            if (CPUPlugin.bukkitScheduler.isQueued(off)) return;
            if (CPU.getDelay() == 0) {
                off = CPUPlugin.bukkitScheduler.runTaskLater(CPUPlugin.plugin, new PowerOff(), 0).getTaskId();
                return;
            }
            off = CPUPlugin.bukkitScheduler.runTaskLater(CPUPlugin.plugin, new PowerOff(), 2).getTaskId();
        }
    }
}
