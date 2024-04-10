package com.geoarrow.core;

public class DataType {

    private DataType(Encoding encoding) {}

    public Encoding getEncoding() { return encoding; }

    public GeometryType getGeometryType() { return geometryType; }

    public Dimensions getDimensions() { return dimensions; }

    public CoordType getCoordType() { return coordType; }

    public EdgeType getEdgeType() { return edgeType; }

    public Crs getCrs() { return crs; }

    private Encoding encoding = Encoding.UNKNOWN;
    private GeometryType geometryType = GeometryType.GEOMETRY;
    private Dimensions dimensions = Dimensions.UNKNOWN;
    private CoordType coordType = CoordType.UNKNOWN;
    private EdgeType edgeType = EdgeType.PLANAR;
    private Crs crs;
}
