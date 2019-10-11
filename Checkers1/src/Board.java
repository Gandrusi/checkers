import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


public class Board extends JPanel{

		public Checker[][] board;

		public int x_init,
		y_init,
		x,y,
		Width, Height;


		//Constructor
		public Board(Checker[][] c){
			board = c;
			x_init = 0;
			y_init = 0;
			x = x_init;
			y = y_init;
			Width = 100;
			Height = 100;
		}

		
		public void update(Checker[][] b){
			board = b;
			repaint();
		}
		

		
		public void paint(Graphics g){
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			x = x_init;
			y = y_init;

			//prints the board based on current board state
			for(int i = 0; i < board.length; i++){
				for(int j = 0; j < board[i].length; j++){
					if( board[i][j] != null){
						pickDraw(board[i][j].getColor(),g2);
						g2.setColor(Color.orange);
						g2.drawString(board[i][j].getBoard_pos_str(), x, y+10);
					}
					else
						g2 = pickDraw('b',g2);
					x+= Width;
				}
				y += Height;
				x = x_init;
			}
			
			

		}

		//draws whatever needs to be in the space
		public Graphics2D pickDraw(char c, Graphics2D d){

			switch(c)
			{
			case 'b':
				d.setColor(Color.gray);
				d.fillRect(x, y, Width, Height);
				return d;

			case 'p':
				d.setColor(Color.black);
				d.fillRect(x, y, Width, Height);
				return d;

			case 'r':
				d.setColor(Color.black);
				d.fillRect(x, y, Width, Height);
				d.setColor(Color.red);
				d.fillOval(x, y, Width, Height);
				return d;

			case 'w':
				d.setColor(Color.black);
				d.fillRect(x, y, Width, Height);
				d.setColor(Color.white);
				d.fillOval(x, y, Width, Height);
				return d;

			default:

			}
			return d;
		}

	}