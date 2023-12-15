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

    public static void removeFactionChunks(short id){
        for (String chunkId : ProjectCivilisation.getInstance().getChunkIdMap().keySet()){
            FastChunk c = ProjectCivilisation.getInstance().getChunkIdMap().get(chunkId);

            if (c.getFactionID() == id){
                ProjectCivilisation.getInstance().getChunkIdMap().remove(chunkId);
            }
        }
    }

    public static void createFaction(String name){
        short id = generateFactionId(ProjectCivilisation.getInstance().getIdsFactionsMap());

        Faction faction = new Faction(name, "none", new HashSet<>(),
                "none", new ArrayList<String>(), 0,
                id);
        instance.plugin.getIdsFactionsMap().put(id, faction);
        instance.plugin.getNamesIdsMap().put(name, id);

        DatabaseHandler.getInstance().insertFaction(faction);
    }

    public static boolean containsFaction(String name){
        return instance.plugin.getNamesIdsMap().containsKey(name);
    }

    public static boolean deleteFaction(short factionID, String factionName){
        if (!instance.plugin.getIdsFactionsMap().containsKey(factionID)){
            return false;
        }

        instance.plugin.getIdsFactionsMap().remove(factionID);
        instance.plugin.getNamesIdsMap().remove(factionName);
        removeFactionChunks(factionID);

        DatabaseHandler.getInstance().removeFaction(factionID);
        return true;
    }

    public static void addFaction(Faction faction){
        instance.plugin.getNamesIdsMap().put(faction.getName(), faction.getFactionID());
        instance.plugin.getIdsFactionsMap().put(faction.getFactionID(), faction);

        for (FastChunk c : faction.getChunks()){
            instance.plugin.getChunkIdMap().put(c.getId(), c);
        }
    }

    public static String getNameFromID(short id){
        return instance.plugin.getIdsFactionsMap().get(id).getName();
    }
}
