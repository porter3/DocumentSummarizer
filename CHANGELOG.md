# Change Log
Project changes including features and fixes will be documented here using semantic versioning in the format 'MAJOR.MINOR.PATCH' (e.g. v1.0.0). I started this log a bit late into the project, soon after a major API change. Because of that and for the sake of simplicity, the starting version here is 2.0.0.

This log exists so I can get a higher-level view of what I've done instead of looking at specific commit messages, as well as helping anyone interested with the same.
___

## 08/19/2020 - v2.0.0

### Changed
- Bug fix: loader messages are properly reset
- General code refactoring and simplified exception handling


## 09/04/2020 - v2.1.0

### Added
-Support for multilingual summarization (Spanish only at the moment)


## 09/06/2020 - v2.2.0

### Changed
-Text validation now executed asynchronously

### Removed
-Design change: Files are no longer uploaded to AWS S3


## 09/07/2020 - v.2.3.0

### Added
-Support for summarization in the following languages: Arabic, Danish, Dutch, Finnish, French, German, Hungarian, Italian, Norweigan, Portuguese, Romanian, Russian, Swedish
-Recognization of 53 more languages (but not necessarily support for all of them)