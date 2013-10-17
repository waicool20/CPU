package me.waicool20.cpu.CPU;

import me.waicool20.cpu.CPU.Types.AdvancedType;
import me.waicool20.cpu.CPU.Types.Type;
import org.bukkit.World;
import org.bukkit.block.Block;

public class AdvancedCPU extends CPU {
    protected Input input3;
    protected Output output2;
    protected AdvancedType advancedType;


    public AdvancedCPU(String owner, World world, int x, int y, int z) {
        super(owner, world, x, y, z);
        getIO();

    }

    private void getIO(){
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) getCore().getBlock().getState().getData();
        setInput2(new Input(getCore().getBlock()));
        switch(chest.getFacing()){

        }
    }

    public Input getInput3() {
        return input3;
    }

    public void setInput3(Input input3) {
        this.input3 = input3;
    }

    public Output getOutput2() {
        return output2;
    }

    public void setOutput2(Output output2) {
        this.output2 = output2;
    }

    public AdvancedType getType() {
        return advancedType;
    }

    public void setType(AdvancedType type) {
        this.type = type;
    }
}
