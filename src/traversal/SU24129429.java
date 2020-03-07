package traversal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SU24129429 {

	public static void main(String[] args) {
		
		System.out.println(args[0]);
		System.out.println(args[1]);

		//declaring variables
		int[] boardSize = new int[2];
		Character[][] board;
		int[] playerPos;
		String moves;
		
		//initializing variables
		{
			//board
			File boardFile = new File(args[0]);
			try {
				Scanner scBoard = new Scanner(boardFile);
				scBoard.nextLine();
				String bPos = scBoard.nextLine();
				//System.out.println("bPos " + bPos + "'");
				boardSize[0] = Integer.parseInt(bPos.substring(0, bPos.indexOf(' ')));
				boardSize[1] = Integer.parseInt(bPos.substring(bPos.indexOf(' ') + 1, bPos.length()));
				
				board = new Character[boardSize[0]][boardSize[1]];
				
				for(int y = 0; y < boardSize[1]; y++) {
					String row = scBoard.nextLine();
					for(int x = 0; x < boardSize[0]; x++) {
						board[x][y] = row.charAt(x);
					}
				}
				scBoard.close();
				printBoard(board, boardSize);
			} catch (FileNotFoundException e) {
				System.err.println("board file not found");
				System.exit(1);
			}
			
			//moves
			File movesFile = new File(args[1]);
		}
		
		
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				
			}
		}
	}
	
	public static void printBoard(Character[][] board, int[] boardSize) {
		System.out.println("Board:");
		for(int y = 0; y < boardSize[1]; y++) {
			for(int x = 0; x < boardSize[0]; x++) {
				System.out.print(board[x][y]);
			}
			System.out.print("\n");
		}
	}

}