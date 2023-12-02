package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.models.Faction;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.models.StoredChunk;

import java.util.*;

public class FactionUtils {
    private ProjectCivilisation plugin;

    private static final FactionUtils instance = new FactionUtils();

    public static FactionUtils getInstance() {
        return instance;
    }

    private FactionUtils(){

    }

    public void init(ProjectCivilisation plugin){
        this.plugin = plugin;
    }

    public static Faction formatFaction(String name,
                                        String description,
                                        HashSet<StoredChunk> storedChunks,
                                        String ownerUUID,
                                        String usMemberUUIDS,
                                        int power,
                                        short factionID){
        HashSet<FastChunk> fastChunks = new HashSet<>();
        for (StoredChunk storedChunk : storedChunks){
            FastChunk c = new FastChunk(storedChunk.getX(), storedChunk.getZ(), factionID);
            fastChunks.add(c);
        }

        List<String> memberUUIDs = Arrays.asList(usMemberUUIDS.split(";"));

        Faction faction = new Faction(name, description, fastChunks, ownerUUID, (ArrayList<String>) memberUUIDs, power, factionID);
        return faction;
    }

    public static short generateFactionId(HashMap<Short, Faction> idsFactionsMap){
        short current = 0;
        while (true){
            if (!idsFactionsMap.containsKey(current)){
                return current;
            }
            current++;
        }
    }

    public static void createFaction(String name, String ownerUUID){
        // TODO: Add faction into database.
        short id = generateFactionId(ProjectCivilisation.getInstance().getIdsFactionsMap());

        Faction faction = new Faction(name, "", new HashSet<>(),
                ownerUUID, new ArrayList<String>(), 0,
                id);
        instance.plugin.getIdsFactionsMap().put(id, faction);



    }

    public static boolean containsFaction(String name){
        return instance.plugin.getNamesIdsMap().containsKey(name);
    }

    public static boolean deleteFaction(String name){
        if (!instance.plugin.getNamesIdsMap().containsKey(name)){
            return false;
        }

        short factionId = instance.plugin.getNamesIdsMap().get(name);
        instance.plugin.getIdsFactionsMap().remove(factionId);

        // TODO: Remove faction from database table.


        return true;
    }

    public static void addWorldChunks(){
        for (short id : instance.plugin.getIdsFactionsMap().keySet()){
            Faction fac = instance.plugin.getIdsFactionsMap().get(id);
            for (FastChunk c : fac.getChunks()){
                instance.plugin.getChunkIdMap().put(c.getId(), c);
            }
        }
    }

    private static void addFaction(Faction faction){
        instance.plugin.getNamesIdsMap().put(faction.getName(), faction.getFactionID());
        instance.plugin.getIdsFactionsMap().put(faction.getFactionID(), faction);
    }

    public static void initFactions(){
        // TODO: Extract faction data from database table and add to local.
        // TODO: Firstly, lookup all factions, get and add into hashmaps


        // Afterward, add the world chunks into the hashmap for it.
        addWorldChunks();
    }

    public static String getNameFromID(short id){
        return instance.plugin.getIdsFactionsMap().get(id).getName();
    }
}
