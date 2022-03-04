package com.gda.system;

import com.gda.system.args.ArgumentUtil;
import com.gda.system.args.CompressOperationArgs;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.util.UnsafeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class LZ4DataCompressor {

  static final LZ4Factory factory = LZ4Factory.fastestInstance();

  static final LZ4Compressor fastCompressor = factory.fastCompressor();

  public byte[] compressData(byte[] source, int level) {
    assert level >= 0 && level <= 17: level;
    LZ4Compressor compressor = level == 0 ? fastCompressor : factory.highCompressor(level);

    byte[] compressed = compressor.compress(source); // Compress source data

    byte[] result = new byte[compressed.length + 1]; // Create result compressed array

    byte header = buildHeader(source.length, compressed.length); // Calculate header

    UnsafeUtils.writeByte(result, 0, header); // Add to start source data array length

    System.arraycopy(compressed, 0, result, 1, compressed.length); // Copy compressed data with Integer offset

    return result;
  }

  // First byte of compressed array is header byte, to specify dictionary
  // and approximate buffer size needed to decompress.
  // The format is 0b0000XXXX
  // 0bXXXX0000 - reserved
  //
  // Mapping: 0000 - compressed length * 1
  //          0001 - compressed length * 2
  //          0010 - compressed length * 4
  //          0011 - compressed length * 8
  //          ...
  //          1111 - compressed length * 32768
  public static byte buildHeader(int inputSize, int compressedSize) {
    if (inputSize > compressedSize) {
      byte header = 0;
      int ratio = inputSize / compressedSize;
      for (int i = 1; i < 16; i++) {
        if (ratio < 1 << i) {
          header |= i;
          break;
        }
      }
      return header;
    } else {
      return 0;
    }
  }

  public static void main(String[] args) throws IOException {
    CompressOperationArgs arguments = ArgumentUtil.parseArguments(args, CompressOperationArgs.class);

    LZ4DataCompressor compressor = new LZ4DataCompressor();

    byte[] source = arguments.getData().getBytes();

    byte[] res = compressor.compressData(source, arguments.getLevel());

    String base64 = Base64.getEncoder().encodeToString(res);

    System.out.println("Source length = " + source.length + ", result length = " + res.length + ", data = " + base64);
    writeToFile("data/compressed.out", base64);
  }

  private static void writeToFile(String fileName, String data) throws IOException {
    Path path = Paths.get(fileName);
    byte[] strToBytes = data.getBytes();
    Files.write(path, strToBytes);
  }
}
