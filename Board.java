package eightQueens;

public class Board {

	int brdSize = 8; //This can change depending on how many queens the user wants to use.
	int[] hCheck;//These are place holders for the diagonal check.
	int[] vCheck;
	int check = 4;// 1 for each diagonal direction, used for the diagonal check.
	boolean noQueen = false;//To show the the space has no queens.
	boolean queen = true;//To show that the space has a queen.
	int xCoord;//Place holders for the user input coordinates.
	int yCoord;
	int i = 1;//Used as a loop later on.

	boolean[][] theBoard;//Creates the 2D array for the board.

	/*
	 * Constructor method to create an empty board.
	 */
	public Board() {
		theBoard = new boolean[brdSize][brdSize];
		for (int row = 0; row < theBoard.length; row++) {//row loop/creation
			for (int col = 0; col < theBoard[row].length; col++) {//column loop/creation
				theBoard[row][col] = noQueen;//Each space will have no queens occupying to begin with.
			}
		}
		
		/*
		 * The following code takes a user input using the BIO class, there is also some validation for the case
		 * that the user inputs a number below 1 or above 8.
		 */
		System.out.println("Please enter row coordinate for Queen Placement (between 1-8): ");
		xCoord = BIO.getInt();//Uses the BIO class to gain the user input. (Created within brighton uni).
		while (xCoord < 1 || xCoord > 8) {//If invalid input, loop until it is valid.
			System.out.println("Placement not on the board.");
			System.out.println("Please enter row coordinate for Queen Placement (between 1-8): ");
			xCoord = BIO.getInt();
		}
		System.out.println("Please enter column coordinate for Queen Placement (between 1-8): ");
		yCoord = BIO.getInt();
		while (yCoord < 1 || yCoord > 8) {
			System.out.println("Placement not on the board.");
			System.out.println("Please enter column coordinate for Queen Placement (between 1-8): ");
			yCoord = BIO.getInt();
		}

		theBoard[xCoord - 1][yCoord - 1] = queen;// Take 1 away from each coord to allow normal input rather than an
												 // index.

	}

	public int getX() {//Returns the user input coordinates if needed.
		return xCoord;
	}

	public int getY() {
		return yCoord;
	}

	/*
	 * This method takes the column (usually 0) and iterates through each, checking
	 * and adding a queen to each one where relevant. The output will then be a
	 * boolean which returns true if all possible solutions have been found. This is
	 * the base case.
	 */
	boolean solveBack(int col) {
		if (col == yCoord - 1) {// This simply checks if the column == the users input,
			// if so then skip
			col++; // that column
		}

		if (col == brdSize) { // If the col == 7, display this solution and return true.
			dispBoard();
			return true;
		}

		boolean all = false;// this returns true when all possible solutions have been found.
		for (int row = 0; row < brdSize; row++) {// This iterates through all the rows(8).
			if (row == xCoord - 1 && row + 1 < brdSize) {// If the row == the users input, skip that row.
				row++;
			}
			// if a queen can be placed on [row][col], place it. Then check rest of the
			// queens, if there's a clash
			// then backtrack to get rid of the previous and try a new placement.
			if (spaceValid(row, col) && !spaceTaken(row, col)) {
				theBoard[row][col] = queen;//Places a queen at the current position.

				all = solveBack(col + 1) || all;//Either tries the next queen or ends the algorithm.

				theBoard[row][col] = noQueen; // Removes the queen via backtracking.
			}
		}

		// Return false if there are more solutions, true if there are not.
		return all;
	}

	// This method finds out if there are solutions to the placement of the first
	// queen, if so return true, else false
	// and inform the user that there are no solutions.
	boolean Solve() {

		if (solveBack(0) == false) {
			System.out.println("No solution for this queen placement.");
			return false;
		}

		return true;
	}

