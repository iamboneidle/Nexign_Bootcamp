package HRS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Main {

    public static void main(String[] args) {
//        DateGenerator daemonThread = new DateGenerator();
//        daemonThread.setDaemon(true);
//        daemonThread.start();
//
//        for (int i = 0; i < 20; i++) {
//            Thread clientThread = new Thread(new DateClient(daemonThread));
//            clientThread.start();
//        }

        System.out.println(generateCallGapTime("01.01.2024"));
    }

    public static List<Long> generateCallGapTime(String call) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        List<Long> callTimeGap = new ArrayList<>();
        String timeStart = " 00:00:00";
        String timeEnd = " 23:59:59";

        try {
            long startBorder = sdf.parse(call + timeStart).getTime() / 1000;
            long endBorder = sdf.parse(call + timeEnd).getTime() / 1000;
            long callTimeStart = (long) (startBorder + (endBorder - startBorder) * Math.random() * 0.8);
            callTimeGap.add(callTimeStart);
            long callTimeEnd = (long) (callTimeStart + Math.random() * 540 + 60);
            callTimeGap.add(callTimeEnd);
            fuck(callTimeStart);
            return callTimeGap;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return callTimeGap;
    }

    public static void fuck(Long time) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());

        // Получаем номер месяца (от 1 до 12)
        int month = dateTime.getMonthValue();

        // Получаем номер года
        int year = dateTime.getYear();

        System.out.println("Номер месяца: " + month);
        System.out.println("Номер года: " + year);
    }

    static class DateGenerator extends Thread {
        private int currentDate = 0;

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    currentDate++;
                    System.out.println("Generated integer: " + currentDate);
                    notifyAll();
                }
            }
        }

        public synchronized int getCurrentDate() {
            return currentDate;
        }
    }

    static class DateClient implements Runnable {
        private final DateGenerator daemonThread;

        public DateClient(DateGenerator daemonThread) {
            this.daemonThread = daemonThread;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (daemonThread) {
                    try {
                        daemonThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int currentDate = daemonThread.getCurrentDate();
                    System.out.println("Client received integer: " + currentDate);
                }
            }
        }
    }
}
