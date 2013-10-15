package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.inventory.ItemStack;

public class AND extends Type {

    public AND(CPU cpu) {
        CPU_MODULE = cpu;
        setName("AND");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, redR, null, null, null, null,
                redW, redW, redR, redW, redW, redW, redR, redW, redW,
                redW, null, null, null, null, null, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (CPU_MODULE.getInput1().isPowered() && CPU_MODULE.getInput2().isPowered()) {
            if (CPU_MODULE.getOutput().getPower()) {
                return;
            }
            CPU_MODULE.getOutput().setPower(true, CPU_MODULE.getDelay());
            return;
        } else {
            if (!CPU_MODULE.getOutput().getPower()) {
                return;
            }
            CPU_MODULE.getOutput().setPower(false, 0);
            return;
        }
    }

    @Override
    public void disable() {
        CPU_MODULE.getOutput().setPower(false, 0);
    }
}
