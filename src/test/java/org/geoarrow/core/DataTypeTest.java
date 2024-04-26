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
  public void setEncodingFromGeoArrowType() {
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
}
