package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUDatabase;
import org.bukkit.inventory.ItemStack;

public class Wireless extends Type {

    public Wireless(CPU cpu){
        CPU = cpu;
        this.setName("Wireless");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {   null, null, null, null, redT, null, null, null, null,
                                        null, null, null, null, redT, null, null, null ,null,
                                        redW, redW, redW, redR, redT, redR, redW, redW, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if(CPUNetworkPowered()) {
            CPU.getOutput().setPower(true);
        } else {
            CPU.getOutput().setPower(false);
        }
    }

    private boolean CPUNetworkPowered(){
        String ID;
        String ID2;
        for(CPU cpu : CPUDatabase.CPUDatabaseMap){
            ID = cpu.getAttributes().getWirelessID();
            ID2 = CPU.getAttributes().getWirelessID();
            if(ID == null || ID2 == null) continue;
            if(ID.equals(ID2)){
                if(cpu.getInput1().isPowered() || cpu.getInput2().isPowered()) return true;
            }
        }
        return false;
    }
}
