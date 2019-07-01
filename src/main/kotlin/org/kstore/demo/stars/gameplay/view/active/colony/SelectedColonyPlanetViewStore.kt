package org.kstore.demo.stars.gameplay.view.active.colony

import org.kstore.demo.stars.gameplay.model.player.colony.ColonyStore
import org.springframework.stereotype.Service
import react.kstore.BasicStore
import react.kstore.optional.present
import java.util.*

@Service
class SelectedColonyPlanetViewStore(
        selectedColonyIdViewStore: SelectedColonyIdViewStore,
        colonyStore: ColonyStore
) : BasicStore<Optional<PlanetView>>(
        initialState = Optional.empty(),
        dependsOn = {
            stores(
                    selectedColonyIdViewStore,
                    colonyStore
            ) {
                rewrite { maybeColonyId, colonies ->
                    present(Optional.empty(), maybeColonyId) { colonyId ->
                        colonies.firstOrNull { it.id == colonyId }?.let {
                            Optional.of(
                                    PlanetView(
                                            position = it.position,
                                            maxBuildings = it.planetMaxBuildings,
                                            planetSize = it.planetSize.name,
                                            resourceMultiplier = it.planetResourceMultiplier,
                                            planetName = it.name
                                    )
                            )
                        } ?: return@rewrite Optional.empty()
                    }
                }
            }
        }
)
