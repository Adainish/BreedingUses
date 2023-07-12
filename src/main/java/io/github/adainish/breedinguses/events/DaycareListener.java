package io.github.adainish.breedinguses.events;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import io.github.adainish.breedinguses.util.ItemUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DaycareListener {

    @SubscribeEvent
    public void onEggAttempt(DayCareEvent.PrePokemonAdd event)
    {
        if (event.getParentOne() != null) {
            if (event.getParentOne().hasFlag("unbreedable")) {
                event.setCanceled(true);
            }
        }
        if (event.getParentTwo() != null) {
            if (event.getParentTwo().hasFlag("unbreedable")) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void onCollectEvent(DayCareEvent.PostCollect event) {
        ItemUtil.handleEggProduction(event.getParentOne());
        ItemUtil.handleEggProduction(event.getParentTwo());
    }
}
