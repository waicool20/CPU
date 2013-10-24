package me.waicool20.cpu;

import net.minecraft.server.v1_6_R3.*;

public class NameTagBat extends EntityBat{

    public NameTagBat(World world) {
        super(world);
        a(0.1F, 0.1F);
        a(false);
        super.setCustomNameVisible(true);
        super.noDamageTicks = Integer.MAX_VALUE;
        this.goalSelector.a(7, new PathfinderGoalFloat(this));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    public float ba(){
        return 0;
    }

    @Override
    public boolean bf(){
        return true;
    }

    @Override
    public void l_() {
        super.l_();
        if(bJ())
        {
            motX = motY = motZ = 0.0D;
            locY = ((double)MathHelper.floor(locY) + 1.0D) - (double)length;
        } else
        {
            motY *= 0.60000002384185791D;
        }
    }

    @Override
    protected void bi() {
        super.bi();
        if(bJ())
        {
            if(!world.u(MathHelper.floor(locX), (int)locY + 1, MathHelper.floor(locZ)))
            {
                a(false);
                world.a(null, 1015, (int)locX, (int)locY, (int)locZ, 0);
            } else
            {
                if(random.nextInt(200) == 0)
                    aP = random.nextInt(360);
                if(world.findNearbyPlayer(this, 4D) != null)
                {
                    a(false);
                    world.a(null, 1015, (int)locX, (int)locY, (int)locZ, 0);
                }
            }
        } else
        {
            motX = 0;
            motY = 0;
            motZ = 0;
            bf = 0;
        }
    }

    public void setIsInWeb(boolean b){
        K = b;
    }

}
