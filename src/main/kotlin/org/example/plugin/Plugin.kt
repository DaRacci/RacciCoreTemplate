package org.example.plugin

import co.aikar.commands.BaseCommand
import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.api.plugin.RacciPlugin

class Plugin: RacciPlugin(
    "&6Example Plugin"
) {

    companion object {
        lateinit var instance: Plugin ; private set // Private set so it can't be messed with later
        // Ease of access methods
        val log get() = instance.log
        val asyncDispatcher get() = instance.asyncDispatcher
        val syncDispatcher get() = instance.minecraftDispatcher
        fun launch(block: suspend CoroutineScope.() -> Unit) = instance.launch(block)
        fun launchAsync(block: suspend CoroutineScope.() -> Unit) = instance.launchAsync(block)
    }

    override suspend fun handleEnable() {
        instance = this
    }

    override suspend fun registerListeners(): List<KotlinListener> {
        return listOf(
            ExampleListener(this),
        )
    }

    override suspend fun registerCommands(): List<BaseCommand> {
        return listOf(
            ExampleCommand(),
        )
    }

    override suspend fun registerLifecycles(): List<Pair<LifecycleListener<*>, Int>> {
        return listOf(
            ExampleLifecycle(this) to 1 // Pairing this to 1 means it will load first and disable last
        )
    }


}