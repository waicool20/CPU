package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.inventory.ItemStack;

public class OR extends Type {

    public OR(CPU cpu) {
        CPU = cpu;
        setName("OR");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, redR, null, null, null, null,
                redW, redW, redW, redW, redW, redW, redW, redW, redW,
                redW, null, null, null, null, null, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (CPU.getInput1().isPowered() || CPU.getInput2().isPowered()) {
            CPU.getOutput1().setPower(true, CPU.getDelay());
        } else {
            CPU.getOutput1().setPower(false, 0);
        }
    }

    @Override
    public void disable() {
        CPU.getOutput1().setPower(false, 0);
    }
}
