# SkriptDoclet
Convert your Javadocs into Skript documentation!

## Gradle Usage
1. Download [skriptdoclet.jar](https://github.com/btk5h/skriptdoclet/releases) and save it into 
your project's `tools` directory.
2. Add a custom Javadoc task to your `build.gradle`:
```groovy
task skriptdoc(type: Javadoc) {
  group = 'documentation'
  source = sourceSets.main.allJava
  classpath = sourceSets.main.compileClasspath
  destinationDir = projectDir
  options.docletpath = [file('tools/skriptdoclet.jar')]
  options.doclet = 'com.btk5h.skriptdoclet.SkriptDoclet'
  options.addStringOption('file', 'docs.json')
}
```

SkriptDoclet can also generate your README files!

```groovy
task buildReadme(type: Javadoc) {
  source = sourceSets.main.allJava
  classpath = sourceSets.main.compileClasspath
  destinationDir = projectDir
  options.docletpath = [file('tools/skriptdoclet.jar')]
  options.doclet = 'com.btk5h.skriptdoclet.SkriptDoclet'
  options.addStringOption('file', 'README.md')
  options.addStringOption('markdown', '-quiet')
}
```

## Doclet Options

### -file <filename>
Specifies SkriptDoclet's output filename. Requires `-d` to be set. 

By default, SkriptDoclet prints to standard output.

### -markdown
Sets the output format to Markdown.

## SkriptDoclet Tags
SkriptDoclet allows you to document your addon's syntax features using Javadoc comments and 
custom tags. All tags must be placed **after** the description, or else the text will be 
interpreted as part of a tag:
```java
/**
 * This is the description of MyEffect
 * 
 * @customTag This is the text of my custom tag.
 * This is still the text of my custom tag!
 * @anotherTag This is the text of another tag.
 */
public class MyEffect extends Effect {
  ...
}
```

If duplicate tags are present in a single doc, only the last value of the tag will be used unless
the tag is explicitly labelled as repeatable in the documentation below.

SkriptDoclet descriptions should be written as Markdown instead of standard Javadoc HTML.

### @skriptdoc
Allows SkriptDoclet to parse classes that would normally be skipped. The following classes are 
automatically documented by SkriptDoclet:
```text
org.bukkit.plugin.java.JavaPlugin
ch.njol.skript.lang.Effect
ch.njol.skript.lang.Expression
ch.njol.skript.lang.Condition
```

### @index <number>
Sets the display priority. Docs with lower values are displayed first. This defaults to `0`.

In the case of multiple docs with the same priority, docs will be sorted by type in order from 
`PLUGIN`, `EFFECT`, `EXPRESSION`, `CONDITION`; then lexicographically.

### @name <string>
Sets the name. This defaults to the class's name.

### @type <string>
Sets the type. This defaults to `PLUGIN`, `EFFECT`, `EXPRESSION`, `CONDITION`,
or `STANDALONE` depending on the element being documented.

The type may be set to any string, not just the ones listed above. Keep in mind, some viewers may 
not be able to interpret custom types.

### @return <string>
Sets the return type. This is useful for expressions.

### @author <string>
Sets the author.

### @since <string>
Sets the version that the element was released in.

### @minecraftVersion <string>
Sets the compatible Minecraft versions. This should be a [semver range].

### @skriptVersion <string>
Sets the compatible Skript versions. This should be a [semver range].

### @propertyType <string>
Sets the target of this property (e.g. `player` in `%player%'s tool`). Defaults to `object`.

This value is only used for `@propertyExpression` and `@propertyCondition` and is not stored.

### @propertyExpression <string>
Repeatable. Registers the appropriate syntaxes for a property expression. The argument is the 
`property` parameter of Skript's `PropertyExpression#register`

### @propertyCondition <string>
Repeatable. Registers the appropriate syntaxes for a property expression. The argument is the 
`property` parameter of Skript's `PropertyCondition#register`

### @pattern <string>
Repeatable. Registers a pattern.

`@propertyExpression` and `@propertyCondition` should be used instead, when applicable.

### @example <string>
Repeatable. Registers an example. Multiple lines are reserved.

[semver range]: https://docs.npmjs.com/misc/semver#ranges
