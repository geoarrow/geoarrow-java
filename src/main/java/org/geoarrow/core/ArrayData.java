package org.geoarrow.core;

import java.nio.Buffer;

public class ArrayData {

  public ArrayData(DataType dataType, Buffer[] buffers) {
    if (buffers.length == 0) {
      throw new IllegalArgumentException("Can't create GeoArrow Array with zero buffers");
    }

    this.dataType = dataType;
    this.buffers = buffers;
  }

  public DataType getDataType() {
    return dataType;
  }

  public Buffer getBuffer(int i) {
    return buffers[i];
  }

  public int numBuffers() {
    return buffers.length;
  }

  private DataType dataType;
  private Buffer[] buffers;
}
