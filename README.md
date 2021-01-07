sbt-tdml
===

Reduce boilerplate by eliminating the TDML unit test companion class.

### Two components
1. Suite generating SBT plugin
2. SBT test interface TDML runner

#### Questions
1. Are both components necessary?
  - Is there value in the test interface?

### Try it
1. `sbt pubishLocal` from root project
2. `sbt plugin/scripted` from root project
3. `sbt -Dplugin.version=0.2-SNAPSHOT test` from test project

#### output

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
