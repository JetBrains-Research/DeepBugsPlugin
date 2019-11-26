package org.jetbrains.research.deepbugs.common

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.util.xmlb.annotations.Property
import org.jetbrains.research.deepbugs.common.ide.msg.DeepBugsLifecycle

abstract class DeepBugsConfig(val configId: String, private val default: State) : PersistentStateComponent<DeepBugsConfig.State> {
    data class State(
        @Property val binOperatorThreshold: Float = 0.0f,
        @Property val binOperandThreshold: Float = 0.0f,
        @Property val swappedArgsThreshold: Float = 0.0f,
        @Property val userDisabledChecks: HashSet<String> = HashSet()
    ) {
        fun copy(): State = State (
            binOperatorThreshold = binOperatorThreshold,
            binOperandThreshold = binOperandThreshold,
            swappedArgsThreshold = swappedArgsThreshold,
            userDisabledChecks = HashSet(userDisabledChecks)
        )
    }

    private var myState: State? = null

    override fun getState(): State {
        if (myState == null) {
            update(default)
        }
        return myState!!.copy()
    }

    override fun loadState(state: State) {
        update(state)
    }

    fun disableCheck(expr: String) {
        val newState = state.also { it.userDisabledChecks.add(expr) }
        update(newState)
    }

    fun enableCheck(expr: String) {
        val newState = state.also { it.userDisabledChecks.remove(expr) }
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
