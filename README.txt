=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: ecao22/31394502
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D-Arrays: The board state is stored within a 2D array of my Piece class. The board is properly encapsulated
  within the Board class, which only allows access to the Pieces through getPiece and setPiece methods.

  2. File I/O: The game saves the board state to a .txt file when the player clicks the save button. The save can
  then be reloaded via the load button and typing the name of the state the user has saved. The program looks for
  invalid board states or bad filenames and rejects improper requests.

  3. Inheritance: Each piece is represented by as a subclass of the Piece class. Pieces that have "normal" movement,
  where capturing and movement are the same, are represented by the NormalPiece class, which extends Piece. Pawns and
  Kings are unique enough to extend Piece themselves.

  Piece contains the logic for drawing and being threatened/protected by another piece, as well as some basic
  attributes (whether a piece is moved or is black or white). NormalPiece contains logic for movement, which the
  subclasses then fill in with creating LineOfSight objects (used to represent possible movement paths).

  4. Complex game logic: Chess is an incredibly complex game. En passant, castling, pawn promotion, checkmate,
  stalemate, pinned pieces, and blocking check are all implemented.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  RunChess: The main class which creates a JFrame for the welcome screen and game. Also contains the buttons and label.

  Welcome: The welcome screen, which displays instructions, then loads the main game upon clicking enter.

  GameBoard: GameBoard handles the drawing of the board and handles user interaction. It initializes the board and
  contains the logic for loading, saving, and resetting games.

  Board: The board of Chess. The main feature is the 2D array of Pieces and contains methods that interface with the
  array, from moving pieces to setting up the board state from a file.

  State: Contains all the information about the current state of the game, including whose turn it is, whether the kings
  are in check, and whether the game is over. It contains information on whether castling is possible, and calls upon
  each piece to update their lines of sight.

  Piece: The superclass of all pieces. Contains the logic for drawing the piece, color of pieces, and whether the piece
  has moved, as mentioned above.

    NormalPiece: The superclass of all pieces that move in the same way that they capture. Contains basic movement,
    capture, and threatening logic.

        Queen: The queen, which extends NormalPiece. Contains the logic for creating the lines of sight for the queen.

        Rook: The rook. Creates four lines of sight for the cardinal directions

        Bishop: The bishop. Creates four lines of sight for the diagonal directions.

        Knight: The knight. Creates eight lines of sight for the L-shaped movement.

    Pawn: The pawn. Contains the logic for creating the lines of sight for the pawn, as well as en passant and promotion.

    King: The king. Contains the logic for creating the lines of sight for the king, as well as castling, and
    disallowing movement when the square is under attack from an opposing piece.

    EmptyPiece: The empty piece. An empty square for use in the array (to avoid having nulls in the array).

  LineOfSight: A class that represents a line of sight for a piece. Contains the logic for determining whether the
  line of sight is blocked by its own color, whether it pins an opposing piece to a king, or is directly checking a
  king. The LineOfSight contains a list of squares that it can move to, and the ones in between its owner piece and the
  king.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  The biggest difficulty in creating Chess was generating the logic for whether a piece was pinned to the king, and the
  valid blocking moves for check. LineOfSight was the solution, without needing to calculate board states for every
  possible move.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I think the design is fairly good, and is relatively efficient when calculating possible moves. The private variables
  are well encapsulated, but certain attributes were left public for easy interfacing between the large amount of
  classes that I've created and the complex game logic. If I were to refactor, I would create more strict privacy
  within classes, limiting information access to getters and setters.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  Chess piece images were taken from Wikipedia.
