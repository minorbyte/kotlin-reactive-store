package org.kstore.demo.stars.rule

interface TurnStage {
    val entOfTurn: Boolean
    val endsByUser: Boolean

    fun next(): TurnStage
    fun previous(): TurnStage
}


enum class PlayerTurnStageType(
        override val entOfTurn: Boolean = false,
        override val endsByUser: Boolean = false

) : TurnStage {
    INCOME(),
    RESEARCH(),
    BUILD(),
    MOVEMENT(true, true);

    override fun next(): PlayerTurnStageType =
            when (this) {
                INCOME -> RESEARCH
                RESEARCH -> BUILD
                BUILD -> MOVEMENT
                MOVEMENT -> INCOME

            }

    override fun previous(): PlayerTurnStageType =
            when (this) {
                INCOME -> MOVEMENT
                RESEARCH -> INCOME
                BUILD -> RESEARCH
                MOVEMENT -> BUILD
            }
}
