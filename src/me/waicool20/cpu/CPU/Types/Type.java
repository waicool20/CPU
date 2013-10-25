package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Type {
    final static ItemStack redW = new ItemStack(Material.REDSTONE, 1);
    final static ItemStack redT = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
    final static ItemStack redR = new ItemStack(Material.DIODE, 1);
    final static ItemStack GOLD = new ItemStack(Material.GOLD_INGOT, 1);
    final static ItemStack IRON = new ItemStack(Material.IRON_INGOT, 1);
    final static ItemStack NPIS = new ItemStack(Material.PISTON_BASE, 1);
    final static ItemStack DISP = new ItemStack(Material.DISPENSER, 1);
    final static ItemStack OBSB = new ItemStack(Material.OBSIDIAN, 1);
    final static ItemStack EYEE = new ItemStack(Material.EYE_OF_ENDER, 1);
    final static ItemStack CLCK = new ItemStack(Material.WATCH, 1);

    int on;
    int off;
    boolean powered = false;
    private String name;
    CPU CPU = null;

    public abstract ItemStack[] typeInventory();

    public abstract void updatePower();

    public void disable() {
        if (CPUPlugin.bukkitScheduler.isQueued(on)) CPUPlugin.bukkitScheduler.cancelTask(on);
        if (CPUPlugin.bukkitScheduler.isQueued(off)) CPUPlugin.bukkitScheduler.cancelTask(off);
        CPU.getOutput().setPower(false);
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public static Type[] getTypes(CPU cpu) {
        Type[] listOfTypes = {new OR(cpu), new AND(cpu), new NAND(cpu), new XOR(cpu),
                new NOR(cpu), new XNOR(cpu), new BlockBreak(cpu),
                new BlockPlace(cpu), new Teleporter(cpu), new PulseLimiter(cpu),
                new PulseExtender(cpu), new Clock(cpu)};
        return listOfTypes;
    }

    protected class PowerOn implements Runnable {
        @Override
        public void run() {
            CPU.getOutput().setPower(true);
            powered = true;
        }
    }

    protected class PowerOff implements Runnable {
        @Override
        public void run() {
            CPU.getOutput().setPower(false);
            powered = false;
        }
    }
}

