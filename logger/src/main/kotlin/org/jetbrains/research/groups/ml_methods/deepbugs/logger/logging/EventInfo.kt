package org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging

enum class EventId(val id: String) {
    BUG_REPORT("registered"),
    THRESHOLD_CONFIGURED("invoked")
}

enum class EventGroup(val id: String, val version: String) {
    PY_BUG_REPORT("deep.bugs.python.inspection", "1"),
    PY_THRESHOLD_CONFIGURED("deep.bugs.python.threshold.configured", "1"),
    JS_BUG_REPORT("deep.bugs.javascript.inspection", "1"),
    JS_THRESHOLD_CONFIGURED("deep.bugs.javascript.threshold.configured", "1")
}