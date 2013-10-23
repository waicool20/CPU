package me.waicool20.cpu;

import net.minecraft.server.v1_6_R3.EntityBat;
import net.minecraft.server.v1_6_R3.World;

public class NameTagBat extends EntityBat {

    public NameTagBat(World world) {
        super(world);
        a(0.1F, 0.1F);
        a(true);
        super.setCustomNameVisible(true);
        super.noDamageTicks = Integer.MAX_VALUE;
    }

    @Override
    public float ba(){
        return 0;
    }

    @Override
    public void l_() {
        return;
    }

    @Override
    protected void bi() {
        return;
    }
}
