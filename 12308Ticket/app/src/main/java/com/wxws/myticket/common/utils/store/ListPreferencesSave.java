package com.wxws.myticket.common.utils.store;

import android.content.Context;

import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.BusHistory;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.train.contants.TrainContants;
import com.wxws.myticket.train.entity.TrainRealCityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储复用的集合
 * User: lixiangxiang
 * Date: 2016-04-14
 * Time: 18:53
 */
public class ListPreferencesSave {

    private static ListPreferencesSave listPreferencesSave;

    public static ListPreferencesSave getInstance() {

        if (listPreferencesSave == null) {
            synchronized (ListPreferencesSave.class){
                if (listPreferencesSave == null){
                    listPreferencesSave = new ListPreferencesSave();
                }
            }
        }

        return listPreferencesSave;
    }


    /**
     * 存储各个历史记录
     * @param context
     * @param city
     * @param origin
     */
    public void saveSingleCityHistory(Context context, CityDisplayEntity city,String origin) {

        if (StringUtils.isNullOrEmpty(city.getCity())){
            return;
        }
        List<CityDisplayEntity> history = (List<CityDisplayEntity>) PreferencesUtils.readDataObject(context, origin);

        if (history == null) {
            history = new ArrayList<>();
            history.add(city);
        } else if (history.size() < 8) {
            history  =  removeHistoryRepeatItem(history, city);
        } else {
            history  =  removeHistoryRepeatItem(history, city);
            if (history.size() > 8) {
                history.remove(history.size() - 1);
            }
        }
        PreferencesUtils.saveObject(context, PreferencesUtils.PREFERENCE_NAME_DATA, origin, history);
    }

    /**
     * 去除重复项目
     *
     * @param history
     * @param city
     */
    private List<CityDisplayEntity> removeHistoryRepeatItem(List<CityDisplayEntity> history, CityDisplayEntity city) {
        int size = history.size();

        for (int i = size; i>0 ;i--){
            if (city.getCity().equals(history.get(i-1).getCity())) {
                history.remove(history.get(i-1));
                break;
            }
        }

        history.add(0, city);

        return history;
    }

    /**
     * 历史记录
     * @param context
     * @param busHistory
     * @return
     */
    public List<BusHistory> saveTicketFragHistory(Context context ,BusHistory busHistory){

        List<BusHistory> busHistories =  (List<BusHistory>) PreferencesUtils.readDataObject(context, Constants.HISTORYCITY);

        if (busHistories == null ){
            busHistories = new ArrayList<>();
            busHistories.add(busHistory);
        }else {
            for (int i = busHistories.size()-1 ;i>=0 ; i--){
                if (busHistory.getEndCity().equals(busHistories.get(i).getEndCity())
                        && busHistory.getStartCity().equals(busHistories.get(i).getStartCity())){
                    busHistories.remove(i);
                }
            }
            if (busHistories.size()>2){
                busHistories.remove(3);
            }
            busHistories.add(0,busHistory);
        }
        PreferencesUtils.saveDataObject(context,Constants.HISTORYCITY,busHistories);
        return  busHistories;
    }
    /**
     *  5 分钟内
     * @param name
     * @param context
     * @param systemTime
     */
    public boolean setTimeFiveMinute(Context context, String name, long systemTime){

        long time =  PreferencesUtils.getLong(context,PreferencesUtils.PREFERENCE_NAME_DATA,name);

        long time1 =  (systemTime - time)/1000;

        PreferencesUtils.putLong(context,PreferencesUtils.PREFERENCE_NAME_DATA,name,systemTime);

        return time1 < 5 * 60;
    }

    /**
     * 火车票出发城市搜索记录
     * @param context
     * @param city
     */
    public void saveTrainCityHistory(Context context, TrainRealCityModel city) {

        if (StringUtils.isNullOrEmpty(city.getCity())){
            return;
        }
        List<TrainRealCityModel> history = (List<TrainRealCityModel>) PreferencesUtils.readDataObject(context, TrainContants.TRAINCITYHISTORY);

        if (history == null) {
            history = new ArrayList<>();
            history.add(city);
        } else if (history.size() < 6) {
            removeHistoryRepeatItem(history, city);
        } else {
            removeHistoryRepeatItem(history, city);
            if (history.size() > 6) {
                history.remove(history.size() - 1);
            }
        }
        PreferencesUtils.saveDataObject(context, TrainContants.TRAINCITYHISTORY, history);
    }

    /**
     * 去除重复项目
     *
     * @param history
     * @param city
     */
    private void removeHistoryRepeatItem(List<TrainRealCityModel> history, TrainRealCityModel city) {
        int size = history.size();
        int position = 0;
        boolean isRepeat = false;
        for (int i = 0; i < size; i++) {
            if (city.getCity().equals(history.get(i).getCity())) {
                position  = i;
                isRepeat = true;
            }
        }
        if (isRepeat){
            history.remove(history.get(position));
        }
        history.add(0, city);
    }
}
