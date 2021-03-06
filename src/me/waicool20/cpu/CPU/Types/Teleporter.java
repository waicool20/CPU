package me.waicool20.cpu.CPU.Types;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.Listeners.InventoryListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Teleporter extends Type {

    private boolean state = false;

    public Teleporter(CPU cpu) {
        CPU = cpu;
        setName("Teleporter");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, OBSB, OBSB, OBSB, null, null, null,
                redW, redW, redR, OBSB, EYEE, OBSB, redR, redW, redW,
                redW, null, null, OBSB, OBSB, OBSB, null, null, redW,};
        return typeInventory;
    }

    @Override
    public void updatePower() {
        if (CPU.getInput1().isPowered() || CPU.getInput2().isPowered()) {
            if (state) return;
            Player[] players = Bukkit.getServer().getOnlinePlayers();
            Inventory input1 = CPU.getInput1().getInventory();
            Inventory input2 = CPU.getInput2().getInventory();

            Inventory inventoryToCheck = input1;

            if (isEmpty(input1.getContents())) {
                inventoryToCheck = input2;
                if (isEmpty(input2.getContents())) {
                    return;
                }
            }
            Location tpLocation = null;
            for (ItemStack itemStack : inventoryToCheck.getContents()) {
                if (itemStack == null || itemStack.getType() != Material.WRITTEN_BOOK) continue;
                BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
                if (!bookMeta.getTitle().equalsIgnoreCase("Destination")) continue;
                String[] locString = bookMeta.getPage(1).split(" ");

                World world = Bukkit.getWorld(locString[0]);
                if (world == null) continue;
                String format = "[-+]?\\d*\\.?\\d+";
                float x;
                float y;
                float z;
                float yaw;
                float pitch;
                if (locString.length == 4 && locString[1].matches(format) && locString[2].matches(format) && locString[3].matches(format)) {
                    x = Float.parseFloat(locString[1]);
                    y = Float.parseFloat(locString[2]);
                    z = Float.parseFloat(locString[3]);
                    tpLocation = new Location(world, x, y, z);
                    break;
                }
                if (locString.length == 6 && locString[1].matches(format) && locString[2].matches(format) && locString[3].matches(format) && locString[4].matches(format) && locString[5].matches(format)) {
                    x = Float.parseFloat(locString[1]);
                    y = Float.parseFloat(locString[2]);
                    z = Float.parseFloat(locString[3]);
                    yaw = Float.parseFloat(locString[4]);
                    pitch = Float.parseFloat(locString[5]);
                    tpLocation = new Location(world, x, y, z, yaw, pitch);
                }
            }
            if (tpLocation != null) {
                for (Player player : players) {
                    if (CPU.getOutput().getBlock().getLocation().getWorld().equals(player.getWorld())) {
                        if (player.getLocation().distance(CPU.getOutput().getBlock().getLocation().add(0.5f, 0f, 0.5f)) < 0.5f) {
                            if (InventoryListener.players.contains(player.getName())) continue;
                            player.teleport(tpLocation);
                            player.sendMessage(ChatColor.GREEN + "[CPU] Teleported you to destination!");
                        }
                    }
                }
            }
            state = true;
            return;
        }
        state = false;
    }

    private boolean isEmpty(ItemStack[] itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                return false;
            }
        }
        return true;
    }
}
