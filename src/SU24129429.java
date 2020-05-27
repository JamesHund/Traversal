import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SU24129429 {

	// declaring variables
	public static int[] boardSize;
	public static Character[][] board;

	public static ArrayList<int[]> horizMovers; // contains positions and types of movers
	public static ArrayList<int[]> vertMovers;
	public static ArrayList<int[]> ports; // contains positions of ports
	public static ArrayList<int[]> hSwitches; // contains positions of horizontal switches
	public static ArrayList<int[]> vSwitches; // contains positions of vertical switches

	public static int[] playerPos;
	public static String moves;

	public static boolean graphics; // true if graphics mode enabled
	public static final int tileSize = 108; // size (in pixels) to render each tile
	public static final int tileOffset = tileSize / 2;
	public static boolean hasLost = false;
	
	public SU24129429(String[] args) {
		main(args);
	}

	public static void main(String[] args) {

		graphics = (args.length == 1);

		if (graphics) { // graphics mode

			initialize(args[0]);

			initializeCanvas();

			// main game loop
			while (true) {
				// wait for input
				while (!StdDraw.hasNextKeyTyped()); 
				char input = StdDraw.nextKeyTyped();

				if (isValidInput(input)) {

					// quit command
					if (input == 'q')
						System.exit(0);

					int[] oldPlayerPos = new int[] { playerPos[0], playerPos[1] };
					boolean isLegal = move(input);

					drawPosition(oldPlayerPos);
					drawPosition(playerPos);

					if (("" + board[playerPos[0]][playerPos[1]]).equalsIgnoreCase("t")) { // checks if player on target
						System.out.println("You won!");
						System.exit(0);
					}
					if (!isLegal) {
						System.out.println("You lost!");
						hasLost = true;
						int[] drawOffset = new int[] {0,0};
						double timeStep = 1000.0/24.0; //framerate of animation
						double totalTime = 4000; //total time of animation (milliseconds)
						long initial = System.currentTimeMillis();
						long previous = initial;
						while(true) {
							long current = System.currentTimeMillis();
							if(current - initial < totalTime) {
								if (current - previous > timeStep) {
									previous = current;
									drawOffset[0] += 4;
									drawOffset[1] += 4;
									StdDraw.setXscale(0 + drawOffset[0], boardSize[0] * tileSize + drawOffset[0]);
									StdDraw.setYscale(0 + drawOffset[1], boardSize[1] * tileSize + drawOffset[1]);
									drawFullBoard();
								}
							}else {
								new SU24129429(args);
								System.exit(0);
							}
						}
					}
				}
			}
		} else { // text mode

			initialize(args[0], args[1]);

			//main game loop
			for (int m = 0; m < moves.length(); m++) {

				Character input = moves.charAt(m);

				if (isValidInput(input)) {
					
					if (input == 'x') { //quit command
						printBoard();
						break;
					}

					boolean isLegal = move(input);

					if (("" + board[playerPos[0]][playerPos[1]]).equalsIgnoreCase("t")) { // checks if player on target
						System.out.println("You won!");
						printBoard();
						break;
					}
					if (!isLegal) {
						System.out.println("You lost!");
						printBoard();
						break;
					}
				} else {
					System.out.println("Incorrect move");
					printBoard();
					break;
				}
			}
		}

	}

	// initializes fields (text mode)
	public static void initialize(String boardPath, String movesPath) {

		initialize(boardPath);
		// initializes moves field
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

	// initializes fields (graphics mode)
	public static void initialize(String boardPath) {

		// initialize boards
		File boardFile = new File(boardPath);
		try {
			// file read in
			Scanner scBoard = new Scanner(boardFile);
			scBoard.nextLine();
			String bPos = scBoard.nextLine();

			// initialize board size
			boardSize = new int[2];
			boardSize[1] = Integer.parseInt(bPos.substring(0, bPos.indexOf(' ')));
			boardSize[0] = Integer.parseInt(bPos.substring(bPos.indexOf(' ') + 1, bPos.length()));

			// initialize board array to specified size
			board = new Character[boardSize[0]][boardSize[1]];
			
			horizMovers = new ArrayList<>();
			vertMovers = new ArrayList<>();
			playerPos = new int[2];
			hasLost = false;

			// populates board and movers arrays with values
			for (int y = 0; y < boardSize[1]; y++) {
				String row = scBoard.nextLine();
				for (int x = 0; x < boardSize[0]; x++) {
					Character temp = row.charAt(x);
					// position is starting position
					if (temp == 's' || temp == 'S') {
						setPlayerPos(x, y);
						temp = '.';
					} else if (("lrudLRUD").contains("" + temp)) { // checks if mover is in position
						int id = -1;
						switch (("" + temp).toLowerCase()) {
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
						if (Character.isLowerCase(temp)) {
							horizMovers.add(new int[] { x, y, id });
						} else {
							vertMovers.add(new int[] { x, y, id });
						}
						temp = '.';
					}
					board[x][y] = temp;
				}
			}
			scBoard.close();
		} catch (FileNotFoundException e) {
			System.err.println("Board file not found");
			System.exit(1);
		}

		hSwitches = searchBoard(new Character[] { 'h', 'H' });
		vSwitches = searchBoard(new Character[] { 'v', 'V' });
		ports = searchBoard(new Character[] { 'p', 'P' });

	}

	// creates a game window and draws initial layout
	public static void initializeCanvas() {
		StdDraw.setCanvasSize(boardSize[0] * tileSize, boardSize[1] * tileSize);
		StdDraw.clear(Color.LIGHT_GRAY);
		StdDraw.setXscale(0, boardSize[0] * tileSize);
		StdDraw.setYscale(0, boardSize[1] * tileSize);

		// draw initial layout of tiles on canvas
		drawFullBoard();
		
	}
	
	public static void drawFullBoard(){
		//StdDraw.clear(Color.LIGHT_GRAY);
		for (int x = 0; x < boardSize[0]; x++) {
			for (int y = 0; y < boardSize[1]; y++) {
				drawPosition(new int[] { x, y });
			}
		}
	}

	// moves the player, returns false the move is illegal
	// does not validate input (this is performed in validateInput() method)
	public static boolean move(char move) {

		// positional offsets by which to move the player
		int x = 0; 
		int y = 0; 

		boolean horizontal = false; // whether the player moves horizontally

		switch (move) {
		// UP
		case 'k':
			horizontal = false;
			y = -1;
			break;
		// DOWN
		case 'j':
			horizontal = false;
			y = 1;
			break;
		// LEFT
		case 'h':
			horizontal = true;
			x = -1;
			break;
		// RIGHT
		case 'l':
			horizontal = true;
			x = 1;
			break;
		}

		// sets the player position
		if (playerPos[0] + x < 0 || playerPos[0] + x == boardSize[0]) {
			// if the player tries to move off the sides of the board the method returns,
			// hence no changes are made to board
			return true;
		} else if (playerPos[1] + y < 0) { // wraps player round bottom
			setPlayerPos(playerPos[0], boardSize[1] - 1);
		} else if (playerPos[1] + y == boardSize[1]) { // wraps player round top
			setPlayerPos(playerPos[0], 0);
		} else {
			setPlayerPos(playerPos[0] + x, playerPos[1] + y);
		}

		moveMovers(horizontal);
		switchSwitches(horizontal);

		int pX = playerPos[0];
		int pY = playerPos[1];

		if (isMoverAtPosition(playerPos))
			return false;

		// determines whether player steps on a dangerous tile
		switch ("" + board[pX][pY]) {
		case "k":
		case "K":
			useKey(playerPos);
		case "H":
		case "V":
		case ".":
		case "I": // inactive key
		case "P":
		case "t":
		case "T":
			return true;
		default:
			return false;
		}

	}

	// Validates user input based on whether program is run in text or graphics mode
	public static boolean isValidInput(char input) {
		String validchars;
		if (graphics) {
			validchars = "hjklq";
		} else {
			validchars = "hjklx";
		}
		if (validchars.contains("" + input)) {
			return true;
		}
		return false;
	}

	// iterates through either horizontal or vertical movers lists and moves them
	// accordingly
	public static void moveMovers(boolean isHorizontal) {
		if (isHorizontal) {
			for (int[] mover : horizMovers) {
				int[] oldPos = new int[] { mover[0], mover[1] };
				int xOffset = 0;
				int yOffset = 0;

				switch (mover[2]) {
				case 0:
					xOffset -= 1;
					if (mover[0] + xOffset < 0) {
						xOffset += boardSize[0];
					}
					break;
				case 1:
					xOffset += 1;
					if (mover[0] + xOffset >= boardSize[0]) {
						xOffset -= boardSize[0];
					}
					break;
				case 2:
					yOffset -= 1;
					if (mover[1] + yOffset < 0) {
						yOffset += boardSize[1];
					}
					break;
				case 3:
					yOffset += 1;
					if (mover[1] + yOffset >= boardSize[1]) {
						yOffset -= boardSize[1];
					}
					break;
				}

				mover[0] += xOffset;
				mover[1] += yOffset;
				if (graphics) {
					drawPosition(new int[] { mover[0], mover[1] });
					drawPosition(oldPos);
				}
			}
		} else {
			for (int[] mover : vertMovers) {

				int xOffset = 0;
				int yOffset = 0;
				int[] oldPos = new int[] { mover[0], mover[1] };

				switch (mover[2]) {
				case 0:
					xOffset -= 1;
					if (mover[0] + xOffset < 0) {
						xOffset += boardSize[0];
					}
					break;
				case 1:
					xOffset += 1;
					if (mover[0] + xOffset >= boardSize[0]) {
						xOffset -= boardSize[0];
					}
					break;
				case 2:
					yOffset -= 1;
					;
					if (mover[1] + yOffset < 0) {
						yOffset += boardSize[1];
					}
					break;
				case 3:
					yOffset += 1;
					if (mover[1] + yOffset >= boardSize[1]) {
						yOffset -= boardSize[1];
					}
					break;
				}

				mover[0] += xOffset;
				mover[1] += yOffset;

				if (graphics) {
					drawPosition(new int[] { mover[0], mover[1] });
					drawPosition(oldPos);
				}

			}
		}
	}

	public static void switchSwitches(boolean isHorizontal) {
		if (isHorizontal) {
			if (hSwitches != null) {
				for (int[] pos : hSwitches) {
					if (board[pos[0]][pos[1]] == 'h') {
						board[pos[0]][pos[1]] = 'H';
					} else {
						board[pos[0]][pos[1]] = 'h';
					}
					if (graphics) {
						drawPosition(pos);
					}
				}
			}
		} else {
			if (vSwitches != null) {
				for (int[] pos : vSwitches) {
					if (board[pos[0]][pos[1]] == 'v') {
						board[pos[0]][pos[1]] = 'V';
					} else {
						board[pos[0]][pos[1]] = 'v';
					}
					if (graphics) {
						drawPosition(pos);
					}
				}
			}
		}
	}

	public static void useKey(int[] pos) {
		board[pos[0]][pos[1]] = 'I';
		for (int[] port : ports) {
			if (board[port[0]][port[1]] == 'p') {
				board[port[0]][port[1]] = 'P';
			} else {
				board[port[0]][port[1]] = 'p';
			}

			if (graphics) {
				drawPosition(port);
			}
		}
	}

	// returns array list of positions in board array where one or more characters
	// are found
	public static ArrayList<int[]> searchBoard(Character[] chars) {
		ArrayList<int[]> positions = new ArrayList<>();
		for (int y = 0; y < boardSize[1]; y++) {
			for (int x = 0; x < boardSize[0]; x++) {
				for (int i = 0; i < chars.length; i++) {
					if (chars[i] == board[x][y]) {
						positions.add(new int[] { x, y });
						break;
					}
				}
			}
		}
		return positions;
	}

	// returns true if a mover is at a certain position
	public static boolean isMoverAtPosition(int[] pos) {
		for (int[] mover : horizMovers) {
			if (pos[0] == mover[0] && pos[1] == mover[1]) {
				return true;
			}
		}
		for (int[] mover : vertMovers) {
			if (pos[0] == mover[0] && pos[1] == mover[1]) {
				return true;
			}
		}
		return false;
	}

	// returns type of mover at position as an int array
	// {direction,horizontal/vertical}
	public static int[] moverTypeAtPosition(int[] pos) {
		for (int[] mover : horizMovers) {
			if (pos[0] == mover[0] && pos[1] == mover[1]) {
				return new int[] { mover[2], 0 };
			}
		}
		for (int[] mover : vertMovers) {
			if (pos[0] == mover[0] && pos[1] == mover[1]) {
				return new int[] { mover[2], 1 };
			}
		}

		return new int[] { -1, -1 }; // returns -1,-1 in the case where mover is not found in position
	}

	// sets the player's position
	public static void setPlayerPos(int x, int y) {
		playerPos[0] = x;
		playerPos[1] = y;
	}

	// takes in position and updates GUI at tile position pos
	public static void drawPosition(int[] pos) {
		String tile = "" + board[pos[0]][pos[1]];
		String image = "images/tvl_";
		if (pos[0] == playerPos[0] && pos[1] == playerPos[1]) {
			image += "s";
		} else if (!isMoverAtPosition(pos)) {
			switch (tile) {
			case "k":
			case "K":
				image += "k1";
				break;
			case "I": // inactive key
				image += "k0";
				break;
			case "h":
				image += "sh1";
				break;
			case "v":
				image += "sv1";
				break;
			case "H":
				image += "sh0";
				break;
			case "V":
				image += "sv0";
				break;
			case "t":
			case "T":
				image += "t";
				break;
			case "x":
			case "X":
				image += "x";
				break;
			case "p":
				image += "p1";
				break;
			case "P":
				image += "p0";
				break;
			default:
				if(!hasLost) {
					StdDraw.setPenColor(Color.LIGHT_GRAY);
					StdDraw.filledSquare(tileSize * pos[0] + tileOffset, boardSize[1] * tileSize - tileSize * pos[1] - tileOffset,
							tileOffset);
				}
				return;
			}
		} else {
			int[] type = moverTypeAtPosition(pos);
			switch (type[0]) {
			case 0:
				image += "l";
				break;
			case 1:
				image += "r";
				break;
			case 2:
				image += "u";
				break;
			case 3:
				image += "d";
				break;
			}
			image += (type[1] == 0) ? "h" : "v";
		}

		image += ".png";
		// System.out.println(image);
		int xCoord = tileSize * pos[0] + tileOffset;
		int yCoord = boardSize[1] * tileSize - tileSize * pos[1] - tileOffset;

		StdDraw.picture(xCoord, yCoord, image);
	}

	// prints the board array (for debugging)
	public static void printBoardDebug() {
		System.out.println("Board:");
		for (int y = 0; y < boardSize[1]; y++) {
			for (int x = 0; x < boardSize[0]; x++) {
				if (!isMoverAtPosition(new int[] { x, y })) {
					System.out.print(board[x][y]);
				} else {
					System.out.print("m");
				}
			}
			System.out.print("\n");
		}
	}

	public static void printBoard() {
		for (int y = 0; y < boardSize[1]; y++) {
			for (int x = 0; x < boardSize[0]; x++) {
				if (playerPos[0] == x && playerPos[1] == y) {
					System.out.print("Y");
				} else if (!isMoverAtPosition(new int[] { x, y })) {
					switch ("" + board[x][y]) {
					case "k":
					case "K":
					case "I": // inactive key
						System.out.print("k");
						break;
					case "h":
					case "v":
						System.out.print("s");
						break;
					case "H":
					case "V":
						System.out.print("S");
						break;
					case "t":
					case "T":
						System.out.print("t");
						break;
					case "X":
						System.out.print("x");
						break;
					default:
						System.out.print("" + board[x][y]);
					}
				} else {
					System.out.print("m");
				}
			}
			System.out.print("\n");
		}
	}

}
