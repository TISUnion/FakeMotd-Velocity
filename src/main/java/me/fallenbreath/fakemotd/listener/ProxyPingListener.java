package me.fallenbreath.fakemotd.listener;

import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.fallenbreath.fakemotd.Configuration;
import net.kyori.adventure.text.Component;

public class ProxyPingListener implements EventHandler<ProxyPingEvent>
{
	private final Configuration config;

	public ProxyPingListener(Configuration config)
	{
		this.config = config;
	}

	@Override
	public void execute(ProxyPingEvent event)
	{
		ServerPing ping = event.getPing();
		ServerPing.Builder builder = ping.asBuilder();

		if (this.config.getDescription() != null)
		{
			builder.description(Component.text(this.config.getDescription()));
		}
		if (this.config.getProtocol() != null && this.config.getVersion() != null)
		{
			builder.version(new ServerPing.Version(this.config.getProtocol(), this.config.getVersion()));
		}
		if (this.config.getMaxPlayer() != null)
		{
			builder.maximumPlayers(this.config.getMaxPlayer());
		}
		if (this.config.getPlayerCount() != null)
		{
			builder.onlinePlayers(this.config.getPlayerCount());
		}

		ServerPing newPing = builder.
				notModCompatible().
				build();

		event.setPing(newPing);
	}
}
