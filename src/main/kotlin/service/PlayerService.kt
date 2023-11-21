package service

import entity.Card
import entity.Player

/**
 * This class is responsible for handling player actions within the Pyramid card game.
 * It allows players to select a pair of cards, draw a card from the pile, or pass their turn.
 * It operates by making calls to the rootService and gameService to manipulate the game state accordingly.
 *
 * @property rootService An instance of RootService which this service uses to interact with the game state.
 * @constructor Creates an instance of PlayerService which is responsible for player-specific actions.
 */
class PlayerService(private val rootService: RootService) : AbstractRefreshingService(){

   /**
    * Attempts to select a pair of cards from the pyramid or reserve pile with a total value of 15.
    * If successful, it will remove the cards from play, update the player's score, reveal any newly uncovered cards,
    * and switch the turn to the other player.
    *
    * @param player The player making the move.
    * @param card1 The first card selected.
    * @param card2 The second card selected.
    * @throws Exception if the move is invalid or if no game has started yet.
    */
   fun selectPair(player: Player, card1: Card, card2: Card) : Unit{

      val game = rootService.currentGame
      checkNotNull(game) { "No game started yet."}

      if(GameService(rootService).checkPair(card1, card2)){
         player.score += GameService(rootService).addScore(card1, card2)
      }else{
         throw Exception("Invalid move.")
      }

      game.passCounter = 0
      GameService(rootService).removePair(card1, card2)
      GameService(rootService).switchPlayer()
      rootService.currentGame = game
      onAllRefreshables { refreshAfterRemovePair(player, listOf(card1, card2)) }

   }

   /**
    * Draws the top card from the draw pile and places it on top of the reserve pile if possible.
    * It also resets the pass counter and updates all refreshables to reflect the change in game state.
    *
    * @param player The player performing the action.
    * @throws Exception if there are no cards to draw or if no game has started yet.
    */
   fun useDrawPile(player: Player) : Unit{
      val game = rootService.currentGame
      checkNotNull(game) { "No game started yet."}

      if(game.drawPile.isEmpty()){
         throw Exception("there are no cards to draw, choose another action.")
      }else{
         val removedCard = game.drawPile.pop()
         game.storagePile.push(removedCard)

         game.passCounter = 0
         GameService(rootService).switchPlayer()
         rootService.currentGame = game
         onAllRefreshables { refreshAfterRevealCard(player, removedCard) }
      }
   }

   /**
    * Represents a player's decision to pass their turn. It increments the game's pass counter
    * and switches the turn to the other player. It also triggers an update for all refreshables.
    *
    * @param player The player who is passing their turn.
    * @throws Exception if no game has started yet.
    */
   fun pass(player: Player) : Unit{
      val game = rootService.currentGame
      checkNotNull(game) { "No game started yet."}

      game.passCounter++
      GameService(rootService).switchPlayer()
      rootService.currentGame = game
      onAllRefreshables { refreshAfterPass(player) }

   }
}