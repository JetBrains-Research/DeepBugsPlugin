# DeepBugsPlugin


[![CircleCI](https://img.shields.io/circleci/build/github/JetBrains-Research/DeepBugsPlugin.svg?style=flat-square)](https://circleci.com/gh/JetBrains-Research/DeepBugsPlugin)
[![JS Plugin](https://img.shields.io/jetbrains/plugin/v/12220-deepbugsjavascript.svg?style=flat-square&label=js%20plugin)](https://plugins.jetbrains.com/plugin/12220-deepbugsjavascript)
[![JS Downloads](https://img.shields.io/jetbrains/plugin/d/12220-deepbugsjavascript.svg?style=flat-square&label=js%20downloads)](https://plugins.jetbrains.com/plugin/12220-deepbugsjavascript)
[![Py Plugin](https://img.shields.io/jetbrains/plugin/v/12218-deepbugspython.svg?style=flat-square&label=py%20plugin)](https://plugins.jetbrains.com/plugin/12218-deepbugspython)
[![Py Downloads](https://img.shields.io/jetbrains/plugin/d/12218-deepbugspython.svg?style=flat-square&label=py%20downloads)](https://plugins.jetbrains.com/plugin/12218-deepbugspython)

Plugins for IntelliJ Platform-based IDEs inspired by [DeepBugs](https://github.com/JetBrains-Research/DeepBugs) framework. 

These plugins provide several code inspections which objectives are to analyze the code of a project opened in IDE and to highlight potential bugs and code quality issues in it.

## Requirements
Currently available only for a 64-bit JVM.

Plugins require model files to perform code analysis. It would be suggested to download required files when each of the plugins is launched for the first time.

## Usage
After the plugins are installed and enabled, code inspections become available.
Inspections are enabled by default and could be disabled in `Preferences/Settings | Editor | Inspections | Python/JavaScript |...` subcategory.

## Supported inspections
### Incorrect binary operator inspection
_Inspection_ _name:_ `DeepBugs: Possibly incorrect binary operator`
Incorrect binary operator inspection is intended to detect and report any potential misuse of
binary operators.

### Incorrect binary operand inspection
_Inspection_ _name:_ `DeepBugs: Possibly incorrect binary operand`

Incorrect binary operand inspection is intended to detect and report any binary expression in which
one of the operands is potentially misused.

### Swapped function arguments inspection
_Inspection_ _name:_ `DeepBugs: Possibly swapped function arguments`

Reports any function call with probably swapped arguments (supported only for calls with two arguments).

## Configuration
Code inspections compute the probability that a piece of code is an instance of a particular bug pattern. Then they report possible bugs if the computed value is above a threshold. 
Each code inspection uses its own threshold value. Thresholds are configurable and could be set manually in `Preferences/Settings | Tools | DeepBugsPython/DeepBugsJavaScript` or reset to the default state with the `Default` or `Default all` buttons.
