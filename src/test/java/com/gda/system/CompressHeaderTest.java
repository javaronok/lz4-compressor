package com.gda.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompressHeaderTest {
  @Test
  public void testCompressRatio() {
    byte header = LZ4DataCompressor.buildHeader(119, 120);
    assertEquals(0, header, "Ratio = 0, header = 0");

    header = LZ4DataCompressor.buildHeader(119, 117);
    assertEquals(1, header, "Ratio = 2, header = sqrt(2) = 1");

    header = LZ4DataCompressor.buildHeader(119, 54);
    assertEquals(2, header, "Ratio = 4, header = sqrt(4) = 2");

    header = LZ4DataCompressor.buildHeader(119, 34);
    assertEquals(2, header, "Ratio = 4, header = sqrt(4) = 2");

    header = LZ4DataCompressor.buildHeader(119, 24);
    assertEquals(3, header, "Ratio = 8, header = sqrt(8) = 3");
  }
}
