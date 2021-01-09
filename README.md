sbt-tdml
===

Reduce boilerplate by eliminating the TDML unit test companion class.

### sbt components
1. A test suite generating plugin
2. A test interface for the generated suites

### try it
1. `sbt pubishLocal` from root project
2. `sbt plugin/scripted` from root project
3. `sbt -Dplugin.version=0.2-SNAPSHOT test` from test project

#### sbt scripted output

Running `sbt test` on the scripted [tdml/simple](plugin/src/sbt-test/tdml/simple) project, which contains no test classes only tdml descriptors, results in

```
[info] [info] TDML Suite: boolean.tdml
[info] [info]   - booleanDefault
[info] [info]   - booleanDefaultSDE
[info] [info]   - booleanInputValueCalc
[info] [info]   - booleanInputValueCalcError
[info] [info] TDML Suite: nested/literal-character-nils.tdml
[info] [info]   - text_01
[info] [info]   - text_01ic
[info] [info]   - text_02
[info] [info]   - text_03
[info] [info]   - text_04
[info] [info]   - binary_01
[info] [info] TDML Suite: literal-value-nils.tdml
[info] [info]   - text_03
[info] [info]   - text_03ic
[info] [info]   - text_04
[info] [info]   - text_05
[info] [info]   - text_06
[info] [info]   - binary_01
[info] [info]   - test_padded_nils
[info] [info]   - test_complex_nil
[info] [info]   - text_nil_characterClass_04_parse
[info] [info]   - nillable_ovc_01
[info] [info] Passed: Total 20, Failed 0, Errors 0, Passed 20
[info] [success] Total time: 18 s, completed Jan 7, 2021 8:56:57 AM
```

### daffodil integration output

A test run in the Daffodil test project after removing all scala unit tests and performing a couple small clean up tweaks

```
#! sbt daffodil-test/test

...

[error] Error: Total 3365, Failed 680, Errors 1, Passed 2684
[error] Failed tests:
[error]         Tdml_multiFile
[error]         Tdml_TextNumberProps
[error]         Tdml_PropertySyntax
[error]         Tdml_Functions
[error]         Tdml_PatternTests
[error]         Tdml_SimpleTypes
[error]         Tdml_BitOrder
[error]         Tdml_Entities
[error]         Tdml_PrefixedTests
[error]         Tdml_Aligned_Data
[error]         Tdml_variables
[error]         Tdml_UserSubmittedTests
[error]         Tdml_dfdl_schema_validation_diagnostics
[error]         Tdml_Facets
[error]         Tdml_PropertyScoping
[error]         Tdml_DelimiterProperties
[error]         Tdml_inputValueCalc
[error]         Tdml_expression_fail
[error]         Tdml_Validation
[error]         Tdml_ProcessingErrors
[error]         Tdml_assert
[error]         Tdml_namespaces
[error]         Tdml_TextStandardBase
[error]         Tdml_defineFormat
[error]         Tdml_expressions
[error]         Tdml_DFDLSubset
[error]         Tdml_escapeSchemeNeg
[error]         Tdml_ContentFramingProps
[error]         Tdml_general
[error]         Tdml_inputTypeCalc_malformed
[error]         Tdml_DelimitedTests
[error]         Tdml_variables_01
[error]         Tdml_SequenceGroup
[error]         Tdml_ExplicitTests
[error]         Tdml_BitOrderInvalid
[error]         Tdml_packed
[error]         Tdml_parseUnparseModeTest
[error]         Tdml_testUnparserFileBuffering
[error]         Tdml_SequenceGroupDelimiters
[error]         Tdml_RegularExpressions
[error]         Tdml_testUnparserGeneral
[error]         Tdml_discriminator
[error]         Tdml_expressions3
[error] Error during tests:
[error]         Tdml_escapeScheme
[error] (daffodil-test / Test / test) sbt.TestsFailedException: Tests unsuccessful
[error] Total time: 159 s (02:39), completed Jan 7, 2021 11:39:16 AM
```

https://github.com/jw3/incubator-daffodil/tree/sbt-tdml-demo
