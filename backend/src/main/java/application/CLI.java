package application;

import application.concurrency.Executor;
import application.concurrency.Observer;
import application.concurrency.UIType;

import application.model.Configuration;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLI {

    private static void printMenu() {
        System.out.println("1: Set configuration");
        System.out.println("2: Start");
        System.out.println("3: Stop");
        System.out.println("4: Exit");
    }

    private static Configuration promptConfiguration() throws Exception {
        int[] arr = new int[4];
        String[] label = {"Total Tickets", "Ticket Release Rate", "Customer Retrieval Rate", "Max Ticket Capacity"};

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < arr.length; i++) {
            int input;
            while (true) {
                System.out.println("Enter " + label[i] + ": ");
                try {
                    input = Integer.parseInt(scanner.nextLine());

                    if (input <= 0) throw new InputMismatchException();
                    break;
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("Invalid input, try again");
                }
            }
            arr[i] = input;
        }

        return new Configuration(arr[0], arr[1], arr[2], arr[3]);
    }

    public static void main(String[] args) {
        System.out.println("Coursework: w2053508");
        printMenu();

        Scanner scanner = new Scanner(System.in);
        Executor executor = new Executor(UIType.CLI);

        boolean isExit = false;

        while (!isExit) {
            try {
                System.out.println("Enter command (press 5 to print menu): ");
                int query = Integer.parseInt(scanner.nextLine());
                switch (query) {
                    case 1:
                        if (!executor.isRunning()) {
                            executor.setConfiguration(promptConfiguration());
                        }
                        break;
                    case 2:
                        if (!executor.isRunning()) {
                            executor.start();
                            new Thread(new Observer(executor)).start();
                            // don't interfere with other threads
                            while (executor.isRunning()) {
                                Thread.yield();
                            }
                        } else {
                            System.out.println("The executor is already running");
                        }
                        break;
                    case 3:
                        if (executor.isRunning()) {
                            executor.stop();
                        } else {
                            System.out.println("The executor is not running");
                        }
                        break;
                    case 4:
                        isExit = true;
                        break;
                    case 5:
                        printMenu();
                    default:
                }
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Invalid command, try again");
            } catch (Exception e) {
                System.out.println("Error occurred");
            }
        }
        scanner.close();
    }
}
