package de.tribemc.signs.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tribemc.signs.main.Main;
import de.tribemc.signs.main.MessageListener;
import de.tribemc.signs.main.SignManager;
import de.tribemc.signs.main.TribeSign;

public class TribeSignCMD implements CommandExecutor {

	private Main m;
	private MessageListener ml;
	private SignManager sm;

	public TribeSignCMD(Main m) {
		this.m = m;
		this.ml = m.getMessageListener();
		this.sm = m.getSignManager();

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2,
			String[] args) {
		if (!cs.hasPermission("tribesigns.command")) {
			cs.sendMessage(ml.getPrefix() + "Plugin erstellt von §a§lV3lop5");
			cs.sendMessage(ml.getPrefix()
					+ "Entwickelt für §1§lTribeMC §7und §1Luka");
			return true;
		}
		if (args.length == 0) {
			sendHelp(cs);
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help")) {
				sendHelp(cs);
				return true;
			} else if (args[0].equalsIgnoreCase("info")) {
				cs.sendMessage(ml.getPrefix()
						+ "Plugin erstellt von §a§lV3lop5");
				cs.sendMessage(ml.getPrefix()
						+ "Entwickelt für §1§lTribeMC §7und §1Luka");
				cs.sendMessage("");
				cs.sendMessage(ml.getMessage("cmd.Info.SignCount").replace(
						"%0", this.sm.getSignCount() + ""));
			} else if (args[0].equalsIgnoreCase("find")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.find")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.find"));
					return true;
				}
				Player p = ((Player) cs);
				ItemStack item = p.getItemInHand();
				List<TribeSign> temp = this.sm.getSign(item);
				if (temp.size() == 0) {
					p.sendMessage(ml.getMessage("cmd.Find.ResultEmpty")
							.replace("%0", item.getType().name()));
					return true;
				}
				p.sendMessage(ml.getMessage("cmd.Find.ResultAmount")
						.replace("%0", "" + temp.size())
						.replace("%1", item.getType().name()));
				for (TribeSign s : temp) {
					p.sendMessage(ml
							.getMessage("cmd.Find.ResultSign")
							.replace("%0", s.getLocation().getWorld().getName())
							.replace("%1", s.getLocation().getBlockX() + "")
							.replace("%2", s.getLocation().getBlockY() + "")
							.replace("%3", s.getLocation().getBlockZ() + "")
							.replace("%4", s.getDelayInSeconds() + "")
							.replace("%5", s.getItemRevard().getType().name())
							.replace("%6", s.getItemRevard().getAmount() + ""));
				}
			} else if (args[0].equalsIgnoreCase("add")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.add")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.add"));
					return true;
				}
				Player p = ((Player) cs);
				ItemStack item = new ItemStack(p.getItemInHand());
				Location loc = p.getTargetBlock(null, 0).getLocation();

				if (!loc.getBlock().getType().equals(Material.WALL_SIGN)) {
					p.sendMessage(ml.getMessage("cmd.Eror.WallSign"));
					return true;
				}

				if (this.sm.getSign(loc) != null) {
					p.sendMessage(ml.getMessage("cmd.Eror.SignExist"));
					return true;
				}
				if (item.getType().equals(Material.AIR)) {
					p.sendMessage(ml.getMessage("cmd.Eror.NoItem"));
					return true;
				}
				TribeSign s = new TribeSign(loc, item, m.getDelay());
				if (this.sm.addSign(s))
					p.sendMessage(ml.getMessage("cmd.Add.Complete"));
				else
					p.sendMessage(ml.getMessage("cmd.Add.Eror"));
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.remove")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.remove"));
					return true;
				}
				Player p = ((Player) cs);
				Location tgt = p.getTargetBlock(null, 0).getLocation();
				TribeSign s = this.sm.getSign(tgt);
				if (s == null) {
					if (tgt.getBlock() != null)
						p.sendMessage(ml.getMessage("cmd.Eror.NoSign").replace(
								"%0", tgt.getBlock().getType().name()));
					return true;
				}
				if (this.sm.removeSign(s))
					p.sendMessage(ml.getMessage("cmd.Remove.Complete"));
				else
					p.sendMessage(ml.getMessage("cmd.Remove.Eror"));
			} else if (args[0].equalsIgnoreCase("setitem")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.setrevard")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.setrevard"));
					return true;
				}
				Player p = ((Player) cs);
				Location tgt = p.getTargetBlock(null, 0).getLocation();

				TribeSign s = this.sm.getSign(tgt);
				if (s == null) {
					if (tgt.getBlock() != null)
						p.sendMessage(ml.getMessage("cmd.Eror.NoSign").replace(
								"%0", tgt.getBlock().getType().name()));
					return true;
				}
				s.setRevard(new ItemStack(p.getItemInHand()));
				p.sendMessage(ml.getMessage("cmd.SetRevard.Complete"));
			} else if (args[0].equalsIgnoreCase("check")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.check")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.check"));
					return true;
				}
				Player p = ((Player) cs);
				List<TribeSign> temp = new LinkedList<>();
				for (TribeSign s : this.sm.getSign(p.getWorld())) {
					if (!s.getLocation().getBlock().getType()
							.equals(Material.WALL_SIGN))
						temp.add(s);
				}
				if (temp.size() == 0) {
					p.sendMessage(ml.getMessage("cmd.Check.ResultEmpty")
							.replace("%0", p.getWorld().getName()));
					return true;
				}
				p.sendMessage(ml.getMessage("cmd.Check.ResultAmount").replace(
						"%0", "" + temp.size()));
				for (TribeSign s : temp) {
					p.sendMessage(ml
							.getMessage("cmd.Check.ResultSign")
							.replace("%0", s.getLocation().getWorld().getName())
							.replace("%1", s.getLocation().getBlockX() + "")
							.replace("%2", s.getLocation().getBlockY() + "")
							.replace("%3", s.getLocation().getBlockZ() + "")
							.replace("%4", s.getDelayInSeconds() + "")
							.replace("%5", s.getItemRevard().getType().name())
							.replace("%6", s.getItemRevard().getAmount() + ""));
				}
			} else {
				sendHelp(cs);
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("setdelay")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.setdelay")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.setdelay"));
					return true;
				}
				Player p = ((Player) cs);
				Location tgt = p.getTargetBlock(null, 0).getLocation();

				TribeSign s = this.sm.getSign(tgt);
				if (s == null) {
					if (tgt.getBlock() != null)
						p.sendMessage(ml.getMessage("cmd.Eror.NoSign").replace(
								"%0", tgt.getBlock().getType().name()));
					return true;
				}
				int delay = 0;
				try {
					delay = Integer.valueOf(args[1]);
				} catch (NumberFormatException e) {
					p.sendMessage(ml.getMessage("cmd.Eror.NoInt").replace("%0",
							args[1]));
					return true;
				}
				s.setDelay(delay);
				p.sendMessage(ml.getMessage("cmd.Delay.Complete").replace("%0",
						delay + ""));
			} else if (args[0].equalsIgnoreCase("check")) {
				if (!cs.hasPermission("tribesigns.check")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.check"));
					return true;
				}
				List<TribeSign> temp = new LinkedList<>();
				for (TribeSign s : this.sm
						.getSign((args[1].equalsIgnoreCase("~") ? ((cs instanceof Player) ? ((Player) cs)
								.getWorld() : null)
								: Bukkit.getWorld(args[1])))) {
					if (!s.getLocation().getBlock().getType()
							.equals(Material.WALL_SIGN))
						temp.add(s);
				}
				if (temp.size() == 0) {
					cs.sendMessage(ml.getMessage("cmd.Check.ResultEmpty")
							.replace("%0", args[1]));
					return true;
				}
				cs.sendMessage(ml.getMessage("cmd.Check.ResultAmount").replace(
						"%0", "" + temp.size()));
				for (TribeSign s : temp) {
					cs.sendMessage(ml
							.getMessage("cmd.Check.ResultSign")
							.replace("%0", s.getLocation().getWorld().getName())
							.replace("%1", s.getLocation().getBlockX() + "")
							.replace("%2", s.getLocation().getBlockY() + "")
							.replace("%3", s.getLocation().getBlockZ() + "")
							.replace("%4", s.getDelayInSeconds() + "")
							.replace("%5", s.getItemRevard().getType().name())
							.replace("%6", s.getItemRevard().getAmount() + ""));
				}
			} else if (args[0].equalsIgnoreCase("add")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ml.getMessage("cmd.Eror.OnlyPlayer"));
					return true;
				}
				if (!cs.hasPermission("tribesigns.add")) {
					cs.sendMessage(ml.getMessage("cmd.Eror.NoPerm").replace(
							"%0", "tribesigns.add"));
					return true;
				}
				Player p = ((Player) cs);
				ItemStack item = new ItemStack(p.getItemInHand());
				int delay = 0;
				try {
					delay = Integer.valueOf(args[1]);
				} catch (NumberFormatException e) {
					p.sendMessage(ml.getMessage("cmd.Eror.NoInt").replace("%0",
							args[1]));
					return true;
				}
				Location loc = p.getTargetBlock(null, 0).getLocation();

				if (!loc.getBlock().getType().equals(Material.WALL_SIGN)) {
					p.sendMessage(ml.getMessage("cmd.Eror.WallSign"));
					return true;
				}
				if (this.sm.getSign(loc) != null) {
					p.sendMessage(ml.getMessage("cmd.Eror.SignExist"));
					return true;
				}
				if (item.getType().equals(Material.AIR)) {
					p.sendMessage(ml.getMessage("cmd.Eror.NoItem"));
					return true;
				}
				TribeSign s = new TribeSign(loc, item, delay);
				if (this.sm.addSign(s))
					p.sendMessage(ml.getMessage("cmd.Add.Complete"));
				else
					p.sendMessage(ml.getMessage("cmd.Add.Eror"));
			} else
				sendHelp(cs);
		}
		return true;
	}

	private void sendHelp(CommandSender cs) {
		cs.sendMessage(ml.getMessage("cmd.Help.Header"));
		cs.sendMessage(ml.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns info")
				.replace("%1", "Gibt Informationen über das Plugin"));
		cs.sendMessage(ml.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns add")
				.replace("%1", "Fügt ein Schild hinzu"));
		cs.sendMessage(ml.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns add [delay]")
				.replace("%1", "Fügt ein Schild mit bestimmtem Delay hinzu"));
		cs.sendMessage(ml.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns remove")
				.replace("%1", "Entfernt ein Schild"));
		cs.sendMessage(ml.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns find")
				.replace("%1", "Findet Schilder mit entsprechendem Item"));
		cs.sendMessage(ml.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns check")
				.replace("%1", "Findet Schilder die unvollständig sind!"));
		cs.sendMessage(ml
				.getMessage("cmd.Help.Command")
				.replace("%0", "tribesigns check [Welt]")
				.replace("%1",
						"Findet Schilder die unvollständig sind in einer Welt"));
	}
}
