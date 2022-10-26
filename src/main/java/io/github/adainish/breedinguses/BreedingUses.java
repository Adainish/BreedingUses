package io.github.adainish.breedinguses;

import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.adainish.breedinguses.command.Command;
import io.github.adainish.breedinguses.config.Config;
import io.github.adainish.breedinguses.events.DaycareListener;
import io.github.adainish.breedinguses.util.PermissionWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("breedinguses")
public class BreedingUses {
    public static BreedingUses instance;
    public static BreedingUses getInstance() {return instance;}
    public static final String MOD_NAME = "BreedingUses";
    public static final String VERSION = "1.0.0";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2022";
    public static MinecraftServer server;
    private static File configDir;

    public static PermissionWrapper permissionWrapper;
    public static final Logger log = LogManager.getLogger(MOD_NAME);
    public BreedingUses() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        BreedingUses.configDir = configDir;
    }

    private void setup(final FMLCommonSetupEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        setupDirectories();
    }

    @SubscribeEvent
    public void onCommandRegistry(RegisterCommandsEvent event) {
        permissionWrapper = new PermissionWrapper();
        event.getDispatcher().register(Command.getCommand());
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        setupConfigs();
        loadConfigs();
    }

    @SubscribeEvent
    public void onServerStartedEvent(FMLServerStartedEvent event) {
        //register server
        server = ServerLifecycleHooks.getCurrentServer();
        //register events
        Pixelmon.EVENT_BUS.register(new DaycareListener());
    }

    public void setupDirectories() {
        log.warn("Setting up Config Paths");
        setConfigDir(new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).toString()));
        getConfigDir().mkdir();
    }

    public void setupConfigs() {
        log.warn("Writing configs if non-existent");
        Config.getConfig().setup();
    }

    public void loadConfigs() {
        log.warn("Loading config data");
        Config.getConfig().load();
    }

    public void reload() {
        log.warn("Reload requested, please check the console for errors!");
        setupDirectories();
        setupConfigs();
        loadConfigs();

    }

}
