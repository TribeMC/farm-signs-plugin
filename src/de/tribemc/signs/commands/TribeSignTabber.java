package de.tribemc.signs.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TribeSignTabber implements TabCompleter {

	List<String> cmdOptions;

	public TribeSignTabber() {
		this.cmdOptions = new ArrayList<String>();
		this.cmdOptions.add("add");
		this.cmdOptions.add("remove");
		this.cmdOptions.add("check");
		this.cmdOptions.add("find");
		this.cmdOptions.add("setdely");
		this.cmdOptions.add("setitem");
		this.cmdOptions.add("info");
		this.cmdOptions.add("help");

	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command arg1,
			String arg2, String[] args) {
		if (args.length == 1) {
			List<String> temp = new ArrayList<>();
			for (String s : cmdOptions)
				if (s.startsWith(args[0]))
					temp.add(s);
			return temp;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
			List<String> temp = new ArrayList<>();
			for (World w : Bukkit.getWorlds())
				if (w.getName().startsWith(args[1]))
					temp.add(w.getName());
			return temp;
		}
		return null;
	}

}
