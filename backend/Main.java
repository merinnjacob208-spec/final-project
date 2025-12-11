package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int totalPeople = 0;
        int totalCarAccident = 0;
        double totalSleepHours = 0;
        int totalStress = 0;

        int groupPeople = 0;
        int groupAccidents = 0;
        int groupStress = 0;

        int choice;

        // Choice comes from frontend (JS)
        if (args.length > 0) {
            choice = Integer.parseInt(args[0]);
        } else {
            System.out.println("{\"error\":\"Missing choice parameter\"}");
            return;
        }

        try {
            File file = new File("data.csv");
            Scanner input = new Scanner(file);

            // Skip header
            if (input.hasNextLine()) input.nextLine();

            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split(",");

                double sleepHours = Double.parseDouble(parts[1]);
                int carAccidentsPerYear = Integer.parseInt(parts[2]);
                int stressLevel = Integer.parseInt(parts[3]);

                totalPeople++;
                totalCarAccident += carAccidentsPerYear;
                totalSleepHours += sleepHours;
                totalStress += stressLevel;

                boolean matches = false;

                if (choice == 1 && sleepHours < 6) matches = true;
                if (choice == 2 && sleepHours > 6) matches = true;
                if (choice == 3 && stressLevel < 5) matches = true;
                if (choice == 4 && stressLevel >= 5) matches = true;

                if (matches) {
                    groupPeople++;
                    groupAccidents += carAccidentsPerYear;
                    groupStress += stressLevel;
                }
            }

            input.close();

            // Output JSON for frontend
            System.out.println("{");
            System.out.println("  \"totalPeople\": " + totalPeople + ",");
            System.out.println("  \"avgSleep\": " + (totalSleepHours / totalPeople) + ",");
            System.out.println("  \"avgAccidents\": " + ((double) totalCarAccident / totalPeople) + ",");
            System.out.println("  \"avgStress\": " + (totalStress / totalPeople) + ",");
            System.out.println("  \"groupPeople\": " + groupPeople + ",");
            System.out.println("  \"groupAccidents\": " + ((double) groupAccidents / groupPeople) + ",");
            System.out.println("  \"groupStress\": " + (choice == 3 || choice == 4 ? (groupStress / groupPeople) : 0));
            System.out.println("}");

        } catch (FileNotFoundException e) {
            System.out.println("{\"error\":\"data.csv not found\"}");
        }
    }
}
