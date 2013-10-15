package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CPUBreakListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if (block.getType() != Material.CHEST) {
            return;
        }
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (cpu.isBlockPartOfCPU(block)) {
                if (player.getName().equalsIgnoreCase(cpu.getAttributes().getOwner())) {
                    checkCPUIntegrity(block, player);
                } else {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "[CPU] You are not the owner of this CPU!");
                }
            }
        }


    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            checkCPUIntegrity(block, null);
        }
    }

    private static void checkCPUIntegrity(Block block, Player player) {
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (cpu.isBlockPartOfCPU(block)) {
                cpu.getType().disable();
                if (cpu.isTypified()) {
                    cpu.getCore().getInventory().clear();
                    cpu.getInput1().getInventory().clear();
                    cpu.getInput2().getInventory().clear();
                }
                if (player != null) player.sendMessage(ChatColor.RED + "[CPU] You just broke a CPU!");
                CPUDatabase.removeCPU(cpu);
            }
        }
    }
}
