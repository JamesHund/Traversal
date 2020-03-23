package traversal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SU24129429 {
	
	//declaring variables
	public static int[] boardSize = new int[2];
	public static Character[][] board;
	public static ArrayList<int[]> horizMovers = new ArrayList<>(); //contains positions and types of movers
	public static ArrayList<int[]> vertMovers = new ArrayList<>(); 
	public static ArrayList<int[]> ports = new ArrayList<>(); //contains positions of ports
	public static ArrayList<int[]> hSwitches; //contains positions of horiz switches
	public static ArrayList<int[]> vSwitches; //contains positions of vertical switches
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
	public static ArrayList<int[]> searchBoard(Character[] chars){
		ArrayList<int[]> positions = new ArrayList<>();
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
		return positions;
	}
	
	//moves the player, returns false if illegal move
	public static boolean move(int xOffset, int yOffset) {
		return false;
	}
	
	public static void moveMovers(boolean isHorizontal) {
		if(isHorizontal) {
			for(int[] mover : horizMovers) {
				switch(mover[2]) {
					case 0:
						mover[0] = (mover[0]-1);
						if(mover[0]<0) {
							mover[0] += boardSize[0];
						}
						break;
					case 1:
						mover[0] = (mover[0]+1);
						if(mover[0]>= boardSize[0]) {
							mover[0] -= boardSize[0];
						}
						break;
					case 3:
						mover[1] = (mover[1]-1);
						if(mover[1]<0) {
							mover[1] += boardSize[1];
						}
						break;
					case 4:
						mover[1] = (mover[1]+1);
						if(mover[1]>= boardSize[1]) {
							mover[1] -= boardSize[1];
						}
						break;
				}
			}
		}else {
			for(int[] mover : vertMovers) {
				switch(mover[2]) {
					case 0:
						mover[0] = (mover[0]-1);
						if(mover[0]<0) {
							mover[0] += boardSize[0];
						}
						break;
					case 1:
						mover[0] = (mover[0]+1);
						if(mover[0]>= boardSize[0]) {
							mover[0] -= boardSize[0];
						}
						break;
					case 3:
						mover[1] = (mover[1]-1);
						if(mover[1]<0) {
							mover[1] += boardSize[1];
						}
						break;
					case 4:
						mover[1] = (mover[1]+1);
						if(mover[1]>= boardSize[1]) {
							mover[1] -= boardSize[1];
						}
						break;
				}
			}			
		}
	}
	
	public static void switchSwitches(boolean isHorizontal) {
		if(isHorizontal) {
			for(int[] pos : hSwitches) {
				if(board[pos[0]][pos[1]] == 'h') {
					board[pos[0]][pos[1]] = 'H';
				}else {
					board[pos[0]][pos[1]] = 'h';
				}
			}
			
		}else {
			for(int[] pos : vSwitches) {
				if(board[pos[0]][pos[1]] == 'v') {
					board[pos[0]][pos[1]] = 'V';
				}else {
					board[pos[0]][pos[1]] = 'v';
				}
			}
		}
	}
	
	public static void activatePorts() {
		//invert port states
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
			
			//populates board and movers arrays with values
			for(int y = 0; y < boardSize[1]; y++) {
				String row = scBoard.nextLine();
				for(int x = 0; x < boardSize[0]; x++) {
					Character temp = row.charAt(x);
					
					//position is starting position
					if (temp == 's' || temp == 'S') {
						setPlayerPos(x,y);
						temp = '.';
					}else if(("lrudLRUD").contains("" + temp)){
						int id = -1;
						switch((""+temp).toLowerCase()) {
							case "l":
								id = 0;
								break;
							case "r":
								id = 1;
								break;
							case "u":
								id = 2;
								break;
							case "d":
								id = 3;
								break;
						}
						if(Character.isLowerCase(temp)) {
							horizMovers.add(new int[] {x,y,id});
						}else {
							vertMovers.add(new int[] {x,y,id});
						}
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
		
		hSwitches = searchBoard(new Character[] {'h','H'});
		vSwitches = searchBoard(new Character[] {'v','V'});
		
	}
	
	//prints the board array 
	public static void printFullBoard() {
		System.out.println("Board:");
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				System.out.print(board[x][y]);
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