	/*
	*This method prints the entire board to the console, while using some line manipulation to make a better looking
	*output.
	*/
	public void dispBoard() {
		System.out.println();
		System.out.printf("solution %d:\n", i++);// iterates from 1, printing each solution.
		System.out.println("________________________");
		for (int r = 0; r < theBoard.length; r++) {// creates the rows and columns of the board.
			for (int c = 0; c < theBoard[r].length; c++) {
				if (theBoard[r][c] == queen && r == xCoord-1 && c == yCoord-1) {
					System.err.print(" Q");// Prints user input queen in red.
					System.out.print("|");
				} else if (theBoard[r][c] == queen) {
					System.out.print(" Q|"); // Prints queen if space is occupied.
				} else {
					System.out.print(" .|");// prints blank space if no queen.
				}
			}
			System.out.println();
		}
		System.out.print("________________________");
		System.out.println();
	}

	/*This method checks if the space isn't under attack by another queen by taking
	* the parameters of the row and column of
	* the desired queen and checking its vertical, horizontal and diagonal axis.
	*/
	public boolean spaceValid(int r, int c) {
		boolean isValid = true;

		// This sets up the ability to check all diagonals.
		hCheck = new int[check];
		vCheck = new int[check];
		// up right
		hCheck[0] = -1;
		vCheck[0] = 1;
		// down left
		hCheck[1] = 1;
		vCheck[1] = -1;
		// up left
		hCheck[2] = -1;
		vCheck[2] = -1;
		// down right
		hCheck[3] = 1;
		vCheck[3] = 1;

		//Check the upper right of the current queen by looping through the 2D array and checking each necessary space.
		for (int row_ = r, col_ = c; row_ >= 0 && col_ < 8; row_ += hCheck[0], col_ += vCheck[0]) {
			if (theBoard[row_][col_] == queen) {
				return false;
			}
		}

		//Check the lower left of the current queen by looping through the 2D array and checking each necessary space.
		for (int row_ = r, col_ = c; row_ < 8 && col_ >= 0; row_ += hCheck[1], col_ += vCheck[1]) {
			if (theBoard[row_][col_] == queen) {
				return false;
			}
		}

		//Check the upper left of the current queen by looping through the 2D array and checking each necessary space.
		for (int row_ = r, col_ = c; row_ >= 0 && col_ >= 0; row_ += hCheck[2], col_ += vCheck[2]) {
			if (theBoard[row_][col_] == queen) {
				return false;
			}
		}

		//Check the lower right of the current queen by looping through the 2D array and checking each necessary space.
		for (int row_ = r, col_ = c; row_ < 8 && col_ < 8; row_ += hCheck[3], col_ += vCheck[3]) {
			if (theBoard[row_][col_] == true) {
				return false;
			}
		}

		//Created one final check for each direction to ensure correctness.
		int i, j;

		// Check left row.
		for (i = 0; i < c; i++) {
			if (theBoard[r][i] == queen) {
				return false;
			}
		}
		// Upper diagonal
		for (i = r, j = c; i >= 0 && j >= 0; i--, j--) {
			if (theBoard[i][j] == queen) {
				return false;
			}
		}

		// Lower diagonal
		for (i = r, j = c; j >= 0 && i < brdSize; i++, j--) {
			if (theBoard[i][j] == queen) {
				return false;
			}
		}
		
		// Row
		for (int k = 0; k < brdSize; k++) {
			if (theBoard[k][c] == queen) {
				return false;
			}
		}

		// Column
		for (int k = 0; k < brdSize; k++) {
			if (theBoard[r][k] == queen) {
				return false;
			}
		}

		return isValid;
	}

	/*
	 * This method take the parameters of the desired row & column and returns true if there is a queen occupying 
	 * the space.
	 */
	public boolean spaceTaken(int r, int c) {
		if (theBoard[r][c] == queen) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * This method checks the amount of queens currently on the board by looping through the 2D array.
	 */
	public int amtQueens() {
		int count = 0;
		for (int r = 0; r < theBoard.length; r++) {
			for (int c = 0; c < theBoard[r].length; c++) {
				if (theBoard[r][c] == queen) {
					count++;
				}
			}
		}
		return count;
	}
}
