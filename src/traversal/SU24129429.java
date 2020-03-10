package traversal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SU24129429 {
	
	//declaring variables
	public static int[] boardSize = new int[2];
	public static Character[][] board;
	public static Character[][] moversBoard;
	public static int[][] horizMovers;
	public static int[][] vertMovers;
	public static int[] playerPos = new int[2];
	public static String moves;

	public static void main(String[] args) {
		
		System.out.println(args[0]);
		System.out.println(args[1]);
		initialize(args[0], args[1]);
		
		//game logic
		for (int m = 0; m < moves.length(); m++) {
			switch(moves.charAt(m)) {
				//UP
				case 'k':
					break;
				//DOWN
				case 'j':
					break;
				//LEFT
				case 'h':
					break;
				//RIGHT
				case 'l':
					break;
			}
		}
	}
	
	//moves the player, returns false if illegal move
	public static boolean move(int xOffset, int yOffset) {
		return false;
	}
	
	public static void moveMovers(boolean isHorizontal) {
		if (isHorizontal) {
			//move horizontal movers
		}else {
			//move veritical movers
		}
	}
	
	//initializes fields
	public static void initialize(String boardPath, String movesPath) {
		
		//initialize boards
		File boardFile = new File(boardPath);
		try {
			//file read in
			Scanner scBoard = new Scanner(boardFile);
			scBoard.nextLine();
			String bPos = scBoard.nextLine();
			
			//initialize board size
			boardSize[0] = Integer.parseInt(bPos.substring(0, bPos.indexOf(' ')));
			boardSize[1] = Integer.parseInt(bPos.substring(bPos.indexOf(' ') + 1, bPos.length()));
			
			//initialize board and movers arrays
			board = new Character[boardSize[0]][boardSize[1]];
			moversBoard = new Character[boardSize[0]][boardSize[1]];
			
			//populates board and movers arrays with values
			for(int y = 0; y < boardSize[1]; y++) {
				String row = scBoard.nextLine();
				for(int x = 0; x < boardSize[0]; x++) {
					Character temp = row.charAt(x);
					
					//position is starting position
					if (temp == 's' || temp == 'S') {
						setPlayerPos(x,y);
						temp = '.';
					}else if(temp == 'l' || temp == 'r' || temp == 'u' || temp == 'd' //
							|| temp == 'L' || temp == 'R' || temp == 'U' || temp == 'D') {
						moversBoard[x][y] = temp;
						temp = '.';
					}
					board[x][y] = temp;
				}
			}
			scBoard.close();
			printBoard();
		} catch (FileNotFoundException e) {
			System.err.println("board file not found");
			System.exit(1);
		}
		
		//initializes moves field
		File movesFile = new File(movesPath);
		Scanner scMoves;
		try {
			scMoves = new Scanner(movesFile);
			moves = scMoves.nextLine();
			scMoves.close();
		} catch (FileNotFoundException e) {
			System.err.println("Moves file not found");
		}
		
	}
	
	//prints the board array
	public static void printBoard() {
		System.out.println("Board:");
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				System.out.print(board[x][y]);
			}
			System.out.print("\n");
		}
	}
	
	//sets the player's position
	public static void setPlayerPos(int x, int y) {
		playerPos[0] = x;
		playerPos[1] = y;
	}
	

}