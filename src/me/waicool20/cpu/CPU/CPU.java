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

import java.util.ArrayList;
import java.util.Arrays;

public class CPU {
    protected Location ID;
    protected Attributes attributes = new Attributes(null, null, null);
    protected World world;
    protected Core core = null;
    protected Output output;
    protected Input input1;
    protected Input input2;
    protected int[] xyz = new int[3];
    protected Type type;
    protected int delay;
    protected boolean typified = false;

    public CPU(String owner, World world, int x, int y, int z) {
        getAttributes().setOwner(owner);
        setWorld(world);
        int[] xyz = {x, y, z};
        setXyz(xyz);
        setID(new Location(world, x, y, z));
        getIO();
        detectType();
        if (getInput2() != null) {
            this.setDelay(detectDelay());
        }
    }

    //TODO add checks for chest facing !
    private void getIO() {
        if (getID().getBlock().getType() == Material.CHEST) {
            setCore(new Core(getID().getBlock()));
        } else {
            return;
        }
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) getCore().getBlock().getState().getData();
        setOutput(new Output(getCore().getBlock().getRelative(chest.getFacing().getOppositeFace())));
        Block center = getCore().getBlock().getRelative(chest.getFacing());
        switch (chest.getFacing()) {
            case NORTH:
                if (center.getRelative(BlockFace.WEST).getType() != Material.CHEST || center.getRelative(BlockFace.EAST).getType() != Material.CHEST) {
                    setInput1(null);
                    setInput2(null);
                    break;
                }
                setInput1(new Input(center.getRelative(BlockFace.EAST)));
                setInput2(new Input(center.getRelative(BlockFace.WEST)));
                break;
            case EAST:
                if (center.getRelative(BlockFace.NORTH).getType() != Material.CHEST || center.getRelative(BlockFace.SOUTH).getType() != Material.CHEST) {
                    setInput1(null);
                    setInput2(null);
                    break;
                }
                setInput1(new Input(center.getRelative(BlockFace.SOUTH)));
                setInput2(new Input(center.getRelative(BlockFace.NORTH)));
                break;
            case SOUTH:
                if (center.getRelative(BlockFace.EAST).getType() != Material.CHEST || center.getRelative(BlockFace.WEST).getType() != Material.CHEST) {
                    setInput1(null);
                    setInput2(null);
                    break;
                }
                setInput1(new Input(center.getRelative(BlockFace.WEST)));
                setInput2(new Input(center.getRelative(BlockFace.EAST)));
                break;
            case WEST:
                if (center.getRelative(BlockFace.SOUTH).getType() != Material.CHEST || center.getRelative(BlockFace.NORTH).getType() != Material.CHEST) {
                    setInput1(null);
                    setInput2(null);
                    break;
                }
                setInput1(new Input(center.getRelative(BlockFace.NORTH, 1)));
                setInput2(new Input(center.getRelative(BlockFace.SOUTH, 1)));
                break;
        }

    }

    public void setID(Location ID) {
        this.ID = ID;
    }

    public Location getID() {
        return ID;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
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

    public void setCore(Core core) {
        this.core = core;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public void setInput1(Input input1) {
        this.input1 = input1;
    }

    public void setInput2(Input input2) {
        this.input2 = input2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setXyz(int[] xyz) {
        this.xyz = xyz;
    }

    public int getXyz(int i) {
        return getXyz()[i];
    }

    public boolean isTypified() {
        return typified;
    }

    public void setTypified(boolean typified) {
        this.typified = typified;
    }

    public int[] getXyz() {
        return xyz;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    private int detectDelay() {
        int delay = 0;
        ItemStack[] itemStacks = getInput2().getInventory().getContents();
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

    public boolean isBlockPartOfCPU(Block block) {
        return block.equals(getCore().getBlock()) || block.equals(getInput1().getBlock()) || block.equals(getInput2().getBlock());
    }

    public void sendCPUInfo(Player player) {
        setDelay(detectDelay());
        player.sendMessage(ChatColor.GREEN + "----CPU INFO----");
        player.sendMessage("The Owner is " + ChatColor.AQUA + getAttributes().getOwner());
        player.sendMessage("The World is " + ChatColor.AQUA + getWorld().getName());
        player.sendMessage("The Type is " + ChatColor.AQUA + getType().getName());
        player.sendMessage("Core is at" + "   X: " + ChatColor.AQUA + getXyz(0) + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getXyz(1) + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getXyz(2));
        player.sendMessage("Input1 is at" + "   X: " + ChatColor.AQUA + getInput1().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getInput1().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getInput1().getLocation().getBlockZ());
        player.sendMessage("Input2 is at" + "   X: " + ChatColor.AQUA + getInput2().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getInput2().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getInput2().getLocation().getBlockZ());
        player.sendMessage("The delay is " + (this.getDelay() / 2) + " redstone ticks!");
    }

    private void detectType() {
        if (getCore() == null) {
            setType(null);
            return;
        }
        ItemStack[] contents = getCore().getInventory().getContents();
        for (Type type : Type.getTypes(this)) {
            if (Arrays.deepEquals(type.typeInventory(), contents)) {
                setType(type);
                return;
            }
        }


    }

    public boolean isValid() {
        return !(getInput1() == null || getInput2() == null || getCore() == null);
    }

    public ArrayList<String> toStorageFormat(){
        ArrayList<String> info = new ArrayList<String>();
        info.add(getAttributes().getOwner());
        info.add(getWorld().getName());
        info.add(String.valueOf(getID().getBlockX()));
        info.add(String.valueOf(getID().getBlockY()));
        info.add(String.valueOf(getID().getBlockZ()));
        int typified = isTypified() ? 1 : 0;
        info.add(String.valueOf(typified));
        return info;
    }

}
