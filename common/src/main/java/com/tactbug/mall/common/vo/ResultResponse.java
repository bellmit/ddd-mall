package com.tactbug.mall.common.vo;


import com.tactbug.mall.common.base.BaseInfo;
import com.tactbug.mall.common.enums.CommonResultEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultResponse implements BaseInfo {

    private String code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public ResultResponse(BaseInfo baseInfo){
        this.code = baseInfo.code();
        this.message = baseInfo.message();
    }

    public static ResultResponse ok(){
        return new ResultResponse(CommonResultEnum.OK);
    }

    public static ResultResponse error(){
        return new ResultResponse(CommonResultEnum.SYSTEM_ERROR);
    }

    public static ResultResponse error(BaseInfo baseInfo){
        return new ResultResponse(baseInfo);
    }

    public ResultResponse data(String key, Object value){
        data.put(key, value);
        return this;
    }

    public ResultResponse data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
