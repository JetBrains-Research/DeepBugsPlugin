package org.jetbrains.research.deepbugs.common

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.util.xmlb.annotations.Property
import org.jetbrains.research.deepbugs.common.ide.msg.DeepBugsLifecycle

abstract class DeepBugsConfig(private val default: State) : PersistentStateComponent<DeepBugsConfig.State> {
    data class State(
        @Property val binOperatorThreshold: Float = 0.0f,
        @Property val binOperandThreshold: Float = 0.0f,
        @Property val swappedArgsThreshold: Float = 0.0f,
        @Property val quickFixesThreshold: Float = 0.3f,
        @Property val userDisabledChecks: Set<String> = emptySet()
    )

    private var myState: State? = null

    override fun getState(): State {
        if (myState == null) {
            update(default)
        }
        return myState!!
    }

    override fun loadState(state: State) {
        update(state)
    }

    fun disableCheck(expr: String) {
        val newState = State(
            binOperatorThreshold = state.binOperatorThreshold,
            binOperandThreshold = state.binOperandThreshold,
            swappedArgsThreshold = state.swappedArgsThreshold,
            userDisabledChecks = state.userDisabledChecks + expr
        )
        update(newState)
    }

    fun enableCheck(expr: String) {
        val newState = State(
            binOperatorThreshold = state.binOperatorThreshold,
            binOperandThreshold = state.binOperandThreshold,
            swappedArgsThreshold = state.swappedArgsThreshold,
            userDisabledChecks = state.userDisabledChecks - expr
        )
        update(newState)
    }


    fun update(new: State) {
        if (myState == null) {
            myState = new

            DeepBugsLifecycle.publisher.init(state)
        } else {
            val prevState = myState!!
            myState = new

            DeepBugsLifecycle.publisher.update(prevState, state)
        }
    }
}
