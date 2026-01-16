# TableTest Plugin Changelog

## [Unreleased]
### Changed
- Expected header columns are now bold by default
### Fixed
- Quoted strings containing delimiters (commas, brackets, braces) inside compound structures were incorrectly parsed

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
