package org.kstore.demo.stars.gameplay.model.player.ship

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.*
import org.kstore.RandomBean
import org.kstore.demo.stars.RANDOM
import org.kstore.demo.stars.gameplay.model.*
import org.kstore.demo.stars.rule.ship.ShipType.*
import org.kstore.demo.stars.rule.tech.TechId

@StarsTest
internal class ShipBattleProcessorTest : DispatcherEnvironmentTest(){

    private lateinit var tested: ShipBattleProcessor

    @BeforeEach
    fun before() {
        tested = ShipBattleProcessor(dispatcher)
    }

    @Test
    fun `capital ship will often miss on fighter and often receive counter damage without techs`(
            @RandomBean attackerTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val attacker = attackerTemplate.copy(shipType = CAPITAL, hp = CAPITAL.maxHp)
        val defender = defenderTemplate.copy(shipType = FIGHTER, hp = FIGHTER.maxHp)
        val attackerTechs = listOf<TechId>()
        val defenderTechs = listOf<TechId>()

        val results = ArrayList<Pair<Boolean, Boolean>>(100)

        (0..100).forEach {
            dispatcher.action(BattleStartAction(attacker, defender, attackerTechs, defenderTechs))

            await().until { emittedActions.lastOrNull(BattleFinishAction::class) != null }
            val battleFinishAction = emittedActions.last(BattleFinishAction::class)
            results.add(Pair(battleFinishAction.defenderDamage > 0, battleFinishAction.attackerDamage > 0))
            emittedActions.clear()
        }


        assertThat(results.count { it.first }).isLessThan(50)
        assertThat(results.count { it.second }).isGreaterThan(50)
    }

@Test
    fun `capital ship will often hit on fighter and less receive counter damage with advanced techs`(
            @RandomBean attackerTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val attacker = attackerTemplate.copy(shipType = CAPITAL, hp = CAPITAL.maxHp)
        val defender = defenderTemplate.copy(shipType = FIGHTER, hp = FIGHTER.maxHp)
        val attackerTechs = listOf<TechId>("C10", "M10")
        val defenderTechs = listOf<TechId>()

        val results = ArrayList<Pair<Boolean, Boolean>>(100)

        (0..100).forEach {
            dispatcher.action(BattleStartAction(attacker, defender, attackerTechs, defenderTechs))

            await().until { emittedActions.lastOrNull(BattleFinishAction::class) != null }
            val battleFinishAction = emittedActions.last(BattleFinishAction::class)
            results.add(Pair(battleFinishAction.defenderDamage > 0, battleFinishAction.attackerDamage > 0))
            emittedActions.clear()
        }


        assertThat(results.count { it.first }).isGreaterThan(50)
        assertThat(results.count { it.second }).isLessThan(50)
    }


    @Test
    fun `capital ship and frigate will destroy fighter with one blow without techs`(
            @RandomBean attackerTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        val attacker = if (RANDOM.nextBoolean()) attackerTemplate.copy(shipType = CAPITAL, hp = CAPITAL.maxHp) else attackerTemplate.copy(shipType = FRIGATE, hp = FRIGATE.maxHp)
        val defender = defenderTemplate.copy(shipType = FIGHTER, hp = FIGHTER.maxHp)
        val attackerTechs = listOf<TechId>()
        val defenderTechs = listOf<TechId>()

        val results = ArrayList<Boolean>(100)

        while (results.size < 100) {
            dispatcher.action(BattleStartAction(attacker, defender, attackerTechs, defenderTechs))

            await().until { emittedActions.lastOrNull(BattleFinishAction::class) != null }
            val battleFinishAction = emittedActions.last(BattleFinishAction::class)
            results.add(battleFinishAction.defenderDamage >= FIGHTER.maxHp)
            emittedActions.clear()
        }

        assertThat(results).matches { true }
    }

    @Test
    fun `ane capital ship will destroy 5 fighters without techs`(
            @RandomBean attackerTemplate: Ship,
            @RandomBean defenderTemplate: Ship
    ) {
        var attacker = attackerTemplate.copy(shipType = CAPITAL, hp = CAPITAL.maxHp)
        val defender = defenderTemplate.copy(shipType = FIGHTER, hp = FIGHTER.maxHp)
        val attackerTechs = listOf<TechId>()
        val defenderTechs = listOf<TechId>()

        (0..5).forEach {
            do {
                dispatcher.action(BattleStartAction(attacker, defender, attackerTechs, defenderTechs))

                await().until { emittedActions.lastOrNull(BattleFinishAction::class) != null }

                val battleFinishAction = emittedActions.last(BattleFinishAction::class)
                attacker = attacker.copy(hp = attacker.hp - battleFinishAction.attackerDamage)
                emittedActions.clear()
            } while (battleFinishAction.defenderDamage == 0)
        }

        assertThat(attacker.hp).isPositive()
    }

}
