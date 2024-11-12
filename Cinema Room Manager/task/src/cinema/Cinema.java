package cinema;

import java.util.*;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input number of rows and seats per row
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seatsPerRow = scanner.nextInt();

        // Initialize office
        Office office = new Office(rows, seatsPerRow);

        while (true) {
            // Display the menu
            System.out.println("\n1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Show the current seating arrangement
                    office.displaySeating();
                    break;
                case 2:
                    // Buy a ticket
                    buyTicket(office, scanner);
                    break;
                case 3:
                    // Show statistics
                    office.showStatistics();
                    break;
                case 0:
                    // Exit the program
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void buyTicket(Office cinema, Scanner scanner) {
        while (true) {
            System.out.println("Enter a row number:");
            int chosenRow = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            int chosenSeat = scanner.nextInt();

            // Check if the input is valid
            if (chosenRow < 1 || chosenRow > cinema.getRows() || chosenSeat < 1 || chosenSeat > cinema.getSeatsPerRow()) {
                System.out.println("Wrong input! Please enter valid coordinates.");
            } else if (!cinema.isSeatAvailable(chosenRow, chosenSeat)) {
                System.out.println("That ticket has already been purchased! Please choose a different seat.");
            } else {
                // If the seat is valid and available, book it
                cinema.bookSeat(chosenRow, chosenSeat);
                int price = cinema.calculateTicketPrice(chosenRow);
                System.out.println("Ticket price: $" + price);
                break;
            }
        }
    }

}

class Ticket {
    private int price;

    public Ticket(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Ticket price: $" + price;
    }
}

class Office {
    private Seat[][] seats;
    private int rows;
    private int seatsPerRow;
    private int purchasedTickets;
    private int currentIncome;

    public Office(int rows, int seatsPerRow) {
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        seats = new Seat[rows][seatsPerRow];
        initializeSeats();
    }

    private void initializeSeats() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                seats[i][j] = new Seat();
            }
        }
    }

    public void displaySeating() {
        System.out.println("Cinema:");
        System.out.print("  ");
        for (int i = 1; i <= seatsPerRow; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < seatsPerRow; j++) {
                System.out.print(seats[i][j].getStatus() + " ");
            }
            System.out.println();
        }
    }

    public boolean bookSeat(int row, int seatNumber) {
        if (isSeatAvailable(row, seatNumber)) {
            seats[row - 1][seatNumber - 1].bookSeat();
            purchasedTickets++;
            currentIncome += calculateTicketPrice(row);
            return true;
        }
        System.out.println("Seat is already booked!");
        return false;
    }

    public boolean isSeatAvailable(int row, int seatNumber) {
        return seats[row - 1][seatNumber - 1].isAvailable();
    }

    public int calculateTicketPrice(int row) {
        int totalSeats = rows * seatsPerRow;
        if (totalSeats <= 60) {
            return 10;
        }
        int frontHalfRows = rows / 2;
        return row <= frontHalfRows ? 10 : 8;
    }

    public int getRows() {
        return rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void showStatistics() {
        int totalSeats = rows * seatsPerRow;
        double percentage = (purchasedTickets * 100.0) / totalSeats;

        System.out.printf("Number of purchased tickets: %d\n", purchasedTickets);
        System.out.printf("Percentage: %.2f%%\n", percentage);
        System.out.printf("Current income: $%d\n", currentIncome);

        // Calculate total income if all tickets were sold
        int totalIncome = calculateTotalIncome();
        System.out.printf("Total income: $%d\n", totalIncome);
    }

    private int calculateTotalIncome() {
        int totalSeats = rows * seatsPerRow;
        if (totalSeats <= 60) {
            return totalSeats * 10;
        }
        int frontHalfRows = rows / 2;
        return frontHalfRows * seatsPerRow * 10 + (rows - frontHalfRows) * seatsPerRow * 8;
    }
}

class Seat {
    private char status; // 'S' for available, 'B' for booked

    public Seat() {
        this.status = 'S';
    }

    public char getStatus() {
        return status;
    }

    public void bookSeat() {
        this.status = 'B';
    }

    public boolean isAvailable() {
        return status == 'S';
    }
}