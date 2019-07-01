package react.kstore

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension


@Configuration
@SpringBootTest
@ExtendWith(SpringExtension::class)
class RunIT {

    /*@Autowired
    lateinit var mapStore: PlayerTurnMapStore

    @Bean
    fun renderService(): RenderService = RenderService(mock(ConsoleReader::class.java), mock(MapRenderer::class.java))

    @Test
    fun run() {

        mapStore.state.tiles.let {
            it[Position(7, 9)]!!.let {
                assertThat(it.objects).containsExactly()
            }
        }
    }*/
}

