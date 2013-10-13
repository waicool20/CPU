package me.waicool20.cpu.CPUModule.Types;

import me.waicool20.cpu.CPUModule.CPUModule;
import org.bukkit.inventory.ItemStack;

public class AND extends Type {

    public AND(CPUModule cpuModule) {
        CPU_MODULE = cpuModule;
        setName("AND");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {   null,null,null,null, redR,null,null,null,null,
                                        redW,redW,redR,redW,redW,redW,redR,redW,redW,
                                        redW,null,null,null,null,null,null,null,redW,};
        return typeInventory;
    }

    @Override
    public boolean updatePower() {
        if(CPU_MODULE.getInput1().isPowered() && CPU_MODULE.getInput2().isPowered()){
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
