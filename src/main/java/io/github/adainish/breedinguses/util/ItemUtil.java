package io.github.adainish.breedinguses.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import info.pixelmon.repack.org.spongepowered.serialize.SerializationException;
import io.github.adainish.breedinguses.config.Config;
import io.leangen.geantyref.TypeToken;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemUtil {

    public static void makeBreedingItem(ItemStack stack, int amount, UUID uuid) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("isUseBreedingItem", true);
        stack.setTag(nbt);
        setStackCount(stack, amount, uuid);
    }
    public static boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().equals(PixelmonItems.destiny_knot) || stack.getItem().equals(PixelmonItems.ever_stone) || stack.getItem().equals(PixelmonItems.power_band) || stack.getItem().equals(PixelmonItems.power_anklet) || stack.getItem().equals(PixelmonItems.power_belt) || stack.getItem().equals(PixelmonItems.power_lens) || stack.getItem().equals(PixelmonItems.power_weight);
    }
    public static boolean isUseBreedingItem(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().getBoolean("isUseBreedingItem");
        }
        return false;
    }

    public static void handleEggProduction(Pokemon pokemon) {
        if (pokemon.getHeldItem().isEmpty())
            return;
        if (!isUseBreedingItem(pokemon.getHeldItem().getStack()))
            return;
        reduceStackCount(pokemon.getOwnerPlayerUUID(), pokemon.getHeldItem().getStack(), 1);
        String msg = Config.getConfig().get().node("ranch-message").getString();
        msg = msg.replace("%{pokemon}", pokemon.getDisplayName())
                .replace("%{item}", pokemon.getHeldItem().getStack().getDisplayName().getUnformattedComponentText())
                .replace("%{uses}", String.valueOf(getUseCountLeft(pokemon.getHeldItem().getStack())));
        Util.send(pokemon.getOwnerPlayerUUID(), msg);
    }

    public static void setStackCount(ItemStack stack, int amount, UUID ownerUUID) {
        stack.getTag().putInt("itemBreedUses", amount);
        updateLore(stack, amount, ownerUUID);
    }

    public static void reduceStackCount(UUID ownerUUID, ItemStack stack, int amount) {
        if (isUseBreedingItem(stack)) {
            int newInt = getUseCountLeft(stack) - amount;
            if (newInt == 0) {
                stack.setCount(0);
                updateLore(stack, amount, ownerUUID);
                return;
            }
            stack.getTag().putInt("itemBreedUses", newInt);
            updateLore(stack, amount, ownerUUID);
        }
    }

    public static void updateLore(ItemStack stack, int amount, UUID ownerUUID) {
        List<String> lore = new ArrayList<>();
        try {
            lore.addAll(Config.getConfig().get().node("lore-message").getList(TypeToken.get(String.class)));
        } catch (Exception e) {
            Util.send(ownerUUID, "&cSomething went wrong while updating your item please contact a staff member!");
            e.printStackTrace();
        }
        ListNBT nbtLore = new ListNBT();
        for (String line : lore) {
            if (line != null) {
                String formattedLine = line.replace("%{uses}", String.valueOf(amount));
                nbtLore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(TextUtil.parseHexCodes(Util.formattedString(formattedLine), false))));
            }
        }
        stack.getOrCreateChildTag("display").put("Lore", nbtLore);
    }

    public static int getUseCountLeft(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().getInt("itemBreedUses");
        }
        return 0;
    }
}
