package com.coruptical.projectcivilisation.models;

import java.io.Serializable;

public class StoredChunk implements Serializable {
    private short x;
    private short z;

    public StoredChunk(short x, short z) {
        this.x = x;
        this.z = z;
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
}
