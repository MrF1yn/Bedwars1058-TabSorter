package dev.mrflyn.bw1058tabsorter;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerListHeaderFooter;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.mrflyn.bw1058tabsorter.integrations.TabInjector;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class BW1058TabSorter extends JavaPlugin {
    public HashMap<Player, Integer> serialID;
    public HashMap<Player, String> lastTeamName;
    public static BW1058TabSorter plugin;
    public HeaderFooterRefreshTask hTask;
    @Override
    public void onEnable(){
        saveDefaultConfig();
        plugin = this;
        serialID = new HashMap<>();
        lastTeamName = new HashMap<>();
        if (loadTABSupport()) return;
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketListener(this, PacketType.Play.Server.SCOREBOARD_TEAM));
        if(getConfig().getBoolean("header-footer.enabled")){
            hTask = new HeaderFooterRefreshTask(getConfig().getLong("header-footer.refresh-interval")*20L);
            hTask.startTask();
        }
        getCommand("bwtabreload").setExecutor(new Commands());
    }

    public boolean loadTABSupport(){
        if(getServer().getPluginManager().getPlugin("TAB")!=null){

            getLogger().info("Neznamy-TAB plugin found hooking into it. Disabling internal header-footer system. Use header and footer from the TAB plugin :)");
            TabInjector injector = new TabInjector();
            try {
                injector.init();
            } catch (Exception e) {
                e.printStackTrace();
                getServer().getPluginManager().disablePlugin(this);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable(){
        if(hTask!=null)hTask.stopTask();
    }

    public static String parsePAPI(Player p, String s){
        if(plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI")==null)return s;
        return PlaceholderAPI.setPlaceholders(p, s);
    }
    public static String List2String(List<String> list)
    {
        return String.join("\n", list);
    }

    public void reload(){
        reloadConfig();
        if(hTask!=null){
            hTask.stopTask();
            hTask = null;
        }
        if(getConfig().getBoolean("header-footer.enabled")){
            hTask = new HeaderFooterRefreshTask(getConfig().getLong("header-footer.refresh-interval")*20L);
            hTask.startTask();
        }
    }

    public void sendHeaderFooter(Player p){
        if(!getConfig().getBoolean("header-footer.enabled"))return;
        WrapperPlayServerPlayerListHeaderFooter wrapper = new WrapperPlayServerPlayerListHeaderFooter();
        String header = BW1058TabSorter.List2String(BW1058TabSorter.plugin.getConfig().getStringList("header-footer.header"));
        String footer = BW1058TabSorter.List2String(BW1058TabSorter.plugin.getConfig().getStringList("header-footer.footer"));
            header = ChatColor.translateAlternateColorCodes('&', BW1058TabSorter.parsePAPI(p,header));
            footer = ChatColor.translateAlternateColorCodes('&', BW1058TabSorter.parsePAPI(p,footer));
            wrapper.setHeader(WrappedChatComponent.fromText(header));
            wrapper.setFooter(WrappedChatComponent.fromText(footer));
            wrapper.sendPacket(p);
    }

}
