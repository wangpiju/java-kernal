package com.hs3.tg;

import com.hs3.utils.HttpUtils;

public class Config {
    //token
    public static final String TOKEN = "587005441:AAHb4QhR9XRgxZNRjaMLON_OPRGySlGkFKE";
    public static final String GETME = "getMe";
    public static final String SENDMESSAGE = "sendMessage";
    public static final String URLPREFIX = "https://api.telegram.org/bot";
    public static final String CHATID = "-184457422";

    public static void main (String[] args){
        String url = URLPREFIX + TOKEN + "/" + GETME;

        try {
            String result = HttpUtils.getString(url);
            System.out.println(result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
