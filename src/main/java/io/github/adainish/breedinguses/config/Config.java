package io.github.adainish.breedinguses.config;

import info.pixelmon.repack.org.spongepowered.serialize.SerializationException;
import io.github.adainish.breedinguses.BreedingUses;

import java.util.Arrays;

public class Config extends Configurable {
    private static Config config;

    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public void setup() {
        super.setup();
    }

    public void load() {
        super.load();
    }

    public void save() {super.save();}
    @Override
    public void populate() {
        try {
            this.get().node("breeding-uses").set(100);
            this.get().node("lore-message").set(Arrays.asList("&r&6%{uses} &bUses Left"));
            this.get().node("ranch-message").set("&b%{pokemon}&b's &e%{item} &bhas &6%{uses} &buses left");
        } catch (SerializationException e) {
            BreedingUses.log.warn(e.getMessage());
        }
    }

    @Override
    public String getConfigName() {
        return "config.yaml";
    }
}
