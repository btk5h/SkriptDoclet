package com.btk5h.skriptdoclet;

import com.btk5h.skriptdoclet.doc.BaseType;
import com.btk5h.skriptdoclet.formatter.Formatter;
import com.btk5h.skriptdoclet.formatter.JSONFormatter;
import com.btk5h.skriptdoclet.formatter.MarkdownFormatter;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkriptDoclet extends Doclet {
  public static boolean start(RootDoc root) {
    Map<String, List<String>> options = extractOptions(root.options());
    Stream<ClassDoc> classDocs = Arrays.stream(root.classes())
      .filter(SkriptDoclet::shouldBeDocumented);

    List<String> destination = options.getOrDefault("-d", Collections.emptyList());
    List<String> fileName = options.getOrDefault("-file", Collections.emptyList());

    try (PrintStream out = getPrintStream(destination, fileName)) {
      Formatter f =
        options.containsKey("-markdown") ? new MarkdownFormatter()
          : new JSONFormatter();
      f.format(root, classDocs, out);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public static LanguageVersion languageVersion() {
    return LanguageVersion.JAVA_1_5;
  }

  public static int optionLength(String option) {
    switch (option) {
      case "-d":
      case "-doctitle":
      case "-windowtitle":
      case "-file":
        return 2;
      case "-markdown":
        return 1;
      default:
        return 0;
    }
  }

  private static PrintStream getPrintStream(List<String> destination, List<String> fileName)
    throws IOException {
    if (destination.size() > 0 && fileName.size() > 0) {
      Path destinationDir = Paths.get(destination.get(0));
      Path targetFile = destinationDir.resolve(fileName.get(0));

      return new PrintStream(Files.newOutputStream(targetFile,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
    }

    return new UncloseablePrintStream(System.out);
  }

  private static boolean shouldBeDocumented(ClassDoc classDoc) {
    return classDoc.tags("skriptdoc").length > 0 || isSkriptElement(classDoc);
  }

  private static boolean isSkriptElement(ClassDoc classDoc) {
    return BaseType.fromClassDoc(classDoc).isPresent();
  }

  private static Map<String, List<String>> extractOptions(String[][] options) {
    return Arrays.stream(options)
      .collect(Collectors.toMap(
        opt -> opt[0],
        opt -> opt.length > 1
          ? Arrays.stream(opt, 1, opt.length).collect(Collectors.toList())
          : Collections.emptyList(),
        (first, second) -> second
      ));
  }
}
