package io.github.adainish.breedinguses.util;

import io.github.adainish.breedinguses.BreedingUses;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.Level;

public class PermissionWrapper {
    public static String userPermission = "breedinguses.user.base";
    public static String adminPermission = "breedinguses.admin";
    public PermissionWrapper() {
        registerPermissions();
    }
    public void registerPermissions() {
        registerCommandPermission(userPermission, "The base permission players need to use it");
        registerCommandPermission(adminPermission, "The admin permission");
    }
    public static void registerCommandPermission(String s) {
        if (s == null || s.isEmpty()) {
            BreedingUses.log.log(Level.FATAL, "Trying to register a permission node failed, please check any configs for null/empty Configs");
            return;
        }
        PermissionAPI.registerNode(s, DefaultPermissionLevel.NONE, s);
    }

    public static void registerCommandPermission(String s, String description) {
        if (s == null || s.isEmpty()) {
            BreedingUses.log.log(Level.FATAL, "Trying to register a permission node failed, please check any configs for null/empty Configs");
            return;
        }
        PermissionAPI.registerNode(s, DefaultPermissionLevel.NONE, description);
    }
}
