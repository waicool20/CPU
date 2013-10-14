package me.waicool20.cpu.CPUModule.Types;

import me.waicool20.cpu.CPUModule.CPUModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockBreak extends Type {

    public BlockBreak(CPUModule cpuModule) {
        CPU_MODULE = cpuModule;
        setName("BlockBreaker");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {   null,null,null,null,null,null,null,null,null,
                                        redW,redW,redW,NPIS,null,NPIS,redW,redW,redW,
                                        redW,null,null,null,null,null,null,null,redW,};
        return typeInventory;
    }

    @Override
    public boolean updatePower() {
        if(CPU_MODULE.getInput1().isPowered() || CPU_MODULE.getInput2().isPowered()){
            Block block = CPU_MODULE.getOutput().getBlock();
            if(block.getType() != Material.BEDROCK && block.getType() != Material.AIR){
                block.breakNaturally();
            }
            return true;
        }else {return false;}
    }

    @Override
    public void disable() {
        return;
    }
}