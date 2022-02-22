package com.gda.system;

import com.gda.system.args.ArgumentUtil;
import com.gda.system.args.DecompressOperationArgs;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import net.jpountz.util.SafeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class LZ4DataDecompressor {
  static final LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();

  public byte[] decompress(byte[] compressed) {
    int destLength = SafeUtils.readInt(compressed, 0);

    byte[] array = new byte[compressed.length-4];
    System.arraycopy(compressed, 4, array, 0, compressed.length-4);
    return decompressor.decompress(array, destLength);
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
