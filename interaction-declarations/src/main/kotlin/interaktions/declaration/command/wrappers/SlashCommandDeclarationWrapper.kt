package interaktions.declaration.command.wrappers

import interaktions.declaration.command.SlashCommandDeclaration

public interface SlashCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): SlashCommandDeclaration
}
