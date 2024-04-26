package com.geoarrow.core;

public class JsonStringCrs implements Crs {

    JsonStringCrs(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public CrsType getCrsType() {
        return CrsType.PROJJSON;
    }

    @Override
    public String toPROJJSON() {
        return jsonString;
    }

    private String jsonString;
}
