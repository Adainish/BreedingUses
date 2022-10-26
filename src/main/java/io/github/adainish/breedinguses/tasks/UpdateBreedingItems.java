package io.github.adainish.breedinguses.tasks;

import io.github.adainish.breedinguses.config.Config;
import io.github.adainish.breedinguses.util.ItemUtil;
import io.github.adainish.breedinguses.util.Util;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class UpdateBreedingItems implements Runnable {
    @Override
    public void run() {
        try {
            if (Util.server.getPlayerList().getPlayers().isEmpty())
                return;
            for (ServerPlayerEntity pl : Util.server.getPlayerList().getPlayers()) {
                for (int i = 0; i < pl.inventory.getSizeInventory(); i++) {
                    ItemStack stack = pl.inventory.getStackInSlot(i);
                    if (stack.isEmpty())
                        continue;
                    if (!ItemUtil.isBreedingItem(stack))
                        continue;
                    if (ItemUtil.isUseBreedingItem(stack))
                        continue;
                    ItemUtil.makeBreedingItem(stack, Config.getConfig().get().node("breeding-uses").getInt(), pl.getUniqueID());
                }

            }
        } catch (NullPointerException e) {

        }
    }
}
