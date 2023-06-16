package com.volmit.rift;

import com.google.gson.Gson;
import org.bukkit.World;
import org.bukkit.WorldType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RiftWorldConfig  {

    private String name = "undefined";
    private long seed = new Random().nextLong();
    private World.Environment environment;
    private String generator;
    private WorldType type = WorldType.NORMAL;

    public void save() {
        try {
            FileUtils.writeAll(FileUtils.file("worlds/" + getName() + ".json"), new Gson().toJson(this));
        } catch(IOException e) {
            Rift.error("Failed to save World Config for \"" + name + "\"!", e);
            e.printStackTrace();
        }
    }

    public static List<RiftWorldConfig> loadAll() {
        List<RiftWorldConfig> configs = new ArrayList<>();
        Arrays.stream(FileUtils.folder("worlds").listFiles())
                .filter(f -> f.isFile() && f.getName().endsWith(".json"))
                .forEach(f -> configs.add(get(f)));
        return configs;
    }

    public static RiftWorldConfig from(World world, String generator) {
        RiftWorldConfig config = new RiftWorldConfig();
        config.setName(world.getName());
        config.setSeed(world.getSeed());
        config.setEnvironment(world.getEnvironment());
        config.setGenerator(generator);
        return config;
    }

    private static RiftWorldConfig get(File file) {
        Rift.info("Loading World Config at " + file.getPath() + "...");
        if(file.exists()) {
            try {
                return new Gson().fromJson(FileUtils.readAll(file), RiftWorldConfig.class);
            } catch(Throwable e) {
                Rift.warn("Failed to load World Config at " + file.getPath() + ". Using default config.");
            }
        }

        RiftWorldConfig config = new RiftWorldConfig();
        try {
            FileUtils.writeAll(file, new Gson().toJson(config));
        } catch(Throwable e) {
            Rift.error("Failed to save default World Config!", e);
            e.printStackTrace();
        }

        return config;
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return this.seed;
    }

    public World.Environment getEnvironment() {
        return this.environment;
    }

    public String getGenerator() {
        return this.generator;
    }

    public WorldType getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RiftWorldConfig other)) {
            return false;
        }
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSeed() != other.getSeed()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) {
            return false;
        }
        World.Environment this$environment = this.getEnvironment();
        World.Environment other$environment = other.getEnvironment();
        if (!Objects.equals(this$environment, other$environment)) {
            return false;
        }
        String this$generator = this.getGenerator();
        String other$generator = other.getGenerator();
        if (!Objects.equals(this$generator, other$generator)) {
            return false;
        }
        WorldType this$type = this.getType();
        WorldType other$type = other.getType();
        return Objects.equals(this$type, other$type);
    }

    protected boolean canEqual(Object other) {
        return other instanceof RiftWorldConfig;
    }

    public int hashCode() {
        int result = 1;
        long $seed = this.getSeed();
        result = result * 59 + (int)($seed >>> 32 ^ $seed);
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        World.Environment $environment = this.getEnvironment();
        result = result * 59 + ($environment == null ? 43 : $environment.hashCode());
        String $generator = this.getGenerator();
        result = result * 59 + ($generator == null ? 43 : $generator.hashCode());
        WorldType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    public String toString() {
        return "RiftWorldConfig(name=" + this.getName() + ", seed=" + this.getSeed() + ", environment=" + this.getEnvironment() + ", generator=" + this.getGenerator() + ", type=" + this.getType() + ")";
    }
}