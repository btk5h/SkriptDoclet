package com.btk5h.skriptdoclet.formatter;

import com.btk5h.skriptdoclet.doc.SkriptDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.PrintStream;
import java.util.stream.Stream;

public class MarkdownFormatter implements Formatter {
  @Override
  public void format(RootDoc root, Stream<ClassDoc> classDocs, PrintStream out) {
    classDocs
      .map(SkriptDoc::fromClassDoc)
      .sorted()
      .map(MarkdownFormatter::toString)
      .forEach(out::println);
  }

  private static String toString(SkriptDoc doc) {
    StringBuilder sb = new StringBuilder();
    switch (doc.getDocType()) {
      case "EFFECT":
        sb.append("### Effect `")
          .append(doc.getDocName())
          .append("`\n");
        break;
      case "EXPRESSION":
        sb.append("### Expression `")
          .append(doc.getDocName())
          .append("` => `")
          .append(doc.getReturnType() != null ? doc.getReturnType() : "object")
          .append("`\n");
        break;
      case "CONDITION":
        sb.append("### Condition `")
          .append(doc.getDocName())
          .append("`\n");
        break;
    }

    sb.append(doc.getDescription());

    if (doc.getPatterns() != null) {
      sb.append("\n");
      sb.append("#### Syntax\n");
      sb.append("```\n");
      for (String s : doc.getPatterns()) {
        sb.append(s)
          .append("\n");
      }
      sb.append("```\n");
    }

    if (doc.getExamples() != null) {
      sb.append("\n");
      sb.append("#### Examples\n");
      for (String s : doc.getExamples()) {
        sb.append("```\n")
          .append(s)
          .append("\n```\n");
      }
    }

    sb.append("\n---\n");

    return sb.toString();
  }
}
