package com.coruptical.projectcivilisation.utils;

import com.coruptical.projectcivilisation.ProjectCivilisation;
import com.coruptical.projectcivilisation.models.Faction;
import com.coruptical.projectcivilisation.models.FastChunk;
import com.coruptical.projectcivilisation.models.StoredChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FactionUtils {
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

    public static short generateFactionId(HashSet<Short> factionIds){
        short current = 0;
        while (true){
            if (!factionIds.contains(current)){
                return current;
            }
            current++;
        }
    }

    public static Faction createFaction(String name, String ownerUUID){
        // TODO: Add faction to local, eventually into database.
        Faction faction = new Faction(name, "", new HashSet<FastChunk>(),
                ownerUUID, new ArrayList<String>(), 0,
                generateFactionId(ProjectCivilisation.getInstance().getFactionIds()));
        return null;
    }
}
