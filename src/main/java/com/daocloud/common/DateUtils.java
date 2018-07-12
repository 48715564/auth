package com.daocloud.common;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class DateUtils {
    public static Long getOffsetDay(int day){
        Date newDate = DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, day);
        return newDate.getTime()/1000;
    }
}
