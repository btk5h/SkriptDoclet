package com.btk5h.skriptdoclet.formatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.btk5h.skriptdoclet.doc.SkriptDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.PrintStream;
import java.util.stream.Stream;

public class JSONFormatter implements Formatter {
  @Override
  public void format(RootDoc root, Stream<ClassDoc> classDocs, PrintStream out) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    SkriptDoc[] docs = classDocs
      .map(SkriptDoc::fromClassDoc)
      .sorted()
      .toArray(SkriptDoc[]::new);
    out.println(gson.toJson(docs));
  }
}
