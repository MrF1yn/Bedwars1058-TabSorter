package dev.mrflyn.bw1058tabsorter;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerListHeaderFooter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HeaderFooterRefreshTask {

    private BukkitTask task;
    private long interval;

    public HeaderFooterRefreshTask(long interval){
        this.interval = interval;
    }

    public void startTask(){
        if(task!=null){
            task.cancel();
            task = null;
        }
        task = new BukkitRunnable(){
            @Override
            public void run(){
                WrapperPlayServerPlayerListHeaderFooter wrapper = new WrapperPlayServerPlayerListHeaderFooter();
                String header = Main.List2String(Main.plugin.getConfig().getStringList("header-footer.header"));
                String footer = Main.List2String(Main.plugin.getConfig().getStringList("header-footer.footer"));
                for(Player p: Bukkit.getServer().getOnlinePlayers()){
                    header = ChatColor.translateAlternateColorCodes('&', Main.parsePAPI(p,header));
                    footer = ChatColor.translateAlternateColorCodes('&', Main.parsePAPI(p,footer));
                    wrapper.setHeader(WrappedChatComponent.fromText(header));
                    wrapper.setFooter(WrappedChatComponent.fromText(footer));
                    wrapper.sendPacket(p);
                }

            }
        }.runTaskTimerAsynchronously(Main.plugin, 0,interval);

    }

    public void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        WrapperPlayServerPlayerListHeaderFooter wrapper = new WrapperPlayServerPlayerListHeaderFooter();
        for(Player p: Bukkit.getServer().getOnlinePlayers()){
            wrapper.setHeader(WrappedChatComponent.fromText(""));
            wrapper.setFooter(WrappedChatComponent.fromText(""));
            wrapper.sendPacket(p);
        }
    }



}
