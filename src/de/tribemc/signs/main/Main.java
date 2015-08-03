package de.tribemc.signs.main;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import de.tribemc.signs.commands.TribeSignCMD;
import de.tribemc.signs.events.Interact;

public class Main extends JavaPlugin {

	private MessageListener ml;
	private SignManager sm;

	@Override
	public void onEnable() {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		cs.sendMessage("§5========== ========== ==========");
		cs.sendMessage("§5========== §a§l" + getDescription().getName()
				+ " §5==========");
		cs.sendMessage("§5========== ========== ==========");
		cs.sendMessage("§5## §9Plugin wird §ageladen§9!");
		boolean temp1 = loadDefault();
		cs.sendMessage("§5## §9Grundgrüst "
				+ ((temp1) ? "§a§lgeladen" : "§c§lfehlerhaft"));
		boolean temp2 = loadCommand();
		cs.sendMessage("§5## §9Command "
				+ ((temp2) ? "§a§lgeladen" : "§c§lfehlerhaft"));
		boolean temp3 = loadEvents();
		cs.sendMessage("§5## §9Events "
				+ ((temp3) ? "§a§lgeladen" : "§c§lfehlerhaft"));
		cs.sendMessage("§5## §9");
		cs.sendMessage("§5## §9");
		cs.sendMessage("§5## §9");
		super.onEnable();
	}

	@Override
	public void onDisable() {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		sm.saveSignsToFile();
		cs.sendMessage("§5========== ========== ==========");
		cs.sendMessage("§5========== §a§l" + getDescription().getName()
				+ " §5==========");
		cs.sendMessage("§5========== ========== ==========");
		super.onDisable();
	}

	private boolean loadEvents() {
		try {
			getServer().getPluginManager().registerEvents(new Interact(this),
					this);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean loadCommand() {
		try {

			TribeSignCMD tscmd = new TribeSignCMD(this);
			getCommand("tribesign").setExecutor(tscmd);
			getCommand("tribesigns").setExecutor(tscmd);

			getCommand("fs").setExecutor(tscmd);
			getCommand("freesign").setExecutor(tscmd);

			getCommand("sign").setExecutor(tscmd);

			return true;
		} catch (Exception e) {

		}
		return false;
	}

	private boolean loadDefault() {
		try {
			saveDefaultConfig();
			this.ml = new MessageListener();
			this.sm = new SignManager(this);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public MessageListener getMessageListener() {
		return this.ml;
	}

	public SignManager getSignManager() {
		return this.sm;
	}

	public int getDelay() {
		return getConfig().getInt("Delay");
	}

	public boolean isActive() {
		return getConfig().getBoolean("Enabled");
	}

	public boolean revardInstant() {
		return getConfig().getBoolean("Revard.Instant");
	}

	public boolean revardRandom() {
		return getConfig().getBoolean("Revard.Random");
	}

	public boolean notifyRecive() {
		return getConfig().getBoolean("Notify.Recive");
	}

	public boolean notifyDelay() {
		return getConfig().getBoolean("Notify.Delay");
	}

	public Inventory getRevardInventory(TribeSign s) {
		return Bukkit.createInventory(
				null,
				getConfig().getInt("Revard.InventorySize"),
				ml.getMessage("TribeSign.InvTitle")
						.replace("%0", s.getLocation().getWorld().getName())
						.replace("%1", s.getLocation().getBlockX() + "")
						.replace("%2", s.getLocation().getBlockY() + "")
						.replace("%3", s.getLocation().getBlockZ() + "")
						.replace("%4", s.getDelayInSeconds() + "")
						.replace("%5", s.getItemRevard().getType().name())
						.replace("%6", s.getItemRevard().getAmount() + ""));
	}
}
