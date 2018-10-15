package com.hs3.service.article;

import com.hs3.models.winNews.WinNewsModel;
import com.hs3.utils.NumUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service("winGoodNewService")
public class WinGoodNewService {
    public List<WinNewsModel> getWinList() {
        List<WinNewsModel> list = new ArrayList();
        String str = "abcdefghijklmnopqrstuvwxyz";
        String str1 = "abcdefghijklmnopqrstuvwxyz0123456789";

        String[] lottery = {"重庆时时彩", "北京赛车", "广东11选5", "山东11选5", "鼎汇一分彩", "鼎汇三分彩", "鼎汇五分彩", "天津时时彩", "新疆时时彩", "江西11选5", "一分11选5", "三分11选5", "五分11选5"};


        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            WinNewsModel sb = new WinNewsModel();
            int firstNumber = random.nextInt(26);
            char first = str.charAt(firstNumber);
            int secondNumber = random.nextInt(26);
            char second = str.charAt(secondNumber);
            int lastNumber = random.nextInt(36);
            sb.setAccount(String.valueOf(first) + String.valueOf(second) + "****" + str1.charAt(lastNumber));
            int lotteryNumber = random.nextInt(lottery.length);
            sb.setLotteryName(lottery[lotteryNumber]);

            int s = 0;
            if (i < 5) {
                s = NumUtils.getRandom(random, 1000, 10000);
            } else if (i < 15) {
                s = NumUtils.getRandom(random, 10000, 50000);
            } else {
                s = NumUtils.getRandom(random, 50000, 100000);
            }
            sb.setWinAmount(new BigDecimal(s));
            list.add(sb);
        }
        return list;
    }
}
