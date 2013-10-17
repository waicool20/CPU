package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.inventory.ItemStack;

public class AND extends Type {

    public AND(CPU cpu) {
        CPU = cpu;
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
        if (CPU.getInput1().isPowered() && CPU.getInput2().isPowered()) {
            if (CPU.getOutput1().getPower()) {
                return;
            }
            CPU.getOutput1().setPower(true, CPU.getDelay());
        } else {
            if (!CPU.getOutput1().getPower()) {
                return;
            }
            CPU.getOutput1().setPower(false, 0);
        }
    }

    @Override
    public void disable() {
        CPU.getOutput1().setPower(false, 0);
    }
}
