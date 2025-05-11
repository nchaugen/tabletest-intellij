# TableTest

![Build](https://github.com/nchaugen/tabletest-intellij/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/27334-tabletest.svg)](https://plugins.jetbrains.com/plugin/27334-tabletest)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/27334-tabletest.svg)](https://plugins.jetbrains.com/plugin/27334-tabletest)

<!-- Plugin description -->
Adds support for the table language used in [TableTest](https://github.com/nchaugen/tabletest) for data-driven testing in JUnit 5.

Features:
- Auto-formatting TableTest table to align columns
- Syntax highlighting for TableTest tables
- Auto-injection of TableTest language for `@TableTest` annotations in Java and Kotlin files
- Shortcut for toggling commenting of selected lines

<!-- Plugin description end -->

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
