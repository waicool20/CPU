package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUPlugin;
import org.bukkit.inventory.ItemStack;

public class XOR extends Type {

    public XOR(CPU cpu) {
        CPU = cpu;
        setName("XOR");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, GOLD, null, null, null, null,
                redW, redR, redT, redT, redW, redT, redT, redR, redW,
                redW, IRON, null, null, null, null, null, IRON, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (CPU.getInput1().isPowered() ^ CPU.getInput2().isPowered()) {
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
