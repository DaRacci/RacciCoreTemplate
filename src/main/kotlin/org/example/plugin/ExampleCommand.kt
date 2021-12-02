package org.example.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import me.racci.raccicore.api.extensions.msg
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ExampleCommand: BaseCommand() {

    @Default
    @CommandCompletion("@players") // This will auto tab complete players
    fun onDefault(
        sender: CommandSender,
        target: Player
    ) {
        if(sender !is Player) return // this is a player only command
        if(sender == target) return // these are the same player
        if(target.isDead) return // the target is dead
        target.teleport(sender)
        target.msg(miniMessage().parse("<aqua>You were teleport to <gray>${sender.displayName()}</gray>!"))
        sender.msg(miniMessage().parse("<aqua>You teleport <gray>${target.displayName()}</gray> to you!"))
    }

}