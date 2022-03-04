package com.gda.system;

import com.gda.system.args.ArgumentUtil;
import com.gda.system.args.DecompressOperationArgs;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import net.jpountz.util.SafeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class LZ4DataDecompressor {
  static final LZ4SafeDecompressor decompressor = LZ4Factory.fastestInstance().safeDecompressor();

  private static final byte BUF_LEN_MASK = 0b00001111;

  public byte[] decompress(byte[] compressed) {
    byte header = SafeUtils.readByte(compressed, 0);
    int destLengthBuffer = estimateBufferLength(header, compressed.length);

    byte[] buffer = new byte[destLengthBuffer];
    int size = decompressor.decompress(compressed, 1, compressed.length-1, buffer, 0);

    return size != destLengthBuffer ? Arrays.copyOfRange(buffer, 0, size) : buffer;
  }

  private int estimateBufferLength(byte header, int compressedSize) {
    int bufLenHint = header & BUF_LEN_MASK;
    return bufLenHint > 0 ? compressedSize << bufLenHint : compressedSize;
  }

  public static void main(String[] args) throws IOException {
    DecompressOperationArgs arguments = ArgumentUtil.parseArguments(args, DecompressOperationArgs.class);

    LZ4DataDecompressor decompressor = new LZ4DataDecompressor();

    String base64Data = arguments.getData() != null ? arguments.getData() : readFromFile(arguments.getFile());
    if (base64Data == null)
      throw new IllegalArgumentException("No input source data");

    byte[] compressed = Base64.getDecoder().decode(base64Data);
    byte[] source = decompressor.decompress(compressed);

    System.out.println("Compressed length = " + compressed.length + ", source length = " + source.length + ", data = " + new String(source));
  }

  private static String readFromFile(String fileName) throws IOException {
    if (fileName == null)
      return null;

    Path path = Paths.get(fileName);
    return Files.readAllLines(path).get(0);
  }
}
