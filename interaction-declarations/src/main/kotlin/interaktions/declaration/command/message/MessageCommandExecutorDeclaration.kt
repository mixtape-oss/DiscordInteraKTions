package interaktions.declaration.command.message

import interaktions.declaration.command.application.ApplicationCommandExecutorDeclaration

// The "parent" is Any to avoid issues with anonymous classes
// When using anonymous classes, you can use another type to match declarations
public open class MessageCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent)
