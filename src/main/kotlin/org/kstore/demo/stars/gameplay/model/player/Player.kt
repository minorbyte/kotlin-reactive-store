package org.kstore.demo.stars.gameplay.model.player

typealias PlayerId = String

data class Player(
        val id: PlayerId,
        val moveOrder: Int,
        val ai: Boolean,
        val name: String = "Player"
)
