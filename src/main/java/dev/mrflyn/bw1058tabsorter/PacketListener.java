package dev.mrflyn.bw1058tabsorter;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketListener extends PacketAdapter {
        public PacketListener(Plugin plugin, PacketType... types) {
            super(plugin, types);
        }

        @Override
        public void onPacketSending(PacketEvent e){
            if (e.getPacketType() == PacketType.Play.Server.SCOREBOARD_TEAM) {
                PacketContainer packet = e.getPacket();
                Player playerTarget = Bukkit.getPlayer((String)packet.getStrings().read(0));
                if (playerTarget == null)
                    return;
                if (!BedWars.getAPI().getArenaUtil().isPlaying(playerTarget))
                    return;
                String teamName = BedWars.getAPI().getArenaUtil().getArenaByPlayer(playerTarget)
                        .getTeam(playerTarget).getColor().name();
                int position = Main.plugin.getConfig().getStringList("Sorting-Teams").indexOf(teamName);

                packet.getStrings().write(0, String.format("%02d", position) + Main.plugin.serialID.get(playerTarget));
            }
        }
}
