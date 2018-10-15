package com.hs3.tg;
import  com.hs3.tg.Config;
import com.hs3.utils.HttpUtils;

public class TelegramUtils {

    public static void main(String[] args){
        sendMessage("Hello everyone! This is just testing message.");
    }

    public static String sendMessage(String message){
        String url = Config.URLPREFIX + Config.TOKEN + "/" + Config.SENDMESSAGE + "?chat_id=" + Config.CHATID
                + "&text=" + message;
        try {
            String result = HttpUtils.getString(url);
//            System.out.println(result);
        }
        catch (Exception e){
            e.printStackTrace();
            return "false";
        }

        return "success";
    }
}
