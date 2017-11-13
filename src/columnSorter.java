

import java.util.Comparator;

public class columnSorter implements Comparator {
	private int columnToSortOn;
	private int columnToSortOn1;
	private int columnToSortOn2;
	private int columnToSortOn3;

	//contructor to set the column to sort on.
	columnSorter(int columnToSortOn) {
		this.columnToSortOn = columnToSortOn;
	}
	columnSorter(int columnToSortOn1, int columnToSortOn2, int columnToSortOn3) {
		this.columnToSortOn1 = columnToSortOn1;
		this.columnToSortOn2 = columnToSortOn2;
		this.columnToSortOn3 = columnToSortOn3;
	}


	// Implement the abstract method which tells
	// how to order the two elements in the array.
	public int compare(Object o1, Object o2) {
		// cast the object args back to string arrays
		String[] row1 = (String[])o1;
		String[] row2 = (String[])o2;

		// compare the desired column values
		return row1[columnToSortOn].compareTo(row2[columnToSortOn]);
	}
}


