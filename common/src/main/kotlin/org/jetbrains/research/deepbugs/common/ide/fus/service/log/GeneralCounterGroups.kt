package org.jetbrains.research.deepbugs.common.ide.fus.service.log

enum class GeneralCounterGroups(val groupId: String) {
    INSPECTION_REPORT("dbp.inspection.metrics"),
    SETTINGS_UI("dbp.settings.ui"),
    SETTINGS("dbp.settings"),
    ERRORS("dbp.errors")
}
