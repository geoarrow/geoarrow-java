package com.geoarrow.core;

public class DataType {

  public DataType(GeometryType geometryType) {
    if (geometryType == GeometryType.GEOMETRY) {
      throw new IllegalArgumentException("Can't create DataType from GEOMETRY");
    }

    this.geometryType = geometryType;
    this.dimensions = Dimensions.XY;
    this.coordType = CoordType.SEPARATE;
    this.edgeType = EdgeType.PLANAR;
  }

  public DataType(Encoding encoding) {
    if (encoding == Encoding.GEOARROW) {
      throw new IllegalArgumentException(
          "Use DataType(GeometryType) to construct a type with GeoArrow encoding");
    }

    this.encoding = encoding;
  }

  private DataType(DataType other) {
    this.encoding = other.encoding;
    this.geometryType = other.geometryType;
    this.dimensions = other.dimensions;
    this.coordType = other.coordType;
    this.edgeType = other.edgeType;
    this.crs = other.crs;
  }

  public DataType withEncoding(Encoding encoding) {
    DataType out = new DataType(this);

    switch (encoding) {
      case WKT:
      case WKB:
        out.geometryType = GeometryType.GEOMETRY;
        out.dimensions = Dimensions.UNKNOWN;
        out.encoding = encoding;
        return out;
      case GEOARROW:
        if (this.encoding == Encoding.GEOARROW) {
          return out;
        }
      default:
        throw new IllegalArgumentException("Can't set encoding");
    }
  }

  public DataType withGeometryType(GeometryType geometryType) {
    if (encoding != Encoding.GEOARROW) {
      throw new IllegalStateException("Can't set geometryType of non-GeoArrow type");
    }

    DataType out = new DataType(this);
    out.geometryType = geometryType;
    return out;
  }

  public DataType withDimensions(Dimensions dimensions) {
    if (encoding != Encoding.GEOARROW) {
      throw new IllegalStateException("Can't set geometryType of non-GeoArrow type");
    }

    DataType out = new DataType(this);
    out.dimensions = dimensions;
    return out;
  }

  public DataType withCoordType(CoordType coordType) {
    if (encoding != Encoding.GEOARROW) {
      throw new IllegalStateException("Can't set geometryType of non-GeoArrow type");
    }

    DataType out = new DataType(this);
    out.coordType = coordType;
    return out;
  }

  public DataType withEdgeType(EdgeType edgeType) {
    DataType out = new DataType(this);
    out.edgeType = edgeType;
    return out;
  }

  public DataType withCrs(Crs crs) {
    DataType out = new DataType(this);
    out.crs = crs;
    return out;
  }

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

  public String getExtensionName() {
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
    StringBuilder out = new StringBuilder();
    out.append("{");

    if (crs != null) {
      out.append("\"crs\": " + crs.toPROJJSON());
    }

    if (edgeType == EdgeType.SPHERICAL) {
      if (crs != null) {
        out.append(",");
      }

      out.append("\"edges\": \"spherical\", ");
    }

    out.append("}");

    return out.toString();
  }

  private Encoding encoding = Encoding.UNKNOWN;
  private GeometryType geometryType = GeometryType.GEOMETRY;
  private Dimensions dimensions = Dimensions.UNKNOWN;
  private CoordType coordType = CoordType.UNKNOWN;
  private EdgeType edgeType = EdgeType.PLANAR;
  private Crs crs;
}
