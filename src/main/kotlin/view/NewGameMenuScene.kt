package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class NewGameMenuScene(private val rootService: RootService) : MenuScene(width = 700 , height = 540, background = ColorVisual(57, 70, 59)), Refreshable {

    private val mainPanel = Label(
        width = 410,
        height = 490,
        posX = (700 - 410) / 2,
        posY = (540 - 490) / 2
    ).apply {
        visual = ImageVisual("NewGameArtwork.png")
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 115,
        height = 24,
        posX = 185,
        posY = 175,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 115,
        height = 24,
        posX = 400,
        posY = 175,
        text = listOf("Fry", "Bender", "Leela", "Amy", "Zoidberg").random()

    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val startButton = Button(
        width = 90,
        height = 30,
        posX = 210,
        posY = 270,
        text = "Start"
    ).apply {
        visual = ColorVisual(116, 146, 102)
        onMouseClicked = {
            rootService.gameService.startNewGame(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    val quitButton = Button(
        width = 90,
        height = 30,
        posX = 400,
        posY = 270,
        text = "Quit"
    ).apply {
        visual = ColorVisual(217, 95, 78)
    }

    init {
        opacity = 1.0
        addComponents(
            mainPanel,
            p1Input,
            p2Input,
            startButton,
            quitButton
        )
    }
}