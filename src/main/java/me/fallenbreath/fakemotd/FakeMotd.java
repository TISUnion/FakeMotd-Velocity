package me.fallenbreath.fakemotd;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.fallenbreath.fakemotd.listener.ProxyPingListener;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(
		id = "fakemotd", name = "FakeMotd", version = "0.1.0",
		url = "https://github.com/TISUnion/FakeMotd-Velocity",
		description = "A plugin to modify your server's motd, protocol version and player count in the Server",
		authors = {"Fallen_Breath"}
)
public class FakeMotd
{
	private final ProxyServer server;
	private final Logger logger;
	private final Path dataDirectory;
	private final Configuration config;

	@Inject
	public FakeMotd(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
	{
		this.server = server;
		this.logger = logger;
		this.dataDirectory = dataDirectory;
		this.config = new Configuration();
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event)
	{
		if (!this.prepareConfig())
		{
			this.logger.error("Failed to prepare config, the plugin will not work");
			return;
		}

		this.logger.info("Description: {}", this.config.getDescription());
		this.logger.info("Version @ Protocol: {} @ {}", this.config.getVersion(), this.config.getProtocol());
		this.logger.info("Player display: {}/{}", this.config.getPlayerCount(), this.config.getMaxPlayer());
		this.server.getEventManager().register(this, ProxyPingEvent.class, new ProxyPingListener(this.config));
	}

	private boolean prepareConfig()
	{
		if (!this.dataDirectory.toFile().exists() && !this.dataDirectory.toFile().mkdir())
		{
			this.logger.error("Create data directory failed");
			return false;
		}

		File file = this.dataDirectory.resolve("config.yml").toFile();

		if (!file.exists())
		{
			try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.yml"))
			{
				Files.copy(Objects.requireNonNull(in), file.toPath());
			}
			catch (Exception e)
			{
				this.logger.error("Generate default config failed", e);
				return false;
			}
		}

		try
		{
			this.config.load(Files.readString(file.toPath()));
		}
		catch (Exception e)
		{
			this.logger.error("Read config failed", e);
			return false;
		}

		return true;
	}
}
