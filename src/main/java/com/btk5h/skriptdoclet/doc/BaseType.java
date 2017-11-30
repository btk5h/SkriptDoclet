package com.btk5h.skriptdoclet.doc;

import com.sun.javadoc.ClassDoc;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public enum BaseType {
  PLUGIN("org.bukkit.plugin.java.JavaPlugin"),
  EFFECT("ch.njol.skript.lang.Effect"),
  EXPRESSION("ch.njol.skript.lang.Expression"),
  CONDITION("ch.njol.skript.lang.Condition");

  private final String baseClass;

  BaseType(String baseClass) {
    this.baseClass = baseClass;
  }

  public String getBaseClass() {
    return baseClass;
  }

  public static Optional<BaseType> fromClassDoc(ClassDoc classDoc) {
    Optional<BaseType> baseType = fromBaseClass(classDoc.qualifiedName());

    if (baseType.isPresent()) {
      return baseType;
    }

    return Stream.concat(Stream.of(classDoc.interfaces()), Stream.of(classDoc.superclass()))
      .filter(Objects::nonNull)
      .map(BaseType::fromClassDoc)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .findFirst();
  }

  private static Optional<BaseType> fromBaseClass(String qualifiedName) {
    return Arrays.stream(BaseType.values())
      .filter(type -> type.getBaseClass().equalsIgnoreCase(qualifiedName))
      .findFirst();
  }
}
