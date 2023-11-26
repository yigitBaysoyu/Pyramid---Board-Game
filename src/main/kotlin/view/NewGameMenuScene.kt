package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * Represents the menu scene for initiating a new game in the Pyramid card game application.
 * This scene provides the interface for entering player names and starting a new game.
 * It includes text fields for player names, a start button to begin the game, and a quit button.
 *
 * @property rootService Instance of RootService to interact with game service.
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(width = 410 , height = 490, background = ImageVisual("NewGameArtwork.png")), Refreshable {

    /**
     * Adjustment values for positioning the player input text fields on the scene.
     */
    val adjustInputPosX = 105
    val adjustInputPosY = -87


    val adjustButtonPosX = 100
    val adjustButtonPosY = 84


    /**
     * Text field for entering the name of the first player. It is pre-populated with a random name
     * and updates the start button's disabled state based on the text content.
     */
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

    /**
     * Text field for entering the name of the second player. It is pre-populated with a random name
     * and updates the start button's disabled state based on the text content.
     */
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

    /**
     * Button to initiate the game using the entered player names. Triggers the game start logic
     * in the GameService when clicked.
     */
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

    /**
     * Button to quit the game. Currently, it is only visually represented and does not have an
     * associated action.
     */
    val quitButton = Button(
        width = 125,
        height = 50,
        posX = (410-120)/2 - adjustButtonPosX,
        posY = (490-50)/2 + adjustButtonPosY,
    ).apply {
        visual = ImageVisual("QuitButton.png")
    }

    /**
     * Initializes the NewGameMenuScene by setting its opacity and adding all the necessary components,
     * including the player input text fields and control buttons.
     */
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