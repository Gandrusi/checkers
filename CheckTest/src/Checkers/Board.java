package Checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board extends JPanel{
	int currentPlayer; //holds the number of the current player 1 = Black, 2 = Red
	int AIplayer; //holds the number of the AI player 1 = Black, 2 = Red
	int[][] game; //holds the board Positions
	
	
	static final int
		EMPTY = 0,
		BLACK = 1,
		RED = 2,
		BKING = 3,
		RKING = 4;
		
	//creates the board array in starting position, sets the current player to black, and takes int representing color AI plays
	public Board(int ai) {
		game = new int[8][8];
		newGame();
		currentPlayer = BLACK;
		AIplayer = ai;
		
	}
	
	
	//creates a new array and places Black and red is the starting positions
	public void newGame() {
		for(int row = 0; row < 8; row++) {
			for(int col = 0; col< 8; col++) {
				if(row%2 != col%2 ) {
					if(row < 3) {
						game[row][col] = BLACK;
					}else if(row>4) {
						game[row][col] = RED;
					}else {
						game[row][col] = EMPTY;
					}
				}
			}
		}
	}
	
	
	//creates linked list of legal moves for the give player. if there are jumps possible, only jumps are legal moves. 
	//if there are no legal moves then null is returned
	public LinkedList<Move> legalMoves(int player){
		int king;
		if(player == RED) {
			king = RKING;
		}else {
			king = BKING;
		}
		LinkedList<Move> list = new LinkedList<Move>();
		for(int row = 0; row<game.length; row++) {
			for(int col = 0; col<game[row].length; col++) {
				if(game[row][col] == player|| game[row][col] == king) {
					if(canJump(row, col, row-1, col-1, row-2, col-2)) {
						list.add(new Move(row, col, row-2,col-2));
					}else if(canJump(row, col, row-1, col+1, row-2, col+2)) {
						list.add(new Move(row, col, row-2, col-2));
					}else if(canJump(row, col, row + 1, col-1, row +2, col-2)) {
						list.add(new Move(row, col, row+2, col-2));
					}else if(canJump(row, col, row + 1, col + 1, row + 2, col +2)) {
						list.add(new Move(row, col, row+2, col+2));
					}
				}
			}
		}
		
		if(list.isEmpty()) {
			for(int row = 0; row<game.length; row++) {
				for(int col = 0; col<game[row].length; col++) {
					if(game[row][col] == player|| game[row][col] == king) {
						if(canMove(row, col, row-1, col-1, player)) {
							list.add(new Move(row, col, row-1,col-1));
						}else if(canMove(row, col, row-1, col+1, player)) {
							list.add(new Move(row, col, row-1, col-1));
						}else if(canMove(row, col, row + 1, col-1, player)) {
							list.add(new Move(row, col, row+1, col-1));
						}else if(canMove(row, col, row + 1, col + 1, player)) {
							list.add(new Move(row, col, row+1, col+1));
						}
					}
				}
			}
		}
		
		if(list.isEmpty()) {
			return null;
		}else {
			return list;
		}
	}
	
	
	//Creates a linked list of the legal jump moves
	public LinkedList<Move> jumpMoves(int player,  int row, int col){
		LinkedList<Move> jumps = new LinkedList<Move>();
		if(player != BLACK && player != RED) return null;
		
		int king = player + 2;
		if(game[row][col] == player|| game[row][col] == king) {
			if(canJump(row, col, row-1, col-1, row-2, col-2)) {
				jumps.add(new Move(row, col, row-2,col-2));
			}else if(canJump(row, col, row-1, col+1, row-2, col+2)) {
				jumps.add(new Move(row, col, row-1, col-2));
			}else if(canJump(row, col, row + 1, col-1, row +2, col-2)) {
				jumps.add(new Move(row, col, row+2, col-2));
			}else if(canJump(row, col, row + 1, col + 1, row + 2, col +2)) {
				jumps.add(new Move(row, col, row+2, col+2));
			}
		}
		return jumps;
		
		
	}
	
	
	//r1,c1 = starting position of player; r2,c2 = piece being jumped; r3,c3 = space jumped to
	public boolean canJump(int r1, int c1, int r2, int c2, int r3, int c3) {
		if(r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
			return false; //jump is off the board
		}else if (game[r3][c3] != EMPTY) {
			return false; //no place to jump to
		}else if(currentPlayer == RED && (game[r1][c1] == RED || game[r1][c1] == RKING)) { //player is red and piece is red
			if((game[r2][c2] == BLACK || game[r2][c2] == BKING)) { //there is a black piece to jump
				if(game[r1][c1] == RKING) { //King can move forward or back
					return true;
				}else {
					return r1 > r3; //Single must move forward
				}
			}else {
				return false; //there is not a black piece to jump
			}
		}else if(currentPlayer == BLACK && (game[r1][c1] == BLACK || game[r1][c1] == BKING)) { //player is black and piece is black
			if((game[r2][c2] == RED || game[r2][c2] == RKING)) { //there is a red piece to jump
				if(game[r1][c1] == BKING) { //King can move forward or back
					return true;
				}else {
					return r1 < r3; //Single must move forward
				}
			}else {
				return false; //there is not a red piece to jump
			}	
		}
		return false;
	}
	
	public boolean canMove(int fr, int fc, int tr, int tc, int player) {
		if(player != BLACK && player != RED) return false; //The player is invalid
		if(tr < 0 || tr >= 8 || tc < 0 || tc >= 8) {
			return false; //jump is off the board
		}else if (game[tr][tc] != EMPTY) {
			return false; //no place to jump to
		}else if(player == RED && (game[fr][fc] == RED || game[fr][fc] == RKING)) { //player is red and piece is red
				if(game[fr][fc] == RKING && (tr == fr + 1 || tr == fr - 1) && (tc == fc + 1 || tc == fc - 1)) { //King can move forward or back
					return true;
				}else {
					return (tr == fr - 1 && (tc == fc+1 || tc==fc-1)); //Single must move forward
				}
		}else if(currentPlayer == BLACK && (game[fr][fc] == BLACK || game[fr][fc] == BKING)) { //player is black and piece is black
				if(game[fr][fc] == BKING && (tr == fr + 1 || tr == fr - 1) && (tc == fc + 1 || tc == fc - 1)) { //King can move forward or back
					return true;
				}else {
					return (tr == fr + 1 && (tc == fc+1 || tc==fc-1)); //Single must move forward
				}
			}else {
				return false; //there is not a red piece to jump
			}	
		
	}
	 
	//prints the current positions of the board to the console for debugging
	public void print() {
		for(int i = 0; i < game.length; i ++) {
			for(int j = 0; j < game[i].length; j++) {
				System.out.print(game[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	//class move creates a move object for ease of passing objects
	public class Move{
		int fromRow, fromCol; //starting position of the checker
		int toRow, toCol; //end position of the checker
		
		public Move(int frow, int fcol, int trow, int tcol) {
			this.fromRow = frow;
			this.fromCol = fcol;
			this.toRow = trow;
			this.toCol = tcol;
		}
		
		public boolean isJump() { //checks if the move is a jump
			return fromRow - toRow == 2 || fromRow - toRow ==-2;
		}
		
		public boolean equals(Move other) {
			return this.fromCol == other.fromCol && this.fromRow == other.fromRow &&
			this.toCol == other.toCol && this.toRow == other.toRow;
		}
	}
	

	public static void main(String[] args) {
		Scanner keys = new Scanner(System.in); 
		int input;
		System.out.println("Please enter 1 for Ai = Black, enter 2 for AI = RED");
		input = keys.nextInt();
		Board game = new Board(input);
		System.out.println("Lets Play!");
		game.print();
	}
}
