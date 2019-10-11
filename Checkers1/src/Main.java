
public class Main {

	/*initial character array for board positions
	 * b = blank non playable space
	 * p = blank playable space
	 * r = red piece position
	 * w = white piece position
	 * R/W = king piece respectively
	 */
	public static char[][] board_init = 
		{
				{'b','r','b','r','b','r','b','r'},
				{'r','b','r','b','r','b','r','b'},
				{'b','r','b','r','b','r','b','r'},
				{'p','b','p','b','p','b','p','b'},
				{'b','p','b','p','b','p','b','p'},
				{'w','b','w','b','w','b','w','b'},
				{'b','w','b','w','b','w','b','w'},
				{'w','b','w','b','w','b','w','b'}};
	
	public static void main(String[] args) {
		Game g = new Game(board_init);

	}

}
