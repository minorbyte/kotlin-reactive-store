package org.kstore.demo.stars.gameplay.model.player.ship

import mu.KotlinLogging
import org.kstore.demo.stars.common.between
import org.kstore.demo.stars.rule.tech.*
import react.kstore.action.Dispatcher
import java.util.*
import javax.annotation.PreDestroy

class ShipBattleProcessor(
        dispatcher: Dispatcher
) {

    private val logger = KotlinLogging.logger {}
    private val random = Random()

    private val battleStartActionSubscription = dispatcher.subscribe(BattleStartAction::class) { action ->
        val (attacker, defender, attackerTechs, defenderTechs) = action
        logger.debug { "Battle: $attacker vs $defender" }

        val defenderDamage = attack(attacker, defender, attackerTechs, defenderTechs)
        val attakerDamage = if (defender.hp > defenderDamage) attack(defender, attacker, defenderTechs, attackerTechs) else 0

        dispatcher.asyncAction(BattleFinishAction(attacker, defender, defenderDamage, attakerDamage))
    }

    @PreDestroy
    fun destroy() {
        battleStartActionSubscription.unsubscribe()
    }

    private fun attack(attacker: Ship, defender: Ship, attackerTechs: List<TechId>, defenderTechs: List<TechId>): Int {
        logger.debug { "$attacker shoots at $defender" }

        val attackMultiplier = TechTree.maxMultiplier(TechAffects.ATTACK, attackerTechs)
        val defenseMultiplier = TechTree.maxMultiplier(TechAffects.MANEUVERABILITY, defenderTechs)
        val computerMultiplier = TechTree.maxMultiplier(TechAffects.COMPUTER, attackerTechs)


        val powerDifference = attacker.shipType.maxAttackPower - attacker.shipType.minAttackPower
        val attackPower = (attacker.shipType.minAttackPower + if (powerDifference > 0) random.nextInt(powerDifference) else 0) * attackMultiplier

        val maneuverability = defender.shipType.baseManeuverability * defenseMultiplier
        val computerLevel = attacker.shipType.baseComputerLevel * computerMultiplier

        val hitPercent = between(
                70 + (5 * (defender.shipType.size - maneuverability - attacker.shipType.size + computerLevel)).toInt(),
                5,
                95
        )

        val hit = random.nextInt(100) <= hitPercent

        val damage = if (hit) (attackPower * (100 - defender.shipType.defense) / 100).toInt() else 0
        logger.debug { "Attacker deals $damage with $hitPercent% hit ratio" }
        return damage
    }
}


