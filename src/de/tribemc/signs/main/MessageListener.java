package de.tribemc.signs.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageListener {

	private File f;
	private YamlConfiguration cfg;
	private HashMap<String, String> messages;

	public MessageListener() {
		f = new File("plugins/TribeSigns", "messages.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		this.messages = new LinkedHashMap<>();
		this.cfg = YamlConfiguration.loadConfiguration(f);
		copyDefaults();
		loadMessages();

	}

	private void loadMessages() {
		String reset = ChatColor.translateAlternateColorCodes('&',
				cfg.getString("Color"));
		for (String s : cfg.getKeys(true)) {
			String temp = ChatColor.translateAlternateColorCodes('&',
					cfg.getString(s));
			try {
				temp = temp.replaceAll("&z", reset);
			} catch (Exception e) {

			}

			this.messages.put(s.toLowerCase(), temp);
		}
	}

	private void copyDefaults() {
		cfg.addDefault("Prefix", "&8[&4&lT&6Signs&8] &9");
		cfg.addDefault("Color", "&9");

		cfg.addDefault("cmd.Info.SignCount",
				"Es exisitieren &a&l%0 Schilder&z!");
		cfg.addDefault("cmd.Eror.OnlyPlayer",
				"&cDiese Aktion kann nur von Spielern ausgeführt werden!");
		cfg.addDefault("cmd.Find.ResultEmpty",
				"Es wurden keine Schilder für &e%0 &zgefunden!");
		cfg.addDefault("cmd.Find.ResultAmount",
				"Es wurden &a&l%0 Schilder &zfür &e%1 &zgefunden!");
		cfg.addDefault("cmd.Find.ResultSign",
				"	&6&l- &z(&e%0&c[%1, %2, %3]&z) &e%6x %5 &z(Delay: &e%4&z)");
		cfg.addDefault("cmd.Eror.WallSign", "Das ist &ckein Wand-Schild&z!");
		cfg.addDefault("cmd.Eror.SignExist",
				"Das Schild wird &abereits verwendet&z!");
		cfg.addDefault("cmd.Eror.NoItem",
				"Du hast &ckein gültiges Item &zin deiner Hand!");
		cfg.addDefault("cmd.Eror.NoPerm", "Dir fehlen Berechtigungen!\n&c%0");
		cfg.addDefault("cmd.Add.Complete",
				"Das Schild wurde &aerfolgreich &zhinzugefügt!");
		cfg.addDefault("cmd.Add.Eror",
				"Das Schild wurde &cnicht &zhinzugefügt!");
		cfg.addDefault("cmd.Remove.Complete",
				"Das Schild wurde &aerfolgreich &zentfernt!");
		cfg.addDefault("cmd.Remove.Eror",
				"Das Schild wurde &cnicht &zentfernt!");
		cfg.addDefault("cmd.Eror.NoSign",
				"Es wurde &ckein &zTRIBE Sign aus &e%0 &zgefunden!");
		cfg.addDefault("cmd.SetRevard.Complete",
				"Die ItemBelohnung wurde &aerfolgreich &zgesetzt!");
		cfg.addDefault("cmd.Check.ResultEmpty",
				"Alle Schilder sind vollständig!");
		cfg.addDefault("cmd.Check.ResultAmount",
				"Es wurden &c&l%0 unvollständige Schilder &zgefunden:");
		cfg.addDefault("cmd.Check.ResultSign",
				"	&6&l- &z(&e%0&c[%1, %2, %3]&z) &e%6x %5 &z(Delay: &e%4&z)");
		cfg.addDefault("cmd.Eror.NoInt",
				"Es konnte &ckeine Zahl &zaus &c%0 &zgebilde werden!");
		cfg.addDefault("cmd.Delay.Complete",
				"Der Delay für das Schild wurde &aerfolgreich &zgesetzt!");
		cfg.addDefault("cmd.Help.Header",
				"Hilfe für den Command von TRIBE Signs");
		cfg.addDefault("cmd.Help.Command", "&c/%0 &8- &z%1");
		cfg.addDefault("Event.Interact.Blocking", "Du blockst mit deinem Schwert!");
		cfg.addDefault("Event.Interact.Recive", "Du hast &a&l%6x %5 &zerhalten!");
		cfg.addDefault("Event.Interact.Delay", "Du kannst das Schild in &e%0 Sekunden &zwieder nutzen!");
		cfg.addDefault("TribeSign.Line3", "");
		cfg.addDefault("TribeSign.Line2", "&a%6x &c%8:%7");
		cfg.addDefault("TribeSign.Line1", "");
		cfg.addDefault("TribeSign.Line0", "&8[&4T&6Signs&8]");
		

		cfg.options().copyDefaults(true);
		save();
	}

	private void save() {
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMSG(String what) {
		if (!this.messages.containsKey(what.toLowerCase())) {
			return what;
		}
		return this.messages.get(what.toLowerCase());
	}

	public String getPrefix() {
		return this.messages.get("prefix");
	}

	public int messageAmount() {
		return this.messages.size();
	}

	public String getMessage(String what) {
		return getPrefix() + getMSG(what);
	}

}
