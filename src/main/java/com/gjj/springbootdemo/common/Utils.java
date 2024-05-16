package com.gjj.springbootdemo.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String statusDescription(Integer goodsStatus){
        switch (goodsStatus){
            case 1:
                return Constants.DOWN_GOODS;
            case 2:
                return Constants.AUDIT_GOODS;
            case 3:
                return Constants.AUDIT_Failed_GOODS;
            case 4:
                return Constants.UP_GOODS;
            case 5:
                return Constants.SOLD_OUT_GOODS;
            case 6:
                return Constants.DELETE_GOODS;
            default:
                return "";
        }
    }

    public static Integer statusNum(String goodsStatus){
        switch (goodsStatus){
            case Constants.DOWN_GOODS:
                return Constants.DOWN_GOODS_1;
            case Constants.AUDIT_GOODS:
                return Constants.AUDIT_GOODS_2;
            case Constants.AUDIT_Failed_GOODS:
                return Constants.AUDIT_Failed_GOODS_3;
            case Constants.UP_GOODS:
                return Constants.UP_GOODS_4;
            case Constants.SOLD_OUT_GOODS:
                return Constants.SOLD_OUT_GOODS_5;
            case Constants.DELETE_GOODS:
                return Constants.DELETE_GOODS_6;
            default:
                return -1;
        }
    }

    public static String toDataString(Date data){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data);
        return dateString;
    }

    public static Date formatData(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static Date getNewTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatData(formatter.format(new Date()));
    }
}
