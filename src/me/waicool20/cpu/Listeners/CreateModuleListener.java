package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU;
import me.waicool20.cpu.CPUModule.CPUModule;
import me.waicool20.cpu.CraftingAndRecipes;
import me.waicool20.cpu.ModuleDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CreateModuleListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();

        if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getItem() == null || clickedBlock == null) {return;}
        if (!e.getItem().isSimilar(CraftingAndRecipes.redstoneActivator())) {return;}
        e.setCancelled(true);

        createModule(player,clickedBlock,false);
    }

    private static boolean hasPermission(Player player, CPUModule cpuModule) {
        return player.isOp() || player.hasPermission("cpu.create." + cpuModule.getType().getName().toLowerCase());
    }

    private static boolean isEmpty(ItemStack[] ie) {
        for(ItemStack item : ie) {
            if(item != null) {
                return false;
            }
        }
        return true;
    }

    private static boolean alreadyActivated(CPUModule newCpuModule){
        for(CPUModule cpuModule : ModuleDatabase.ModuleDatabaseMap){
            if(newCpuModule.getID().equals(cpuModule.getID())){
                return true;
            }
        }
        return false;
    }

    private static boolean isValidModule(CPUModule newCpuModule){
        return !(newCpuModule.getInput1() == null || newCpuModule.getInput2() == null);
    }

    static void createModule(Player player, Block clickedBlock, boolean typified){
        if (clickedBlock.getType() != Material.CHEST) {player.sendMessage(ChatColor.RED + "[CPU] Please use the activator on CPU Modules only!!");return;}
        InventoryHolder inventoryHolder = (Chest) clickedBlock.getState();
        ItemStack[] clickedBlockContents = inventoryHolder.getInventory().getContents();

        for(CPUModule cpuModule : ModuleDatabase.ModuleDatabaseMap){
            if(clickedBlock.equals(cpuModule.getInput1().getBlock()) || clickedBlock.equals(cpuModule.getInput2().getBlock())){
                player.sendMessage(ChatColor.RED + "[CPU] This is a part of a existing CPU!");
                return;
            }
        }

        if(isEmpty(clickedBlockContents)) {player.sendMessage(ChatColor.RED + "[CPU] Unable to activate a " + ChatColor.DARK_RED + "EMPTY " + ChatColor.RED + "CPU!"); return;}

        CPUModule newCpuModule = new CPUModule(player.getName(),clickedBlock.getWorld(),clickedBlock.getX(),clickedBlock.getY(),clickedBlock.getZ());

        newCpuModule.setTypified(typified);

        if(newCpuModule.getType() == null ) {player.sendMessage(ChatColor.RED + "[CPU] Invalid CPU Type!     " + ChatColor.GREEN + "HINT:  Check Inventory!");return;}

        if(!hasPermission(player,newCpuModule)){
            player.sendMessage(ChatColor.RED + "[CPU] You do not have the permission to create a " + ChatColor.DARK_RED + newCpuModule.getType().getName() + ChatColor.RED + " CPU");
            return;
        }

        if(!isValidModule(newCpuModule)){player.sendMessage(ChatColor.RED + "[CPU] This CPU seems to be missing some inputs?");return;}

        if(alreadyActivated(newCpuModule)){
            player.sendMessage(ChatColor.GREEN + "----MODULE INFO----");
            for(CPUModule cpuModule: ModuleDatabase.ModuleDatabaseMap){
                if(clickedBlock.getLocation().equals(cpuModule.getID())){
                    newCpuModule.sendCpuModuleINFO(player);
                }
            }
            return;
        }

        ModuleDatabase.addModule(newCpuModule);

        newCpuModule.getType().updatePower();

        //newCpuModule.getID().getWorld().spawnEntity(newCpuModule.getID(), EntityType.BAT);

        player.sendMessage(ChatColor.GREEN + "[CPU] You have successfully activated this CPU!!");

        if(!CPU.plugin.getConfig().getBoolean("send-info")){return;}
        newCpuModule.sendCpuModuleINFO(player);
    }
}
