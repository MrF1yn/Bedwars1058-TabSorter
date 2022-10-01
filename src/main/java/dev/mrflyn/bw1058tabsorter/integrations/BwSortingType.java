package dev.mrflyn.bw1058tabsorter.integrations;

import com.andrei1058.bedwars.BedWars;
import dev.mrflyn.bw1058tabsorter.BW1058TabSorter;
import me.neznamy.tab.shared.ITabPlayer;
import me.neznamy.tab.shared.features.sorting.Sorting;
import me.neznamy.tab.shared.features.sorting.types.SortingType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BwSortingType extends SortingType {

    public BwSortingType(Sorting sorting, String options) {
    }

    @Override
    public String getChars(ITabPlayer p) {
        String prefix = "";
        Player player =(Player) p.getPlayer();
        if (player == null)
            return prefix;
        if (!BedWars.getAPI().getArenaUtil().isPlaying(player))
            return prefix;
        if(BedWars.getAPI().getArenaUtil().getArenaByPlayer(player)==null)return prefix;
        if(BedWars.getAPI().getArenaUtil().getArenaByPlayer(player).getTeam(player)==null)return prefix;
        String teamName = BedWars.getAPI().getArenaUtil().getArenaByPlayer(player)
                .getTeam(player).getColor().name();
        int position = BW1058TabSorter.plugin.configManager.getConfig().getStringList("Sorting-Teams").indexOf(teamName);
        prefix = String.format("%02d", position);
        return prefix;
    }
}
