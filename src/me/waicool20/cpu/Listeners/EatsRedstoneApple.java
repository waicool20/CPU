package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU;
import me.waicool20.cpu.CraftingAndRecipes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class EatsRedstoneApple implements Listener {

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent e){
        final Player player = e.getPlayer();
        ItemStack itemConsumed = e.getItem();
        if(itemConsumed.isSimilar(CraftingAndRecipes.redstoneApple())){
            player.setVelocity(player.getVelocity().setY(0.4));
            player.sendMessage(ChatColor.RED + "[CPU] You just got shocked by eating a redstone apple!");
            player.sendMessage(ChatColor.RED + "[CPU] You can't move that well!");
            CPU.bukkitScheduler.scheduleSyncDelayedTask(CPU.plugin,new BukkitRunnable() {
                @Override
                public void run() {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,2000,1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,2000,4));
                }
            },60);
        }
    }
}
