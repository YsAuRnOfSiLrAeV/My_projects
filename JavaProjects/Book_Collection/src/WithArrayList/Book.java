package WithArrayList;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

// Main class of the program
public class Book {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int option;

        System.out.println("Welcome to the book database!");

        do {
            // Displaying the menu for user interaction
            System.out.println("\nChoose an option to proceed:");
            System.out.println("1 - Add books to collection");
            System.out.println("2 - Remove book from the collection");
            System.out.println("3 - Clear all books");
            System.out.println("4 - Print all books");
            System.out.println("5 - Print one book");
            System.out.println("6 - Sort books by year");
            System.out.println("7 - Search books by title");
            System.out.println("8 - Search books by year");
            System.out.println("9 - Save books to the file");
            System.out.println("10 - Read books from the file");
            System.out.println("0 - Exit the program");

            option = cin.nextInt(); // Reading user option
            cin.nextLine(); // Clearing the buffer

            // Handling user's choice
            switch (option) {
                case 1:
                    BookOperations.inputInfo(cin);
                    break;
                case 2:
                    BookOperations.remove(cin);
                    break;
                case 3:
                    BookOperations.removeAll();
                    break;
                case 4:
                    BookPrinter.print();
                    break;
                case 5:
                    BookPrinter.printOne(cin);
                    break;
                case 6:
                    sortBookArr.sort(0, BookOperations.getCount() - 1);
                    System.out.println("Collection is sorted by publishing year.");
                    break;
                case 7:
                    BookFinder.ByTitle(cin);
                    break;
                case 8:
                    BookFinder.ByYear(cin);
                    break;
                case 9:
                    FileHandler.saveToFile();
                    break;
                case 10:
                    FileHandler.loadFromFile();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Please, enter 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 or 0");
            }
        } while (option != 0);
    }
}

// Class to custom book and get full information about it
class CustomBook {
    private String title;
    private String genre;
    private String author;
    private int yearPublished;

    public CustomBook(String title, String genre, String author, int yearPublished) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.yearPublished = yearPublished;
    }

    // Getters and setters for each attribute
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public String getFullInfo() {
        return "Title: " + title + ".  Genre: " + genre + ".  Author: " + author + ".  Year published: " + yearPublished;
    }
}

// Class for book operations like adding, removing, and clearing books
class BookOperations {
    private static ArrayList<CustomBook> books = new ArrayList<>();

    // Returns the list of books
    public static ArrayList<CustomBook> getBooks() {
        return books;
    }

    // Returns the number of books in the collection
    public static int getCount() {
        return books.size();
    }

    // Adds a book to the collection
    public static void add(CustomBook newBook) {
        books.add(newBook);
    }

    // Removes a book by its number
    public static void remove(Scanner cin) {
        System.out.println("Enter number of the book you want to remove:");
        int n = cin.nextInt();
        cin.nextLine();

        while (n < 1 || n > getCount()) {
            System.out.println("Please, enter the right number - from 1 to " + getCount());
            n = cin.nextInt();
            cin.nextLine();
        }

        books.remove(n - 1);
        System.out.println("Book №" + n + " is removed");
    }

    // Clears the entire collection
    public static void removeAll() {
        books.clear();
        System.out.println("Collection is cleared.");
    }

    // Adds books based on user input
    public static void inputInfo(Scanner cin) {
        String title, author, genre;
        int year;

        System.out.println("Enter book details (or type 'exit' to stop): ");

        while (true) {
            System.out.print("Enter title: ");
            title = cin.nextLine();

            if (title.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Enter genre: ");
            genre = cin.nextLine();

            System.out.print("Enter author: ");
            author = cin.nextLine();

            System.out.print("Enter year: ");
            year = cin.nextInt();
            cin.nextLine();

            BookOperations.add(new CustomBook(title, genre, author, year));
        }
    }
}

// Class for sorting books by publishing year
class sortBookArr {
    public static void sort(int left, int right) {
        if (left < right) {
            int indexPivot = preparing(left, right);    // Class for sorting books by publishing year
            sort(left, indexPivot - 1); // Sort left partition
            sort(indexPivot + 1, right);    // Sort right partition
        }
    }

