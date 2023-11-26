package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

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
        quitButton.onMouseClicked = {
            exit()
        }

        newGameButton.onMouseClicked = {
            showMenuScene(newGameMenuScene, 0)
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

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }

    override fun refreshAfterGameEnd() {
        this.showMenuScene(gameFinishedMenuScene)
    }


}

