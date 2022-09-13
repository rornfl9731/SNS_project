package com.jinwoo.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


/*
반환 규칙을 일관되게 만들기 위해
만든 클래스
 */
@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static Response<Void> error(String errorCode){
        return new Response<Void>(errorCode,null);
    }

    public static Response<Void> success(){
        return new Response<Void>("SUCCESS",null);
    }

    public static <T> Response<T> success(T result){
        return new Response<T>("SUCCESS",result);
    }

    public String toStream() {
        if(result==null){
            return "{" +
                    "\"resultCode\":"+"\""+resultCode + "\","+
                    "\"result\":" + null + "}";
        }
        return "{" +
                "\"resultCode\":"+"\""+resultCode + "\","+
                "\"result\":" + "\"" +  result + "\"" + "}";

    }
}
