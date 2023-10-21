package me.fallenbreath.fakemotd;

import com.google.common.collect.Maps;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class Configuration
{
	private final Map<String, Object> options = Maps.newHashMap();

	@SuppressWarnings("unchecked")
	public void load(String yamlContent)
	{
		this.options.clear();
		this.options.putAll(new Yaml().loadAs(yamlContent, this.options.getClass()));
	}

	public String getDescription()
	{
		Object description = this.options.get("description");
		if (description instanceof String)
		{
			return (String)description;
		}
		return null;
	}

	public String getVersion()
	{
		Object version = this.options.get("version");
		if (version instanceof String)
		{
			return (String)version;
		}
		return null;
	}

	public Integer getProtocol()
	{
		Object protocol = this.options.get("protocol");
		if (protocol instanceof Integer)
		{
			return (Integer)protocol;
		}
		return null;
	}

	public Integer getMaxPlayer()
	{
		Object maxPlayer = this.options.get("max_player");
		if (maxPlayer instanceof Integer)
		{
			return (Integer)maxPlayer;
		}
		return null;
	}

	public Integer getPlayerCount()
	{
		Object playerCount = this.options.get("player_count");
		if (playerCount instanceof Integer)
		{
			return (Integer)playerCount;
		}
		return null;
	}
}
