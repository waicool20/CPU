package me.waicool20.cpu.CPU.Types;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class ItemSorter extends Type {

    private ArrayList<Integer> LeftIDList = new ArrayList<Integer>();
    private ArrayList<Integer> RightIDList = new ArrayList<Integer>();

    public ItemSorter(me.waicool20.cpu.CPU.CPU cpu) {
        CPU = cpu;
        this.setName("ItemSorter");
    }

    public ItemSorter() {
        this.setName("ItemSorter");
    }

    @Override
    public ItemStack[] typeInventory() {
        ItemStack[] typeInventory = {null, null, null, null, redR, null, null, null, null,
                                    null, null, null, redW, HOPP, redW, null, null, null,
                                    DISP, redW, redW, redR, null, redR, redW, redW, DISP,};
        return typeInventory;
    }

    @Override
    public void updatePower() {

    }

    public void updateList() {
        LeftIDList.clear();
        RightIDList.clear();
        Inventory Inv1 = CPU.getInput1().getInventory();
        Inventory Inv2 = CPU.getInput2().getInventory();
        ItemStack LeftBook;
        ItemStack RightBook;
        for (ItemStack itemStack : Inv1.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.WRITTEN_BOOK) {
                LeftBook = itemStack;
                BookMeta LeftBkMeta = (BookMeta) LeftBook.getItemMeta();
                if (LeftBkMeta.getTitle().equalsIgnoreCase("ItemList")) {
                    String[] List = LeftBkMeta.getPage(1).split(",");
                    for (String ID : List) {
                        LeftIDList.add(Integer.parseInt(ID));
                    }
                    break;
                }
            }
        }
        for (ItemStack itemStack : Inv2.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.WRITTEN_BOOK) {
                RightBook = itemStack;
                BookMeta RightBkMeta = (BookMeta) RightBook.getItemMeta();
                if (RightBkMeta.getTitle().equalsIgnoreCase("ItemList")) {
                    String[] List = RightBkMeta.getPage(1).split(",");
                    for (String ID : List) {
                        RightIDList.add(Integer.parseInt(ID));
                    }
                    break;
                }
            }
        }
    }

    public ArrayList<Integer> getRightIDList() {
        return RightIDList;
    }

    public ArrayList<Integer> getLeftIDList() {
        return LeftIDList;
    }
}
