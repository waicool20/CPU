package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
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
            if (CPU.getOutput().getPower()) {
                return;
            }
            CPU.getOutput().setPower(true, CPU.getDelay());
        } else {
            if (!CPU.getOutput().getPower()) {
                return;
            }
            CPU.getOutput().setPower(false, 0);
        }
    }

    @Override
    public void disable() {
        CPU.getOutput().setPower(false, 0);
    }
}
