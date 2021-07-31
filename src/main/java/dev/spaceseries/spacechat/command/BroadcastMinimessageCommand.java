package dev.spaceseries.spacechat.command;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Split;
import dev.spaceseries.spacechat.api.command.SpaceChatCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.sync.redis.stream.packet.broadcast.RedisBroadcastPacket;
import org.bukkit.command.CommandSender;

import static dev.spaceseries.spacechat.config.SpaceChatConfigKeys.BROADCAST_USE_LANG_WRAPPER;
import static dev.spaceseries.spacechat.config.SpaceChatConfigKeys.REDIS_SERVER_IDENTIFIER;

@CommandPermission("space.chat.command.broadcastminimessage")
@CommandAlias("broadcastminimessage|scbcastm|bcastm")
public class BroadcastMinimessageCommand extends SpaceChatCommand {

    public BroadcastMinimessageCommand(SpaceChatPlugin plugin) {
        super(plugin);
    }

    @Default
    public void onBroadcastMinimessage(CommandSender sender, @Split(" ") String[] args) {
        // if message length is not long enough
        if (args.length <= 0) {
            Messages.getInstance(plugin).broadcastArgs.message(sender);
            return;
        }

        // compile args into single message
        String message = String.join(" ", args);

        // parse through minimessage
        Component component = MiniMessage.get().deserialize(message);

        // use lang wrapper?
        if (BROADCAST_USE_LANG_WRAPPER.get(plugin.getSpaceChatConfig().getAdapter())) {
            Component previousComponent = component;
            component = Messages.getInstance(plugin).broadcastWrapper.compile()
                    .replaceText((b) -> b.match("%message%")
                            .replacement(previousComponent));
        }

        // send broadcast packet (redis)
        plugin.getServerSyncServiceManager().getStreamService().publishBroadcast(new RedisBroadcastPacket(REDIS_SERVER_IDENTIFIER.get(plugin.getSpaceChatConfig().getAdapter()), component));

        // output to game
        plugin.getChatManager().sendComponentMessage(component);
    }
}
