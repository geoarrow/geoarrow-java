package org.geoarrow.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataTypeTest {
  @Test
  public void constructSerializedType() {
    DataType dt = new DataType(Encoding.WKB);

    assertEquals(dt.getEncoding(), Encoding.WKB);
    assertEquals(dt.getGeometryType(), GeometryType.GEOMETRY);
    assertEquals(dt.getDimensions(), Dimensions.UNKNOWN);
    assertEquals(dt.getCoordType(), CoordType.UNKNOWN);
    assertEquals(dt.getEdgeType(), EdgeType.PLANAR);
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

    assertEquals(dt.getEncoding(), Encoding.GEOARROW);
    assertEquals(dt.getGeometryType(), GeometryType.POINT);
    assertEquals(dt.getDimensions(), Dimensions.XY);
    assertEquals(dt.getCoordType(), CoordType.SEPARATE);
    assertEquals(dt.getEdgeType(), EdgeType.PLANAR);
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
    assertEquals(wkb.getEncoding(), Encoding.WKB);
    assertEquals(wkb.getEdgeType(), EdgeType.SPHERICAL);
    assertEquals(wkb.getCoordType(), CoordType.UNKNOWN);
    assertEquals(wkb.getDimensions(), Dimensions.UNKNOWN);
    assertEquals(wkb.getCrs(), new JsonStringCrs("{}"));

    assertEquals(dt.withEncoding(Encoding.GEOARROW), dt);
  }

  @Test
  public void withEncodingFromSerializedType() {
    DataType dt =
        new DataType(Encoding.WKT)
            .withEdgeType(EdgeType.SPHERICAL)
            .withCrs(new JsonStringCrs("{}"));

    DataType wkb = dt.withEncoding(Encoding.WKB);
    assertEquals(wkb.getEncoding(), Encoding.WKB);
    assertEquals(wkb.getEdgeType(), EdgeType.SPHERICAL);
    assertEquals(wkb.getCoordType(), CoordType.UNKNOWN);
    assertEquals(wkb.getDimensions(), Dimensions.UNKNOWN);
    assertEquals(wkb.getCrs(), new JsonStringCrs("{}"));

    assertEquals(dt.withEncoding(Encoding.WKT), dt);
  }

  @Test
  public void extensionName() {
    assertEquals(new DataType(Encoding.WKT).getExtensionName(), "geoarrow.wkt");
    assertEquals(new DataType(Encoding.WKB).getExtensionName(), "geoarrow.wkb");
    assertEquals(new DataType(GeometryType.POINT).getExtensionName(), "geoarrow.point");
    assertEquals(new DataType(GeometryType.LINESTRING).getExtensionName(), "geoarrow.linestring");
    assertEquals(new DataType(GeometryType.POLYGON).getExtensionName(), "geoarrow.polygon");
    assertEquals(new DataType(GeometryType.MULTIPOINT).getExtensionName(), "geoarrow.multipoint");
    assertEquals(
        new DataType(GeometryType.MULTILINESTRING).getExtensionName(), "geoarrow.multilinestring");
    assertEquals(
        new DataType(GeometryType.MULTIPOLYGON).getExtensionName(), "geoarrow.multipolygon");
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
