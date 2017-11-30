package com.btk5h.skriptdoclet.doc;

import com.btk5h.skriptdoclet.Util;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Tag;

import java.util.Arrays;
import java.util.stream.Stream;

public class SkriptDoc implements Comparable<SkriptDoc> {
  private transient int index;
  private String sourcePath;
  private String docName;
  private String docType;
  private String returnType;
  private String author;
  private String since;
  private String minecraftVersion;
  private String skriptVersion;
  private String description;
  private String[] patterns;
  private String[] examples;

  public int getIndex() {
    return index;
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public String getDocName() {
    return docName;
  }

  public String getDocType() {
    return docType;
  }

  public String getReturnType() {
    return returnType;
  }

  public String getAuthor() {
    return author;
  }

  public String getSince() {
    return since;
  }

  public String getMinecraftVersion() {
    return minecraftVersion;
  }

  public String getSkriptVersion() {
    return skriptVersion;
  }

  public String getDescription() {
    return description;
  }

  public String[] getPatterns() {
    return patterns;
  }

  public String[] getExamples() {
    return examples;
  }

  public static SkriptDoc fromClassDoc(ClassDoc classDoc) {
    SkriptDoc doc = new SkriptDoc();

    doc.index = Util.singleTagText(classDoc, "index").map(Integer::parseInt).orElse(0);

    doc.sourcePath = classDoc.qualifiedName();

    doc.docName = Util.singleTagText(classDoc, "name").orElse(classDoc.name());
    doc.docType = Util.singleTagText(classDoc, "type")
      .orElseGet(() -> BaseType.fromClassDoc(classDoc).map(Enum::name).orElse("STANDALONE"));
    doc.returnType = Util.singleTagText(classDoc, "return").orElse(null);
    doc.author = Util.singleTagText(classDoc, "author").orElse(null);
    doc.since = Util.singleTagText(classDoc, "since").orElse(null);
    doc.minecraftVersion = Util.singleTagText(classDoc, "minecraftVersion").orElse(null);
    doc.skriptVersion = Util.singleTagText(classDoc, "skriptVersion").orElse(null);

    doc.description = classDoc.commentText();

    String fromType = Util.singleTagText(classDoc, "propertyType").orElse("object");

    String[] patterns = Stream.concat(
      Stream.concat(
        Arrays.stream(classDoc.tags("propertyExpression"))
          .flatMap(tag -> Stream.of(
            "[the] " + tag.text() + " of %" + fromType + "%",
            "%" + fromType + "%'[s] " + tag.text()
          )),
        Arrays.stream(classDoc.tags("propertyCondition"))
          .flatMap(tag -> Stream.of(
            "%" + fromType + "% (is|are) " + tag.text(),
            "%" + fromType + "% (isn't|is not|aren't|are not) " + tag.text()
          ))
      ),
      Arrays.stream(classDoc.tags("pattern"))
        .map(Tag::text)
    )
      .map(s -> s.replaceAll("\n", ""))
      .toArray(String[]::new);
    if (patterns.length > 0) {
      doc.patterns = patterns;
    }

    String[] examples =
      Arrays.stream(classDoc.tags("example"))
        .map(Tag::text)
        .toArray(String[]::new);
    if (examples.length > 0) {
      doc.examples = examples;
    }

    return doc;
  }

  @Override
  public int compareTo(SkriptDoc o) {
    int comp = index - o.index;

    if (comp == 0) {
      BaseType myType = null;
      BaseType oType = null;

      try {
        myType = BaseType.valueOf(docType);
      } catch (IllegalArgumentException ignored) {}

      try {
        oType = BaseType.valueOf(o.docType);
      } catch (IllegalArgumentException ignored) {}

      if (myType != oType) {
        if (myType == null) {
          return -1;
        } else if (oType == null){
          return 1;
        }

        comp = myType.compareTo(oType);
      } else if (myType != null) {
        comp = docType.compareTo(o.docType);
      }
    }

    if (comp == 0) {
      comp = docName.compareTo(o.docName);
    }

    return comp;
  }
}
