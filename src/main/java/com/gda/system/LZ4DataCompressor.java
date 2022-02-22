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

    byte[] result = new byte[compressed.length + 4]; // Create result compressed array

    UnsafeUtils.writeInt(result, 0, source.length); // Add to start source data array length

    System.arraycopy(compressed, 0, result, 4, compressed.length); // Copy compressed data with Integer offset

    return result;
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
