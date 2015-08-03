package de.tribemc.signs.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.server.v1_7_R4.Material;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class SignManager {

	private LinkedList<TribeSign> signs;

	private MessageListener ml;

	private File f;
	private YamlConfiguration cfg;

	public SignManager(Main m) {
		this.ml = m.getMessageListener();
		this.signs = new LinkedList<>();
		setUPFile();
	}

	public int loadSignsFromFile() {
		if (cfg.getConfigurationSection("Schilder") == null) {
			return -1;
		}
		int err = 0;
		for (String s : cfg.getConfigurationSection("Schilder").getKeys(false)) {
			try {
				Location loc = new Location(Bukkit.getWorld(cfg
						.getString("Schilder." + s + ".Location.World")),
						cfg.getInt("Schilder." + s + ".Location.BlockX"),
						cfg.getInt("Schilder." + s + ".Location.BlockY"),
						cfg.getInt("Schilder." + s + ".Location.BlockZ"));
				ItemStack rev = cfg
						.getItemStack("Schilder." + s + ".ItemStack");
				int delay = cfg.getInt("Schilder." + s + ".Delay");
				this.signs.add(new TribeSign(loc, rev, delay));
			} catch (Exception e) {
				err++;
			}
		}
		return err;
	}

	public int saveSignsToFile() {
		for (String s : cfg.getKeys(false))
			cfg.set(s, null);
		int count = 0;
		for (TribeSign s : this.signs) {
			cfg.set("Schilder.Schild" + 1 + ".ItemStack", s.getItemRevard());
			cfg.set("Schilder.Schild" + 1 + ".Delay", s.getDelayInSeconds());
			cfg.set("Schilder.Schild" + 1 + ".Location.World", s.getLocation()
					.getWorld().getName());
			cfg.set("Schilder.Schild" + 1 + ".Location.BlockX", s.getLocation()
					.getBlockX());
			cfg.set("Schilder.Schild" + 1 + ".Location.BlockY", s.getLocation()
					.getBlockY());
			cfg.set("Schilder.Schild" + 1 + ".Location.BlockZ", s.getLocation()
					.getBlockZ());

			count++;
		}

		if (!save())
			return -1;
		return count;
	}

	private void setUPFile() {
		f = new File("plugins/TribeSigns", "signs.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		this.cfg = YamlConfiguration.loadConfiguration(f);
	}

	public TribeSign getSign(Location loc) {
		if (loc == null)
			return null;
		for (TribeSign s : this.signs) {
			if (s.getLocation().equals(loc))
				return s;
		}
		return null;
	}

	public List<TribeSign> getSign(ItemStack item) {
		List<TribeSign> temp = new LinkedList<>();
		if (item == null || item.getType().equals(Material.AIR))
			return temp;
		for (TribeSign s : this.signs) {
			if (s.getItemRevard().isSimilar(item))
				temp.add(s);
		}
		return temp;
	}

	public List<TribeSign> getSign(World world) {
		List<TribeSign> temp = new LinkedList<>();
		if (world == null)
			return temp;
		for (TribeSign s : this.signs) {
			if (s.getLocation().getWorld().equals(world))
				temp.add(s);
		}
		return temp;
	}

	@SuppressWarnings("deprecation")
	public boolean addSign(TribeSign s) {
		org.bukkit.block.Sign sign = (org.bukkit.block.Sign) s.getLocation()
				.getBlock().getState();
		sign.setLine(
				0,
				ml.getMSG("TribeSign.Line0")
						.replace("%0", s.getLocation().getWorld().getName())
						.replace("%1", s.getLocation().getBlockX() + "")
						.replace("%2", s.getLocation().getBlockY() + "")
						.replace("%3", s.getLocation().getBlockZ() + "")
						.replace("%4", s.getDelayInSeconds() + "")
						.replace("%5", s.getItemRevard().getType().name())
						.replace("%6", s.getItemRevard().getAmount() + "")
						.replace("%7",
								s.getItemRevard().getData().getData() + "")
						.replace("%8", s.getItemRevard().getType().getId() + ""));
		sign.setLine(
				1,
				ml.getMSG("TribeSign.Line1")
						.replace("%0", s.getLocation().getWorld().getName())
						.replace("%1", s.getLocation().getBlockX() + "")
						.replace("%2", s.getLocation().getBlockY() + "")
						.replace("%3", s.getLocation().getBlockZ() + "")
						.replace("%4", s.getDelayInSeconds() + "")
						.replace("%5", s.getItemRevard().getType().name())
						.replace("%6", s.getItemRevard().getAmount() + "")
						.replace("%7",
								s.getItemRevard().getData().getData() + "")
						.replace("%8", s.getItemRevard().getType().getId() + ""));
		sign.setLine(
				2,
				ml.getMSG("TribeSign.Line2")
						.replace("%0", s.getLocation().getWorld().getName())
						.replace("%1", s.getLocation().getBlockX() + "")
						.replace("%2", s.getLocation().getBlockY() + "")
						.replace("%3", s.getLocation().getBlockZ() + "")
						.replace("%4", s.getDelayInSeconds() + "")
						.replace("%5", s.getItemRevard().getType().name())
						.replace("%6", s.getItemRevard().getAmount() + "")
						.replace("%7",
								s.getItemRevard().getData().getData() + "")
						.replace("%8", s.getItemRevard().getType().getId() + ""));
		sign.setLine(
				3,
				ml.getMSG("TribeSign.Line3")
						.replace("%0", s.getLocation().getWorld().getName())
						.replace("%1", s.getLocation().getBlockX() + "")
						.replace("%2", s.getLocation().getBlockY() + "")
						.replace("%3", s.getLocation().getBlockZ() + "")
						.replace("%4", s.getDelayInSeconds() + "")
						.replace("%5", s.getItemRevard().getType().name())
						.replace("%6", s.getItemRevard().getAmount() + "")
						.replace("%7",
								s.getItemRevard().getData().getData() + "")
						.replace("%8", s.getItemRevard().getType().getId() + ""));
		sign.update();
		return this.signs.add(s);
	}

	public boolean removeSign(TribeSign s) {
		return this.signs.remove(s);
	}

	public boolean save() {
		try {
			this.cfg.save(f);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getSignCount() {
		return this.signs.size();
	}
}
