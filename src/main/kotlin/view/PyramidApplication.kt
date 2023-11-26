package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * This is the main scene for the Pyramid card game. The scene displays the entire game layout,
 * including the pyramid of cards, draw and reserve piles, and player information.
 * Player interactions like card selection, drawing from piles, and passing turns are managed here.
 */
class PyramidApplication : BoardGameApplication("Pyramid Game"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes

    // This is where the actual game takes place
    private val gameScene = PyramidGameScene(rootService)

    // This menu scene is shown after application start and if the "new game" button
    // is clicked in the gameFinishedMenuScene
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    private val gameFinishedMenuScene = GameFinishedMenuScene(rootService).apply {
        newGameButton.onMouseClicked = {
            showMenuScene(newGameMenuScene, 0)
        }

        quitButton.onMouseClicked = {
            exit()
        }
    }

    init {

        // all scenes and the application itself need to
        // react to changes done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,
            gameFinishedMenuScene,
            newGameMenuScene
        )

        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene, 0)

    }

    /**
     * Refreshes the application after starting a new game. This method hides the menu scene,
     * transitioning to the game scene for gameplay.
     */
    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }

    /**
     * Refreshes the application after a game ends. It displays the game finished menu scene,
     * showing the results of the game and providing options to start a new game or exit.
     */
    override fun refreshAfterGameEnd() {
        this.showMenuScene(gameFinishedMenuScene)
    }


}

