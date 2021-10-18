package net.perfectdreams.discordinteraktions.declarations.commands.application

import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration

interface ApplicationCommandDeclarationBuilder {
    fun build(): InteractionCommandDeclaration
}
