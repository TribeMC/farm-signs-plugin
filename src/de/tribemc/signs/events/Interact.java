package de.tribemc.signs.events;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import de.tribemc.signs.main.Main;
import de.tribemc.signs.main.MessageListener;
import de.tribemc.signs.main.SignManager;
import de.tribemc.signs.main.TribeSign;

public class Interact implements Listener {

	private MessageListener ml;
	private SignManager sm;
	private Main m;
	private Random r;

	public Interact(Main m) {
		this.ml = m.getMessageListener();
		this.sm = m.getSignManager();
		this.m = m;
		this.r = new Random();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!m.isActive())
			return;
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
				Player p = e.getPlayer();
				TribeSign s = this.sm
						.getSign(e.getClickedBlock().getLocation());
				if (s == null)
					return;
				if (s.getRemainingTime(p.getName()) < 0) {

					org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(
							s.getItemRevard());
					if (m.revardInstant()) {
						if (p.getInventory().firstEmpty() < 0) {
							p.getWorld().dropItemNaturally(p.getLocation(),
									item);
						} else {
							p.getInventory().addItem(item);
							p.updateInventory();
						}
					} else {
						Inventory inv = m.getRevardInventory(s);
						if (m.revardRandom()) {
							inv.setItem(r.nextInt(inv.getSize()), item);
						} else {
							inv.setItem((int) inv.getSize() / 2, item);
						}
						p.openInventory(inv);
						e.setCancelled(true);
						if (p.isBlocking())
							p.sendMessage(ml
									.getMessage("Event.Interact.Blocking"));

					}

					s.setNextUse(p.getName());
					if (m.notifyRecive())
						p.sendMessage(ml
								.getMessage("Event.Interact.Recive")
								.replace("%0",
										s.getLocation().getWorld().getName())
								.replace("%1", s.getLocation().getBlockX() + "")
								.replace("%2", s.getLocation().getBlockY() + "")
								.replace("%3", s.getLocation().getBlockZ() + "")
								.replace("%4", s.getDelayInSeconds() + "")
								.replace("%5",
										s.getItemRevard().getType().name())
								.replace("%6",
										s.getItemRevard().getAmount() + ""));
				} else if (m.notifyDelay())
					p.sendMessage(ml
							.getMessage("Event.Interact.Delay")
							.replace(
									"%0",
									((int) s.getRemainingTime(p.getName()) / 1000)
											+ ""));
			}
		}
	}
}
