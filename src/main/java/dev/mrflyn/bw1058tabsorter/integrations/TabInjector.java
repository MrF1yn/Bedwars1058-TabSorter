package dev.mrflyn.bw1058tabsorter.integrations;

import dev.mrflyn.bw1058tabsorter.BW1058TabSorter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.event.plugin.TabLoadEvent;
import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.shared.features.sorting.Sorting;

import me.neznamy.tab.shared.features.sorting.types.SortingType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class TabInjector {

    public TabInjector(){

    }


    public void init() throws Exception {

        TabAPI.getInstance().getEventBus().register(TabLoadEvent.class, event -> {
                BW1058TabSorter.plugin.loadTABSupport();
        });

        BW1058TabSorter.plugin.getLogger().info("Initializing Neznamy-TAB-Injector.");

        TAB tab = TAB.getInstance();
        if (!tab.getFeatureManager().isFeatureEnabled("sorting")){
            tab.getFeatureManager().registerFeature("sorting", new Sorting(null));
            BW1058TabSorter.plugin.getLogger().info("Sorting not enabled in TAB by default. Enabling...");
        }

        Sorting sorting = (Sorting) tab.getFeatureManager().getFeature("sorting");
        BW1058TabSorter.plugin.getLogger().info("Staring Reflection sequence.");
        Field fTypes = Sorting.class.getDeclaredField("types");
        Field fUsedSortingTypes = Sorting.class.getDeclaredField("usedSortingTypes");

        fTypes.setAccessible(true);
        fUsedSortingTypes.setAccessible(true);

        Map<String, BiFunction<Sorting, String, SortingType>> types = (Map<String, BiFunction<Sorting, String, SortingType>>) fTypes.get(sorting);
        types.put("BW_TAB_SORTER", BwSortingType::new);


        SortingType[] usedSortingTypes = (SortingType[]) fUsedSortingTypes.get(sorting);

        List<SortingType> lUsedSortingTypes =  Arrays.stream(usedSortingTypes).collect(Collectors.toList());
        lUsedSortingTypes.add(types.get("BW_TAB_SORTER").apply(sorting, ""));

        fUsedSortingTypes.set(sorting, lUsedSortingTypes.toArray(new SortingType[0]));

        BW1058TabSorter.plugin.getLogger().info("TAB-Injection success.");


    }

}
