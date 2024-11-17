package WithStaticArray;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Book {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int option;

        System.out.println("Welcome to the book database!");

        // Loop to display menu and execute user options until exit (option 0)
        do {
            // Display menu options
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
        } while (option != 0);  // Exit when option 0 is chosen
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
    private static int count = 0;   // Number of books in the collection
    private static CustomBook[] books = new CustomBook[5];  // Array to store books

    // Getters and setters for books and count
    public static CustomBook[] getBooks() {
        return books;
    }
    public static void setBooks(CustomBook[] newBooks) {
        books = newBooks;
    }

    public static int getCount() {
        return count;
    }
    public static void setCount(int newCount) {
        count = newCount;
    }

    // Add a new book to the collection, resizing if necessary
    public static void add(CustomBook newBook) {
        if (count >= books.length) {
            int newSize = (count + 1) * 2;

            CustomBook[] newBooks = new CustomBook[newSize];

            for (int i = 0; i < count; i++) {
                newBooks[i] = books[i];
            }

            books = newBooks;
        }

        books[count] = newBook; // Add new book
        count++;
    }

    // Remove a specific book by its index
    public static void remove(Scanner cin) {
        System.out.println("Enter number of the book you want to remove:");
        int n = cin.nextInt();
        cin.nextLine();

        while (n < 1 || n > count) {
            System.out.println("PLease, enter the right number - from 1 to " + count);
            n = cin.nextInt();
            cin.nextLine();
        }

        // Create a new array without the removed book
        CustomBook[] newBooks = new CustomBook[count - 1];
        int j = 0;

        for (int i = 0; i < count; i++) {
            if (i != (n - 1)) {
                newBooks[j] = books[i];
                j++;
            }
        }

        books = newBooks;   // Update the books array
        count--;

        System.out.println("Book №" + (n) + " is removed");
    }

    // Clear all books from the collection
    public static void removeAll() {
        books = new CustomBook[5];
        count = 0;
        System.out.println("Collection is cleared.");
    }

    // Input book information from the user and add it to the collection
    public static void inputInfo(Scanner cin) {
        String title, author, genre;
        int year;

        System.out.println("Enter book details (or type 'exit' to stop): ");

        while (true) {
            System.out.print("Enter title: ");
            title = cin.nextLine();

            if (title.equalsIgnoreCase("exit")) {
                break;  // Stop if 'exit' is entered
            }

            System.out.print("Enter genre: ");
            genre = cin.nextLine();

            System.out.print("Enter author: ");
            author = cin.nextLine();

            System.out.print("Enter year: ");
            year = cin.nextInt();
            cin.nextLine();


            BookOperations.add(new CustomBook(title, genre, author, year)); // Add new book
        }
    }
}

// Sorting functionality for books by year
class sortBookArr {
    public static void sort(int left, int right) {
        if(left < right) {
            int indexPivot = preparing(left, right);    // Partition the array
            sort(left, indexPivot - 1); // Sort left partition
            sort(indexPivot + 1, right);    // Sort right partition
        }
    }

    public static int preparing(int left, int right) {
        int i = left + 1;
        int j = right;

        while(j >= i) {
            while((i <= right) && (BookOperations.getBooks()[i].getYearPublished() < BookOperations.getBooks()[left].getYearPublished())) {
                i++;
            }
            while((j >= left) && (BookOperations.getBooks()[j].getYearPublished() > BookOperations.getBooks()[left].getYearPublished())) {
                j--;
            }
            if(j > i) {
                // Swap elements to rearrange
                CustomBook swap = BookOperations.getBooks()[j];
                BookOperations.getBooks()[j] = BookOperations.getBooks()[i];
                BookOperations.getBooks()[i] = swap;
            }
        }

        // Swap pivot into correct position
        CustomBook swap = BookOperations.getBooks()[j];
        BookOperations.getBooks()[j] = BookOperations.getBooks()[left];
        BookOperations.getBooks()[left] = swap;

        return j;   // Return pivot index
    }
}

// Class for finding books by title or year
class BookFinder {
    // Search books by title
    public static void findByTitle(String name) {
        boolean found = false;

        for (int i = 0; i < BookOperations.getCount(); i++) {
            if (BookOperations.getBooks()[i].getTitle().equalsIgnoreCase(name)) {
                System.out.println(BookOperations.getBooks()[i].getFullInfo()); // Print book details
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found with the title: " + name);   // Print message if no match
        }
    }

    // Search books by publishing year
    public static void findByYear(int year)
    {
        boolean found = false;

        for (int i = 0; i < BookOperations.getCount(); i++) {
            if (BookOperations.getBooks()[i].getYearPublished() == year) {
                System.out.println(BookOperations.getBooks()[i].getFullInfo());
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

// Class for printing book details
class BookPrinter {
    public static void printOne(Scanner cin) {
        System.out.println("Enter number of the book you want to have information about:");
        int n = cin.nextInt();
        cin.nextLine();

        while (n < 1 || n > BookOperations.getCount()) {
            System.out.println("PLease, enter the right number - from 1 to " + BookOperations.getCount());
            n = cin.nextInt();
            cin.nextLine();
        }

        System.out.println(BookOperations.getBooks()[n - 1].getFullInfo());
    }

    public static void print() {
        if (BookOperations.getCount() == 0) {
            System.out.println("There are no books in the collection.");
        }
        else {
            for (int i = 0; i < BookOperations.getCount(); i++) {
                System.out.println((i + 1) + ". " + BookOperations.getBooks()[i].getFullInfo());
            }
        }
    }
}

// File handling operations
class FileHandler {
    // Saves the current list of books to a text file
    public static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            // Loop through all books and write each book's details to file
            for (int i = 0; i < BookOperations.getCount(); i++) {
                bw.write(BookOperations.getBooks()[i].getTitle() + "," +
                        BookOperations.getBooks()[i].getGenre() + "," +
                        BookOperations.getBooks()[i].getAuthor() + "," +
                        BookOperations.getBooks()[i].getYearPublished() + "\n");
            }

            System.out.print("Collection saved to the file.\n");
        }
        catch (IOException e) { // Catching any file issues
            e.printStackTrace();    // Print stack trace if an error occurs
        }
    }

    // Loads books from the file into the program
    public static void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            int i = 0;

            // Count the number of lines in the file to determine how many books to load
            while ((br.readLine()) != null) {
                i++;
            }

            // Create a new array to hold the books from the file
            CustomBook[] newBooks = new CustomBook[i];
            int newCount = 0;
            String line;

            try (BufferedReader br2 = new BufferedReader(new FileReader("books.txt"))) {
                // Read each line from the file and parse the book details
                while ((line = br2.readLine()) != null) {
                    String[] parts = line.split(",");   // Split line by comma to extract book attributes
                    newBooks[newCount] = new CustomBook(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                    newCount++; // Increment the count of books loaded
                }
            }

            // Update the global book collection with the loaded books
            BookOperations.setBooks(newBooks);
            BookOperations.setCount(newCount);

            System.out.print("Collection downloaded from the file.\n"); // Confirmation message
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}