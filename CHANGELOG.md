# Changelog
All notable changes to this project will be documented in this file.

## [Unreleased]
- Rename canSupport() method to supports() in ToStringStrategy.
- Add optional ReflectionToStringBuilder support in ObjectToStringStrategy.

## [1.0.5] - 2022-01-16

### Added
- Support custom ToStringStrategy, fallback to ObjectToStringStrategy.


## [1.0.4] - 2022-01-14

### Changed
- Add ToStringStrategy for StringSupplierRegistrar supply String value.
- Rename package for afterreturning and afterthrowing.

### Fixed
- Fix exception when MethodSignature.getParameterNames() return null.


## [1.0.3] - 2021-11-11

### Changed
- Change to use StringSubstitutor and all StringSupplierRegistrar as Bean.
- Refactor package naming.
- Do not create loggers when disabled.


