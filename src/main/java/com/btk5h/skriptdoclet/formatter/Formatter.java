package com.btk5h.skriptdoclet.formatter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.PrintStream;
import java.util.stream.Stream;

public interface Formatter {
  void format(RootDoc root, Stream<ClassDoc> classDocs, PrintStream out);
}
