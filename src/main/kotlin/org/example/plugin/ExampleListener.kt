package org.example.plugin

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.extensions.broadcast
import me.racci.raccicore.api.extensions.hasPermissionOrStar
import me.racci.raccicore.api.extensions.msg
import me.racci.raccicore.api.flow.playerEventFlow
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import net.kyori.adventure.text.minimessage.template.TemplateResolver
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class ExampleListener(
    private val plugin: Plugin
): KotlinListener {

    @EventHandler(priority = EventPriority.MONITOR)
    suspend fun onChat(event: AsyncChatEvent) {
        // If the player has this permission make them confirm this
        if(event.player.hasPermissionOrStar("plugin.doubleCheckChat")) {
            val message = event.message()
            event.isCancelled = true
            event.player.msg(miniMessage().deserialize("Message: <message>", TemplateResolver.resolving("message" to message)))
            event.player.msg(miniMessage().parse("<gold>Please jump to <green>confirm</green> your message!"))
            // Give the player 3 seconds to confirm the message
            withTimeoutOrNull(3000L) {
                // Activate a flow which pauses this event until it has finished. ONLY DO THIS ASYNC
                playerEventFlow<PlayerJumpEvent>(
                    event.player,
                    plugin,
                ).first {it.player == event.player}
                // Send the players message since they confirmed
                broadcast(message)
            } ?: return
            // If it timed out it would have returned, so we can just send this
            event.player.msg(miniMessage().parse("<red>You didn't confirm your message in time!"))
        }
    }


}