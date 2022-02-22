package com.gda.system.args;

import org.kohsuke.args4j.Option;

public class DecompressOperationArgs implements CommandLineArguments {
  @Option(name = "-data", usage = "Compressed data")
  private String data;

  @Option(name = "-file", usage = "File with compressed content")
  private String file;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }
}
