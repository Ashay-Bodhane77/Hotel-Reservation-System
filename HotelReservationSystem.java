import java.io.*;
import java.util.*;

// Class for Hotel Room
class HotelRoom {

    private int roomNumber;
    private String category;
    private double price;
    private boolean isBooked;

    public HotelRoom(int roomNumber, String category, double price) {

        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void bookRoom() {
        isBooked = true;
    }

    public void cancelRoom() {
        isBooked = false;
    }

    public void displayRoom() {

        System.out.println(
                "Room No: " + roomNumber +
                        " | Category: " + category +
                        " | Price: ₹" + price +
                        " | Status: " + (isBooked ? "Booked" : "Available")
        );
    }
}

// Class for Reservation
class Reservation {

    private String customerName;
    private int roomNumber;
    private String category;
    private double paymentAmount;

    public Reservation(
            String customerName,
            int roomNumber,
            String category,
            double paymentAmount
    ) {

        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.paymentAmount = paymentAmount;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void displayReservation() {

        System.out.println(
                "Customer Name: " + customerName +
                        " | Room No: " + roomNumber +
                        " | Category: " + category +
                        " | Payment: ₹" + paymentAmount
        );
    }

    public String toFileString() {

        return customerName + "," +
                roomNumber + "," +
                category + "," +
                paymentAmount;
    }
}

// Main Class
public class HotelReservationSystem {

    static ArrayList<HotelRoom> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    static final String FILE_NAME = "reservations.txt";

    // Load Rooms
    public static void loadRooms() {

        rooms.add(new HotelRoom(101, "Standard", 2000));
        rooms.add(new HotelRoom(102, "Standard", 2000));

        rooms.add(new HotelRoom(201, "Deluxe", 4000));
        rooms.add(new HotelRoom(202, "Deluxe", 4000));

        rooms.add(new HotelRoom(301, "Suite", 7000));
    }

    // Display Available Rooms
    public static void displayAvailableRooms() {

        System.out.println("\n===== AVAILABLE ROOMS =====");

        for (HotelRoom room : rooms) {

            if (!room.isBooked()) {
                room.displayRoom();
            }
        }
    }

    // Book Room
    public static void bookRoom(Scanner sc) {

        System.out.print("Enter Customer Name: ");
        sc.nextLine();
        String name = sc.nextLine();

        displayAvailableRooms();

        System.out.print("Enter Room Number to Book: ");
        int roomNo = sc.nextInt();

        for (HotelRoom room : rooms) {

            if (room.getRoomNumber() == roomNo && !room.isBooked()) {

                room.bookRoom();

                System.out.println(
                        "Payment of ₹" +
                                room.getPrice() +
                                " Successful!"
                );

                Reservation reservation = new Reservation(
                        name,
                        roomNo,
                        room.getCategory(),
                        room.getPrice()
                );

                reservations.add(reservation);

                saveReservations();

                System.out.println("Room Booked Successfully!");
                return;
            }
        }

        System.out.println("Room Not Available!");
    }

    // Cancel Reservation
    public static void cancelReservation(Scanner sc) {

        System.out.print("Enter Room Number to Cancel Booking: ");
        int roomNo = sc.nextInt();

        Iterator<Reservation> iterator = reservations.iterator();

        while (iterator.hasNext()) {

            Reservation reservation = iterator.next();

            if (reservation.getRoomNumber() == roomNo) {

                iterator.remove();

                for (HotelRoom room : rooms) {

                    if (room.getRoomNumber() == roomNo) {

                        room.cancelRoom();
                    }
                }

                saveReservations();

                System.out.println("Reservation Cancelled Successfully!");
                return;
            }
        }

        System.out.println("Reservation Not Found!");
    }

    // View Reservations
    public static void viewReservations() {

        System.out.println("\n===== BOOKING DETAILS =====");

        if (reservations.isEmpty()) {

            System.out.println("No Reservations Found.");
            return;
        }

        for (Reservation reservation : reservations) {

            reservation.displayReservation();
        }
    }

    // Save Reservations to File
    public static void saveReservations() {

        try {

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(FILE_NAME)
            );

            for (Reservation reservation : reservations) {

                writer.write(reservation.toFileString());
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            System.out.println("Error Saving Reservations!");
        }
    }

    // Load Reservations from File
    public static void loadReservations() {

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                return;
            }

            BufferedReader reader = new BufferedReader(
                    new FileReader(FILE_NAME)
            );

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                String customerName = data[0];
                int roomNo = Integer.parseInt(data[1]);
                String category = data[2];
                double payment = Double.parseDouble(data[3]);

                reservations.add(
                        new Reservation(
                                customerName,
                                roomNo,
                                category,
                                payment
                        )
                );

                for (HotelRoom room : rooms) {

                    if (room.getRoomNumber() == roomNo) {

                        room.bookRoom();
                    }
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println("Error Loading Reservations!");
        }
    }

    // Main Method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        loadRooms();
        loadReservations();

        int choice;

        do {

            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");

            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Booking Details");
            System.out.println("5. Exit");

            System.out.print("Enter Your Choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    displayAvailableRooms();
                    break;

                case 2:

                    bookRoom(sc);
                    break;

                case 3:

                    cancelReservation(sc);
                    break;

                case 4:

                    viewReservations();
                    break;

                case 5:

                    System.out.println("Exiting System...");
                    break;

                default:

                    System.out.println("Invalid Choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}