package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockPlace extends Type {
    private boolean state = false;

    public BlockPlace(CPU cpu) {
        CPU_MODULE = cpu;
        setName("BlockPlacer");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, null, null, null, null, null,
                redW, redW, redW, DISP, null, DISP, redW, redW, redW,
                redW, null, null, null, null, null, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (CPU_MODULE.getInput1().isPowered() || CPU_MODULE.getInput2().isPowered()) {
            if (state) return;
            if (CPU_MODULE.getOutput().getBlock().getType() != Material.AIR) {
                return;
            }
            state = true;
            Inventory input1 = CPU_MODULE.getInput1().getInventory();
            Inventory input2 = CPU_MODULE.getInput2().getInventory();

            Inventory inventoryToCheck = input1;

            Material material = null;
            if (isEmpty(input1.getContents())) {
                inventoryToCheck = input2;
                if (isEmpty(input2.getContents())) {
                    return;
                }
            }
            for (ItemStack itemStack : inventoryToCheck.getContents()) {
                if (itemStack != null) {
                    Material itemStackType = itemStack.getType();
                    if (itemStackType.isBlock()) {
                        if (itemStack.getAmount() > 1) {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                        } else {
                            inventoryToCheck.remove(itemStack);
                        }
                        material = itemStackType;
                        break;
                    }
                }
            }
            if (material == null) {
                return;
            }
            CPU_MODULE.getOutput().getBlock().setType(material);
            state = true;
            return;
        }
        state = false;
    }

    @Override
    public void disable() {
    }

    boolean isEmpty(ItemStack[] itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                return false;
            }
        }
        return true;
    }
}
