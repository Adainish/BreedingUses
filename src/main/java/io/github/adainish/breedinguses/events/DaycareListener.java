package io.github.adainish.breedinguses.events;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import io.github.adainish.breedinguses.util.ItemUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DaycareListener {

    @SubscribeEvent
    public void onCollectEvent(DayCareEvent.PostEggCalculate event) {
        ItemUtil.handleEggProduction(event.getParentOne());
        ItemUtil.handleEggProduction(event.getParentTwo());
    }
}
