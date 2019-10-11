
public class Checker {

	int x, y;
	int board_pos;
	char color;
	int pieceType;
	int[] valid_moves;

	public Checker() {

	}

	public Checker(int i, int j, char c, int bp) {
		x = i;
		y = j;
		color = c;
		pieceType = pieceInt(c);
		board_pos = bp;
	}

	public int pieceInt(int c){
			switch(c)
			{
				case 'b' : return 0; //blank non playable
				case 'p': return 0; //blank playable
				case 'r': return 1; //red = black in int array
				case 'w': return 2; //white = red in int array
				case 'R': return 3; //black king
				case 'W': return 4; //red king
				default: return -1;
				

			}
		}

	public void data() {
		System.out.println(x + " " + y + " " + color + " " + board_pos);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getBoard_pos() {
		return board_pos;
	}

	public String getBoard_pos_str() {
		return String.valueOf(board_pos);
	}

	public void setBoard_pos(int board_pos) {
		this.board_pos = board_pos;
	}

	public char getColor() {
		return color;
	}

	public void setColor(char color) {
		this.color = color;
		pieceType = this.pieceInt(color);
	}

	public int[] getValid_moves() {
		return valid_moves;
	}

	public void setValid_moves(int[] valid_moves) {
		this.valid_moves = valid_moves;
	}

}