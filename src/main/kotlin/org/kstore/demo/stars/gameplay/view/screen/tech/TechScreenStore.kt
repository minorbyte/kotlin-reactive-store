package org.kstore.demo.stars.gameplay.view.screen.tech

import org.kstore.demo.stars.gameplay.model.player.tech.*
import org.kstore.demo.stars.gameplay.model.turn.PlayerTurnStore
import org.kstore.demo.stars.gameplay.view.console.component.tech.*
import org.kstore.demo.stars.gameplay.view.screen.BasicScreenStore
import org.kstore.demo.stars.rule.tech.*
import org.springframework.stereotype.Service
import react.kstore.dependency.dependsOn
import java.util.*

@Service
class TechScreenStore(
        playerTurnStore: PlayerTurnStore,
        playerTechStore: ActivePlayerVisibleResearchedTechStore,
        playerAvailableToResearchTechStore: ActivePlayerAvailableTechStore,
        playerResearchingTechStore: PlayerResearchingTechStore,
        selectedAvailableTechStore: SelectedAvailableTechStore

) : BasicScreenStore() {
    init {
        dependsOn {
            stores(
                    playerTurnStore,
                    playerTechStore,
                    playerAvailableToResearchTechStore,
                    playerResearchingTechStore,
                    selectedAvailableTechStore
            ) rewrite { playerTurn, techs, available, researching, selectedTech ->
                printTechScreen(
                        researchedTechs = techs.map {
                            ResearchedTechViewItem(
                                    affects = it.affects,
                                    multiplier = it.multiplier,
                                    name = it.name
                            )
                        },
                        researcheableTechs = available.map {
                            ResearcheableTechViewItem(
                                    techId = it.techId,
                                    affects = it.affects,
                                    multiplier = it.multiplier,
                                    name = it.name,
                                    cost = it.cost,
                                    alreadySpent = getSpentRPForCurrentTech(researching[playerTurn.playerId], it.techId),
                                    selected = getTechSelected(selectedTech, it.techId),
                                    researched = getTechResearched(researching[playerTurn.playerId], it.techId)
                            )
                        }
                )
            }
        }
    }
}

fun getSpentRPForCurrentTech(maybeResearchedTech: Optional<ResearchingTech>?, currentTechId: TechId): Int {
    if (maybeResearchedTech != null && maybeResearchedTech.isPresent) {
        val researchedTech = maybeResearchedTech.get()

        if (researchedTech.techId == currentTechId) {
            return researchedTech.spentSP
        }
    }

    return 0
}

fun getTechResearched(maybeResearchedTech: Optional<ResearchingTech>?, currentTechId: TechId): Boolean {
    if (maybeResearchedTech != null && maybeResearchedTech.isPresent) {
        val researchedTech = maybeResearchedTech.get()

        if (researchedTech.techId == currentTechId) {
            return true
        }
    }

    return false
}

fun getTechSelected(maybeSelectedTech: Optional<Tech>, currentTechId: TechId): Boolean {
    if (maybeSelectedTech.isPresent) {
        val researchedTech = maybeSelectedTech.get()

        if (researchedTech.techId == currentTechId) {
            return true
        }
    }

    return false
}

data class ResearchedTechViewItem(
        val affects: TechAffects,
        val name: String,
        val multiplier: Double
)

data class ResearcheableTechViewItem(
        val techId: TechId,
        val affects: TechAffects,
        val name: String,
        val cost: Int,
        val alreadySpent: Int,
        val multiplier: Double,
        val selected: Boolean,
        val researched: Boolean
)
