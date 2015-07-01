package ellpeck.actuallyadditions.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import ellpeck.actuallyadditions.achievement.InitAchievements;

public class PickupEvent{

    @SubscribeEvent
    public void onPickupEvent(PlayerEvent.ItemPickupEvent event){
        CraftEvent.checkAchievements(event.pickedUp.getEntityItem(), event.player, InitAchievements.PICKUP_ACH);
    }
}
