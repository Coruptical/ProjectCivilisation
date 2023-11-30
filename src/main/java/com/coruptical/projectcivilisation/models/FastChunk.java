package com.coruptical.projectcivilisation.models;

public class FastChunk {
    private int x;
    private int z;
    private short factionID;

    public FastChunk(int x, int z, short factionID) {
        this.x = x;
        this.z = z;
        this.factionID = factionID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public short getFactionID() {
        return factionID;
    }

    public void setFactionID(short factionID) {
        this.factionID = factionID;
    }
}
