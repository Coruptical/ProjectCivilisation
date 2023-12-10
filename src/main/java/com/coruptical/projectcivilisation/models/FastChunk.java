package com.coruptical.projectcivilisation.models;

public class FastChunk {
    private short x;
    private short z;
    private short factionID;
    private String id;

    public FastChunk(short x, short z, short factionID) {
        this.x = x;
        this.z = z;
        this.factionID = factionID;
        this.id = x + ";" + z;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getZ() {
        return z;
    }

    public void setZ(short z) {
        this.z = z;
    }

    public short getFactionID() {
        return factionID;
    }

    public void setFactionID(short factionID) {
        this.factionID = factionID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
