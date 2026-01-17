# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

IntelliJ Platform plugin providing language support for TableTest – a data-driven testing format for JUnit. The plugin adds syntax highlighting, auto-formatting of table columns, and language injection into `@TableTest` annotations in Java and Kotlin files.

## Build Commands

```bash
./gradlew build                    # Build the plugin
./gradlew test                     # Run all tests
./gradlew test --tests "TableTestCodeInsightTest.testFormatter"  # Run single test
./gradlew runIde                   # Launch IDE sandbox with plugin
./gradlew buildPlugin              # Create distribution JAR
```

## Architecture

### Grammar & Parser (GrammarKit + JFlex)

- `src/main/kotlin/.../TableTest.bnf` - BNF grammar defining table syntax (headers, rows, elements)
- `src/main/kotlin/.../TableTest.flex` - JFlex lexer with token definitions
- `src/main/gen/` - Auto-generated parser and PSI classes (regenerated on build)

### Core Components

| Component                              | Purpose                              |
|----------------------------------------|--------------------------------------|
| `TableTestLanguage.kt`                 | Language singleton (ID: "TableTest") |
| `TableTestFileType.kt`                 | Registers `.table` file extension    |
| `TableTestParserDefinition.kt`         | Connects lexer, parser, PSI          |
| `TableTestFormattingModelBuilder.kt`   | Entry point for formatting           |
| `TableTestBlock/RowBlock/CellBlock.kt` | Pipe alignment and spacing           |
| `TableTestSyntaxHighlighter.kt`        | Token-to-color mapping               |

### Language Injection

- `META-INF/injections.xml` - Injects TableTest into `@TableTest` annotation values
- Uses `injector-id="java"` pattern matching on annotation FQN `io.github.nchaugen.tabletest.junit.TableTest`
- Kotlin code imports and uses this same Java annotation, so auto-injection works in both languages
- The `//language=tabletest` hint can also be used and is useful during development before the tabletest library is on the classpath

### Formatting Logic

The formatter handles four concerns: indentation, row alignment, column alignment, and value formatting. See [FEATURES.md](FEATURES.md) for details.

Key implementation points:
- Pipe alignment uses `Alignment.createAlignment(true)` shared across rows
- Indentation is delegated to host formatter via `InjectedFormattingOptionsProvider`
- First column alignment ensures header and data rows align

## Test Structure

See [TEST-STRATEGY.md](TEST-STRATEGY.md) for the full testing strategy.

**Key principle**: Context-independent logic (lexer, parser, alignment, spacing) is tested once with `.table` files. Context-dependent logic (indentation, boundary handling) has targeted tests per context.

Test data in `src/test/testData/` - pairs of input files and expected outputs.

**Known test limitation**: Kotlin formatter tests with blank lines/comments are disabled due to a `LightJavaCodeInsightFixtureTestCase` bug. Formatting works correctly in the editor; this is a test framework issue only.

## Plugin Configuration

Main config: `src/main/resources/META-INF/plugin.xml`
- Dependencies: `com.intellij.modules.platform`, `org.intellij.intelliLang` (optional)
- Platform: IntelliJ 2025.2+ (build 242+)
