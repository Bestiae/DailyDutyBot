package org.miroslav;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MyDailyBot extends TelegramLongPollingBot {

    private List<String> dutyPersons;
    private int dutyPerson;

    public MyDailyBot() {
        dutyPersons = List.of("@Bestiae", "@miri_3008", "@ru_vladislav");
        dutyPerson = 0;
    }

    @Override
    public String getBotUsername() {
        return "CleanerKitcherDutyBot";
    }

    @Override
    public String getBotToken() {
        return "6899200468:AAFsMLBtQdfxzNTfiZOQliUUUTvYDhR5oWE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Мы не обрабатываем прямые сообщения или команды здесь
    }

//    public void startSendingDailyMessages() {
//        Timer timer = new Timer();
//        // Задаем задачу на выполнение каждые 24 часа (86400000 миллисекунд)
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                sendDailyMessage(dutyPersons.get(dutyPerson));
//                updateDutyPerson();
//            }
//        }, 0, minutesToMilliseconds(1));
//
//    }

    public void startSendingDailyMessages() {
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis(); // текущее время

        // Установите время начала на 01:00
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Если текущее время уже после 01:00, установите начало на следующий день
        if (calendar.getTimeInMillis() <= now) {
            calendar.add(Calendar.DATE, 1);
        }

        // Вычисляем задержку до первого выполнения
        long delay = calendar.getTimeInMillis() - now;

        // Период - 24 часа в минутах = 1440
        long period = 24 * 60;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // during first start, and after that need to write some statistics of my application.
                // postgreSQL for this purpose -> date, name,
                sendDailyMessage(dutyPersons.get(dutyPerson));
                updateDutyPerson();
            }
        }, delay, minutesToMilliseconds(period));
    }

    private long minutesToMilliseconds(long minutes) {
        // 1440 for 24 hour
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    private void updateDutyPerson() {
        if (dutyPerson == dutyPersons.size() - 1) {
            dutyPerson = 0;
        } else {
            dutyPerson += 1;
        }
    }

    private String dutyMessage(String name) {
        StringBuilder sb = new StringBuilder();

        sb.append("\uD83C\uDF1F *Напоминание для " + name + "!* \uD83C\uDF1F\n" +
                "\n" +
                "Привет, " + name + "! Сегодня ты дежурный по поддержанию порядка в нашем общем пространстве. \uD83D\uDE80\n" +
                "\n" +
                "\uD83D\uDD39 *Пожалуйста, удели внимание следующим задачам:*\n" +
                "- **Посуда**: после использования мой её сразу. Не оставляй грязные тарелки и чашки.\n" +
                "- **Мусор**: проверь, не переполнены ли мусорные баки. Если нужно, вынеси мусор.\n" +
                "\n" +
                "Твои усилия помогают нам всем чувствовать себя комфортнее! \uD83C\uDF08\n" +
                "\n" +
                "Благодарим за ответственность и заботу! \uD83D\uDC4D");

        return sb.toString();
    }


    private void sendDailyMessage(String name) {
        SendMessage message = new SendMessage();
        message.setChatId("-1001945110213");
        message.setText(dutyMessage(name));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}