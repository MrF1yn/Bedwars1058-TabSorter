package dev.mrflyn.bw1058tabsorter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    public HashMap<Player, Integer> serialID;
    public static Main plugin;
    @Override
    public void onEnable(){
        plugin = this;
        serialID = new HashMap<>();
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketListener(this, PacketType.Play.Server.SCOREBOARD_TEAM));
    }
}
