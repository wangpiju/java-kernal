package com.hs3.service.lotts;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hs3.dao.lotts.LotterySeasonWeightDao;
import com.hs3.entity.lotts.LotterySeasonWeight;

@Service("lotterySeasonWeightService")
public class LotterySeasonWeightService {
    @Autowired
    private LotterySeasonWeightDao lotterySeasonWeightDao;

    public boolean timeout(Date getLotTime, Date closeTime) {
        if (getLotTime.getTime() > closeTime.getTime()) {
            return true;
        }
        return false;
    }

    public void saveSeasonWeight(LotterySeasonWeight lotterySeasonWeight) {
        this.lotterySeasonWeightDao.save(lotterySeasonWeight);
    }

    public int SeasonWeightCount(String lotteryId, String seasonId, int allWeight) {
        /**jd-gui
         List<LotterySeasonWeight> lotterySeasonWeights = this.lotterySeasonWeightDao.list(lotteryId, seasonId);
         HashMap<String, Integer> weightList = new HashMap();
         for (LotterySeasonWeight lotterySeasonWeight : lotterySeasonWeights)
         {
         String openNum = lotterySeasonWeight.getNums();
         int weight = lotterySeasonWeight.getWeight().intValue();
         if (weightList.containsKey(openNum))
         {
         Integer oldWeight = (Integer)weightList.get(openNum);
         weightList.put(openNum, Integer.valueOf(oldWeight.intValue() + weight));
         }
         else
         {
         weightList.put(openNum, Integer.valueOf(weight));
         }
         }
         Iterator<Map.Entry<String, Integer>> it = weightList.entrySet().iterator();
         while (it.hasNext())
         {
         Object entry = (Map.Entry)it.next();
         if (((Integer)((Map.Entry)entry).getValue()).intValue() >= allWeight) {
         return 2;
         }
         }
         return 3;*/

        List<LotterySeasonWeight> lotterySeasonWeights = lotterySeasonWeightDao.list(lotteryId, seasonId);
        HashMap<String, Integer> weightList = new HashMap();
        for (Iterator iterator = lotterySeasonWeights.iterator(); iterator.hasNext(); ) {
            LotterySeasonWeight lotterySeasonWeight = (LotterySeasonWeight) iterator.next();
            String openNum = lotterySeasonWeight.getNums();
            int weight = lotterySeasonWeight.getWeight().intValue();
            if (weightList.containsKey(openNum)) {
                Integer oldWeight = (Integer) weightList.get(openNum);
                weightList.put(openNum, Integer.valueOf(oldWeight.intValue() + weight));
            } else {
                weightList.put(openNum, Integer.valueOf(weight));
            }
        }

        for (Iterator it = weightList.entrySet().iterator(); it.hasNext(); ) {
            Entry entry = (Entry) it.next();
            if (((Integer) entry.getValue()).intValue() >= allWeight)
                return 2;
        }

        return 3;

    }

    public int saveProcessWeight(LotterySeasonWeight sea, int weight) {
        if (this.lotterySeasonWeightDao.getObject(sea.getLotteryId(), sea.getSeasonId(), sea.getWeightType()) == null) {
            saveSeasonWeight(sea);
        }
        return SeasonWeightCount(sea.getLotteryId(), sea.getSeasonId(), weight);
    }

    public int saveProcessWeightByAuto(LotterySeasonWeight lotterySeasonWeight, int weight) {
        if (this.lotterySeasonWeightDao.getObject(lotterySeasonWeight.getLotteryId(), lotterySeasonWeight.getSeasonId(),
                lotterySeasonWeight.getWeightType()) == null) {
            saveSeasonWeight(lotterySeasonWeight);
        }
        return SeasonWeightCount(lotterySeasonWeight.getLotteryId(), lotterySeasonWeight.getSeasonId(), weight);
    }
}
