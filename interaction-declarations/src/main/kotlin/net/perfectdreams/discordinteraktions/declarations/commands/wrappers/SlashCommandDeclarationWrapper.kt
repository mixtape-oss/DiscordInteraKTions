package net.perfectdreams.discordinteraktions.declarations.commands.wrappers

import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration

public interface SlashCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): SlashCommandDeclaration
}
