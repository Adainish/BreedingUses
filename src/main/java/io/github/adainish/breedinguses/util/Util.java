package io.github.adainish.breedinguses.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import io.github.adainish.breedinguses.BreedingUses;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Util {
    public static MinecraftServer server = BreedingUses.server;

    private static final MinecraftServer SERVER = server;
    public static boolean isPlayerOnline(ServerPlayerEntity player) {
        return isPlayerOnline(player.getUniqueID());
    }

    public static boolean isPlayerOnline(String username) {
        ServerPlayerEntity player = SERVER.getPlayerList().getPlayerByUsername(username);
        // IJ says it's always true ignore
        return player != null;
    }
    public static boolean isPlayerOnline(UUID uuid) {
        ServerPlayerEntity player = SERVER.getPlayerList().getPlayerByUUID(uuid);
        // IJ says it's always true ignore
        return player != null;
    }
    public static void send(ServerPlayerEntity sender, String message) {
        sender.sendMessage(new StringTextComponent((message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")), sender.getUniqueID());
    }
    public static ServerPlayerEntity getPlayer(String playerName) {
        return server.getPlayerList().getPlayerByUsername(playerName);
    }

    public static ServerPlayerEntity getPlayer(UUID uuid) {
        return server.getPlayerList().getPlayerByUUID(uuid);
    }
    public static void send(UUID uuid, String message) {
        ServerPlayerEntity playerEntity = getPlayer(uuid);
        if (playerEntity == null)
            return;
        send(playerEntity, message);
    }

    public static void send(CommandSource sender, String message) {
        sender.sendFeedback(new StringTextComponent((message).replaceAll("&([0-9a-fk-or])", "\u00a7$1")), true);
    }

    public static RegistryKey<World> getDimension(String dimension) {
        return dimension.isEmpty() ? null : getDimension(ResourceLocationHelper.of(dimension));
    }

    public static RegistryKey<World> getDimension(ResourceLocation key) {
        return RegistryKey.getOrCreateKey(Registry.WORLD_KEY, key);
    }

    public static Optional<ServerWorld> getWorld(RegistryKey<World> key) {
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer().getWorld(key));
    }

    public static Optional<ServerWorld> getWorld(String key) {
        return getWorld(getDimension(key));
    }



    public static ArrayList<String> pokemonLore(Pokemon p) {
        ArrayList<String> list = new ArrayList<>();
        list.add("&9Click to select this Pokemon");
        list.add("&9You can select a max of 3 Pokemon for the Challenge");
        list.add("");
        list.add("&7Ball:&e " + p.getBall().getName().replace("_", ""));
        list.add("&7Ability:&e " + p.getAbility().getName());
        list.add("&7Nature:&e " + p.getNature().name());
        list.add("&7Gender:&e " + p.getGender().name().toLowerCase());
        list.add("&7Size:&e " + p.getGrowth().name());
        if (p.getPalette().getTexture() != null) {
            if (!p.getPalette().getTexture().toString().isEmpty()) {
                list.add("&5Custom Texture: &b" + p.getPalette().getName());
            }
        }
        list.add("&7IVS: (&f%ivs%%&7)".replace("%ivs%", String.valueOf(p.getIVs().getPercentage(1))));
        list.add("&cHP: %hp% &7/ &6Atk: %atk% &7/ &eDef: %def%"
                .replace("%hp%", String.valueOf(p.getIVs().getStat(BattleStatsType.HP)))
                .replace("%atk%", String.valueOf(p.getIVs().getStat(BattleStatsType.ATTACK)))
                .replace("%def%", String.valueOf(p.getIVs().getStat(BattleStatsType.DEFENSE)))
        );

        list.add("&9SpA: %spa% &7/ &aSpD: %spd% &7/ &dSpe: %spe%"
                .replace("%spa%", String.valueOf(p.getIVs().getStat(BattleStatsType.SPECIAL_ATTACK)))
                .replace("%spd%", String.valueOf(p.getIVs().getStat(BattleStatsType.SPECIAL_DEFENSE)))
                .replace("%spe%", String.valueOf(p.getIVs().getStat(BattleStatsType.SPEED)))
        );

        list.add("&7EVS: (&f%evs%%&7)".replace("%evs%", String.valueOf(getEVSPercentage(1, p))));
        list.add("&cHP: %hp% &7/ &6Atk: %atk% &7/ &eDef: %def%"
                .replace("%hp%", String.valueOf(p.getEVs().getStat(BattleStatsType.HP)))
                .replace("%atk%", String.valueOf(p.getEVs().getStat(BattleStatsType.ATTACK)))
                .replace("%def%", String.valueOf(p.getEVs().getStat(BattleStatsType.DEFENSE)))
        );

        list.add("&9SpA: %spa% &7/ &aSpD: %spd% &7/ &dSpe: %spe%"
                .replace("%spa%", String.valueOf(p.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK)))
                .replace("%spd%", String.valueOf(p.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE)))
                .replace("%spe%", String.valueOf(p.getEVs().getStat(BattleStatsType.SPEED)))
        );


        for (Attack a:p.getMoveset().attacks) {
            if (a == null)
                continue;
            list.add("&7- " + a.getActualMove().getAttackName());
        }

        return list;
    }
    public static int[] getEvsArray(Pokemon p) {
        return new int[]{p.getEVs().getStat(BattleStatsType.HP),
                p.getEVs().getStat(BattleStatsType.ATTACK),
                p.getEVs().getStat(BattleStatsType.DEFENSE),
                p.getEVs().getStat(BattleStatsType.SPECIAL_ATTACK),
                p.getEVs().getStat(BattleStatsType.SPECIAL_DEFENSE),
                p.getEVs().getStat(BattleStatsType.SPEED)};
    }

    public static List<String> formattedArrayList(List<String> list) {

        List<String> formattedList = new ArrayList <>();
        for (String s:list) {
            formattedList.add(formattedString(s));
        }

        return formattedList;
    }

    public static boolean isEvenAmount(int amount, int perc) {
        return amount / perc >= 1;
    }

    public static Item getItemFromString(String id) {
        ResourceLocation location = new ResourceLocation(id);
        return ForgeRegistries.ITEMS.getValue(location);
    }

    public static void runCommand(String cmd) {
        try {
            SERVER.getCommandManager().getDispatcher().execute(cmd, server.getCommandSource());
        } catch (CommandSyntaxException e) {
            BreedingUses.log.error(e);
        }
    }

    public static String formattedPokemonNameString(Pokemon p) {

        String s = "&b%pokemon% &eLvl: %lvl%"
                .replace("%pokemon%", p.getDisplayName())
                .replace("%lvl%", String.valueOf(p.getPokemonLevel()));

        if (p.isShiny())
            s += " &6Shiny";

        return formattedString(s);
    }
    public static String formattedString(String s) {
        return s.replaceAll("&", "§");
    }

    public static double getEVSPercentage(int decimalPlaces, Pokemon p) {
        int total = 0;
        int[] evs = getEvsArray(p);

        for (int evStat : evs) {
            total += evStat;
        }

        double percentage = (double)total / 510.0D * 100.0D;
        return Math.floor(percentage * Math.pow(10.0D, decimalPlaces)) / Math.pow(10.0D, decimalPlaces);
    }
}
