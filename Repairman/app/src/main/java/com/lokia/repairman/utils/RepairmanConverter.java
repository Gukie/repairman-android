package com.lokia.repairman.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by lokia on 16-8-20.
 */
public class RepairmanConverter {

    private static ObjectMapper myObjectMapper;

    public static ObjectMapper getObjectMapper(){

        if (myObjectMapper == null){

            synchronized (RepairmanConverter.class){
                if(myObjectMapper == null){
                    myObjectMapper = new ObjectMapper();
                }
            }
        }
        return  myObjectMapper;
    }

    public static  <T> T  readValue(String content, Class<T> valueType){
        T t = null;
        if(!StringUtils.hasText(content)){
            return t;
        }
        try {
            t = getObjectMapper().readValue(content,valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    public static  <T> T  readValue(String content, TypeReference<T> valueType){
        T t = null;
        if(!StringUtils.hasText(content)){
            return t;
        }
        try {
            t = getObjectMapper().readValue(content,valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

}
