package org.geoarrow.core;

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

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof JsonStringCrs)) {
      return false;
    }

    return ((JsonStringCrs) other).jsonString == jsonString;
  }

  private String jsonString;
}
