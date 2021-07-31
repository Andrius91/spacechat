package dev.spaceseries.spacechat.command.channel;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import dev.spaceseries.spacechat.model.Channel;
import org.bukkit.entity.Player;

@CommandPermission("space.chat.command.channel.mute")
@CommandAlias("channel")
@Subcommand("mute")
public class MuteCommand extends SpaceChatCommand {

    public MuteCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onMute(Player player, @Single String channel) {
        // get channel
        Channel applicable = plugin.getChannelManager().get(channel, null);
        if (applicable == null) {
            // send message
            Messages.getInstance(plugin).channelInvalid.message(player, "%channel%", channel);
            return;
        }

        // do they have permission?
        if (!player.hasPermission(applicable.getPermission())) {
            Messages.getInstance(plugin).channelAccessDenied.message(player);
            return;
        }

        // set current channel
        plugin.getUserManager().use(player.getUniqueId(), (user) -> {
            user.unsubscribeFromChannel(applicable);

            // send message
            Messages.getInstance(plugin).channelMute.message(player, "%channel%", channel);
        });
    }
}