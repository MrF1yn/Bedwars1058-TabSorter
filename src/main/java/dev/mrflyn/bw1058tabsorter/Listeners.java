package dev.mrflyn.bw1058tabsorter;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<Player> allPlayers = new ArrayList<>(e.getArena().getPlayers());
        allPlayers.addAll(e.getArena().getSpectators());
        allPlayers.addAll(e.getArena().getLeavingPlayers());
        for(Player p : allPlayers) {
            String teamName = Main.plugin.lastTeamName.get(p);
            if (teamName==null)continue;
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
            packet.getStrings().write(0, teamName);
            packet.getIntegers().write(0,  1);
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), packet);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.plugin.serialID.remove(p);
        Main.plugin.lastTeamName.remove(p);
    }
}
