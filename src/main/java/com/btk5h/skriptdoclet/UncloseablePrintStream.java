package com.btk5h.skriptdoclet;

import java.io.OutputStream;
import java.io.PrintStream;

public class UncloseablePrintStream extends PrintStream {
  public UncloseablePrintStream(OutputStream out) {
    super(out);
  }

  @Override
  public void close() {
    flush();
  }
}
