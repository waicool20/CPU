package me.waicool20.cpu.CPU;

import me.waicool20.cpu.CPU.Types.Type;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CPU {
    private final Location ID;
    private Attributes attributes = new Attributes(null, null, null);
    private final World world;
    private Core core = null;
    private Output output;
    private Input input1;
    private Input input2;
    private final int[] xyz = new int[3];
    private Type type;
    private int delay;
    private boolean typified = false;

    public CPU(String owner, World world, int x, int y, int z) {
        this.attributes.setOwner(owner);
        this.world = world;
        this.xyz[0] = x;
        this.xyz[1] = y;
        this.xyz[2] = z;
        Location loc = new Location(world, x, y, z);
        this.ID = loc;
        getIO();
        detectType();
        if (input2 != null) {
            this.delay = detectDelay();
        }
    }

    private void getIO() {
        if (ID.getBlock().getType() == Material.CHEST) {
            core = new Core(ID.getBlock());
        } else {
            return;
        }
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) core.getBlock().getState().getData();
        output = new Output(core.getBlock().getRelative(chest.getFacing().getOppositeFace()));
        Block center = core.getBlock().getRelative(chest.getFacing());
        switch (chest.getFacing()) {
            case NORTH:
                if (center.getRelative(BlockFace.WEST).getType() != Material.CHEST || center.getRelative(BlockFace.EAST).getType() != Material.CHEST) {
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.EAST));
                input2 = new Input(center.getRelative(BlockFace.WEST));
                break;
            case EAST:
                if (center.getRelative(BlockFace.NORTH).getType() != Material.CHEST || center.getRelative(BlockFace.SOUTH).getType() != Material.CHEST) {
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.SOUTH));
                input2 = new Input(center.getRelative(BlockFace.NORTH));
                break;
            case SOUTH:
                if (center.getRelative(BlockFace.EAST).getType() != Material.CHEST || center.getRelative(BlockFace.WEST).getType() != Material.CHEST) {
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.WEST));
                input2 = new Input(center.getRelative(BlockFace.EAST));
                break;
            case WEST:
                if (center.getRelative(BlockFace.SOUTH).getType() != Material.CHEST || center.getRelative(BlockFace.NORTH).getType() != Material.CHEST) {
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.NORTH, 1));
                input2 = new Input(center.getRelative(BlockFace.SOUTH, 1));
                break;
        }

    }

    public Location getID() {
        return ID;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    World getWorld() {
        return world;
    }

    public Core getCore() {
        return core;
    }

    public Output getOutput() {
        return output;
    }

    public Input getInput1() {
        return input1;
    }

    public Input getInput2() {
        return input2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDelay() {
        return delay;
    }

    int getXyz(int i) {
        return xyz[i];
    }

    void detectType() {
        if (core == null) {
            setType(null);
            return;
        }
        ItemStack[] contents = core.getInventory().getContents();
        for (Type type : Type.getTypes(this)) {
            if (Arrays.deepEquals(type.typeInventory(), contents)) {
                setType(type);
                return;
            }
        }


    }

    private int detectDelay() {
        int delay = 0;
        ItemStack[] itemStacks = input2.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            if (itemStack.getType() == Material.COAL) {
                delay += ((itemStack.getAmount() * (5 * 20L)));
            } else if (itemStack.getType() == Material.IRON_INGOT) {
                delay += ((itemStack.getAmount() * (1 * 20L)));
            } else if (itemStack.getType() == Material.GOLD_INGOT) {
                delay += (itemStack.getAmount() * (0.1 * 20L));
            }
        }
        return delay;
    }

    public boolean isTypified() {
        return typified;
    }

    public void setTypified(boolean typified) {
        this.typified = typified;
    }

    public boolean isBlockPartOfModule(Block block) {
        return block.equals(core.getBlock()) || block.equals(input1.getBlock()) || block.equals(input2.getBlock());
    }

    public void sendCpuModuleINFO(Player player) {
        player.sendMessage("The Owner is " + ChatColor.AQUA + this.getAttributes().getOwner());
        player.sendMessage("The World is " + ChatColor.AQUA + this.getWorld().getName());
        player.sendMessage("The Type is " + ChatColor.AQUA + this.getType().getName());
        player.sendMessage("Core is at" + "   X: " + ChatColor.AQUA + this.getXyz(0) + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + this.getXyz(1) + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + this.getXyz(2));
        player.sendMessage("Input1 is at" + "   X: " + ChatColor.AQUA + this.getInput1().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + this.getInput1().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + this.getInput1().getLocation().getBlockZ());
        player.sendMessage("Input2 is at" + "   X: " + ChatColor.AQUA + this.getInput2().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + this.getInput2().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + this.getInput2().getLocation().getBlockZ());
        //player.sendMessage("The delay is " + cpuModule.getDelay() + " ticks!");
    }
}
