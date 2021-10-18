package net.perfectdreams.discordinteraktions.declarations.commands.wrappers

import net.perfectdreams.discordinteraktions.declarations.commands.UserCommandDeclaration

public interface UserCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): UserCommandDeclaration
}
