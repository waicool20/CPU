package me.waicool20.cpu;

import net.minecraft.server.v1_7_R1.*;
import net.minecraft.server.v1_7_R1.EntityBat;
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
    public float bg() {
        if (isCPUSpawned) {
            return 0;
        }
        return 0.1F;
    }

    @Override
    public void h() {
        super.h();
        if (isCPUSpawned) {
            if (bN()) {
                motX = motY = motZ = 0.0D;
                locY = ((double) MathHelper.floor(locY) + 1.0D) - (double) length;
            } else {
                motY *= 0.60000002384185791D;
            }

        }
    }

    @Override
    protected void bn() {
        super.bn();
        if (isCPUSpawned) {
            if (bN()) {
                if (!this.world.getType(MathHelper.floor(this.locX), (int)this.locY + 1, MathHelper.floor(this.locZ)).r()) {
                    a(false);
                    this.world.a(null, 1015, (int)this.locX, (int)this.locY, (int)this.locZ, 0);
                }
                else {
                    if (this.random.nextInt(200) == 0) {
                        this.aP = this.random.nextInt(360);
                    }

                    if (this.world.findNearbyPlayer(this, 4.0D) != null) {
                        a(false);
                        this.world.a(null, 1015, (int)this.locX, (int)this.locY, (int)this.locZ, 0);
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
