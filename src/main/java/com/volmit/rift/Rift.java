package com.volmit.rift;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rift extends JavaPlugin {

    public static Rift INSTANCE;

    private List<RiftWorldConfig> configs;

    public void onEnable() {
        INSTANCE = this;
        configs = RiftWorldConfig.loadAll();

        RiftConfig.get().getDeleting().stream().filter(f -> new File(f).exists()).forEach(f -> {
            info("Attempting to cleanup remaining deleted world files for " + f);
            FileUtils.deleteWorld(new File(f));
        });

        configs.forEach(this::init);
        checkForBukkitWorlds();

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            RiftCommand.init(commands);
        });
    }

    public void onDisable() { }

    private void init(RiftWorldConfig c) {
        File f = new File(c.getName());
        String generator = c.getGenerator();
        Bukkit.getWorlds().stream().filter(w -> !w.getWorldFolder().equals(f)).forEach(w -> WorldCreator.name(f.getName()).generator(!generator.equals("normal") ? generator : null)
                .type(generator.equalsIgnoreCase("flat") ? WorldType.FLAT : generator.equalsIgnoreCase("amplified") ? WorldType.AMPLIFIED : generator.equalsIgnoreCase("largebiomes") ? WorldType.LARGE_BIOMES : WorldType.NORMAL)
                .createWorld());
    }

    private void checkForBukkitWorlds() {
        FileConfiguration fc = new YamlConfiguration();
        Set<String> hit = new HashSet<>();
        try {
            fc.load(new File("bukkit.yml"));
            if(!fc.isConfigurationSection("worlds"))
                return;

            ConfigurationSection section = fc.getConfigurationSection("worlds");
            for(String worldName : section.getKeys(false)) {
                if(hit.contains(worldName))
                    continue;
                hit.add(worldName);

                String generator = section.getString(worldName + ".generator");

                if(generator != null && generator.startsWith("Iris")) {
                    info("World \"" + worldName + "\" is a Iris World, skipping.");
                    continue;
                }

                if(Bukkit.getWorlds().stream().anyMatch(w -> w.getName().equals(worldName)))
                    continue;

                info("Loading world \"" + worldName + "\" from bukkit.yml using generator \"" + generator + "\"...");
                new WorldCreator(worldName)
                        .generator(generator != null && !generator.equals("normal") ? generator : null)
                        .type(generator != null ? generator.equalsIgnoreCase("flat") ? WorldType.FLAT : generator.equalsIgnoreCase("amplified") ? WorldType.AMPLIFIED : generator.equalsIgnoreCase("largebiomes") ? WorldType.LARGE_BIOMES : WorldType.NORMAL : WorldType.NORMAL)
                        .createWorld();
                info("Successfully loaded world \"" + worldName + "\" from bukkit.yml!");
            }
        } catch(Throwable e) {
            error("Failed to load bukkit.yml worlds!", e);
            e.printStackTrace();
        }
    }

    public List<RiftWorldConfig> getConfigs() {
        return configs;
    }

    public static void info(String message) {
        INSTANCE.getLogger().info(message);
    }

    public static void warn(String message) {
        INSTANCE.getLogger().warning(message);
    }

    public static void error(String message, Throwable t) {
        INSTANCE.getLogger().severe(t.getClass().getSimpleName() + " | " + message);
        if(t.getMessage() != null)
            INSTANCE.getLogger().severe("\t" + t.getMessage());
    }

    public static void verbose(String message) {
        if(RiftConfig.get().isVerbose())
            INSTANCE.getLogger().fine(message);
    }
}
