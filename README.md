# DeepBugsPlugin

IntelliJ Platform plugins inspired by [DeepBugs](https://github.com/ml-in-programming/DeepBugs) framework. 

These plugins provide several code inspections which objectives are to analyze the code of a project opened in IDE and to highlight potential bugs and code quality issues in it.

## Supported languages
- Python ([PyCharm/IntelliJ IDEA plugin](https://github.com/ml-in-programming/DeepBugsPlugin/tree/master/DeepBugsPython))
- JavaScript ([WebStorm/IntelliJ IDEA Ultimate plugin](https://github.com/ml-in-programming/DeepBugsPlugin/tree/master/DeepBugsJavaScript))

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
