package entity

/**
 * Entity to represent a player in the game "Pyramid". Besides having a [name],
 * it also has a [score] that increases when the player collects a card pair.
 * Additionally, it has an indicator [isFirstPlayer] which shows if the game starts with this player.
 */
class Player(
    val name : String,
    var score: Int = 0,
    val isFirstPlayer: Boolean
)