package me.waicool20.cpu.CPUModule.Types;

import me.waicool20.cpu.CPUModule.CPUModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Type {
    final static ItemStack redW = new ItemStack(Material.REDSTONE,1);
    final static ItemStack redT = new ItemStack(Material.REDSTONE_TORCH_ON,1);
    final static ItemStack redR = new ItemStack(Material.DIODE,1);
    final static ItemStack GOLD = new ItemStack(Material.GOLD_INGOT,1);
    final static ItemStack IRON = new ItemStack(Material.IRON_INGOT,1);
    final static ItemStack NPIS = new ItemStack(Material.PISTON_BASE,1);
    final static ItemStack DISP = new ItemStack(Material.DISPENSER,1);

    String name;
    CPUModule CPU_MODULE = null;
    public abstract ItemStack[] typeInventory();

    public abstract boolean updatePower();

    public abstract void disable();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Type[] getTypes(CPUModule cpuModule){
        Type[] listOfTypes = {new OR(cpuModule), new AND(cpuModule),new NAND(cpuModule),new XOR(cpuModule),new NOR(cpuModule),new XNOR(cpuModule),new BlockBreak(cpuModule),new BlockPlace(cpuModule)} ;
        return listOfTypes;
    }
}

