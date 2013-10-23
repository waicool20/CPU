package me.waicool20.cpu.CPU;

import me.waicool20.cpu.CPU.Types.AdvancedType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class AdvancedCPU extends CPU {
    protected Input input3;
    protected Output output2;
    protected AdvancedType advancedType;


    public AdvancedCPU(String owner, World world, int x, int y, int z) {
        super(owner, world, x, y, z);
        getIO();
        detectType();
    }

    //TODO add checks for chest facing !
    private void getIO() {
        if (getLocation().getBlock().getType() == Material.CHEST) {
            setCore(new Core(getLocation().getBlock()));
        } else {
            return;
        }
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) getCore().getBlock().getState().getData();
        setInput2(new Input(getCore().getBlock()));
        Block core = getCore().getBlock();
        Block[] blocks = new Block[5];
        switch (chest.getFacing()) {
            case NORTH:
                blocks[0] = core.getRelative(BlockFace.EAST, 2);
                blocks[1] = core.getRelative(BlockFace.EAST).getRelative(BlockFace.SOUTH);
                blocks[2] = core;
                blocks[3] = core.getRelative(BlockFace.WEST, 2);
                blocks[4] = core.getRelative(BlockFace.WEST).getRelative(BlockFace.SOUTH);
                for (Block block : blocks) {
                    if (block.getType() != Material.CHEST) {
                        setInput1(null);
                        setInput2(null);
                        setInput3(null);
                        setOutput(null);
                        setOutput2(null);
                        return;
                    }
                }
                setInput1(new Input(blocks[0]));
                setOutput(new Output(blocks[1]));
                setInput2(new Input(blocks[2]));
                setOutput2(new Output(blocks[3]));
                setInput3(new Input(blocks[4]));
                break;
            case EAST:
                blocks[0] = core.getRelative(BlockFace.SOUTH, 2);
                blocks[1] = core.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST);
                blocks[2] = core;
                blocks[3] = core.getRelative(BlockFace.NORTH, 2);
                blocks[4] = core.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST);
                for (Block block : blocks) {
                    if (block.getType() != Material.CHEST) {
                        setInput1(null);
                        setInput2(null);
                        setInput3(null);
                        setOutput(null);
                        setOutput2(null);
                        return;
                    }
                }
                setInput1(new Input(blocks[0]));
                setOutput(new Output(blocks[1]));
                setInput2(new Input(blocks[2]));
                setOutput2(new Output(blocks[3]));
                setInput3(new Input(blocks[4]));
                break;
            case SOUTH:
                blocks[0] = core.getRelative(BlockFace.WEST, 2);
                blocks[1] = core.getRelative(BlockFace.WEST).getRelative(BlockFace.NORTH);
                blocks[2] = core;
                blocks[3] = core.getRelative(BlockFace.EAST, 2);
                blocks[4] = core.getRelative(BlockFace.EAST).getRelative(BlockFace.NORTH);
                for (Block block : blocks) {
                    if (block.getType() != Material.CHEST) {
                        setInput1(null);
                        setInput2(null);
                        setInput3(null);
                        setOutput(null);
                        setOutput2(null);
                        return;
                    }
                }
                setInput1(new Input(blocks[0]));
                setOutput(new Output(blocks[1]));
                setInput2(new Input(blocks[2]));
                setOutput2(new Output(blocks[3]));
                setInput3(new Input(blocks[4]));
                break;
            case WEST:
                blocks[0] = core.getRelative(BlockFace.NORTH, 2);
                blocks[1] = core.getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST);
                blocks[2] = core;
                blocks[3] = core.getRelative(BlockFace.SOUTH, 2);
                blocks[4] = core.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST);
                for (Block block : blocks) {
                    if (block.getType() != Material.CHEST) {
                        setInput1(null);
                        setInput2(null);
                        setInput3(null);
                        setOutput(null);
                        setOutput2(null);
                        return;
                    }
                }
                setInput1(new Input(blocks[0]));
                setOutput(new Output(blocks[1]));
                setInput2(new Input(blocks[2]));
                setOutput2(new Output(blocks[3]));
                setInput3(new Input(blocks[4]));
                break;
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

    private void detectType() {
        if (getCore() == null) {
            setType(null);
            return;
        }
        ItemStack[] contents = getCore().getInventory().getContents();
        for (AdvancedType type : AdvancedType.getTypes(this)) {
            if (Arrays.deepEquals(type.typeInventory(), contents)) {
                setType(type);
                return;
            }
        }
    }

    public boolean isBlockPartOfCPU(Block block) {
        return block.equals(getCore().getBlock()) || block.equals(getInput1().getBlock()) || block.equals(getInput3().getBlock()) || block.equals(getOutput().getBlock()) || block.equals(getOutput2().getBlock());
    }

    public void sendCPUInfo(Player player) {
        player.sendMessage(ChatColor.GREEN + "----CPU INFO----");
        player.sendMessage("The Owner is " + ChatColor.AQUA + getAttributes().getOwner());
        player.sendMessage("The World is " + ChatColor.AQUA + getWorld().getName());
        player.sendMessage("The Type is " + ChatColor.AQUA + getType().getName());
        player.sendMessage("Core/Input2 is at" + "   X: " + ChatColor.AQUA + getXyz(0) + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getXyz(1) + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getXyz(2));
        player.sendMessage("Input1 is at" + "   X: " + ChatColor.AQUA + getInput1().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getInput1().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getInput1().getLocation().getBlockZ());
        player.sendMessage("Input2 is at" + "   X: " + ChatColor.AQUA + getInput2().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getInput2().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getInput2().getLocation().getBlockZ());
        player.sendMessage("Input3 is at" + "   X: " + ChatColor.AQUA + getInput3().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getInput3().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getInput3().getLocation().getBlockZ());
        player.sendMessage("Input3 is at" + "   X: " + ChatColor.AQUA + getOutput().getBlock().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getOutput().getBlock().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getOutput().getBlock().getLocation().getBlockZ());
        player.sendMessage("Input3 is at" + "   X: " + ChatColor.AQUA + getOutput2().getBlock().getLocation().getBlockX() + ChatColor.WHITE + "   Y: " + ChatColor.AQUA + getOutput2().getBlock().getLocation().getBlockY() + ChatColor.WHITE + "   Z: " + ChatColor.AQUA + getOutput2().getBlock().getLocation().getBlockZ());

    }
}
