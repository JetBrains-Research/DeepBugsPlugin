package org.jetbrains.research.deepbugs.common

import com.intellij.openapi.components.PersistentStateComponent
import org.jetbrains.research.deepbugs.common.ide.msg.DeepBugsLifecycle

abstract class DeepBugsConfig(val configId: String, private val default: State) : PersistentStateComponent<DeepBugsConfig.State> {
    data class State(val binOperatorThreshold: Float, val binOperandThreshold: Float, val swappedArgsThreshold: Float)

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
