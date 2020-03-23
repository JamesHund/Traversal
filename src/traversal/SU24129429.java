package traversal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
		//PUT CODE TO TEST METHODS HERE
		//searchBoard(new Character[] {'x','t'});
		
		
		//END OF METHOD TESTING CODE
		
		
		//game logic
		for (int m = 0; m < moves.length(); m++) {
			int x = 0; //x offset
			int y = 0; //y offset
			switch(moves.charAt(m)) {
				//UP
				case 'k':
					moveMovers(false);
					y = -1;
					break;
				//DOWN
				case 'j':
					moveMovers(false);
					y = 1;
					break;
				//LEFT
				case 'h':
					moveMovers(true);
					x = -1;
					break;
				//RIGHT
				case 'l':
					moveMovers(true);
					x = 1;
					break;
			}
			if(!move(x,y)) {
				if(("" + board[playerPos[0]][playerPos[1]]).equalsIgnoreCase("t")) {
					System.out.println("player won (mover on target)");
					//print board
				}
				System.out.println("Illegal move");
				//print board
				break;
			}
		}
	}
	
	//returns array list of positions in board array where one or more characters are found
	public static ArrayList<int[]> searchBoard(Character[] chars, boolean movers){
		ArrayList<int[]> positions = new ArrayList<>();
		if (!movers) {
			for(int y = 0; y < boardSize[1]; y++) {
				for(int x = 0; x < boardSize[0]; x++) {
					for(int i = 0; i < chars.length; i++) {
						if(chars[i] == board[x][y]) {
							positions.add(new int[]{x,y});
							break;
						}
					}
				}
			}
		}else {
			for(int y = 0; y < boardSize[1]; y++) {
				for(int x = 0; x < boardSize[0]; x++) {
					for(int i = 0; i < chars.length; i++) {
						if(chars[i] == moversBoard[x][y]) {
							positions.add(new int[]{x,y});
							break;
						}
					}
				}
			}
		}
		return positions;
	}
	
	//moves the player, returns false if illegal move
	public static boolean move(int xOffset, int yOffset) {
		return false;
	}
	
	
	public static void moveMovers(boolean isHorizontal) {
		ArrayList<int[]> positions;
		Character[][] tempBoard = new Character[boardSize[0]][boardSize[1]];
		if (isHorizontal) {
			positions = searchBoard(new Character[] {'u','d','l','r'}, true);
		}else {
			positions = searchBoard(new Character[] {'U','D','L','R'}, true);
		}
		for (int[] pos : positions) {
			int xOffset = 0;
			int yOffset = 0;
			switch (("" + moversBoard[pos[0]][pos[1]]).toLowerCase()) {
				case "u":
					yOffset = -1;
					break;
				case "d":
					yOffset = 1;
					break;
				case "l":
					xOffset = -1;
					break;
				case "r":
					xOffset = 1;
					break;
			}
			int x = (pos[0] + xOffset)%boardSize[0]+boardSize[0];
			int y = (pos[1] + yOffset)%boardSize[1]+boardSize[1];
			tempBoard[x][y] = moversBoard[pos[0]][pos[1]];
			moversBoard[pos[0]][pos[1]] = null;
		}
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				if (tempBoard[x][y] != null) {
					moversBoard[x][y] = tempBoard[x][y];
				}
			}
		}
		
	}
	
	//sets the player's position
		public static void setPlayerPos(int x, int y) {
			playerPos[0] = x;
			playerPos[1] = y;
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
			printFullBoard();
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
	
	//prints the board array overlayed with the movers array
	public static void printFullBoard() {
		System.out.println("Board:");
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				if (moversBoard[x][y] != null){
					System.out.print(moversBoard[x][y]);
				}else{
					System.out.print(board[x][y]);
				}
			}
			System.out.print("\n");
		}
	}
	
	//prints the
	public static void printMoversBoard() {
		System.out.println("Movers Board:");
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				System.out.print(board[x][y]);
			}
			System.out.print("\n");
		}
	}
	

}