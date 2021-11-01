package interaktions.common.component

import dev.kord.common.entity.ButtonStyle
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.ButtonBuilder
import dev.kord.rest.builder.component.SelectMenuBuilder
import interaktions.common.component.buttons.ButtonClickExecutorDeclaration
import interaktions.common.component.selects.SelectMenuExecutorDeclaration

public fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    executor: ButtonClickExecutorDeclaration,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in an interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        executor.id
    ) {
        builder.invoke(this)
    }
}

public fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    label: String,
    executor: ButtonClickExecutorDeclaration,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {}
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in an interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        executor.id
    ) {
        this.label = label
        builder.invoke(this)
    }
}

public fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    executor: ButtonClickExecutorDeclaration,
    data: String,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in an interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        "${executor.id}:$data"
    ) {
        builder.invoke(this)
    }
}

public fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    label: String,
    executor: ButtonClickExecutorDeclaration,
    data: String,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {}
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in an interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        "${executor.id}:$data"
    ) {
        this.label = label
        builder.invoke(this)
    }
}

public fun ActionRowBuilder.selectMenu(
    executor: SelectMenuExecutorDeclaration,
    builder: SelectMenuBuilder.() -> Unit = {}
) {
    selectMenu(
        executor.id
    ) {
        builder.invoke(this)
    }
}

public fun ActionRowBuilder.selectMenu(
    executor: SelectMenuExecutorDeclaration,
    data: String,
    builder: SelectMenuBuilder.() -> Unit = {}
) {
    selectMenu(
        "${executor.id}:$data"
    ) {
        builder.invoke(this)
    }
}
