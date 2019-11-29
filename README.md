<h1> <img align="left" width="40" height="40" src="https://s3-eu-west-1.amazonaws.com/public-resources.ml-labs.aws.intellij.net/static/deep-bugs-icon.svg" alt="DeepBugs Icon"> DeepBugs for IntelliJ </h1>

[![JB Research](https://jb.gg/badges/research-flat-square.svg)](https://research.jetbrains.org/)
[![CircleCI](https://img.shields.io/circleci/build/github/JetBrains-Research/DeepBugsPlugin.svg?style=flat-square)](https://circleci.com/gh/JetBrains-Research/DeepBugsPlugin)
[![Plugin Gitter](https://img.shields.io/static/v1?message=deepbugs&label=gitter&color=brightgreen&logo=gitter&style=flat-square)](https://gitter.im/deepbugs4intellij/community)
[![JS Plugin](https://img.shields.io/jetbrains/plugin/v/12220-deepbugsjavascript.svg?style=flat-square&label=js%20plugin)](https://plugins.jetbrains.com/plugin/12220-deepbugsjavascript)
[![JS Downloads](https://img.shields.io/jetbrains/plugin/d/12220-deepbugsjavascript.svg?style=flat-square&label=js%20downloads)](https://plugins.jetbrains.com/plugin/12220-deepbugsjavascript)
[![Py Plugin](https://img.shields.io/jetbrains/plugin/v/12218-deepbugspython.svg?style=flat-square&label=py%20plugin)](https://plugins.jetbrains.com/plugin/12218-deepbugspython)
[![Py Downloads](https://img.shields.io/jetbrains/plugin/d/12218-deepbugspython.svg?style=flat-square&label=py%20downloads)](https://plugins.jetbrains.com/plugin/12218-deepbugspython)


__DeepBugs for IntelliJ__ is a pair of plugins for  IntelliJ-based IDEs that provide semantics-aware bug detection in Python and JavaScript. The plugins use deep learning models inspired by the [DeepBugs framework](https://github.com/michaelpradel/DeepBugs) to extract the semantics of code and find bugs. 

The plugins are available for download: [Python](https://plugins.jetbrains.com/plugin/12218-deepbugspython/), [JavaScript](https://plugins.jetbrains.com/plugin/12220-deepbugsjavascript/).

<p align="center">
  <img src="https://s3-eu-west-1.amazonaws.com/public-resources.ml-labs.aws.intellij.net/static/deep-bugs-demo.gif" width="70%" />
</p>

## Getting started
Code inspections in the plugins detect several types of bugs, including incorrect function arguments, incorrect comparison, and others, based on code semantics.

If the plugin provides too many false positives for your code, you can increase any specific threshold manually in __Preferences / Settings__ under __Tools | DeepBugsPython/DeepBugsJS__

To fall back to original precalculated values, you can use the __Default__ and __Default All__ buttons.

## Supported code inspections
### Incorrect binary operator
The inspection detects misuse of binary operators (such as `<`, `<=`, `+`, etc.).

For example, it may detect the following bugs:
- `i <= length` (index is less or equal to length, but should be less)
- `text + binary` (concatenated non-compatible types)

### Incorrect binary operand
The inspection detects misuse of binary operands (arguments of binary operations).

For example:
- `height - x` (in most cases it should be `height - y`)
- `j < params` (should be `params.length`)

### Incorrect function arguments
The inspection detects misuse of function arguments (specifically, their order).

For example:
- `startPoller(100, function(delay, fn) { … })` (should be fn, delay)
- `2 % i` (unusual order of operands)

## Got any more questions?
If you want to know more about the DeepBugs framework that these plugins are based on, please [refer to this paper](http://software-lab.org/publications/oopsla2018_DeepBugs.pdf).

If you have any questions about the plugins themselves, please don’t hesitate to contact us on [Gitter](https://gitter.im/deepbugs4intellij/community).
