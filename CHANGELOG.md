# Changelog
All notable changes to this project will be documented in this file.

## [Unreleased]

### Fixed
- DataDomainToStringStrategyConfiguration not imported via AutoConfiguration.

## [1.0.10] - 2023-08-30

### Changed
- Refactor ToStringStrategy.supports to accept target object instead of Class<?>, to support JDK proxy instance.
- Change all ToStringStrategy to use @Autowired variables, instead of constructor arguments. 
- Support ToStringStrategy for Pageable, Slice and Page.

## [1.0.9] - 2023-08-26

### Fixed
- Fix ReflectionToStringStrategy getting inaccessible exception when given JDK proxy instance.

## [1.0.8] - 2022-05-10

### Changed
- Upgrade Spring Boot version to 2.6.6
- Upgrade Jacoco Maven Plugin version to 0.8.7
- Extend ReflectionToStringStrategy to exclude empty and zero values by default.


## [1.0.7] - 2022-01-26

### Changed
- Change ReflectionToStringStrategy.supports() to filter by baseClasses instead of basePackages.


## [1.0.6] - 2022-01-25

### Changed
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


