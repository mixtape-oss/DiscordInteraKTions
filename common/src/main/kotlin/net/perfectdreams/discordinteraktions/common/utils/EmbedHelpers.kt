package net.perfectdreams.discordinteraktions.common.utils

import dev.kord.rest.builder.message.EmbedBuilder

// Utilities extension methods for Kord Embeds, mostly providing functions to replace Kord's embed callbacks
public fun EmbedBuilder.author(name: String, url: String? = null, iconUrl: String? = null) {
    author {
        this.name = name
        this.url = this.url
        this.icon = iconUrl
    }
}

public var EmbedBuilder.thumbnailUrl: String?
    get() = thumbnail?.url
    set(value) {
        if (value == null) {
            thumbnail = null
        } else {
            thumbnail {
                this.url = value
            }
        }
    }

public fun EmbedBuilder.footer(text: String, iconUrl: String? = null) {
    footer {
        this.text = text
        this.icon = iconUrl
    }
}

public fun EmbedBuilder.field(name: String, value: String, inline: Boolean = false): Unit =
    field(name, inline) { value }

public fun EmbedBuilder.inlineField(name: String, value: String): Unit =
    field(name, true) { value }
