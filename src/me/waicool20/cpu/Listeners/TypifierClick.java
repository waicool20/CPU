package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPU.Types.OR;
import me.waicool20.cpu.CPU.Types.Type;
import me.waicool20.cpu.CPUDatabase;
import me.waicool20.cpu.CraftingAndRecipes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.Chest;

import java.util.Arrays;

public class TypifierClick implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getItem() == null || clickedBlock == null) {
            return;
        }
        if (!e.getItem().isSimilar(CraftingAndRecipes.typifier())) {
            return;
        }
        e.setCancelled(true);
        if(player.isSneaking()){
            if(clickedBlock.getType() == Material.CHEST){
                for(CPU cpu : CPUDatabase.CPUDatabaseMap){
                    if(clickedBlock.equals(cpu.getCore().getBlock())){
                        cpu.sendCPUInfo(player);
                        return;
                    }
                }
            } else {
                Location location = clickedBlock.getRelative(BlockFace.UP).getLocation();
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookMeta = (BookMeta) book.getItemMeta();

                bookMeta.setTitle("Destination");
                bookMeta.setPages(Arrays.asList(location.getWorld().getName() + " " + (location.getX()+0.5f) + " " + location.getBlockY() + " " + (location.getZ()+0.5f)));
                book.setItemMeta(bookMeta);
                player.getInventory().addItem(book);
                player.updateInventory();
                player.sendMessage(ChatColor.GREEN + "[CPU] Destination book has been given to you!");
                return;
            }
        }
        if (clickedBlock.getType() == Material.CHEST) {
            for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                if (clickedBlock.equals(cpu.getCore().getBlock())) {
                    if (!player.getName().equalsIgnoreCase(cpu.getAttributes().getOwner())) {
                        cpu.sendCPUInfo(player);
                        return;
                    }
                    Type[] types = Type.getTypes(cpu);
                    for (int i = 0; i < types.length; i++) {
                        if (cpu.getType().getName().equalsIgnoreCase(types[i].getName())) {
                            if (i + 1 < types.length) {
                                cpu.getOutput().setPower(false, 0);
                                cpu.setType(types[i + 1]);
                                cpu.getCore().getInventory().setContents(types[i + 1].typeInventory());
                                cpu.getType().updatePower();
                            } else {
                                cpu.getOutput().setPower(false, 0);
                                cpu.setType(types[0]);
                                cpu.getCore().getInventory().setContents(types[0].typeInventory());
                                cpu.getType().updatePower();
                            }
                            player.sendMessage(ChatColor.GREEN + "[CPU] " + ChatColor.WHITE + "The Type is " + ChatColor.AQUA + cpu.getType().getName());
                            return;
                        }
                    }
                }
            }
            Chest chest = (Chest) clickedBlock.getState().getData();
            org.bukkit.block.Chest chestBlock = (org.bukkit.block.Chest) clickedBlock.getState();
            Block center = clickedBlock.getRelative(chest.getFacing());
            if (checkBlocks(chest.getFacing(), center)) {
                chestBlock.getInventory().setContents((new OR(null)).typeInventory());
                CreateCPUListener.createCPU(player, clickedBlock, true);
                return;
            }
            player.sendMessage(ChatColor.RED + "[CPU] Could not create CPU!");
        } else {
            player.sendMessage(ChatColor.RED + "[CPU] Click on Chest only!");
        }
    }

    private static boolean checkBlocks(BlockFace blockFace, Block center) {
        if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
            return (center.getRelative(BlockFace.WEST).getType() == Material.CHEST && center.getRelative(BlockFace.EAST).getType() == Material.CHEST);
        }
        return (blockFace == BlockFace.WEST || blockFace == BlockFace.EAST) && (center.getRelative(BlockFace.NORTH).getType() == Material.CHEST && center.getRelative(BlockFace.SOUTH).getType() == Material.CHEST);
    }

    @SuppressWarnings("ConstantConditions")
    public static BlockFace getPlayerDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return BlockFace.WEST;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH_WEST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return BlockFace.NORTH;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return BlockFace.NORTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return BlockFace.EAST;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH_EAST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return BlockFace.SOUTH;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return BlockFace.SOUTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return BlockFace.WEST;
        } else {
            return null;
        }
    }
}
