package pl.dejwideek.mbwcheckplayerarena;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.dejwideek.mbwcheckplayerarena.commands.CheckCommand;
import pl.dejwideek.mbwcheckplayerarena.commands.ReloadCommand;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ALL")
public class CheckPlayerAddon extends JavaPlugin {

    public YamlDocument config;

    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("MBedwars") != null) {
            final int supportedAPIVersion = 14;

            try {
                Class apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
                int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

                if (apiVersion < supportedAPIVersion)
                    throw new IllegalStateException();
            } catch (Exception e) {
                this.getLogger().warning("Your MBedwars version is not supported. Supported version: 5.0.14 or higher!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            try {
                config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                        GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(),
                        DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            new UpdateChecker(this, 104524).getVersion(version -> {
                if (this.getDescription().getVersion().equals(version)) {
                    this.getLogger().info("You are using latest version.");
                }
                else {
                    this.getLogger().info("There is a new update available. (v" + version + ")");
                    this.getLogger().info("https://spigotmc.org/resources/104524/updates");
                }
            });

            this.getCommand("checkarena").setExecutor(new CheckCommand(this));
            this.getCommand("checkarenareload").setExecutor(new ReloadCommand(this));
        } else {
            this.getLogger().warning("MBedwars is not enabled!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
