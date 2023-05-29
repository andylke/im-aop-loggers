# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

- Upgrade Java version to 17.
- Upgrade Spring Boot version to 3.1.0.
- Upgrade Jacoco Maven Plugin version to 0.8.10.
- Fix failed unit test due to Spring Boot upgrade.

## [1.0.8] - 2022-05-10

### Change

- Upgrade Spring Boot version to 2.6.6.
- Upgrade Jacoco Maven Plugin version to 0.8.7.
- Extend ReflectionToStringStrategy to exclude empty and zero values by default.

## [1.0.7] - 2022-01-26

### Change

- Change ReflectionToStringStrategy.supports() to filter by baseClasses instead of basePackages.

## [1.0.6] - 2022-01-25

### Change

- Rename canSupport() method to supports() in ToStringStrategy.

### Added

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


