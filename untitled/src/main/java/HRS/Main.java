package HRS;

public class Main {

    public static void main(String[] args) {
        DateGenerator daemonThread = new DateGenerator();
        daemonThread.setDaemon(true);
        daemonThread.start();

        for (int i = 0; i < 20; i++) {
            Thread clientThread = new Thread(new DateClient(daemonThread));
            clientThread.start();
        }
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
