package com.btk5h.skriptdoclet;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;

import java.util.Optional;

public final class Util {
  private Util() {}

  public static Optional<Tag> singleTag(Doc doc, String tagName) {
    Tag[] tags = doc.tags(tagName);
    return tags.length > 0 ? Optional.of(tags[tags.length - 1]) : Optional.empty();
  }

  public static Optional<String> singleTagText(Doc doc, String tagName) {
    return singleTag(doc, tagName).map(Tag::text);
  }
}
