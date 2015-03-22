package com.progress.backend.utils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Armen Arzumanyan
 */
public class CommonUtils {

    private static DecimalFormatSymbols fs = new DecimalFormatSymbols();

  

    public static Double doubleValue(String strValue) {
        Double reValue = null;

        if (strValue == null || strValue.trim().equals("")) {
            return null;
        }
        fs.setGroupingSeparator(',');
        fs.setDecimalSeparator('.');
        try {
            DecimalFormat nf = new DecimalFormat("#,###,###,##0.00", fs);
            nf.setMaximumFractionDigits(3);
            nf.setMaximumIntegerDigits(3);
            reValue = nf.parse(strValue).doubleValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reValue;
    }

    public static Long longValue(String strValue) {
        Long reValue = null;
        if (strValue == null || strValue.trim().equals("")) {
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            reValue = nf.parse(strValue).longValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reValue;
    }

    public static Integer integerValue(String strValue) {
        Integer reValue = null;
        if (strValue == null || strValue.trim().equals("")) {
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            reValue = nf.parse(strValue).intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reValue;
    }

   
  

    public static String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            byte[] bs;
            bs = messageDigest.digest(password.getBytes());
            for (int i = 0; i < bs.length; i++) {
                String hexVal = Integer.toHexString(0xFF & bs[i]);
                if (hexVal.length() == 1) {
                    sb.append("0");
                }
                sb.append(hexVal);
            }
        } catch (NoSuchAlgorithmException ex) {
            //log.debug(ex);
        }
        return sb.toString();
    }



    
}
