package me.waicool20.cpu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CraftingAndRecipes implements Listener {

    @SuppressWarnings("deprecation")
    static void addRecipes(){
        /**Redstone Apple Recipe**/
        ShapedRecipe redstoneApple = new ShapedRecipe(redstoneApple());
        redstoneApple.shape("XXX",
                            "XAX",
                            "XXX");
        redstoneApple.setIngredient('X',Material.REDSTONE);
        redstoneApple.setIngredient('A',Material.APPLE);
        Bukkit.getServer().addRecipe(redstoneApple);

        /**Redstone Activator Recipe**/
        ShapedRecipe Activator = new ShapedRecipe(redstoneActivator());
        Activator.shape("XXX",
                        "XXX",
                        "XXX");
        Activator.setIngredient('X', Material.APPLE, (short)1000);
        Bukkit.getServer().addRecipe(Activator);
    }

    public static ItemStack redstoneActivator() {
        ItemStack itemStack = new ItemStack(Material.REDSTONE_BLOCK,1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Redstone Activator");
        /**ADD LORE**/
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GREEN + "Used to activate Redstone Modules!");
        itemMeta.setLore(lore);
        Enchantment DDR = new EnchantmentWrapper(34);
        itemMeta.addEnchant(DDR,1,true);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack redstoneApple(){
        ItemStack itemStack = new ItemStack(Material.APPLE,1,(short)1000);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Redstone Apple");
        /**ADD LORE**/
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GREEN + "Used to make Redstone Activators!");
        lore.add(ChatColor.GREEN + "A stomach full of redstone!");
        itemMeta.setLore(lore);
        Enchantment enchantment = new EnchantmentWrapper(34);
        itemMeta.addEnchant(enchantment,1,true);


        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack typifier(){
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD,1,(short)1000);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Typifier");
        /**ADD LORE**/
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GREEN + "Create CPUs!");
        lore.add(ChatColor.GREEN + "Loop through types!");
        itemMeta.setLore(lore);
        Enchantment enchantment = new EnchantmentWrapper(34);
        itemMeta.addEnchant(enchantment,1,true);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
