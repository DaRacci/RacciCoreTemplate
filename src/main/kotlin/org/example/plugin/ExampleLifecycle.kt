package org.example.plugin

import me.racci.raccicore.api.lifecycle.LifecycleListener
import org.example.plugin.Plugin.Companion.log

class ExampleLifecycle(
    override val plugin: Plugin
): LifecycleListener<Plugin> {

    // Fired when the plugin starts
    override suspend fun onEnable() {
        log.info("This is a really cool plugin!")
    }

    // Fired when the plugin reloads
    override suspend fun onReload() {
        log.info("I'm reloading this really cool plugin!")
    }

    // Fired when the plugin disables
    override suspend fun onDisable() {
        log.info("I'm disabling this really cool plugin!")
    }

}