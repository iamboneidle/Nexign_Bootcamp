package cdr.cdr_service.CDRUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateGenerator extends Thread {
    private String currentDate = "31.12.2023";
    private static final Logger LOGGER = Logger.getLogger(DateGenerator.class.getName());

    @Override
    public void run() {
        while (true) {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
            }
            synchronized (this) {
                currentDate = increaseDate(currentDate);
                LOGGER.log(Level.INFO, currentDate + " new day has come but we're still alive");
                notifyAll();
            }
        }
    }

    private String increaseDate(String currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = sdf.parse(currentDate);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }

        return sdf.format(calendar.getTime());
    }

    public synchronized String getCurrentDate() {
        return currentDate;
    }
}
