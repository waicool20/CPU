package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.inventory.ItemStack;

public class NOR extends Type {

    public NOR(CPU cpu) {
        CPU_MODULE = cpu;
        setName("NOR");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, redT, null, null, null, null,
                redW, redW, redW, redW, redW, redW, redW, redW, redW,
                redW, null, null, null, null, null, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (!CPU_MODULE.getInput1().isPowered() && !CPU_MODULE.getInput2().isPowered()) {
            if (CPU_MODULE.getOutput().getPower()) {
                return;
            }
            CPU_MODULE.getOutput().setPower(true, CPU_MODULE.getDelay());
        } else {
            if (!CPU_MODULE.getOutput().getPower()) {
                return;
            }
            CPU_MODULE.getOutput().setPower(false, 0);
        }
    }

    @Override
    public void disable() {
        CPU_MODULE.getOutput().setPower(false, 0);
    }
}
