sbt-tdml
===

Reduce boilerplate by eliminating the TDML unit test companion class.

#### Two components
1. Suite generating SBT plugin
2. SBT test interface TDML runner

#### Questions
1. Are both components necessary?
  - Is there value in the test interface?

#### Try it
1. `sbt pubishLocal` from root project
2. `sbt plugin/scripted` from root project
3. `sbt -Dplugin.version=0.1-SNAPSHOT test` from test project

### output

```
[info] [info] TDML Suite: boolean
[info] [info]   - booleanDefault
[info] [info]   - booleanDefaultSDE
[info] [info]   - booleanInputValueCalc
[info] [info] Passed: Total 3, Failed 0, Errors 0, Passed 3
```
