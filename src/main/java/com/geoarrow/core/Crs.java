package com.geoarrow.core;

public interface Crs {
    public CrsType getCrsType();
    public String toPROJJSON();
}
