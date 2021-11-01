package interaktions.declaration.command.wrappers

import interaktions.declaration.command.UserCommandDeclaration

public interface UserCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): UserCommandDeclaration
}
