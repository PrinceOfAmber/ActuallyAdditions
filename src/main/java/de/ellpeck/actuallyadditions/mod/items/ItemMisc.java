/*
 * This file ("ItemMisc.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.items.base.ItemBase;
import de.ellpeck.actuallyadditions.mod.items.metalists.TheMiscItems;
import de.ellpeck.actuallyadditions.mod.util.StringUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMisc extends ItemBase{

    public static final TheMiscItems[] ALL_MISC_ITEMS = TheMiscItems.values();

    public ItemMisc(String name){
        super(name);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage){
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return stack.getItemDamage() >= ALL_MISC_ITEMS.length ? StringUtil.BUGGED_ITEM_NAME : this.getUnlocalizedName()+ALL_MISC_ITEMS[stack.getItemDamage()].name;
    }


    @Override
    public EnumRarity getRarity(ItemStack stack){
        return stack.getItemDamage() >= ALL_MISC_ITEMS.length ? EnumRarity.COMMON : ALL_MISC_ITEMS[stack.getItemDamage()].rarity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list){
        for(int j = 0; j < ALL_MISC_ITEMS.length; j++){
            if(j != TheMiscItems.YOUTUBE_ICON.ordinal()){
                list.add(new ItemStack(this, 1, j));
            }
        }
    }

    @Override
    protected void registerRendering(){
        for(int i = 0; i < ALL_MISC_ITEMS.length; i++){
            String name = this.getRegistryName()+ALL_MISC_ITEMS[i].name;
            ActuallyAdditions.proxy.addRenderRegister(new ItemStack(this, 1, i), new ResourceLocation(name), "inventory");
        }
    }
}
