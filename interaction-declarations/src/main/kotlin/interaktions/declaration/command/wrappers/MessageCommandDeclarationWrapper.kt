package interaktions.declaration.command.wrappers

import interaktions.declaration.command.MessageCommandDeclaration

public interface MessageCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): MessageCommandDeclaration
}
