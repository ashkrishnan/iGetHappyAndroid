package com.example.singhrahuldeep.igethappy.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gharjyot Singh
 */


public class DateTimeUtil {

    public static String changeFormat(String inputValue) {
        String outputValue = "";
        //mm/dd/yyyy
        String[] aray = null;
        if (inputValue.contains("/")) {
            aray = inputValue.split("/");
        } else if (inputValue.contains("-")) {
            aray = inputValue.split("-");
        }
        try {
            int month = Integer.parseInt(aray[0]) - 1;
            int day = Integer.parseInt(aray[1]);
            int year = Integer.parseInt(aray[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            outputValue = getFormattedDate(calendar);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            outputValue = inputValue;
        }
        /*SimpleDateFormat inputFormat = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = inputFormat.parse(inputValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMMM, yyyy");
            outputValue = displayFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return outputValue;
    }


    public static String getFormattedDate(Calendar selectedCalendar) {
        String monthText = new DateFormatSymbols(Locale.getDefault()).getMonths()[selectedCalendar.get(Calendar.MONTH)];
        monthText = monthText.substring(0, 1).toUpperCase() + monthText.subSequence(1, monthText.length());
        // String dayText = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        //dateText = dateText.substring(0, 3);//get only first three character
        return String.format("%s %s, %s", selectedCalendar.get(Calendar.DAY_OF_MONTH), monthText, selectedCalendar.get(Calendar.YEAR));
    }


    public static String getTwentyFourHourFormat(String inputValue) {
        String outputValue = "";
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm a");
        Date date = null;
        try {
            date = inputFormat.parse("2010-10-10 " + inputValue);
        } catch (ParseException e) {
            e.printStackTrace();
            if (inputValue.contains("M")) {
                inputValue = inputValue.replace("AM", "a.m.");
                inputValue = inputValue.replace("PM", "p.m.");
                date = getOtherDeviceUtcSupport(inputValue);
            }

        }
        try {
            outputValue = displayFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputValue;
    }

    private static Date getOtherDeviceUtcSupport(String inputValue) {
        //SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm a");
        Date date = null;
        try {
            date = inputFormat.parse("2010-10-10 " + inputValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean checkTimePresence(int startTime, int startMin, Calendar selectedCalendar) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        boolean isToday = checkToday(calendar, selectedCalendar);
        if (isToday && (startTime < hours || (startTime == hours && startMin < minutes))) {
            return true;
        }
        return false;
    }

    private static boolean checkToday(Calendar now, Calendar mSelectedCalender) {
        if (mSelectedCalender.getTime().getTime() > now.getTime().getTime()) {
            return false;
        }
        return true;
    }

    private static boolean checkPastDay(Calendar now, Calendar selectedCalendar) {
        if (selectedCalendar.getTime().getTime() < now.getTime().getTime() && selectedCalendar.get(Calendar.DAY_OF_MONTH) < now.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public static boolean isTimePast(int startHour, int startMin, Calendar selectedCalendar) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        //boolean isPastDays = checkPastDay(calendar, selectedCalendar);
        boolean isPastDay = checkPastDay(calendar, selectedCalendar);
        if (isPastDay) {
            return true;
        }
        boolean isToday = checkToday(calendar, selectedCalendar);
        if (isToday && (startHour < hours || (startHour == hours && startMin < minutes))) {
            return true;
        }
        return false;
    }


    public static String getRemainingTimeInMinutes(String expiration) {
        long expireTimeStamp = 0L;
        try {
            expireTimeStamp = Long.parseLong(expiration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
        Date start = new Date(); // now
        Date end = new Date(expireTimeStamp); // APRIL_1_2007
        if (start.getTime() < end.getTime()) {
            long diffInSeconds = Math.abs((end.getTime() - start.getTime()) / 1000);
            if (diffInSeconds < 60) {
                return String.format("%ds", diffInSeconds);// return secs
            } else if (diffInSeconds < 3600) {
                return String.format("%dm", (int) Math.floor((double) diffInSeconds / 60));//return mins
            } else if (diffInSeconds < 86400) {
                return String.format("%dh", (int) Math.floor((double) diffInSeconds / 3600));//return mins
            } else if (diffInSeconds < 604800) {
                return String.format("%dd", (int) Math.floor((double) diffInSeconds / 86400));//return mins
            } else if (diffInSeconds < 2628000) {
                return String.format("%dw", (int) Math.floor((double) diffInSeconds / 604800));//return mins
            }
        } else {
            return "Exp";
        }
        return "Exp";

    }

    public static String getElapseTimeInMinutes(String dateStamp) {
        long postTimeStamp = 0L;
        try {
            postTimeStamp = Long.parseLong(dateStamp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "10m";
        }

        Date postDate = new Date(postTimeStamp); // APRIL_1_2007
        Date now = new Date(); // JANUARY_1_2007
        long diffInSeconds = Math.abs((now.getTime() - postDate.getTime()) / 1000);
        /*if (diffInSeconds ==0) {
            return "Exp";
        } else*/
        if (diffInSeconds < 60) {
            return String.format("%ds", diffInSeconds);// return secs
        } else if (diffInSeconds < 3600) {
            return String.format("%dm", (int) Math.floor((double) diffInSeconds / 60));//return mins
        } else if (diffInSeconds < 86400) {
            return String.format("%dh", (int) Math.floor((double) diffInSeconds / 3600));//return mins
        } else if (diffInSeconds < 604800) {
            return String.format("%dd", (int) Math.floor((double) diffInSeconds / 86400));//return mins
        } else if (diffInSeconds < 2628000 || diffInSeconds >= 2628000) {
            return String.format("%dw", (int) Math.floor((double) diffInSeconds / 604800));//return mins
        }
        return "";

    }


    public static boolean checkExpiration(String expiration) {
        try {
            long expirationTimeStamp = Long.parseLong(expiration);
            Date current = new Date();
            if (current.getTime() >= expirationTimeStamp) {
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getTopTime(String expiration, String dateStamp) {
        if (expiration == null || dateStamp == null) {
            return "10m";
        }
        Date expirationDate = new Date(Long.parseLong(expiration));
        Date timeStampDate = new Date(Long.parseLong(dateStamp));
        double diffInSeconds = (expirationDate.getTime() - timeStampDate.getTime()) / 1000;
        try {
            if (diffInSeconds < 60) {
                return String.format("%ds", diffInSeconds);// return secs
            } else if (diffInSeconds < 3600) {
                return String.format("%dm", (int) Math.floor((double) diffInSeconds / 60));//return mins
            } else if (diffInSeconds < 86400) {
                return String.format("%dh", (int) Math.floor((double) diffInSeconds / 3600));//return mins
            } else if (diffInSeconds < 604800) {
                return String.format("%dd", (int) Math.floor((double) diffInSeconds / 86400));//return mins
            } else if (diffInSeconds < 2628000) {
                return String.format("%dw", (int) Math.floor((double) diffInSeconds / 604800));//return mins
            }
        } catch (Exception e) {

        }

       /* long diff[] = new long[]{0, 0, 0, 0};
        diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);*//* sec *//*
        diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds; *//* min *//*
        //diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;*//* hours *//*
        //diff[0] = (diffInSeconds = (diffInSeconds / 24)); *//* days *//*
        if (diff[2] > 0) {
            return String.format("%dm", diff[2]);//return mins
        } else if (diff[3] > 0) {
            return String.format("%ds", diff[3]);// return secs
        }*/

       /* DateTime expiration = new DateTime(1970, 1, 1).AddMilliseconds(double.Parse(post.expiration.ToString()));
        DateTime datestamp = new DateTime(1970, 1, 1).AddMilliseconds(double.Parse(post.datestamp.ToString()));
        TimeSpan span = expiration.Subtract(datestamp);

        var sb = new StringBuilder();
        if (span.Days > 365)
            sb.AppendFormat("{0}y", span.Days - 365);
        else if (span.Days > 0)
            sb.AppendFormat("{0}d", span.Days);
        else if (span.Hours > 0)
            sb.AppendFormat("{0}h", span.Hours);
        else if (span.Minutes > 0) {
            sb.AppendFormat("{0}m", span.Minutes);
        }*/
        return "10m";
    }

    /***
     *
     * @param totalTimeInMins - multiply by single user time
     * @param startTime - start time in 24hrs format
     * @param closingTime - end time of restaurant in 24hrs format
     * @param reserveStartTimeList - get reserve list according to select table
     * @param reseveEndTimeList -
     * @return
     */
    public static ArrayList<String> getAvailableSlot(int totalTimeInMins, int tableSize, String startTime, String openingTime, String closingTime, ArrayList<String> reserveStartTimeList, ArrayList<String> reseveEndTimeList) {
        ArrayList<String> availableSlotList = new ArrayList<>();

        startTime = getStartTime(startTime, openingTime);//new Line--
        String startTimeArray[] = startTime.split(":");
        int startHour = Integer.parseInt(startTimeArray[0]);
        int startMin = Integer.parseInt(startTimeArray[1]);

        String closingNewTime = getSubractTime(totalTimeInMins, closingTime);
        String closingTimeArray[] = closingNewTime.split(":");
        int endHour = Integer.parseInt(closingTimeArray[0]);
        int endMin = Integer.parseInt(closingTimeArray[1]);

        if (endHour <= startHour) {

            availableSlotList = getListFromReserved(totalTimeInMins, tableSize, startHour, startMin, 23, 45, reserveStartTimeList, reseveEndTimeList);

           /* ArrayList<String> nextDayAvailableSlotList = new ArrayList<>();
            nextDayAvailableSlotList = getListFromReserved(totalTimeInMins, tableSize, startHour, startMin, endHour, endMin, reserveStartTimeList, reseveEndTimeList);
            availableSlotList.addAll(nextDayAvailableSlotList);*/
        }else {
            availableSlotList = getListFromReserved(totalTimeInMins, tableSize, startHour, startMin, endHour, endMin, reserveStartTimeList, reseveEndTimeList);
        }

        return availableSlotList;
    }

    private static ArrayList<String> getListFromReserved(int totalTimeInMins, int tableSize, int startHour, int startMin, int endHour, int endMin, ArrayList<String> reserveStartTimeList, ArrayList<String> reseveEndTimeList) {
        ArrayList<String> availableSlotList = new ArrayList<>();
        while (startHour < endHour || (startHour == endHour && startMin <= endMin)) {
            if (reserveStartTimeList == null || reserveStartTimeList.size() == 0) {// if there is reservation of that date & selected table type
                // add all slot to list
                String time = getTwelveHourFormat(startHour + ":" + startMin);//new Line--
                availableSlotList.add(time);
            } else {
                //if some tables are already reserved
                int nextCounter = 0, previousCounter = 0;
                for (int i = 0; i < reserveStartTimeList.size(); i++) {
                    String reserveEndTime = reseveEndTimeList.get(i);
                    String reserveStartTime = reserveStartTimeList.get(i);
                    //reserveStartTime = getSubractTime(totalTimeInMins, reserveStartTime);
                    String[] reserveStartHM = reserveStartTime.split(":");
                    int reserverStartHour = Integer.parseInt(reserveStartHM[0]);
                    int reserverStartMin = Integer.parseInt(reserveStartHM[1]);
                    if (startHour <= reserverStartHour) {
                        Calendar currentCalendar = Calendar.getInstance();
                        currentCalendar.set(Calendar.HOUR, startHour);
                        currentCalendar.set(Calendar.MINUTE, startMin);
                        currentCalendar.add(Calendar.MINUTE, totalTimeInMins);
                        Date startDate = currentCalendar.getTime();

                        Calendar reserveCalendar = Calendar.getInstance();
                        reserveCalendar.set(Calendar.HOUR, reserverStartHour);
                        reserveCalendar.set(Calendar.MINUTE, reserverStartMin);
                        Date reserveDate = reserveCalendar.getTime();

                        if (startDate.getTime() > reserveDate.getTime()) {
                            nextCounter++;
                        }
                    } else {
                        //compare end time of reserve slots
                        String[] reserveEndHM = reserveEndTime.split(":");
                        int reservedEndHour = Integer.parseInt(reserveEndHM[0]);
                        int reservedEndMin = Integer.parseInt(reserveEndHM[1]);
                        if (startHour < reservedEndHour || (startHour == reservedEndHour && startMin < reservedEndMin)) {
                            previousCounter++;
                        }
                    }
                }

                if (nextCounter < tableSize && previousCounter != tableSize) {
                    String time = getTwelveHourFormat(startHour + ":" + startMin);//new Line--
                    availableSlotList.add(time);
                }
            }
            //increase to get next slot
            startMin = startMin + 15;
            if (startMin == 60) {
                startMin = 0;
                startHour++;
            }
        }
        return null;
    }

    private static String getStartTime(String startTime, String openingTime) {

        String startTimeArray[] = startTime.split(":");
        int endHour = Integer.parseInt(startTimeArray[0]);
        int endMin = Integer.parseInt(startTimeArray[1]);
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR, endHour);
        currentTime.set(Calendar.MINUTE, endMin);

        String openingTimeArray[] = openingTime.split(":");
        int openingHour = Integer.parseInt(openingTimeArray[0]);
        int openingMin = Integer.parseInt(openingTimeArray[1]);
        Calendar openigCalendar = Calendar.getInstance();
        openigCalendar.set(Calendar.HOUR, openingHour);
        openigCalendar.set(Calendar.MINUTE, openingMin);

        if (currentTime.getTime().getTime() < openigCalendar.getTime().getTime()) {
            return openingTime;
        }

        return startTime;
    }


    private static String getSubractTime(int totalTimeInMins, String closingTime) {

        String closingTimeArray[] = closingTime.split(":");

        int endHour = Integer.parseInt(closingTimeArray[0]);
        int endMin = Integer.parseInt(closingTimeArray[1]);

        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR, endHour);
        currentTime.set(Calendar.MINUTE, endMin);

        // subtract minutes
        currentTime.add(Calendar.MINUTE, -totalTimeInMins);
        currentTime.set(Calendar.AM_PM, 1);
        //int dgf = currentTime.get(Calendar.MINUTE);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
        String newDateTime = dateFormatter.format(currentTime.getTime());
        String[] valueArray = newDateTime.split(":");
        String hourMin = valueArray[0] + ":" + valueArray[1];

        return hourMin;
    }

    public static String getTwelveHourFormat(String inputValue) {
        String outputValue = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse("2010-10-10 " + inputValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            outputValue = displayFormat.format(date);
            if (outputValue.contains("m.")) {
                outputValue = outputValue.replace("a.m.", "AM");
                outputValue = outputValue.replace("p.m.", "PM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputValue;
    }
}
