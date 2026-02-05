# TableTest Plugin Changelog

## [Unreleased]

## [0.2.1] - 2026-02-05

### Added

- Support `org.tabletest.junit.TableTest` annotations
- Treat `@TypeConverter` methods as used in both Java and Kotlin by the unused declaration/symbol inspections

## [0.2.0] - 2026-01-17

### Changed

- Expected header columns are now bold by default
- Data rows now align with header row position in Kotlin tables, allowing manual control of table indentation

### Fixed

- Quoted strings containing delimiters (commas, brackets, braces) inside compound structures were incorrectly parsed
- Comments now align with header and data rows in Kotlin raw strings (previously comments lost their indentation)
- Data rows following comments now re-align correctly in Java text blocks (previously rows with extra indentation stayed misaligned)
- Lines with varying input indentation now normalize to a consistent alignment position

## [0.1.0] - 2026-01-16

### Added

- Move row up/down with keyboard shortcuts (Cmd+Shift+Up/Down on Mac, Alt+Shift+Up/Down on Windows/Linux)

### Fixed

- First-line comments were incorrectly parsed as headers
- Unpaired quotes in unquoted strings were incorrectly shown as illegal

## [0.0.10] - 2025-12-08

### Security

- Bumped dependencies

## [0.0.9] - 2025-06-08

### Added

- Autoformatting of elements inside maps, sets and lists

### Fixed

- Allow comma and colon in unquoted values except when elements in a compound value

## [0.0.8] - 2025-06-01

### Fixed

- Allow first data cell to be blank
- Allow comments to be empty

## [0.0.7] - 2025-05-22

### Added

- Support set type values

### Fixed

- Allow first data cell to be a list or quoted string

## [0.0.6] - 2025-05-13

### Fixed

- Injection in `@TableTest` annotation for Java and Kotlin
- Indentation when adding data rows to a TableTest file (`*.table`)
- Improved alignment in Kotlin multi-line strings

## [0.0.5] - 2025-05-11

### Added

- Plugin icons

### Security

- Bumped dependencies

## [0.0.4] - 2025-05-11

### Fixed

- Syntax highlighting for non-comment occurrence of '//'

## [0.0.3] - 2025-05-02

### Added

- General support for empty lines anywhere in table

## [0.0.2] - 2025-04-26

### Added

- Allowing initial empty line to better support Kotlin multiline strings

## [0.0.1] - 2025-03-18

### Added

- Formatting of TableTest tables
- Syntax highlighting in TableTest tables
- Auto-injection of TableTest language for Java `@TableTest` annotations

[Unreleased]: https://github.com/nchaugen/tabletest-intellij/compare/v0.2.1...HEAD
[0.2.1]: https://github.com/nchaugen/tabletest-intellij/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/nchaugen/tabletest-intellij/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.10...v0.1.0
[0.0.10]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.9...v0.0.10
[0.0.9]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.8...v0.0.9
[0.0.8]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.7...v0.0.8
[0.0.7]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.6...v0.0.7
[0.0.6]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.5...v0.0.6
[0.0.5]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.4...v0.0.5
[0.0.4]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.3...v0.0.4
[0.0.3]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/nchaugen/tabletest-intellij/compare/v0.0.1...v0.0.2
[0.0.1]: https://github.com/nchaugen/tabletest-intellij/commits/v0.0.1
