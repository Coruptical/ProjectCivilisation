package com.coruptical.projectcivilisation.models;

import java.util.ArrayList;
import java.util.HashSet;

public class Faction {
    private String name;
    private String description;
    private HashSet<FastChunk> chunks;
    private String ownerUUID;
    private ArrayList<String> memberUUIDs;
    private long power;
    private short factionID;

    public Faction(String name, String description, HashSet<FastChunk> chunks, String ownerUUID, ArrayList<String> memberUUIDs, long power, short factionID) {
        this.name = name;
        this.description = description;
        this.chunks = chunks;
        this.ownerUUID = ownerUUID;
        this.memberUUIDs = memberUUIDs;
        this.power = power;
        this.factionID = factionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<FastChunk> getChunks() {
        return chunks;
    }

    public void setChunks(HashSet<FastChunk> chunks) {
        this.chunks = chunks;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public ArrayList<String> getMemberUUIDs() {
        return memberUUIDs;
    }

    public void setMemberUUIDs(ArrayList<String> memberUUIDs) {
        this.memberUUIDs = memberUUIDs;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public short getFactionID() {
        return factionID;
    }

    public void setFactionID(short factionID) {
        this.factionID = factionID;
    }
}
