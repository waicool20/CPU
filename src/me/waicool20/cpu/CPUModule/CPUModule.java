package me.waicool20.cpu.CPUModule;

import me.waicool20.cpu.CPUModule.Types.Type;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CPUModule {
    private Location ID;
    private Attributes attributes = new Attributes(null,null,null);
    private World world;
    private Core core = null;
    private Output output;
    private Input input1;
    private Input input2;
    private int[] xyz = new int[3];
    private Type type;
    private int delay;
    private boolean typified = false;

    public CPUModule(String owner, World world, int x, int y, int z) {
        this.attributes.setOwner(owner);
        this.world = world;
        this.xyz[0] = x;
        this.xyz[1] = y;
        this.xyz[2] = z;
        Location loc = new Location(world,x,y,z);
        this.ID = loc;
        getIO();
        detectType();
        if(input2 != null){
            this.delay = detectDelay();
        }
    }

    private void getIO(){
        if(ID.getBlock().getType() == Material.CHEST){
            core = new Core(ID.getBlock());
        } else {
            return;
        }
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) core.getBlock().getState().getData();
        output = new Output(this,core.getBlock().getRelative(chest.getFacing().getOppositeFace()));
        Block center = core.getBlock().getRelative(chest.getFacing());
        switch(chest.getFacing()){
            case NORTH:
                if(center.getRelative(BlockFace.WEST).getType() != Material.CHEST || center.getRelative(BlockFace.EAST).getType() != Material.CHEST){
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.EAST), Input.Side.RIGHT);
                input2 = new Input(center.getRelative(BlockFace.WEST), Input.Side.LEFT);
                break;
            case EAST:
                if(center.getRelative(BlockFace.NORTH).getType() != Material.CHEST || center.getRelative(BlockFace.SOUTH).getType() != Material.CHEST){
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.SOUTH), Input.Side.RIGHT);
                input2 = new Input(center.getRelative(BlockFace.NORTH), Input.Side.LEFT);
                break;
            case SOUTH:
                if(center.getRelative(BlockFace.EAST).getType() != Material.CHEST || center.getRelative(BlockFace.WEST).getType() != Material.CHEST){
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.WEST), Input.Side.RIGHT);
                input2 = new Input(center.getRelative(BlockFace.EAST), Input.Side.LEFT);
                break;
            case WEST:
                if(center.getRelative(BlockFace.SOUTH).getType() != Material.CHEST || center.getRelative(BlockFace.NORTH).getType() != Material.CHEST){
                    input1 = input2 = null;
                    break;
                }
                input1 = new Input(center.getRelative(BlockFace.NORTH,1), Input.Side.RIGHT);
                input2 = new Input(center.getRelative(BlockFace.SOUTH,1), Input.Side.LEFT);
                break;
        }

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

    public int getXyz(int i) {
        return xyz[i];
    }

    public Location getInputLocation(Input.Side side){
        if (side == Input.Side.LEFT){
            return input1.getLocation();
        }
        if (side == Input.Side.RIGHT){
            return input2.getLocation();
        }
        return null;
    }

    public void detectType(){
        if(core == null){
            setType(null);
            return;
        }
        ItemStack[] contents = core.getInventory().getContents();
        for(Type type : Type.getTypes(this)){
            if(Arrays.deepEquals(type.typeInventory(),contents)) {
                setType(type);
                return;
            }
        }


    }

    private int detectDelay(){
        int delay = 0;
        ItemStack[] itemStacks = input2.getInventory().getContents();
        for(ItemStack itemStack: itemStacks){
            if(itemStack == null){continue;}
            if(itemStack.getType() == Material.COAL){
                delay += ((itemStack.getAmount()*(5*20L)));
            } else if(itemStack.getType() == Material.IRON_INGOT){
                delay += ((itemStack.getAmount()*(1*20L)));
            }else if (itemStack.getType() == Material.GOLD_INGOT){
                delay += (itemStack.getAmount()*(0.1*20L));
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

    public boolean isBlockPartOfModule(Block block){
        return block.equals(core.getBlock()) || block.equals(input1.getBlock()) || block.equals(input2.getBlock());
    }

    public static void sendCpuModuleINFO(Player player, CPUModule cpuModule){
        player.sendMessage("The Owner is " + ChatColor.AQUA + cpuModule.getAttributes().getOwner());
        player.sendMessage("The World is " + ChatColor.AQUA + cpuModule.getWorld().getName());
        player.sendMessage("The Type is " + ChatColor.AQUA + cpuModule.getType().getName());
        player.sendMessage("Core is at" + "   X: " + ChatColor.AQUA +cpuModule.getXyz(0) +ChatColor.WHITE + "   Y: " + ChatColor.AQUA +cpuModule.getXyz(1) +ChatColor.WHITE + "   Z: " + ChatColor.AQUA +cpuModule.getXyz(2));
        player.sendMessage("Input1 is at" + "   X: " + ChatColor.AQUA + cpuModule.getInputLocation(Input.Side.LEFT).getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + cpuModule.getInputLocation(Input.Side.LEFT).getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + cpuModule.getInputLocation(Input.Side.LEFT).getBlockZ());
        player.sendMessage("Input2 is at" + "   X: " + ChatColor.AQUA + cpuModule.getInputLocation(Input.Side.RIGHT).getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + cpuModule.getInputLocation(Input.Side.RIGHT).getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + cpuModule.getInputLocation(Input.Side.RIGHT).getBlockZ());
        //player.sendMessage("The delay is " + cpuModule.getDelay() + " ticks!");
    }
}
