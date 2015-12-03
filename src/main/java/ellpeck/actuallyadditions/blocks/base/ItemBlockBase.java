/*
 * This file ("ItemBlockBase.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://github.com/Ellpeck/ActuallyAdditions/blob/master/README.md
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015 Ellpeck
 */

package ellpeck.actuallyadditions.blocks.base;

import ellpeck.actuallyadditions.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlock{

    public ItemBlockBase(Block block){
        super(block);
        this.setHasSubtypes(false);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return this.getUnlocalizedName();
    }

    @Override
    public int getMetadata(int damage){
        return damage;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        if(this.field_150939_a instanceof BlockBase){
            return ((BlockBase)this.field_150939_a).getRarity(stack);
        }
        else if(this.field_150939_a instanceof BlockContainerBase){
            return ((BlockContainerBase)this.field_150939_a).getRarity(stack);
        }
        else if(this.field_150939_a instanceof BlockFluidFlowing){
            return ((BlockFluidFlowing)this.field_150939_a).getRarity(stack);
        }
        else if(this.field_150939_a instanceof BlockPlant){
            return ((BlockPlant)this.field_150939_a).getRarity(stack);
        }
        else if(this.field_150939_a instanceof BlockStair){
            return ((BlockStair)this.field_150939_a).getRarity(stack);
        }
        else if(this.field_150939_a instanceof BlockWallAA){
            return ((BlockWallAA)this.field_150939_a).getRarity(stack);
        }
        else if(this.field_150939_a instanceof BlockBushBase){
            return ((BlockBushBase)this.field_150939_a).getRarity(stack);
        }
        else{
            return Util.FALLBACK_RARITY;
        }
    }
}
