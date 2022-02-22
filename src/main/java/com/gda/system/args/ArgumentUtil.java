package com.gda.system.args;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class ArgumentUtil {
  public static <T extends CommandLineArguments> T parseArguments(String[] args, Class<T> argumentsContainer) {
    T argumentsObj;
    try {
      argumentsObj = argumentsContainer.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Can not create argument container for class " + argumentsContainer);
    }

    CmdLineParser parser = new CmdLineParser(argumentsObj);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException ce) {
      System.err.println(ce.getMessage());
      System.err.println();
      System.err.println(" Options are:");
      parser.printUsage(System.err); // print the list of available options
      System.err.println();
      System.exit(0);
    }
    return argumentsObj;
  }
}
