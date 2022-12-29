package pl.dejwideek.mbwcheckplayerarena.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Description;
import com.iridium.iridiumcolorapi.IridiumColorAPI;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.remote.RemoteAPI;
import de.marcely.bedwars.api.remote.RemoteArena;
import de.marcely.bedwars.api.remote.RemotePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.dejwideek.mbwcheckplayerarena.CheckPlayerAddon;

@SuppressWarnings("ALL")
public class CheckCmd extends BaseCommand {

    private static CheckPlayerAddon plugin;

    public CheckCmd(CheckPlayerAddon plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("checkplayerarena|checkarenaplayer|checkarena|checkplayer|check")
    @CommandCompletion("@players")
    @Description("Check player arena")
    public void check(CommandSender commandSender, String[] strings) {
        BedwarsAPI.onReady(() -> {
            if(commandSender instanceof Player) {
                Player p = (Player) commandSender;
                String permission = plugin.config.getString("Permissions.Check");
                String usageMsg = plugin.config.getString("Messages.Usage");
                String arenaInfoMsg = plugin.config.getString("Messages.Arena-Info");
                String noInsideMsg = plugin.config.getString("Messages.No-Inside-Arena");
                String notFoundMsg = plugin.config.getString("Messages.Not-Found");
                String noPermsMsg = plugin.config.getString("Messages.No-Permission");

                if(p.hasPermission(permission)) {
                    if(strings.length == 0) {
                        p.sendMessage(IridiumColorAPI.process(usageMsg));
                        return;
                    }
                    if(strings.length >= 1) {
                        RemotePlayer target = RemoteAPI.get().getOnlinePlayer(strings[0]);

                        if(target != null) {
                            RemoteArena a = RemoteAPI.get().getArenaByPlayingPlayer(target);

                            if(a != null) {
                                p.sendMessage(IridiumColorAPI.process(arenaInfoMsg
                                        .replaceAll("%player%", target.getName())
                                        .replaceAll("%arena%", a.getName())));
                            }
                            if(a == null) {
                                p.sendMessage(IridiumColorAPI.process(noInsideMsg));
                            }
                        }
                        if(target == null) {
                            p.sendMessage(IridiumColorAPI.process(notFoundMsg));
                        }
                    }
                }
                else {
                    p.sendMessage(IridiumColorAPI.process(noPermsMsg
                            .replaceAll("%permission%", permission)));
                    return;
                }
            }
        });

        return;
    }
}
