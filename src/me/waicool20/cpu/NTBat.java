package me.waicool20.cpu;

import net.minecraft.server.v1_6_R3.EntityBat;
import net.minecraft.server.v1_6_R3.World;

class NTBat extends EntityBat {

    public NTBat(World world, String name) {
        super(world);
        this.setCustomName(name);
        this.setCustomNameVisible(true);
    }

    public void bh() {

    }
}
