package org.kstore.demo.stars.gameplay.view

import org.springframework.scheduling.annotation.Scheduled

//@Service
class UIRenderService (
        val mapRenderer: UIRenderer
) {

    @Scheduled(fixedDelay = 100, initialDelay = 100)
    fun doSomething() {
        mapRenderer.render()
    }

}
