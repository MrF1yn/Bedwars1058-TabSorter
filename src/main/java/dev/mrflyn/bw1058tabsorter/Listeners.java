package dev.mrflyn.bw1058tabsorter;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        int i = 0;
        if (Main.plugin.serialID.containsKey(p)) return;
        while (Main.plugin.serialID.containsValue(i)) {
            i++;
        }
        Main.plugin.serialID.put(p, i);
        Main.plugin.sendHeaderFooter(p);

    }

    @EventHandler
    public void onArenaLeave(PlayerLeaveArenaEvent e){

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.plugin.serialID.remove(p);
    }
}
