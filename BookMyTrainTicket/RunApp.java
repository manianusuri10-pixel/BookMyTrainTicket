package BookMyTrainTicket;

import javax.swing.SwingUtilities;

public final class RunApp {

    private RunApp() {}

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                System.out.println("STEP 1: Starting database connection...");

                DatabaseManager.getInstance();

                System.out.println("STEP 2: Database connection successful!");

                new BookMyTicketApp();

                System.out.println("STEP 3: Application window created!");

            } catch (Exception e) {

                e.printStackTrace();

            }

        });

    }

}