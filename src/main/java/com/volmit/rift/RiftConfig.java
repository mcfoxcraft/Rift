package com.volmit.rift;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RiftConfig {

    private static RiftConfig instance;
    private boolean verbose = false;
    private Set<String> deleting = new HashSet<>();

    public static RiftConfig get() {
        if(instance != null)
            return instance;

        File file = FileUtils.file("config.json");
        Rift.info("Loading Rift Config at " + file.getPath() + "...");
        if(file.exists()) {
            try {
                return new Gson().fromJson(FileUtils.readAll(file), RiftConfig.class);
            } catch(Throwable e) {
                Rift.warn("Failed to load Rift Config at " + file.getPath() + ". Using default config.");
            }
        }

        RiftConfig config = new RiftConfig();
        try {
            FileUtils.writeAll(file, new Gson().toJson(config));
        } catch(Throwable e) {
            Rift.error("Failed to save default Rift Config!", e);
            e.printStackTrace();
        }

        instance = config;

        return config;
    }

    public void save() {
        try {
            FileUtils.writeAll(FileUtils.file("config.json"), new Gson().toJson(instance));
        } catch(IOException e) {
            Rift.error("Failed to save Rift Config!", e);
            e.printStackTrace();
        }
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public Set<String> getDeleting() {
        return this.deleting;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RiftConfig other)) {
            return false;
        }
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.verbose != other.verbose) {
            return false;
        }

        return Objects.equals(this.deleting, other.deleting);
    }

    protected boolean canEqual(Object other) {
        return other instanceof RiftConfig;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.isVerbose() ? 79 : 97);
        Set<String> $deleting = this.getDeleting();
        result = result * 59 + ($deleting == null ? 43 : ((Object)$deleting).hashCode());
        return result;
    }

    public String toString() {
        return "RiftConfig(verbose=" + this.isVerbose() + ", deleting=" + this.getDeleting() + ")";
    }
}