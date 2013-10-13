package me.waicool20.cpu.CPUModule.Types;

import me.waicool20.cpu.CPUModule.CPUModule;
import org.bukkit.inventory.ItemStack;

public class XOR extends Type {

    public XOR(CPUModule cpuModule) {
        CPU_MODULE = cpuModule;
        setName("XOR");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {   null,null,null,null,GOLD,null,null,null,null,
                                        redW,redR,redT,redT,redW,redT,redT,redR,redW  ,
                                        redW,IRON,null,null,null,null,null,IRON,redW,};
        return typeInventory;
    }

    @Override
    public boolean updatePower() {
        if(CPU_MODULE.getInput1().isPowered() ^ CPU_MODULE.getInput2().isPowered()){
            if(CPU_MODULE.getOutput().getPower()){return true;}
            CPU_MODULE.getOutput().setPower(true,CPU_MODULE.getDelay());
            return true;
        }else {
            if(!CPU_MODULE.getOutput().getPower()){return false;}
            CPU_MODULE.getOutput().setPower(false,0); return false;
        }
    }

    @Override
    public void disable() {
        CPU_MODULE.getOutput().setPower(false,0);
    }
}
