package com.example.demo.converter;

import com.example.demo.constant.StatusEvent;
import com.example.demo.exception.CustomException;
import com.example.demo.request.Request.CreateLeaveRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateConvert {
    public static String convertLongToDate(Long agr) {
        Date date = new Date(agr);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String convertReplyCommentToString(Long createdDate) {
        long timeNow = System.currentTimeMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeNow - createdDate);
        if (minutes > 60) {
            return minutes / 60 + " hour";
        }
        if (minutes > 60 * 24) {
            return minutes / (60 * 24) + " day";
        }
        if (minutes < 60) {
            return minutes + " minutes";
        }
        return "Just comment";
    }

    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    public static StatusEvent checkEvent(Long timeBegin, Long timeEnd) {
        Date now = new Date(System.currentTimeMillis());
        Date start = new Date(timeBegin);
        Date end = new Date(timeBegin);
        if (now.before(start)) {
            return StatusEvent.INCOMING;
        } else if (now.after(end)) {
            return StatusEvent.HAPPENING;
        } else {
            return StatusEvent.FINISHED;
        }

    }

    public static long fromStringToMillis(String stringDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException ex) {
            throw new CustomException("Fail to parse from to string to long");
        }
        return date.getTime();
    }

    public static boolean checkInYear(Long timeBegin) {
        int thisYear = ZonedDateTime.now().getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeBegin);
        int yearBegin = calendar.get(Calendar.YEAR);
        return thisYear == yearBegin;
    }

    public static Double calculateDayOff(CreateLeaveRequest leaveRequest) {
        long startMillis = fromStringToMillis(leaveRequest.getDateBegin());
        long endMillis = fromStringToMillis(leaveRequest.getDateEnd());
        LocalDate startDate = Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        double result = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (leaveRequest.getShiftStart().equals(leaveRequest.getShiftEnd())) {
            if (startDate.equals(endDate)) {
                return 0.5;
            }
            return result - 0.5;
        }
        return result;
    }

}
