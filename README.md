# TableTest

![Build](https://github.com/nchaugen/tabletest-intellij/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/27334-tabletest.svg)](https://plugins.jetbrains.com/plugin/27334-tabletest)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/27334-tabletest.svg)](https://plugins.jetbrains.com/plugin/27334-tabletest)

<!-- Plugin description -->
Adds support for the table language used in [TableTest](https://github.com/nchaugen/tabletest) for data-driven testing in JUnit.

## What is TableTest?

[TableTest](https://github.com/nchaugen/tabletest) is a JUnit extension that lets you write data-driven tests as readable tables instead of repetitive test methods. Instead of writing multiple similar test methods or complex parameterized tests, you define test cases in a table format where:

- Each **column** represents a test input parameter or expected output
- Each **row** represents a test case

This approach makes test data clear, maintainable, and easy to extend. This plugin provides IDE support for working with TableTest tables, including syntax highlighting, auto-formatting, and language injection into Java and Kotlin test files.

## Features
- Auto-formatting TableTest table to align columns
- Syntax highlighting for TableTest tables
- Auto-injection of TableTest language for `@TableTest` annotations in Java and Kotlin files
- Shortcut for toggling commenting of selected lines
- Move row up/down with keyboard shortcuts

<!-- Plugin description end -->

## Table of Contents

- [Quick Start](#quick-start)
- [Usage Guide](#usage-guide)
  - [Writing TableTest Tables](#writing-tabletest-tables)
  - [Keyboard Shortcuts](#keyboard-shortcuts)
  - [Language Injection](#language-injection)
  - [Customizing Colours](#customizing-colours)
- [Installation](#installation)

## Quick Start

Here's what a TableTest looks like in your Java or Kotlin test:

```java
@TableTest("""
    | a | b | sum? |
    | 1 | 2 | 3    |
    | 5 | 3 | 8    |
    """)
void testAddition(int a, int b, int sum) {
    assertThat(a + b).isEqualTo(sum);
}
```

The plugin provides:

**Syntax Highlighting**: Different colours for headers (`a`, `b`, `sum?`), delimiters (`|`), and values

**Auto-Formatting**: Press `Ctrl+Alt+L` (Windows/Linux) or `Cmd+Option+L` (macOS) to align columns:

```java
// Before formatting:
| a | b | sum? |
| 1 | 2 | 3 |
| 500 | 300 | 800 |

// After formatting:
| a   | b   | sum? |
| 1   | 2   | 3    |
| 500 | 300 | 800  |
```

**Automatic Language Injection**: When you use `@TableTest` annotations, the plugin automatically provides TableTest language support. No manual configuration needed.

## Usage Guide

### Writing TableTest Tables

A TableTest table consists of:

- **Header row**: Column names defining test parameters
  - Add `?` suffix for expected output columns (e.g., `sum?`)
- **Data rows**: Test case values

The plugin supports various value types:
- Simple values: `1`, `true`, `hello`
- Quoted strings: `"text with spaces"`
- Lists: `[1, 2, 3]`
- Sets: `{a, b, c}`
- Maps: `{key: value, foo: bar}`

### Keyboard Shortcuts

| Action            | Windows/Linux     | macOS            |
|-------------------|-------------------|------------------|
| Format table      | `Ctrl+Alt+L`      | `Cmd+Option+L`   |
| Comment/uncomment | `Ctrl+/`          | `Cmd+/`          |
| Move row up       | `Ctrl+Shift+Up`   | `Cmd+Shift+Up`   |
| Move row down     | `Ctrl+Shift+Down` | `Cmd+Shift+Down` |

### Language Injection

The plugin automatically injects TableTest language support into `@TableTest` annotations. For development scenarios where the TableTest library isn't on your classpath yet, you can manually enable support using:

```java
//language=tabletest
String table = """
    | a | b |
    | 1 | 2 |
    """;
```

### Customizing Colours

Adjust syntax highlighting colours:

1. Go to **Settings/Preferences** > **Editor** > **Colour Scheme** > **TableTest**
2. Customise colours for headers, delimiters, values, and comments
3. Changes apply immediately

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "tabletest"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/nchaugen/tabletest-intellij/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
