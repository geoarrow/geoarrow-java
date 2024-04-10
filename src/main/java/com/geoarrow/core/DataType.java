package com.geoarrow.core;

public class DataType {

  private DataType(Encoding encoding) {}

  public Encoding getEncoding() {
    return encoding;
  }

  public GeometryType getGeometryType() {
    return geometryType;
  }

  public Dimensions getDimensions() {
    return dimensions;
  }

  public CoordType getCoordType() {
    return coordType;
  }

  public EdgeType getEdgeType() {
    return edgeType;
  }

  public Crs getCrs() {
    return crs;
  }

  public String getExtensionType() {
    switch (encoding) {
      case WKT:
        return "geoarrow.wkt";
      case WKB:
        return "geoarrow.wkb";
      case GEOARROW:
        break;
      default:
        throw new IllegalArgumentException("Unsupported or unknown encoding");
    }

    switch (geometryType) {
      case POINT:
        return "geoarrow.point";
      case LINESTRING:
        return "geoarrow.linestring";
      case POLYGON:
        return "geoarrow.polygon";
      case MULTIPOINT:
        return "geoarrow.multipoint";
      case MULTILINESTRING:
        return "geoarrow.multilinestring";
      case MULTIPOLYGON:
        return "geoarrow.multipolygon";
      default:
        throw new IllegalArgumentException("Unsupported or unknown geometry type");
    }
  }

  public String getExtensionMetadata() {
    String out = "{";

    if (crs != null) {
      out = out + "\"crs\": " + crs.toPROJJSON();
    }

    // Edge type

    return out + "}";
  }

  private Encoding encoding = Encoding.UNKNOWN;
  private GeometryType geometryType = GeometryType.GEOMETRY;
  private Dimensions dimensions = Dimensions.UNKNOWN;
  private CoordType coordType = CoordType.UNKNOWN;
  private EdgeType edgeType = EdgeType.PLANAR;
  private Crs crs;
}
