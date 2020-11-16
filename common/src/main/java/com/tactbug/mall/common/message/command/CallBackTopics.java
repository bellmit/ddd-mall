package com.tactbug.mall.common.message.command;

import lombok.Data;

@Data
public class CallBackTopics {
    private String name;
    private String key;

    public static CallBackTopics create(String name, String key){
        CallBackTopics callBackTopics = new CallBackTopics();
        callBackTopics.setName(name);
        callBackTopics.setKey(key);
        return callBackTopics;
    }
}
