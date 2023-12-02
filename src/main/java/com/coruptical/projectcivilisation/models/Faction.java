package com.coruptical.projectcivilisation.models;

import java.util.List;
import java.util.Set;

public class Faction {
    private String name;
    private String description;
    private Set<FastChunk> chunks;
    private String ownerUUID;
    private List<String> memberUUIDs;
    private int power;
    private short factionID;

    public Faction(String name, String description, Set<FastChunk> chunks, String ownerUUID, List<String> memberUUIDs, int power, short factionID) {
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

    public Set<FastChunk> getChunks() {
        return chunks;
    }

    public void setChunks(Set<FastChunk> chunks) {
        this.chunks = chunks;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public List<String> getMemberUUIDs() {
        return memberUUIDs;
    }

    public void setMemberUUIDs(List<String> memberUUIDs) {
        this.memberUUIDs = memberUUIDs;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public short getFactionID() {
        return factionID;
    }

    public void setFactionID(short factionID) {
        this.factionID = factionID;
    }
}
