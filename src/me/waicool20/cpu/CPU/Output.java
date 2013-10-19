package me.waicool20.cpu.CPU;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Output {
    private final Block block;

    public Output(Block block) {
        this.block = block;
    }

    public void setPower(boolean powered) {
        if (powered) {
            block.setType(Material.REDSTONE_BLOCK);
        } else {
            block.setType(Material.AIR);
        }
    }

    public Block getBlock() {
        return block;
    }
}
