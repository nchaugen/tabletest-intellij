# TableTest Plugin Test Strategy

This document describes the testing strategy for the TableTest IntelliJ plugin. It references features documented in [FEATURES.md](FEATURES.md).

## Principle: Test Context-Independent Logic Once

The plugin operates in three contexts: native `.table` files, Java text blocks, and Kotlin raw strings. However, not all functionality varies by context.

**Context-independent logic** (lexer, parser, PSI operations) produces identical results regardless of where the TableTest content originates. Testing this logic once with `.table` files provides complete coverage.

**Context-dependent logic** (host language interaction, offset mapping) may behave differently in each context and requires targeted tests for each.

## Feature Test Coverage

### Parsing and Conformance

| Aspect                  | Test Once | Test Per Context |
|-------------------------|-----------|------------------|
| Lexer tokenisation      | Yes       | -                |
| Parser AST construction | Yes       | -                |
| Error detection         | Yes       | -                |
| Syntax edge cases       | Yes       | -                |

**Rationale**: The lexer and parser operate on raw text extracted from any context. The PSI structure is identical regardless of source.

### Syntax Highlighting

| Aspect                  | Test Once | Test Per Context |
|-------------------------|-----------|------------------|
| Token-to-colour mapping | Yes       | -                |
| All syntax elements     | Yes       | -                |

**Rationale**: Highlighting is applied based on PSI element types, which are context-independent. Once tokens are correctly identified (verified by parsing tests), highlighting rules apply uniformly.

### Formatting

| Aspect                             | Test Once | Test Per Context |
|------------------------------------|-----------|------------------|
| Column alignment (pipe alignment)  | Yes       | -                |
| Row alignment (straight left edge) | -         | Yes              |
| Value formatting (spacing rules)   | Yes       | -                |
| Indentation                        | -         | Yes              |

**Rationale**: Column alignment and value formatting are pure TableTest logic operating on the PSI.

Row alignment and indentation are context-dependent because Java text blocks and Kotlin raw strings handle whitespace differently. Java text blocks strip shared leading indentation from the content, while Kotlin raw strings preserve indentation as-is. This affects how row alignment must be calculated.

### Comment Toggle

| Aspect              | Test Once | Test Per Context |
|---------------------|-----------|------------------|
| Comment prefix      | Yes       | -                |
| Toggle behaviour    | Yes       | Smoke test       |

**Rationale**: The comment prefix is context-independent. Caret position handling in injected content justifies a smoke test per context.

### Move Row Up/Down

| Aspect                      | Test Once | Test Per Context |
|-----------------------------|-----------|------------------|
| Row swapping logic          | Yes       | -                |
| Boundary constraints        | Yes       | Smoke test       |
| Comment/blank line handling | Yes       | -                |

**Rationale**: Row movement logic is context-independent, but boundary detection uses offset mapping in injected contexts. A smoke test per context verifies boundaries work correctly.

### Language Injection

| Aspect                                         | Test Once | Test Per Context |
|------------------------------------------------|-----------|------------------|
| Hint-based injection (`//language=tabletest`)  | -         | Yes              |
| XML-based auto-injection                       | -         | Yes              |

**Rationale**: The XML-based injection pattern matches on the annotation class FQN (`io.github.nchaugen.tabletest.junit.TableTest`). Since Kotlin code imports and uses this same Java annotation, auto-injection works in both languages. Tests verify that auto-injection and hint-based injection work correctly.

## Known Limitations

### Kotlin Formatter Test Framework Bug

Tests for formatting injected content in Kotlin raw strings containing blank lines or comments are disabled due to a bug in `LightJavaCodeInsightFixtureTestCase`. The test framework incorrectly maps offsets when formatting Kotlin raw strings, corrupting the text.

**This is a test framework limitation only** – formatting works correctly in the actual editor.

See `kotlin-formatter-test-bug-report.md` for details.

## Adding New Tests

When adding tests for new functionality:

1. **Determine context dependency**: Does the feature interact with the host language or use offset mapping?

2. **If context-independent**: Add comprehensive tests using `.table` files only

3. **If context-dependent**:
   - Add comprehensive tests using `.table` files for the core logic
   - Add focused integration tests for each context (Java, Kotlin) covering only the context-specific aspects

4. **Consider the Kotlin test limitation**: If testing formatter behaviour with blank lines or comments in Kotlin, expect the test framework bug to apply
