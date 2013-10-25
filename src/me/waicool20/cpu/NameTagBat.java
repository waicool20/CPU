package me.waicool20.cpu;

import net.minecraft.server.v1_6_R3.*;
import org.bukkit.Location;

public class NameTagBat extends EntityBat {
    private boolean isCPUSpawned = false;
    private Location location;

    public NameTagBat(World world) {
        super(world);
    }

    public NameTagBat(World world, Location location) {
        super(world);
        this.isCPUSpawned = true;
        this.location = location;
        a(0.1F, 0.1F);
        a(false);
        this.setCustomNameVisible(true);
        this.noDamageTicks = Integer.MAX_VALUE;
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 15F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY() + 1;
        double z = location.getBlockZ() + 0.5;
        this.setPosition(x, y, z);
    }

    @Override
    public float ba() {
        if (isCPUSpawned) {
            return 0;
        }
        return 0.1F;
    }

    @Override
    public void l_() {
        super.l_();
        if (isCPUSpawned) {
            if (bJ()) {
                motX = motY = motZ = 0.0D;
                locY = ((double) MathHelper.floor(locY) + 1.0D) - (double) length;
            } else {
                motY *= 0.60000002384185791D;
            }

        }
    }

    @Override
    protected void bi() {
        super.bi();
        if (isCPUSpawned) {
            if (bJ()) {
                if (!world.u(MathHelper.floor(locX), (int) locY + 1, MathHelper.floor(locZ))) {
                    a(false);
                    world.a(null, 1015, (int) locX, (int) locY, (int) locZ, 0);
                } else {
                    if (random.nextInt(200) == 0)
                        aP = random.nextInt(360);
                    if (world.findNearbyPlayer(this, 4D) != null) {
                        a(false);
                        world.a(null, 1015, (int) locX, (int) locY, (int) locZ, 0);
                    }
                }
            } else {
                double x = location.getBlockX() + 0.5;
                double y = location.getBlockY() + 1;
                double z = location.getBlockZ() + 0.5;
                motX = 0;
                motY = 0;
                motZ = 0;
            }
        }
    }
}
