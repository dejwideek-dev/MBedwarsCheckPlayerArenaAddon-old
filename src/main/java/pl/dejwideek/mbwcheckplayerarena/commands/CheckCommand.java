package pl.dejwideek.mbwcheckplayerarena.commands;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.dejwideek.mbwcheckplayerarena.CheckPlayerAddon;

@SuppressWarnings("ALL")
public class CheckCommand implements CommandExecutor {

    private final CheckPlayerAddon plugin;

    public CheckCommand(CheckPlayerAddon plg) {
        plugin = plg;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        BedwarsAPI.onReady(() -> {
            if(commandSender instanceof Player) {
                Player p = (Player) commandSender;

                if(p.hasPermission(plugin.config.getString("Permissions.Check"))) {
                    if(strings.length == 0) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("Messages.Usage")));
                        return;
                    }
                    if(strings.length >= 1) {
                        Player target = Bukkit.getPlayer(strings[0]);

                        if(target != null) {
                            Arena a = BedwarsAPI.getGameAPI().getArenaByPlayer(target);

                            if(a != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("Messages.Arena-Info")
                                        .replaceAll("%player%", target.getName())
                                        .replaceAll("%arena%", a.getName())));
                            }
                            if(a == null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("Messages.No-Inside-Arena")));
                            }
                        }
                        if(target == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("Messages.Not-Found")));
                        }
                    }
                }
                else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("Messages.No-Permission")
                            .replaceAll("%permission%", plugin.config.getString("Permissions.Check"))));
                    return;
                }
            }
        });

        return true;
    }
}
