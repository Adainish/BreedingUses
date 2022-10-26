package io.github.adainish.breedinguses.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.adainish.breedinguses.BreedingUses;
import io.github.adainish.breedinguses.config.Config;
import io.github.adainish.breedinguses.util.ItemUtil;
import io.github.adainish.breedinguses.util.PermissionUtil;
import io.github.adainish.breedinguses.util.PermissionWrapper;
import io.github.adainish.breedinguses.util.Util;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemStack;

public class Command {
    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("breedinguses")
                .requires(cs -> PermissionUtil.checkPermAsPlayer(cs, PermissionWrapper.userPermission))
                .executes(cc -> {
                    Util.send(cc.getSource(), "&b&l(&e&l!&b&l) &cPlease provide a valid argument such as reload or set <uses>");
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("reload")
                        .requires(cs -> PermissionUtil.checkPermAsPlayer(cs, PermissionWrapper.adminPermission))
                        .executes(cc -> {
                            BreedingUses.instance.reload();
                            Util.send(cc.getSource(), "&b&l(&e&l!&b&l) &aReloaded the config file(s), please check your console for any errors");
                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("set")
                        .requires(cs -> PermissionUtil.checkPermAsPlayer(cs, PermissionWrapper.adminPermission))
                        .executes(cc -> {
                            ItemStack stack = cc.getSource().asPlayer().getHeldItemMainhand();
                            if (stack.isEmpty()) {
                                Util.send(cc.getSource(), "&b&l(&e&l!&b&l) &eYou're currently not holding any items in your hand!");
                                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                            }
                            if (ItemUtil.isBreedingItem(stack)) {
                                ItemUtil.makeBreedingItem(stack, Config.getConfig().get().node("breeding-uses").getInt(), cc.getSource().asPlayer().getUniqueID());
                            } else {
                                Util.send(cc.getSource(), "&b&l(&e&l!&b&l) &aThis is not a recognised breeding item, you may need ");
                            }
                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(cc -> {
                                    int amount = IntegerArgumentType.getInteger(cc, "amount");
                                    ItemStack stack = cc.getSource().asPlayer().getHeldItemMainhand();
                                    if (stack.isEmpty()) {
                                        Util.send(cc.getSource(), "&b&l(&e&l!&b&l) &eYou're currently not holding any items in your hand!");
                                        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                                    }
                                    if (ItemUtil.isBreedingItem(stack)) {
                                        ItemUtil.makeBreedingItem(stack, amount, cc.getSource().asPlayer().getUniqueID());
                                    } else {
                                        Util.send(cc.getSource(), "&b&l(&e&l!&b&l) &aThis is not a recognised breeding item, you may need ");
                                    }
                                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                                })
                        )
                )
                ;
    }
}
