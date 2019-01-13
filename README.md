# DeepBugsPlugin

PyCharm/IntelliJ IDEA(with Python plugin installed) plugin for [DeepBugs](https://github.com/ml-in-programming/DeepBugs) framework. 

This plugin provides several code inspections which objectives are to analyze the code of a project opened in IDE and to highlight potential bugs in it.

## Requirements
Currently available only for a 64-bit JVM.

DeepBugsPlugin requires models files to perform code analysis. It would be suggested to download required files when the plugin is launched for the first time.

## Usage
After the plugin is installed and enabled, code inspections become available.
They are enabled by default and could be disabled in `Preferences/Settings | Editor | Inspections | Python |...` subcategory.

## Supported inspections
### Incorrect binary operator inspection
_Inspection_ _name:_ `DeepBugs: Possibly incorrect binary operator`

Code inspection, intended to detect and report any possibly incorrect binary operators.

### Incorrect binary operand inspection
_Inspection_ _name:_ `DeepBugs: Possibly incorrect binary operand`

Code inspection, intended to detect and report any binary expression in which one of the operands is probably incorrect.

### Swapped function arguments inspection
_Inspection_ _name:_ `DeepBugs: Possibly swapped function arguments`

Code inspection, intended to detect report any function call in which arguments are probably swapped.

## Configuration
DeepBugsPlugin computes the probability that a piece of code is an instance of a particular bug pattern. Then it reports possible bugs, if the computed value is above a threshold. 
Each code inspection uses its own threshold value. Thresholds are configurable and could be set manually in `Preferences/Settings | Tools | DeepBugs` or reset to the default state with the `Default` button.
