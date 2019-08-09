package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.service.log

enum class GeneralCounterGroups(val groupId: String) {
    INSPECTION_REPORT("inspection.report"),
    UI_INVOKED("settings.ui.invoked"),
    TO_DEFAULT("settings.default"),
    CONFIGURED("settings.configured"),
}