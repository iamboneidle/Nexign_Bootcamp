package cdr.cdr_service.CDRUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Демон поток, генерирующий новую дату фоновым процессом, между генерацией дат он ждет
 * TIME_SLEEP секунд, я это сделал для того, чтобы немного замедлить процесс генерации транзакций
 * и, так сказать, приблизить модель к реальной.
 */
public class DateGenerator extends Thread {
    /**
     * Дата, с которой начинают генерироваться звонки. Я выбрал так, чтобы наш сервис запустился 1 января 2024 года,
     * потому currentDate стоит на 31 декабря 2023, при запуске она сразу же перещелкнется на следующую.
     */
    private String currentDate = "31.12.2023";
    /**
     * Формат даты, в которым мы получаем новый день ("dd.MM.yyyy"),
     */
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    /**
     * Логгер, выводящий уведомления об ошибке и о том, что наступил новый день.
     */
    private static final Logger LOGGER = Logger.getLogger(DateGenerator.class.getName());
    /**
     * Время, которое спит поток между генерацией дат (в секундах).
     */
    private static final int TIME_SLEEP = 2;

    /**
     * Перегруженный метод, который представляет собой таску для потока. Он спит 2 секунды, затем вызывает метод
     * increaseDate(), после генерации даты он оповещает потоки, что можно забрать новую дату и сгенерировать звонки.
     */
    @Override
    public void run() {
        while (true) {
            try {
                sleep(TIME_SLEEP * 1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
            }
            synchronized (this) {
                currentDate = increaseDate();
                LOGGER.log(Level.INFO, currentDate + " new day has come but we're still alive");
                notifyAll();
            }
        }
    }

    /**
     * Метод, который генерирует следующую дату потокам.
     *
     * @return Возвращает дату в формате "dd.MM.yyyy".
     */
    @NotNull
    private String increaseDate() {
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = simpleDateFormat.parse(currentDate);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Синхронизированный геттер, через который потоки получают новую дату.
     *
     * @return Строка сегодняшней даты формата "dd.MM.yyyy".
     */
    public synchronized String getCurrentDate() {
        return currentDate;
    }
}
