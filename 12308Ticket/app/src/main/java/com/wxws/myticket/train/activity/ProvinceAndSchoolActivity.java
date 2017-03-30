package com.wxws.myticket.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.MyLetterView;
import com.wxws.myticket.my.activity.EditPassengersActivity;
import com.wxws.myticket.train.adapter.ProvinceAdapter;
import com.wxws.myticket.train.contants.TrainContants;
import com.wxws.myticket.train.entity.ProvinceModel;
import com.wxws.myticket.train.entity.SchoolModel;
import com.wxws.myticket.train.entity.TrainRealCityModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * desc: 省份和学校
 * Date: 2016/11/8 11:30
 *
 * @auther: lixiangxiang
 */
public class ProvinceAndSchoolActivity extends BaseActivity {

    private MyLetterView mlLetter;
    private ListView lsSchool;
    private EditText etSearch;
    private TextView tvCancel;
    private InnerHandler  innerHandler;
    private ListView lsSearchSchool;
    private LinearLayout llFirst;
    private TextView tvNoKey;

    private int isSelect;
    private String provinceId;//省份id
    private List<TrainRealCityModel> isShowList;//展示的数据
    private ProvinceAdapter provinceAdapter;
    private String code;
    private List<SchoolModel> listSchool;//学校
    private List<ProvinceModel> listProvince;
    private ProvinceAdapter shaiXuanAdapter;
    private List<TrainRealCityModel> showShaixuan;//搜索后显示的shuju
    private FrameLayout layoutLeft;
    private ImageButton imgBtnDel;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_school);

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {

        mlLetter.setOnTouchingLetterChangedListener(new MyLetterView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                ToastManager.getInstance().showToast(ProvinceAndSchoolActivity.this, s);
                int select = alphaIndex(s,isShowList);
                lsSchool.setSelection(select);
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (View.VISIBLE == mLlLayoutLoading.getVisibility()){
                    return;
                }
                if (!StringUtils.isNullOrEmpty(s.toString())){
                    imgBtnDel.setVisibility(View.VISIBLE);
                    showShaixuan = getShaixuanData(s.toString());
                    if (showShaixuan!=null && showShaixuan.size()>0){
                        shaiXuanAdapter.replaceAll(showShaixuan);
                        shouldShowWhatView(1);
                    }else {
                        shouldShowWhatView(2);
                        tvNoKey.setText(String.format("未找到与%s相关的城市", etSearch.getText()));
                    }
                }else {
                    imgBtnDel.setVisibility(View.GONE);
                    shouldShowWhatView(2);
                    tvNoKey.setText("请输入关键字");
                }
            }
        });

        lsSearchSchool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (showShaixuan!=null && showShaixuan.size()>0 ){
                    sendData(100,showShaixuan.get(position));
                    InputMethod.closeInputMethod(ProvinceAndSchoolActivity.this);
                }

            }
        });

        lsSchool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShowList!=null && isShowList.size()>0 & !isShowList.get(position).isGroup()) {
                    sendData(100, isShowList.get(position));
                    InputMethod.closeInputMethod(ProvinceAndSchoolActivity.this);
                }
            }
        });

        layoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethod.closeInputMethod(ProvinceAndSchoolActivity.this);
                etSearch.setText("");
                shouldShowWhatView(0);
            }
        });
    }

    //回传数据
    private void sendData(int code,TrainRealCityModel trainRealCityModel){
        Intent intent = new Intent(ProvinceAndSchoolActivity.this, EditPassengersActivity.class);
        intent.putExtra("train",trainRealCityModel);
        setResult(code,intent);
        finish();
    }
    private List<TrainRealCityModel> getShaixuanData(String s) {
        List<TrainRealCityModel>  listShaixuan = new ArrayList<>();
        if (1 == isSelect){
            for (int i=0;i<listSchool.size();i++){
                if (listSchool.get(i).getSchoolName().contains(s) || listSchool.get(i).getPinyin().contains(s)){
                    TrainRealCityModel trainRealCityModel = new TrainRealCityModel();
                    trainRealCityModel.setGroup(false);
                    trainRealCityModel.setCity(listSchool.get(i).getSchoolName());
                    trainRealCityModel.setCode(listSchool.get(i).getSchoolCode());
                    listShaixuan.add(trainRealCityModel);
                }
            }
        }else {
            for (int i=0;i<listProvince.size();i++){
                if (listProvince.get(i).getPinyin().contains(s) ||listProvince.get(i).getName().contains(s)){
                    TrainRealCityModel trainRealCityModel = new TrainRealCityModel();
                    trainRealCityModel.setGroup(false);
                    trainRealCityModel.setCity(listProvince.get(i).getName());
                    trainRealCityModel.setCode(listProvince.get(i).getId());
                    listShaixuan.add(trainRealCityModel);
                }
            }
        }
        return listShaixuan;
    }

    /**
     * 需要跳转的条目
     * @param s
     * @return
     */
    private int alphaIndex(String s, List<TrainRealCityModel> list) {
        int i = 0;
        while (i < list.size()) {
            if (list.get(i).isGroup() && list.get(i).getCity().equals(s)) {
                break;
            }
            ++i;
        }
        return i;
    }

    private void initData() {
        innerHandler = new InnerHandler(ProvinceAndSchoolActivity.this);
        shouldShowWhatView(0);

        //默认显示数据
        provinceAdapter = new ProvinceAdapter(ProvinceAndSchoolActivity.this,isShowList);
        lsSchool.setAdapter(provinceAdapter);

        //显示的数据
        shaiXuanAdapter = new ProvinceAdapter(ProvinceAndSchoolActivity.this,showShaixuan);
        lsSearchSchool.setAdapter(shaiXuanAdapter);

        isSelect = getIntent().getIntExtra("select",1);
        code = getIntent().getStringExtra("code");
        provinceId = getIntent().getStringExtra("province");
        if (1 == isSelect){
            getSchoolData();
            etSearch.setHint(getString(R.string.edit_search_school));
        }else {
            getProvinceData();
            etSearch.setHint(getString(R.string.edit_search_province));
        }
    }

    private void initView() {
        etSearch = (EditText) findViewById(R.id.et_search);
        lsSchool = (ListView) findViewById(R.id.ls_school);
        lsSearchSchool = (ListView) findViewById(R.id.ls_search_school);
        mlLetter = (MyLetterView) findViewById(R.id.ml_letter);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvNoKey = (TextView) findViewById(R.id.tv_no_key);
        llFirst = (LinearLayout) findViewById(R.id.ll_first);
        layoutLeft = (FrameLayout) findViewById(R.id.layout_left);
        imgBtnDel = (ImageButton) findViewById(R.id.imgBtnDel);
    }

    //获取学校列表
    private void getSchoolData(){

        HttpRequestManager.getInstance().getSchoolList(provinceId, "0", new ApiSubscriber<JsonResult<List<SchoolModel>>>(ProvinceAndSchoolActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<List<SchoolModel>> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())){
                    listSchool = listJsonResult.getObject();
                    if (listSchool !=null && listSchool.size()>0){
                        getSchoolList();
                    }
                }else {
                    ToastManager.getInstance().showToast(ProvinceAndSchoolActivity.this,listJsonResult.getResultMsg());
                }
            }
        });
    }

    //获取省份列表
    private void getProvinceData() {
        listProvince = (List<ProvinceModel>) PreferencesUtils.readDataObject(ProvinceAndSchoolActivity.this, TrainContants.TRAINPROVINCE);
        if (isShowList == null || isShowList.size() < 1) {
            HttpRequestManager.getInstance().getProvinceList(new ApiSubscriber<JsonResult<List<ProvinceModel>>>(ProvinceAndSchoolActivity.this,ApiSubscriber.NETVIEWLOADING) {
                @Override
                public void onNext(JsonResult<List<ProvinceModel>> listJsonResult) {
                    if ("0000".equals(listJsonResult.getResultCode())) {
                        listProvince = listJsonResult.getObject();
                        if (listProvince != null && listProvince.size() > 0) {
                            getAllList();
                            PreferencesUtils.saveDataObject(ProvinceAndSchoolActivity.this, TrainContants.TRAINPROVINCE, listProvince);
                        }
                    }else {
                        ToastManager.getInstance().showToast(ProvinceAndSchoolActivity.this,listJsonResult.getResultMsg());
                    }
                }
                });
        }else {
            getAllList();
        }
    }

    //获取所有的集合数据
    private void getSchoolList(){
        ArrayList<String> firstArray = new ArrayList<>();
        List<TrainRealCityModel>  schoolList = new ArrayList<>();
        for (int i=0;i<listSchool.size();i++){
            String fir = listSchool.get(i).getFirstChar().toUpperCase();
            if (!firstArray.contains(fir)){
                firstArray.add(fir);
            }
        }
        Collections.sort(firstArray);
        for (int i = 0;i<firstArray.size();i++){
            Iterator<SchoolModel> iterator =  listSchool.iterator();
            if (!StringUtils.isNullOrEmpty(firstArray.get(i))) {
                TrainRealCityModel tHeader = new TrainRealCityModel();
                tHeader.setGroup(true);
                tHeader.setCity(firstArray.get(i).toUpperCase());
                schoolList.add(tHeader);
                while (iterator.hasNext()) {
                    SchoolModel trainStartCityModel = iterator.next();
                    if (trainStartCityModel.getFirstChar().contains(firstArray.get(i).toLowerCase())) {
                        TrainRealCityModel tCity = new TrainRealCityModel();
                        tCity.setGroup(false);
                        tCity.setCity(trainStartCityModel.getSchoolName());
                        tCity.setCode(trainStartCityModel.getSchoolCode());
                        schoolList.add(tCity);
                    }
                }
            }
        }
        //刷新 letter
        MyLetterView.b = firstArray.toArray(new String[]{});
        mlLetter.postInvalidate();
        isShowList = schoolList;
        innerHandler.sendEmptyMessage(1);
    }


    //获取所有的集合数据
    private void getAllList(){
        ArrayList<String> firstArray = new ArrayList<>();
        List<TrainRealCityModel>  provinceList = new ArrayList<>();

        for (int i=0;i<listProvince.size();i++){
            String fir = listProvince.get(i).getFirstChar().toUpperCase();
            if (!firstArray.contains(fir)){
                firstArray.add(fir.toUpperCase());
            }
        }
        Collections.sort(firstArray);

        for (int i = 0;i<firstArray.size();i++){
            Iterator<ProvinceModel> iterator =  listProvince.iterator();
            if (!StringUtils.isNullOrEmpty(firstArray.get(i))) {
                TrainRealCityModel tHeader = new TrainRealCityModel();
                tHeader.setGroup(true);
                tHeader.setCity(firstArray.get(i).toUpperCase());
                provinceList.add(tHeader);
                while (iterator.hasNext()) {
                    ProvinceModel trainStartCityModel = iterator.next();
                    if (trainStartCityModel.getFirstChar().equals(firstArray.get(i).toLowerCase())) {
                        TrainRealCityModel tCity = new TrainRealCityModel();
                        tCity.setGroup(false);
                        tCity.setCity(trainStartCityModel.getName());
                        tCity.setCode(trainStartCityModel.getId());
                        provinceList.add(tCity);
                    }
                }
            }
        }
        //刷新 letter
        MyLetterView.b = firstArray.toArray(new String[]{});
        mlLetter.postInvalidate();
        isShowList = provinceList;
        innerHandler.sendEmptyMessage(0);
    }


    /**
     * 应该展示那个view
     * @param i 0 展示 正常的学校，省份 1 展示搜索后的学校和省份  2 没有搜索到结果的展示
     */
    private void shouldShowWhatView(int i){

        switch (i){
            case 0:
                llFirst.setVisibility(View.VISIBLE);
                lsSearchSchool.setVisibility(View.GONE);
                tvNoKey.setVisibility(View.GONE);
                break;
            case 1:
                llFirst.setVisibility(View.GONE);
                lsSearchSchool.setVisibility(View.VISIBLE);
                tvNoKey.setVisibility(View.GONE);
                break;
            case 2:
                llFirst.setVisibility(View.GONE);
                lsSearchSchool.setVisibility(View.GONE);
                tvNoKey.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    static class InnerHandler  extends Handler {

        private WeakReference<ProvinceAndSchoolActivity> weakReference;

        public InnerHandler(ProvinceAndSchoolActivity provinceAndSchoolActivity){
            this.weakReference = new WeakReference<>(provinceAndSchoolActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ProvinceAndSchoolActivity pro =  weakReference.get();
            if (pro == null){
                return;
            }
            switch (msg.what){
                case 0:
                    pro.provinceAdapter.replaceAll(pro.isShowList);
                    break;
                case 1:
                    pro.provinceAdapter.replaceAll(pro.isShowList);
                    break;
            }
        }
    }

}
