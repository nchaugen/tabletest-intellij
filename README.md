# TableTest

![Build](https://github.com/nchaugen/tabletest-intellij/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/27334-tabletest.svg)](https://plugins.jetbrains.com/plugin/27334-tabletest)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/27334-tabletest.svg)](https://plugins.jetbrains.com/plugin/27334-tabletest)

<!-- Plugin description -->
Adds Java and Kotlin editor support for the [TableTest](https://github.com/nchaugen/tabletest) table language used in data-driven JUnit tests.

TableTest expresses test cases as readable tables, where each column is an input or expected output and each row is a test case. This plugin makes those tables easier to read, edit, and maintain in IntelliJ IDEA.

## Features
- Configurable syntax highlighting colours for headers, values, separators, comments, and invalid values
- Smart auto-formatting for table indentation, row/column alignment, and value separators
- Configurable value spacing rules in `Code Style > TableTest > Spaces`
- Language injection for `@TableTest` in Java and Kotlin files
- Row move shortcuts and comment toggle support

<!-- Plugin description end -->

## Table of Contents

- [Quick Start](#quick-start)
- [Usage Guide](#usage-guide)
  - [Writing TableTest Tables](#writing-tabletest-tables)
  - [Keyboard Shortcuts](#keyboard-shortcuts)
  - [Language Injection](#language-injection)
  - [Customizing Formatting](#customizing-formatting)
  - [Customizing Colours](#customizing-colours)
- [Installation](#installation)

## Quick Start

Here's what a TableTest looks like in your Java or Kotlin test:

```java
@TableTest("""
    a | b | sum?
    1 | 2 | 3
    5 | 3 | 8
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
a | b | sum?
1 | 2 | 3
500 | 300 | 800

// After formatting:
a   | b   | sum?
1   | 2   | 3
500 | 300 | 800
```

**Automatic Language Injection**: When you use `@TableTest` annotations, the plugin automatically provides TableTest language support. No manual configuration needed. It also recognizes these methods as test entry points to suppress "Unused declaration" warnings.

## Usage Guide

### Writing TableTest Tables

A TableTest table consists of:

- **Header row**: Column names defining test parameters
  - Add `?` suffix for expected output columns (e.g., `sum?`)
- **Data rows**: Test case values

The plugin supports various value types:
- Simple values: `1`, `true`, `hello`
- Quoted strings: `"text with spaces"` (supports escaped quotes: `"contains \"escaped\" quotes"`)
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
    a | b
    1 | 2
    """;
```

### Customizing Formatting

Adjust value spacing rules used by the TableTest formatter:

1. Go to **Settings/Preferences** > **Editor** > **Code Style** > **TableTest** > **Spaces**
2. Configure spacing around commas and colons, plus spaces within `[]` and `{}`
3. Use the preview pane to see formatting changes while keeping columns aligned

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

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/27334-tabletest/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/nchaugen/tabletest-intellij/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
