package com.gda.system.args;

import org.kohsuke.args4j.Option;

public class CompressOperationArgs implements CommandLineArguments {
  @Option(name = "-data", usage = "Source data")
  private String data;

  @Option(name = "-lvl", usage = "Compression level")
  private int level;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }
}
