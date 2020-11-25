package com.dcits.restapiapp.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionStack {

    public static String exceptionStackPrint(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try{
            e.printStackTrace(pw);
            return sw.toString();
        } catch(Exception e1) {
            return "";
        }

    }
}