    public static int preparing(int left, int right) {
        int i = left + 1;
        int j = right;

        while (j >= i) {
            // Find an element larger than the pivot on the left
            while (i <= right && BookOperations.getBooks().get(i).getYearPublished() < BookOperations.getBooks().get(left).getYearPublished()) {
                i++;
            }
            // Find an element smaller than the pivot on the right
            while (j >= left && BookOperations.getBooks().get(j).getYearPublished() > BookOperations.getBooks().get(left).getYearPublished()) {
                j--;
            }
            // Swap elements if needed
            if (j > i) {
                CustomBook swap = BookOperations.getBooks().get(j);
                BookOperations.getBooks().set(j, BookOperations.getBooks().get(i));
                BookOperations.getBooks().set(i, swap);
            }
        }

        // Place pivot in its correct position
        CustomBook swap = BookOperations.getBooks().get(j);
        BookOperations.getBooks().set(j, BookOperations.getBooks().get(left));
        BookOperations.getBooks().set(left, swap);

        return j;   // Return the pivot index
    }
}

// Class for searching books by title or year
class BookFinder {
    // Search books by title
    public static void findByTitle(String name) {
        boolean found = false;

        for (int i = 0; i < BookOperations.getCount(); i++) {
            if (BookOperations.getBooks().get(i).getTitle().equalsIgnoreCase(name)) {
                System.out.println(BookOperations.getBooks().get(i).getFullInfo());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found with the title: " + name);
        }
    }

    // Search books by publishing year
    public static void findByYear(int year) {
        boolean found = false;

        for (int i = 0; i < BookOperations.getCount(); i++) {
            if (BookOperations.getBooks().get(i).getYearPublished() == year) {
                System.out.println(BookOperations.getBooks().get(i).getFullInfo());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books that are published in: " + year);
        }
    }

    // Wrapper method to search by title using user input
    public static void ByTitle(Scanner cin) {
        System.out.print("Enter the title to search for: ");
        String title = cin.nextLine();
        BookFinder.findByTitle(title);
    }

    // Wrapper method to search by year using user input
    public static void ByYear(Scanner cin) {
        System.out.print("Enter the year to search for: ");
        int year = cin.nextInt();
        cin.nextLine();
        BookFinder.findByYear(year);
    }
}

// Class for printing book information
class BookPrinter {
    // Print information of a single book by its number
    public static void printOne(Scanner cin) {
        System.out.println("Enter number of the book you want to have information about:");
        int n = cin.nextInt();
        cin.nextLine();

        while (n < 1 || n > BookOperations.getCount()) {
            System.out.println("Please, enter the right number - from 1 to " + BookOperations.getCount());
            n = cin.nextInt();
            cin.nextLine();
        }

        System.out.println(BookOperations.getBooks().get(n - 1).getFullInfo());
    }

    // Print information of all books in the collection
    public static void print() {
        if (BookOperations.getCount() == 0) {
            System.out.println("There are no books in the collection.");
        }
        else {
            for (int i = 0; i < BookOperations.getCount(); i++) {
                System.out.println((i + 1) + ". " + BookOperations.getBooks().get(i).getFullInfo());
            }
        }
    }
}

// File handling operations
class FileHandler {
    // Save book collection to a file
    public static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (int i = 0; i < BookOperations.getCount(); i++) {
                bw.write(BookOperations.getBooks().get(i).getTitle() + "," + BookOperations.getBooks().get(i).getGenre() + "," + BookOperations.getBooks().get(i).getAuthor() + "," + BookOperations.getBooks().get(i).getYearPublished() + "\n");
            }

            System.out.print("Collection saved to the file.\n");

        }
        catch (IOException e) { // Catching any file issues
            e.printStackTrace();    // Print stack trace if an error occurs
        }
    }

    // Load book collection from a file
    public static void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;

            // Read each line from the file and add it as a book
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                String genre = parts[1];
                String author = parts[2];
                int year = Integer.parseInt(parts[3]);

                BookOperations.add(new CustomBook(title, genre, author, year));
            }

            System.out.println("Books loaded from the file.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}