package com.geo.iptracker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpTrackerUtil {

    /**
     * Validate ip address
     * @param ip
     * @return true if ip is valid, otherwise false
     */
    public static boolean isValidIP(String ip){

        if (ip == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();

    }

    /**
     * Calculate distance in kms between 2 coordinates
     * @param lat1 latitude 1
     * @param lat2 latitude 2
     * @param lon1 longitude 1
     * @param lon2 longitude 2
     * @param el1 height 1
     * @param el2 height 2
     * @return disntace in kms
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
