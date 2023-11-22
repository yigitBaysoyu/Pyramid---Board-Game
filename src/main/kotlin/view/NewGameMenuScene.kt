package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class NewGameMenuScene(private val rootService: RootService) : MenuScene(width = 410 , height = 490, background = ImageVisual("NewGameArtwork.png")), Refreshable {

    val adjustInputPosX = 105
    val adjustInputPosY = -87

    val adjustButtonPosX = 100
    val adjustButtonPosY = 84


    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 116,
        height = 24,
        posX = (410-116)/2 - adjustInputPosX,
        posY = (490-24)/2 + adjustInputPosY,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 116,
        height = 24,
        posX = (410-116)/2 + adjustInputPosX,
        posY = (490-24)/2 + adjustInputPosY,
        text = listOf("Fry", "Bender", "Leela", "Amy", "Zoidberg").random()

    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val startButton = Button(
        width = 120,
        height = 50,
        posX = (410-120)/2 + adjustButtonPosX,
        posY = (490-50)/2 + adjustButtonPosY,
    ).apply {
        visual = ImageVisual("StartButton.png")
        onMouseClicked = {
            rootService.gameService.startNewGame(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    val quitButton = Button(
        width = 125,
        height = 50,
        posX = (410-120)/2 - adjustButtonPosX,
        posY = (490-50)/2 + adjustButtonPosY,
    ).apply {
        visual = ImageVisual("QuitButton.png")
    }

    init {
        opacity = 1.0
        addComponents(
            p1Input,
            p2Input,
            startButton,
            quitButton
        )
    }
}