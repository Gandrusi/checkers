import java.util.Scanner;
import java.util.LinkedList;
import javax.swing.JFrame;

public class Game extends JFrame {

	static final int EMPTY = 0, BLACK = 1, RED = 2, BKING = 3, RKING = 4;

	private static Scanner kb = new Scanner(System.in);

	Checker[][] board;
	int currentPlayer;
	boolean finish = false;
	Board b;
	int takenByBlk; // pieces take by Black
	int takenByRed; // pieces taken by Red

	public Game() {

	}

	// takes a char array and generates the board of checker objects
	Game(char[][] c) {
		currentPlayer = 1;
		board = new Checker[8][8];
		int real = 1;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				// creates a valid checker at initial checker position
				if (c[i][j] == 'r' || c[i][j] == 'w' || c[i][j] == 'p') {
					board[i][j] = new Checker(i, j, c[i][j], real);
					real++;
				}
				// creates a dummy 'checker' for board drawing for a non playable square
				else
					board[i][j] = new Checker(-1, -1, c[i][j], -1);
				board[i][j].data();
			}
		}
		b = new Board(board);

		add(b);
		setTitle("Checkers");
		setSize(808, 830);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		play();
	}

	public Checker[][] getBoard() {
		return board;
	}

	public void makeMove(int start, int end, LinkedList<Move> moves) {
		Move current = new Move(start, end);
		Boolean found = false;
		

		// valid move check
		if (start > 32 || start < 1 || end > 32 || end < 1) {
			System.out.println("invalid move");
			play();
		}
		for (int i = 0; i < moves.size(); i++) {
			if (current.equals(moves.get(i))) {
				if (current.isJump()) { // copies the intermediate location
					current.inter = moves.get(i).inter;
				}
				found = true;

				break;
			}
		}
		if (found == false) {
			System.out.println("invalid move");
			play();
		}

		System.out.println("making move " + start + " " + end);

		Checker temp = new Checker();
		int x = 0, y = 0, bp = 0;
		if (current.isJump()) {
			System.out.println("is JUMP");
			// loop through board to find pieces
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					// copy checker at start location
					if (board[i][j].getBoard_pos() == start) {
						temp = board[i][j];
						// set space to empty
						bp = board[i][j].getBoard_pos();
						board[i][j] = new Checker(i, j, 'p', bp);
					}
					if (board[i][j].getBoard_pos() == current.inter) {
						if (board[i][j].pieceType == BLACK) {
							takenByRed++;
						} else {
							takenByBlk++;
						}
						board[i][j].setColor('p');
					}
					// finds ending position and updates array index for new space
					if (board[i][j].getBoard_pos() == end) {
						x = i;
						y = j;
						System.out.println(x + " " + y);
					}
				}
			}

		} else {
			// loop through board to find pieces
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					// copy checker at start location
					if (board[i][j].getBoard_pos() == start) {
						temp = board[i][j];
						// set space to empty
						bp = board[i][j].getBoard_pos();
						board[i][j] = new Checker(i, j, 'p', bp);
					}
					// finds ending position and updates array index for new space
					if (board[i][j].getBoard_pos() == end) {
						x = i;
						y = j;
					}
				}
			}
		}

		// updates board then redraws
		temp.setBoard_pos(end);
		temp.setX(x);
		temp.setY(y);
		board[x][y] = temp;
		
		System.out.println(temp.getX() + " " + temp.getY());
		// check if piece is king
		if (board[x][y].pieceType == BLACK && end >= 29) {
			board[x][y].setColor('R');
		} else if (board[x][y].pieceType == RED && end <= 4) {
			board[x][y].setColor('W');
		}
		b.update(board);
		if (current.isJump()) {
			System.out.println("is Jump");
			
			LinkedList<Move> jumps = multiJump(x,y);
			System.out.println(x + " " + y);
			while (jumps != null) {
				System.out.println("entered while loop");
				System.out.println(playerName() + " must jump again!");
				System.out.println("Here are Possible moves: ");
				for (int i = 0; i < jumps.size(); i++) {
					System.out.println(jumps.get(i).toString());
				}
				makeMove(kb.nextInt(), kb.nextInt(), jumps);

			}
		}
		currentPlayer++;
		if (currentPlayer > 2)
			currentPlayer = 1;
		play();
	}

	public LinkedList<Move> multiJump(int row1, int col1) {
		int col = col1;
		int row = row1;
		LinkedList<Move> list = new LinkedList<Move>();
		if(canJump(row, col, row+1, col+1, row+2, col+2)) {
			list.add(new Move(board[row][col].board_pos, board[row+2][col+2].board_pos, board[row+1][col+1].board_pos));
		}
		if(canJump(row, col, row+1, col-1, row+2, col-2)) {
			list.add(new Move(board[row][col].board_pos, board[row+2][col-2].board_pos, board[row+1][col-1].board_pos));
		}
		if(canJump(row, col, row-1, col+1, row-2, col+2)) {
			list.add(new Move(board[row][col].board_pos, board[row-2][col+2].board_pos, board[row-1][col+1].board_pos));
		}
		if(canJump(row, col, row-1, col-1, row-2, col-2)) {
			list.add(new Move(board[row][col].board_pos, board[row-2][col-2].board_pos, board[row-1][col-1].board_pos));
		}
		if(list.isEmpty()) {
			System.out.println("list is empty");
			return null;
		}
		return list;
	}

	// creates linked list of legal moves for the give player. if there are jumps
	// possible, only jumps are legal moves.
	// if there are no legal moves then null is returned
	public LinkedList<Move> legalMoves(int player) {
		int king;
		if (player == RED) {
			king = RKING;
		} else {
			king = BKING;
		}
		LinkedList<Move> list = new LinkedList<Move>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col].pieceType == player || board[row][col].pieceType == king) {
					if (canJump(row, col, row - 1, col - 1, row - 2, col - 2)) {
						list.add(new Move(board[row][col].board_pos, board[row - 2][col - 2].board_pos,
								board[row - 1][col - 1].board_pos));
					}
					if (canJump(row, col, row - 1, col + 1, row - 2, col + 2)) {
						list.add(new Move(board[row][col].board_pos, board[row - 2][col + 2].board_pos,
								board[row - 1][col + 1].board_pos));
					}
					if (canJump(row, col, row + 1, col - 1, row + 2, col - 2)) {
						list.add(new Move(board[row][col].board_pos, board[row + 2][col - 2].board_pos,
								board[row + 1][col - 1].board_pos));
					}
					if (canJump(row, col, row + 1, col + 1, row + 2, col + 2)) {
						list.add(new Move(board[row][col].board_pos, board[row + 2][col + 2].board_pos,
								board[row + 1][col + 1].board_pos));
					}
				}
			}
		}

		if (list.isEmpty()) {
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board[row].length; col++) {
					if (board[row][col].pieceType == player || board[row][col].pieceType == king) {
						if (canMove(row, col, row - 1, col - 1, player)) {
							list.add(new Move(board[row][col].board_pos, board[row - 1][col - 1].board_pos));
						}
						if (canMove(row, col, row - 1, col + 1, player)) {
							list.add(new Move(board[row][col].board_pos, board[row - 1][col + 1].board_pos));
						}
						if (canMove(row, col, row + 1, col - 1, player)) {
							list.add(new Move(board[row][col].board_pos, board[row + 1][col - 1].board_pos));
						}
						if (canMove(row, col, row + 1, col + 1, player)) {
							list.add(new Move(board[row][col].board_pos, board[row + 1][col + 1].board_pos));
						}
					}
				}
			}
		}

			return list;
	}

	// r1,c1 = starting position of player; r2,c2 = piece being jumped; r3,c3 =
	// space jumped to
	public boolean canJump(int r1, int c1, int r2, int c2, int r3, int c3) {
		if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
			return false; // jump is off the board
		} else if (board[r3][c3].pieceType != EMPTY) {
			return false; // no place to jump to
		} else if (currentPlayer == RED && (board[r1][c1].pieceType == RED || board[r1][c1].pieceType == RKING)) { 
			// player is red and piece is red
			if ((board[r2][c2].pieceType == BLACK || board[r2][c2].pieceType == BKING)) { // there is a black piece to
																							// jump
				if (board[r1][c1].pieceType == RKING) { // King can move forward or back
					return true;
				} else {
					return r1 > r3; // Single must move forward
				}
			} else {
				return false; // there is not a black piece to jump
			}
		} else if (currentPlayer == BLACK && (board[r1][c1].pieceType == BLACK || board[r1][c1].pieceType == BKING)) { // player
																														// is
																														// black
																														// and
																														// piece
																														// is
																														// black
			if ((board[r2][c2].pieceType == RED || board[r2][c2].pieceType == RKING)) { // there is a red piece to jump
				if (board[r1][c1].pieceType == BKING) { // King can move forward or back
					return true;
				} else {
					return r1 < r3; // Single must move forward
				}
			} else {
				return false; // there is not a red piece to jump
			}
		}
		return false;
	}

	public boolean canMove(int fr, int fc, int tr, int tc, int player) {
		if (player != BLACK && player != RED)
			return false; // The player is invalid
		if (tr < 0 || tr >= 8 || tc < 0 || tc >= 8) {
			return false; // jump is off the board
		} else if (board[tr][tc].pieceType != EMPTY) {
			return false; // no place to jump to
		} else if (player == RED && (board[fr][fc].pieceType == RED || board[fr][fc].pieceType == RKING)) { // player is
																											// red and
																											// piece is
																											// red
			if (board[fr][fc].pieceType == RKING && (tr == fr + 1 || tr == fr - 1) && (tc == fc + 1 || tc == fc - 1)) { // King
																														// can
																														// move
																														// forward
																														// or
																														// back
				return true;
			} else {
				return (tr == fr - 1 && (tc == fc + 1 || tc == fc - 1)); // Single must move forward
			}
		} else if (currentPlayer == BLACK && (board[fr][fc].pieceType == BLACK || board[fr][fc].pieceType == BKING)) { // player
																														// is
																														// black
																														// and
																														// piece
																														// is
																														// black
			if (board[fr][fc].pieceType == BKING && (tr == fr + 1 || tr == fr - 1) && (tc == fc + 1 || tc == fc - 1)) { // King
																														// can
																														// move
																														// forward
																														// or
																														// back
				return true;
			} else {
				return (tr == fr + 1 && (tc == fc + 1 || tc == fc - 1)); // Single must move forward
			}
		} else {
			return false; // there is not a red piece to jump
		}

	}

	public String playerName() {
		if (currentPlayer == BLACK) {
			return "Black";
		} else {
			return "RED";
		}
	}

	// class move creates a move object for ease of passing objects
	public class Move {
		int from; // starting position of the checker
		int to; // end position of the checker
		int inter = -1;

		public Move(int from, int to) {
			this.from = from;
			this.to = to;
		}

		public Move(int from, int to, int inter) {
			this.from = from;
			this.to = to;
			this.inter = inter;
		}

		public boolean isJump() { // checks if the move is a jump
			return to - from == 9 || to - from == 7 || from - to == 9 || from - to == 7 || inter != -1;
		}

		// checks if the moves are equal
		public boolean equals(Move other) {
			return other.from == this.from && other.to == this.to;
		}

		public String toString() {
			return "From: " + from + " To position: " + to;
		}

//		
	}

	// technically the board loop
	public void play() {
		LinkedList<Move> moves = legalMoves(currentPlayer);
		// lose check
				if (moves.isEmpty()) {
					System.out.println(playerName() + "loses");
					System.exit(0);
				}
		System.out.println(playerName() + " make a move");
		System.out.println("Here are Possible moves: ");
		for (int i = 0; i < moves.size(); i++) {
			System.out.println(moves.get(i).toString());
		}
		makeMove(kb.nextInt(), kb.nextInt(), moves);
	}

}
