# Pull Request Validation Instructions

When reviewing or preparing a pull request for this repository, apply these
constraints before considering the work complete.

## Scope and quality

- Keep changes focused on the pull request's stated purpose. Do not mix
  refactoring, formatting-only changes, or dependency upgrades into an
  unrelated fix.
- Preserve public API and persistence compatibility. Renaming, removing, or
  changing the behavior of public classes, methods, serialized values, or
  database fields is a breaking change unless the pull request explicitly
  documents a migration path and a compatible version change.
- Do not silently change validation, persistence, or point-calculation
  semantics. Cover intentional behavior changes with tests.
- Keep source, tests, and documentation consistent. Update relevant
  documentation when a public API or supported behavior changes.

## Java correctness

- For every modified `equals` implementation, verify that `hashCode` uses
  exactly the same fields and equivalence rules.
- Do not use the JVM default locale for identifiers, lookup keys, or hash-code
  normalization. Use `Locale.ROOT` for language-neutral case conversions, for
  example `value.toLowerCase(Locale.ROOT)`.
- Treat entity identity changes carefully. Ensure equality, hash code, and
  persistence mappings remain stable across a persistence round trip.
- Prefer explicit validation and error propagation. Do not introduce broad
  exception handling or silent fallback behavior.

## Tests and build

- Add or update a focused regression test for every bug fix and behavioral
  change. Test observable behavior, including relevant edge cases.
- Run the smallest Maven command that covers the affected behavior. Run
  `mvn -B verify` when changing persistence, integration behavior, build
  configuration, dependencies, or public APIs.
- Do not report validation as successful when a required command was skipped
  or failed. State the command and reason clearly in the pull request.

## Pull request review

- Review the complete diff, including generated configuration and dependency
  metadata, for unintended changes.
- Call out breaking API changes, data compatibility risks, missing tests,
  thread-safety concerns, and locale-sensitive behavior as actionable review
  findings.
- Do not approve a pull request with failing tests, compiler errors, or
  unresolved high-severity review findings.
