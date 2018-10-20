# DeepBugsPlugin

PyCharm/IntelliJ IDEA(with Python plugin installed) plugin for [DeepBugs](https://github.com/ml-in-programming/DeepBugs) framework.

This plugin provides several code inspections which objectives are to analyze the code of a project opened in IDE and to highlight potential bugs in it.

## Usage
After the plugin is installed and enabled, code inspections become available.
They are enabled by default and can be disabled in `Preferences|Settings|Editor|Inspections|Python|...` subcategory.

## Supported inspections
### Incorrect binary operator inspection
_Inspection_ _name:_ `DeepBugs: Possibly incorrect binary operator`

Code inspection, intended to detect and report any possibly incorrect binary operators.

## Configuration
DeepBugs plugin computes the probability that a piece of code is an instance of a particular bug pattern. Then it reports possible bugS, if the computed value is above a threshold. Threshold is configurable and can be set manually in `Preferences/Settings|Tools|DeepBugs` or reset to the default state with the `Default` button.
