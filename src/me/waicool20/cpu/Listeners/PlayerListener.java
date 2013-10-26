package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPUPlugin;
import me.waicool20.cpu.Updater;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (CPUPlugin.plugin.getConfig().getBoolean("notify-updates")) {
            if (CPUPlugin.updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE && (player.hasPermission("cpu.notifyupdate") || player.hasPermission("cpu.*"))) {
                CPUPlugin.bukkitScheduler.scheduleSyncDelayedTask(CPUPlugin.plugin, new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.GREEN + "[CPU] New update available: " + ChatColor.AQUA + CPUPlugin.updater.getLatestName());
                        player.sendMessage(ChatColor.GREEN + "[CPU] Go get it at: " + ChatColor.AQUA + CPUPlugin.updater.getLatestFileLink());
                    }
                }, 60);
            }
        }
    }
}
