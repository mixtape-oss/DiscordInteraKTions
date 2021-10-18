package net.perfectdreams.discordinteraktions.declarations.commands.slash

import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptions

// The "parent" is Any to avoid issues with anonymous classes
// When using anonymous classes, you can use another type to match declarations
public open class SlashCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent) {
    public open val options: CommandOptions = CommandOptions.NO_OPTIONS
}
