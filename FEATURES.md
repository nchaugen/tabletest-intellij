# TableTest Plugin Features

This document describes the features provided by the TableTest IntelliJ plugin.

## Supported Contexts

The plugin provides TableTest language support in three contexts:

| Context | Description |
|---------|-------------|
| **Native files** | Standalone `.table` files |
| **Java injection** | TableTest content inside `@TableTest` annotation text blocks |
| **Kotlin injection** | TableTest content inside `@TableTest` annotation raw strings |

All features work identically across these contexts unless otherwise noted.

## Features

### Syntax Highlighting

Applies distinct colours to different elements of the table syntax:

- **Headers**: Input headers and output headers (with `?` suffix) use different colours
- **Delimiters**: Pipe characters (`|`) that separate columns
- **Values**: Quoted strings, lists (`[...]`), sets (`{...}`), maps (`{key: value}`)
- **Comments**: Line comments starting with `//`

Colours are configurable via Settings > Editor > Color Scheme > TableTest.

### Auto-Formatting

The formatter produces clean, aligned tables:

#### Indentation
Positions the table appropriately within its context. In native `.table` files, the table starts at the left margin. In Java text blocks or Kotlin raw strings, the table aligns with the surrounding code according to the host language's formatting rules.

#### Row Alignment
Creates a straight left edge – all rows start at the same column position, regardless of content length. Without this, rows with shorter first values would appear jagged.

#### Column Alignment
Aligns columns vertically by padding cells to match the widest value in each column. Pipe delimiters (`|`) line up across all rows. Values are left-aligned within their columns.

#### Value Formatting
Applies consistent spacing within cells:
- One space around pipe delimiters
- No space before colons, one space after (for maps)
- No space before commas, one space after (for lists/sets/maps)
- No space inside brackets

### Comment Toggle

Toggle line comments (`//`) on selected lines using the standard IDE shortcut (Cmd+/ on macOS, Ctrl+/ on Windows/Linux).

### Move Row Up/Down

Move table rows up or down using the standard IDE shortcuts for moving statements:
- **macOS**: Cmd+Shift+Up/Down
- **Windows/Linux**: Ctrl+Shift+Up/Down

Constraints:
- Header row cannot be moved
- Data rows cannot be moved above the header
- Rows cannot be moved beyond file/table boundaries

The move operation respects blank lines and comments, swapping entire logical rows.

### Language Injection

Automatically injects TableTest language support into `@TableTest` annotation values. The plugin registers an XML-based injection pattern that matches on the annotation class FQN (`io.github.nchaugen.tabletest.junit.TableTest`). Since Kotlin code imports and uses this same Java annotation, auto-injection works in both languages.

The `//language=tabletest` hint can also be used and is useful during development if the tabletest library isn't yet on the classpath.

## Feature Dependencies

| Feature           | Depends on Host Language?                                                     |
|-------------------|-------------------------------------------------------------------------------|
| Syntax Highlighting | No                                                                          |
| Column Alignment  | No                                                                            |
| Row Alignment     | Yes – Java text blocks strip shared indentation, Kotlin raw strings do not    |
| Value Formatting  | No                                                                            |
| Indentation       | Yes – delegated to Java/Kotlin formatter                                      |
| Comment Toggle    | Partially – caret position handling                                           |
| Move Row          | Partially – offset mapping at boundaries                                      |
