/*
 * This file ("PageCrafting.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.booklet.page;

import de.ellpeck.actuallyadditions.api.internal.IBookletGui;
import de.ellpeck.actuallyadditions.mod.booklet.GuiBooklet;
import de.ellpeck.actuallyadditions.mod.proxy.ClientProxy;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import de.ellpeck.actuallyadditions.mod.util.ModUtil;
import de.ellpeck.actuallyadditions.mod.util.StringUtil;
import de.ellpeck.actuallyadditions.mod.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;


public class PageCrafting extends BookletPageAA{

    private final IRecipe[] recipes;
    private int recipePos;

    public PageCrafting(int id, ArrayList<IRecipe> recipes){
        this(id, recipes.toArray(new IRecipe[recipes.size()]));
    }

    public PageCrafting(int id, IRecipe... recipes){
        super(id);
        this.recipes = recipes;
        this.addToPagesWithItemStackData();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderPre(IBookletGui gui, int mouseX, int mouseY, int ticksElapsed, boolean mousePressed){
        if(this.recipes[this.recipePos] != null){
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiBooklet.RES_LOC);
            gui.drawRect(gui.getGuiLeft()+27, gui.getGuiTop()+20, 146, 20, 99, 60);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(IBookletGui gui, int mouseX, int mouseY, int ticksElapsed, boolean mousePressed){
        IRecipe recipe = this.recipes[this.recipePos];

        if(recipe == null){
            StringUtil.drawSplitString(Minecraft.getMinecraft().fontRendererObj, TextFormatting.DARK_RED+StringUtil.localize("booklet."+ModUtil.MOD_ID+".recipeDisabled"), gui.getGuiLeft()+14, gui.getGuiTop()+15, 115, 0, false);
        }
        else{
            String strg = StringUtil.localize("booklet."+ModUtil.MOD_ID+"."+(recipe instanceof ShapedRecipes ? "shapedRecipe" : (recipe instanceof ShapelessRecipes ? "shapelessRecipe" : (recipe instanceof ShapelessOreRecipe ? "shapelessOreRecipe" : "shapedOreRecipe"))));
            Minecraft.getMinecraft().fontRendererObj.drawString(strg, gui.getGuiLeft()+gui.getXSize()/2-Minecraft.getMinecraft().fontRendererObj.getStringWidth(strg)/2, gui.getGuiTop()+10, 0);
        }

        String text = gui.getCurrentEntrySet().getCurrentPage().getText();
        if(text != null && !text.isEmpty()){
            StringUtil.drawSplitString(Minecraft.getMinecraft().fontRendererObj, text, gui.getGuiLeft()+14, gui.getGuiTop()+90, 115, 0, false);
        }

        if(recipe != null){

            ItemStack[] stacks = new ItemStack[9];
            int width = 3;
            int height = 3;

            if(recipe instanceof ShapedRecipes){
                ShapedRecipes shaped = (ShapedRecipes)recipe;
                width = shaped.recipeWidth;
                height = shaped.recipeHeight;
                for(int i = 0; i < shaped.recipeItems.length; i++){
                    ItemStack stack = shaped.recipeItems[i];
                    if(stack != null){
                        stacks[i] = stack.copy();
                    }
                }
            }
            else if(recipe instanceof ShapelessRecipes){
                ShapelessRecipes shapeless = (ShapelessRecipes)recipe;
                for(int i = 0; i < shapeless.recipeItems.size(); i++){
                    ItemStack stack = shapeless.recipeItems.get(i);
                    if(stack != null){
                        stacks[i] = stack.copy();
                    }
                }
            }
            else if(recipe instanceof ShapedOreRecipe){
                ShapedOreRecipe shaped = (ShapedOreRecipe)recipe;
                try{
                    width = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 4);
                    height = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 5);
                }
                catch(Exception e){
                    ModUtil.LOGGER.error("Something went wrong trying to get the Crafting Recipe in the booklet to display!", e);
                }
                for(int i = 0; i < shaped.getInput().length; i++){
                    Object input = shaped.getInput()[i];
                    if(input != null){
                        ItemStack stack = input instanceof ItemStack ? (ItemStack)input : (((List<ItemStack>)input).isEmpty() ? null : ((List<ItemStack>)input).get(0));
                        if(stack != null){
                            stacks[i] = stack.copy();
                        }
                    }
                }
            }
            else if(recipe instanceof ShapelessOreRecipe){
                ShapelessOreRecipe shapeless = (ShapelessOreRecipe)recipe;
                for(int i = 0; i < shapeless.getInput().size(); i++){
                    Object input = shapeless.getInput().get(i);
                    ItemStack stack = input instanceof ItemStack ? (ItemStack)input : (((List<ItemStack>)input).isEmpty() ? null : ((List<ItemStack>)input).get(0));
                    if(stack != null){
                        stacks[i] = stack.copy();
                    }
                }
            }

            int xShowOutput = gui.getGuiLeft()+27+82;
            int yShowOutput = gui.getGuiTop()+20+22;
            AssetUtil.renderStackToGui(recipe.getRecipeOutput(), xShowOutput, yShowOutput, 1.0F);
            for(int i = 0; i < 2; i++){
                boolean tooltip = i == 1;
                for(int x = 0; x < width; x++){
                    for(int y = 0; y < height; y++){
                        ItemStack stack = stacks[y*width+x];
                        if(stack != null){
                            stack.stackSize = 1;
                            if(stack.getItemDamage() == Util.WILDCARD){
                                stack.setItemDamage(0);
                            }
                            int xShow = gui.getGuiLeft()+27+4+x*18;
                            int yShow = gui.getGuiTop()+20+4+y*18;
                            if(!tooltip){
                                AssetUtil.renderStackToGui(stack, xShow, yShow, 1.0F);
                            }
                            else{
                                if(mouseX >= xShow && mouseX <= xShow+16 && mouseY >= yShow && mouseY <= yShow+16){
                                    gui.renderTooltipAndTransferButton(this, stack, mouseX, mouseY, true, mousePressed);
                                }
                            }
                        }
                    }
                }
            }
            if(mouseX >= xShowOutput && mouseX <= xShowOutput+16 && mouseY >= yShowOutput && mouseY <= yShowOutput+16){
                gui.renderTooltipAndTransferButton(this, recipe.getRecipeOutput(), mouseX, mouseY, false, mousePressed);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateScreen(int ticksElapsed){
        if(ticksElapsed%15 == 0){
            if(this.recipePos+1 >= this.recipes.length){
                this.recipePos = 0;
            }
            else{
                this.recipePos++;
            }
        }
    }

    @Override
    public ItemStack[] getItemStacksForPage(){
        if(this.recipes != null){
            ItemStack[] stacks = new ItemStack[this.recipes.length];
            for(int i = 0; i < this.recipes.length; i++){
                if(this.recipes[i] != null){
                    ItemStack output = this.recipes[i].getRecipeOutput();
                    if(output != null){
                        if(!this.arePageStacksWildcard){
                            stacks[i] = output;
                        }
                        else{
                            ItemStack wildcardOutput = output.copy();
                            wildcardOutput.setItemDamage(Util.WILDCARD);
                            stacks[i] = wildcardOutput;
                        }
                    }
                }
            }
            return stacks;
        }
        return null;
    }
}
