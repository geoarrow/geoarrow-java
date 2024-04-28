package org.geoarrow.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataTypeTest {
  @Test
  public void constructSerializedType() {
    DataType dt = new DataType(Encoding.WKB);

    assertEquals(Encoding.WKB, dt.getEncoding());
    assertEquals(GeometryType.GEOMETRY, dt.getGeometryType());
    assertEquals(Dimensions.UNKNOWN, dt.getDimensions());
    assertEquals(CoordType.UNKNOWN, dt.getCoordType());
    assertEquals(EdgeType.PLANAR, dt.getEdgeType());
    assertNull(dt.getCrs());
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructFromEncodingGeoArrowError() {
    new DataType(Encoding.GEOARROW);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructFromEncodingUnknownError() {
    new DataType(Encoding.UNKNOWN);
  }

  @Test
  public void constructGeoArrowType() {
    DataType dt = new DataType(GeometryType.POINT);

    assertEquals(Encoding.GEOARROW, dt.getEncoding());
    assertEquals(GeometryType.POINT, dt.getGeometryType());
    assertEquals(Dimensions.XY, dt.getDimensions());
    assertEquals(CoordType.SEPARATE, dt.getCoordType());
    assertEquals(EdgeType.PLANAR, dt.getEdgeType());
    assertNull(dt.getCrs());
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructFromGeometryUnknownError() {
    new DataType(GeometryType.GEOMETRY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructFromGeometryCollectionError() {
    new DataType(GeometryType.GEOMETRYCOLLECTION);
  }

  @Test
  public void withEncodingFromGeoArrowType() {
    DataType dt =
        new DataType(GeometryType.POINT)
            .withEdgeType(EdgeType.SPHERICAL)
            .withCoordType(CoordType.INTERLEAVED)
            .withDimensions(Dimensions.XYZ)
            .withCrs(new JsonStringCrs("{}"));

    DataType wkb = dt.withEncoding(Encoding.WKB);
    assertEquals(Encoding.WKB, wkb.getEncoding());
    assertEquals(EdgeType.SPHERICAL, wkb.getEdgeType());
    assertEquals(CoordType.UNKNOWN, wkb.getCoordType());
    assertEquals(Dimensions.UNKNOWN, wkb.getDimensions());
    assertEquals(new JsonStringCrs("{}"), wkb.getCrs());

    assertEquals(dt, dt.withEncoding(Encoding.GEOARROW));
  }

  @Test
  public void withEncodingFromSerializedType() {
    DataType dt =
        new DataType(Encoding.WKT)
            .withEdgeType(EdgeType.SPHERICAL)
            .withCrs(new JsonStringCrs("{}"));

    DataType wkb = dt.withEncoding(Encoding.WKB);
    assertEquals(Encoding.WKB, wkb.getEncoding());
    assertEquals(EdgeType.SPHERICAL, wkb.getEdgeType());
    assertEquals(CoordType.UNKNOWN, wkb.getCoordType());
    assertEquals(Dimensions.UNKNOWN, wkb.getDimensions());
    assertEquals(new JsonStringCrs("{}"), wkb.getCrs());

    assertEquals(dt, dt.withEncoding(Encoding.WKT));
  }

  @Test
  public void withEdgeType() {
    DataType dt = new DataType(Encoding.WKT);
    assertEquals(EdgeType.PLANAR, dt.getEdgeType());
    assertEquals(EdgeType.SPHERICAL, dt.withEdgeType(EdgeType.SPHERICAL).getEdgeType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void withEdgeTypeUnknownError() {
    DataType dt = new DataType(Encoding.WKT);
    dt.withEdgeType(EdgeType.UNKNOWN);
  }

  @Test
  public void withCrs() {
    DataType dt = new DataType(Encoding.WKT);
    assertEquals(null, dt.getCrs());
    assertEquals("{}", dt.withCrs(new JsonStringCrs("{}")).getCrs().toPROJJSON());
  }

  @Test
  public void withGeometryType() {
    DataType dt = new DataType(GeometryType.POINT);
    assertEquals(GeometryType.POINT, dt.getGeometryType());
    assertEquals(
        GeometryType.LINESTRING, dt.withGeometryType(GeometryType.LINESTRING).getGeometryType());
  }

  @Test(expected = IllegalStateException.class)
  public void serializedTypeWithGeometryTypeError() {
    DataType dt = new DataType(Encoding.WKT);
    dt.withGeometryType(GeometryType.POINT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void withGeometryTypeGeometryError() {
    DataType dt = new DataType(GeometryType.POINT);
    dt.withGeometryType(GeometryType.GEOMETRY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void withGeometryTypeGeometryCollectionError() {
    DataType dt = new DataType(GeometryType.POINT);
    dt.withGeometryType(GeometryType.GEOMETRYCOLLECTION);
  }

  @Test
  public void withDimensions() {
    DataType dt = new DataType(GeometryType.POINT);
    assertEquals(Dimensions.XY, dt.getDimensions());
    assertEquals(Dimensions.XYZ, dt.withDimensions(Dimensions.XYZ).getDimensions());
  }

  @Test(expected = IllegalStateException.class)
  public void serializedTypeWithDimensionsError() {
    DataType dt = new DataType(Encoding.WKT);
    dt.withGeometryType(GeometryType.POINT);
  }

  @Test(expected = IllegalArgumentException.class)
  public void withDimensionsUnknownError() {
    DataType dt = new DataType(GeometryType.POINT);
    dt.withDimensions(Dimensions.UNKNOWN);
  }

  @Test
  public void withCoordType() {
    DataType dt = new DataType(GeometryType.POINT);
    assertEquals(CoordType.SEPARATE, dt.getCoordType());
    assertEquals(CoordType.INTERLEAVED, dt.withCoordType(CoordType.INTERLEAVED).getCoordType());
  }

  @Test(expected = IllegalStateException.class)
  public void serializedTypeWithCoordTypeError() {
    DataType dt = new DataType(Encoding.WKT);
    dt.withCoordType(CoordType.SEPARATE);
  }

  @Test
  public void extensionName() {
    assertEquals("geoarrow.wkt", new DataType(Encoding.WKT).getExtensionName());
    assertEquals("geoarrow.wkb", new DataType(Encoding.WKB).getExtensionName());
    assertEquals("geoarrow.point", new DataType(GeometryType.POINT).getExtensionName());
    assertEquals("geoarrow.linestring", new DataType(GeometryType.LINESTRING).getExtensionName());
    assertEquals("geoarrow.polygon", new DataType(GeometryType.POLYGON).getExtensionName());
    assertEquals("geoarrow.multipoint", new DataType(GeometryType.MULTIPOINT).getExtensionName());
    assertEquals(
        "geoarrow.multilinestring", new DataType(GeometryType.MULTILINESTRING).getExtensionName());
    assertEquals(
        "geoarrow.multipolygon", new DataType(GeometryType.MULTIPOLYGON).getExtensionName());
  }

  @Test
  public void extensionMetadata() {
    DataType dt = new DataType(Encoding.WKB);
    DataType spherical = dt.withEdgeType(EdgeType.SPHERICAL);
    DataType withCrs = dt.withCrs(new JsonStringCrs("{}"));
    DataType sphericaWithCrs = withCrs.withEdgeType(EdgeType.SPHERICAL);

    assertEquals("{}", dt.getExtensionMetadata());
    assertEquals("{\"edges\":\"spherical\"}", spherical.getExtensionMetadata());

    assertEquals("{\"crs\":{}}", withCrs.getExtensionMetadata());

    assertEquals("{\"crs\":{},\"edges\":\"spherical\"}", sphericaWithCrs.getExtensionMetadata());
  }
}
