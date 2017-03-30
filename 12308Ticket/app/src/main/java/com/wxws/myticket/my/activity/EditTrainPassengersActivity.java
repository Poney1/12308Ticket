package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.activity.CityActivity;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.CertificateEntity;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.widgets.dialog.EditPassengersDialog;
import com.wxws.myticket.my.widgets.view.ClearEditText;
import com.wxws.myticket.train.activity.ProvinceAndSchoolActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * desc: 修改/添加火车票联系人
 * Date: 2016/11/7 16:51
 *
 * @auther: lixiangxiang
 */
public class EditTrainPassengersActivity extends BaseActivity {
    // Content View Elements

    private ClearEditText etPassenger;
    private ClearEditText etCredentialsno;
    private ClearEditText etMobielNumber;
    private Button btnSave;
    private TextView tvDesc;
    //
    private LinearLayout llBoy;
    private LinearLayout llGirl;
    private TextView tvGirl;
    private TextView tvBoy;
    //
    private TextView tvEditPassengerType;
    private TextView tvPassengerType;
    private LinearLayout llEditStrudent;
    private TextView tvProvince;
    private TextView tvSchool;
    private EditText etStuNumber;
    private TextView tvChooseYear;
    private TextView tvXuezhi;
    private TextView tvCouponnum;
    private TextView tvFromPlace;
    private TextView tvToPlace;
    private LinearLayout llSelect18;

    /**
     * 添加0;编辑1;
     */
    private int flag;
    private boolean isFromBook = false;

    private String pid;
    private String realname;
    private String cardid = "";
    private String mobile;

    private PassengerInfoEntity mPassenger;
    private List<Map<String, String>> instanceList = new ArrayList<Map<String, String>>();
    /**
     * 每份保险的价格
     */
    public int instance_price;

    private int instancenum;
    private int contractStatus;

    //省份学校的返回码
    public int schoolCode = 1;
    public int provinceCode = 0;

    private TextView tvTicketSchool;

    //提交保存资料
    private String certificate = "01";
    private String personType = "1";//旅客类型
    private String provinceId = "";//省id
    private String schoolId = "";//学校id
    private String fromStationCode = "";//优惠区间始发站 code
    private String toStationCode = "";//优惠期间 终点站code
    private String cerType;//身份证类型
    private int from = 101;//出发的请求吗
    private int to = 102;//到达的请求吗
    private CityDisplayEntity trainFromPlace;
    private CityDisplayEntity trainToPlace;
    private OptionsPickerView opXuezhi;
    private OptionsPickerView opEnterYear;
    private String flag1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_train_passengers);

        getPreData();

        initView();
        initData();
        bindClick();

    }

    private void initData() {
        if (isFromBook && contractStatus == 1 && instance_price > 0) {
        }
    }

    private void getPreData() {
        getUserInfo();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        flag = intent.getIntExtra("editType", 0);
        isFromBook = intent.getBooleanExtra("from", false);

        bindTitleViews();
        /*if (flag == PassengersActivity.MODIFY_PASSENGER) {
           *//* barTitle.setText(R.string.modify_passenger);
            mPassenger = (Passenger) intent.getSerializableExtra("PASSENGER");
            instance_price = intent.getIntExtra("Insuranceprice", 0);
            contractStatus = intent.getIntExtra("ContractStatus", 0);
            if (isFromBook) {
                for (int i = 0; i < 2; ++i) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(String.valueOf(i), String.format(getString(R.string.buy_insurance_info), i, instance_price));
                    instancelist.add(map);
                }
                mSingleChoiceDialog = new SingleChoiceDialog(this, instancelist, "请选择", new ISingleChoicAdapter() {

                    @Override
                    public void onItemClick(Dialog mDialogInterface, int position) {
                        Map<String, String> m = instancelist.get(position);

                        for (Map.Entry<String, String> entry : m.entrySet()) {
                            tvBuyInsurance.setText(entry.getValue());
                            instancenum = Integer.parseInt(entry.getKey());
                        }
                        mDialogInterface.dismiss();
                    }

                });
                mSingleChoiceDialog.setSelectItem(mPassenger.getInstancenum());*//*
            }
        } else {
//            mTvTitle.setText(R.string.add_passenger);
        }
//        mLayout_right.setVisibility(View.GONE);*/
    }

    private void bindClick() {
        llBoy.setOnClickListener(this);
        llGirl.setOnClickListener(this);
        tvEditPassengerType.setOnClickListener(this);
        llEditStrudent.setOnClickListener(this);
        tvPassengerType.setOnClickListener(this);
        tvProvince.setOnClickListener(this);
        tvSchool.setOnClickListener(this);
        tvChooseYear.setOnClickListener(this);
        tvXuezhi.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvFromPlace.setOnClickListener(this);
        tvToPlace.setOnClickListener(this);
    }

    private void initView() {
        etPassenger = (ClearEditText) findViewById(R.id.et_passenger_name);//填写姓名
        etCredentialsno = (ClearEditText) findViewById(R.id.et_passenger_cert_number);//填写身份证号
        etMobielNumber = (ClearEditText) findViewById(R.id.et_passenger_mobile_phone);//填写手机号码
        btnSave = (Button) findViewById(R.id.btn_passenger_save);
        tvDesc = (TextView) findViewById(R.id.tv_passenger_desc);

        //TODO new textview
        tvPassengerType = (TextView) findViewById(R.id.tv_edit_passenger_type);
        tvEditPassengerType = (TextView) findViewById(R.id.tv_edit_passenger_passenger_type);
        llEditStrudent = (LinearLayout) findViewById(R.id.ll_editpassengers_student);  //选择学生票
        tvTicketSchool = (TextView) findViewById(R.id.tv_ticket_school);  //是否选择学生票
        //选择省
        tvProvince = (TextView) findViewById(R.id.tv_select_province);
        tvSchool = (TextView) findViewById(R.id.tv_choose_school);
        etStuNumber = (EditText) findViewById(R.id.et_input_stu_num);
        //入学年份
        tvChooseYear = (TextView) findViewById(R.id.tv_choose_year);
        //选择学制
        tvXuezhi = (TextView) findViewById(R.id.tv_choose_xuezhi);
        //输入优惠码
        tvCouponnum = (TextView) findViewById(R.id.et_input_coupon_num);
        //出发优惠点
        tvFromPlace = (TextView) findViewById(R.id.tv_from_place);
        //到达优惠点
        tvToPlace = (TextView) findViewById(R.id.tv_to_place);
        //性别选择
        llBoy = (LinearLayout) findViewById(R.id.ll_boy);
        llGirl = (LinearLayout) findViewById(R.id.ll_girl);
        tvGirl = (TextView) findViewById(R.id.tv_girl);
        tvBoy = (TextView) findViewById(R.id.tv_boy);

        //popwindow  漂浮
        llSelect18 = (LinearLayout) findViewById(R.id.ll_select_18);

        selectSex(true);
        if (mPassenger != null) {
            flag1 = "newPerson";
            realname = mPassenger.getPassengerName();
            cardid = mPassenger.getCertificateNo();
            mobile = mPassenger.getMobilePhone();
            pid = String.valueOf(mPassenger.getPassengerId());
            etPassenger.setText(mPassenger.getPassengerName());
            etCredentialsno.setText(mPassenger.getCertificateNo());
            etMobielNumber.setText(mPassenger.getMobilePhone());

            if (2 == mPassenger.getGender()) {
                selectSex(false);
            }
            if ("03".equals(mPassenger.getCertificateType())) {
                tvPassengerType.setText("护照");
                certificate = "03";
            }
            if (3 == mPassenger.getPersonType()) {
//                tvProvince.setText(mPassenger.getStudent().get());
                tvSchool.setText(mPassenger.getStudent().getSchoolName());
                etStuNumber.setText(mPassenger.getStudent().getStudentNo());
                tvChooseYear.setText(mPassenger.getStudent().getEnterYear());
                tvXuezhi.setText(String.format("%s年", mPassenger.getStudent().getSchoolSystem()));
                tvCouponnum.setText(mPassenger.getStudent().getPreferenceCardNo());
                tvFromPlace.setText(mPassenger.getStudent().getPreferenceFromStationName());
                tvToPlace.setText(mPassenger.getStudent().getPreferenceToStationName());
                fromStationCode = mPassenger.getStudent().getPreferenceFromStationCode();
                toStationCode = mPassenger.getStudent().getPreferenceToStationCode();
                provinceId = mPassenger.getStudent().getProvinceCode();
                schoolId = mPassenger.getStudent().getSchoolCode();
                tvEditPassengerType.setText(getString(R.string.edit_train_students));
                llEditStrudent.setVisibility(View.VISIBLE);
                tvTicketSchool.setVisibility(View.VISIBLE);
            } else if (2 == mPassenger.getPersonType()) {
                tvEditPassengerType.setText(getString(R.string.edit_train_child));
            }
        }

    }

    //初始化 身份证件
    private void initCerNoPopupWindowData() {
        List<CertificateEntity> listPassenger = new ArrayList<>();
        CertificateEntity certificateModel = new CertificateEntity();
        certificateModel.setPassengerType("身份证");
        certificateModel.setPassengerNo("01");
        listPassenger.add(certificateModel);
        EditPassengersDialog editPassengersDialog = new EditPassengersDialog(EditTrainPassengersActivity.this, listPassenger);
        editPassengersDialog.show();
        editPassengersDialog.setOnPassengerListener(new EditPassengersDialog.OnSelectPassengerListener() {
            @Override
            public void setOnSelectPassengerListener(CertificateEntity passengerType) {
                tvPassengerType.setText(passengerType.getPassengerType());
                certificate = passengerType.getPassengerNo();
                cerType = passengerType.getPassengerType();
            }
        });
    }

    //初始化popupWindowData
    private void initPopupWindowData() {
        List<CertificateEntity> listPassenger = new ArrayList<>();
        CertificateEntity certificateModel = new CertificateEntity();
        certificateModel.setPassengerType(getString(R.string.edit_train_person));
        certificateModel.setPassengerNo("1");
        listPassenger.add(certificateModel);
        CertificateEntity certificateModel3 = new CertificateEntity();
        certificateModel3.setPassengerType(getString(R.string.edit_train_students));
        certificateModel3.setPassengerNo("3");
        listPassenger.add(certificateModel3);
        CertificateEntity certificateModel2 = new CertificateEntity();
        certificateModel2.setPassengerType(getString(R.string.edit_train_child));
        certificateModel2.setPassengerNo("2");
        listPassenger.add(certificateModel2);

        EditPassengersDialog editPassengersDialog = new EditPassengersDialog(EditTrainPassengersActivity.this, listPassenger);
        editPassengersDialog.show();
        editPassengersDialog.setOnPassengerListener(new EditPassengersDialog.OnSelectPassengerListener() {
            @Override
            public void setOnSelectPassengerListener(CertificateEntity passengerType) {
                tvEditPassengerType.setText(passengerType.getPassengerType());
                personType = passengerType.getPassengerNo();
                if (getString(R.string.edit_train_students).equals(passengerType.getPassengerType())) {
                    llEditStrudent.setVisibility(View.VISIBLE);
                    tvTicketSchool.setVisibility(View.VISIBLE);
                } else {
                    llEditStrudent.setVisibility(View.GONE);
                    tvTicketSchool.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_passenger_save:
                validPost();
                break;
            case R.id.ll_boy:
                selectSex(true);
                break;
            case R.id.ll_girl:
                selectSex(false);
                break;
            case R.id.tv_edit_passenger_passenger_type:
                initPopupWindowData();
                break;
            case R.id.ll_editpassengers_student:

                break;
            case R.id.tv_choose_xuezhi:
                setXuezhi();
                break;
            case R.id.tv_choose_year:
                selectEnterYear();
                break;
            case R.id.tv_edit_passenger_type:
                initCerNoPopupWindowData();
                break;
            case R.id.tv_select_province:
                intent = new Intent(EditTrainPassengersActivity.this, ProvinceAndSchoolActivity.class);
                intent.putExtra("select", 0);
                startActivityForResultWithAnim(intent, provinceCode);
                break;
            case R.id.tv_choose_school:
                if (StringUtils.isNullOrEmpty(provinceId)) {
                    ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_select_province));
                    return;
                }
                intent = new Intent(EditTrainPassengersActivity.this, ProvinceAndSchoolActivity.class);
                intent.putExtra("select", 1);
                intent.putExtra("code", "1");
                intent.putExtra("province", provinceId);
                startActivityForResultWithAnim(intent, schoolCode);
                break;
            case R.id.tv_from_place:
                startActivityForResultWithAnim(new Intent(EditTrainPassengersActivity.this, CityActivity.class), from);
                break;
            case R.id.tv_to_place:
                startActivityForResultWithAnim(new Intent(EditTrainPassengersActivity.this, CityActivity.class), to);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            CityDisplayEntity trainRealCityModel = (CityDisplayEntity) data.getSerializableExtra("train");
            if (requestCode == schoolCode) {
                tvSchool.setText(trainRealCityModel.getCity());
                schoolId = trainRealCityModel.getCityId();
            } else if (requestCode == provinceCode) {
                tvProvince.setText(trainRealCityModel.getCity());
                if (!StringUtils.isNullOrEmpty(tvSchool.getText().toString())) {
                    tvSchool.setText("");
                    tvSchool.setHint(getString(R.string.edit_select_school));
                }
                provinceId = trainRealCityModel.getCityId();
            }
        } else if (resultCode == 101) {
            if (requestCode == 101) {
                trainFromPlace = (CityDisplayEntity) data.getSerializableExtra("coupon");
                tvFromPlace.setText(trainFromPlace.getCity());
                fromStationCode = trainFromPlace.getCityId();
            } else if (requestCode == 102) {
                trainToPlace = (CityDisplayEntity) data.getSerializableExtra("coupon");
                toStationCode = trainToPlace.getCityId();
                tvToPlace.setText(trainToPlace.getCity());
            }
        } else if (resultCode == 102) {
            if (requestCode == 101) {
                trainFromPlace = (CityDisplayEntity) data.getSerializableExtra("coupon");
                fromStationCode = trainFromPlace.getCityId();
                tvFromPlace.setText(trainFromPlace.getCity());
            } else if (requestCode == 102) {
                trainToPlace = (CityDisplayEntity) data.getSerializableExtra("coupon");
                tvToPlace.setText(trainToPlace.getCity());
                toStationCode = trainToPlace.getCityId();
            }
        }

    }

    //选择性别
    private void selectSex(boolean isSelect) {
        tvBoy.setSelected(isSelect);
        tvGirl.setSelected(!isSelect);
    }

    /**
     * 验证用户提交信息
     *
     * @return
     */
    public void validPost() {
        realname = etPassenger.getText().toString();
        cardid = etCredentialsno.getText().toString();
        mobile = etMobielNumber.getText().toString();

        String passerId = "";
        if (mPassenger != null) {
            passerId = mPassenger.getPassengerId();
        }
        String isStudent = tvEditPassengerType.getText().toString();
        String province = "";
        String schoolName = "";
        String stuNumber = "";
        String chooseYear = "";
        String xueZhi1 = "";
        String couponNum = "";
        String fromPlace = "";
        String toPlace = "";

        if (!StringUtils.isHaveChinaEnglish(realname)) {
            ToastManager.getInstance().showToast(this, R.string.passenger_name_required);
            return;
        }
        if (StringUtils.isNullOrEmpty(cardid)) {
            ToastManager.getInstance().showToast(this, R.string.passenger_credentials_no);
            return;
        }
        if (!StringUtils.isPhone(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.passenger_number_required);
            return;
        }

        //学生票
        if (getString(R.string.edit_train_students).equals(isStudent)) {
            province = tvProvince.getText().toString();
            schoolName = tvSchool.getText().toString();
            stuNumber = etStuNumber.getText().toString();
            chooseYear = tvChooseYear.getText().toString();
            xueZhi1 = tvXuezhi.getText().toString();
            xueZhi1 = xueZhi1.substring(0, xueZhi1.length() - 1);
            couponNum = tvCouponnum.getText().toString();
            fromPlace = tvFromPlace.getText().toString();
            toPlace = tvToPlace.getText().toString();
            personType = "3";
            if (StringUtils.isNullOrEmpty(province)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_province));
                return;
            }

            if (StringUtils.isNullOrEmpty(schoolName)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_school));
                return;
            }
            if (StringUtils.isNullOrEmpty(stuNumber)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_student_num));
                return;
            }
            if (StringUtils.isNullOrEmpty(chooseYear)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_select_year));
                return;
            }
            if (StringUtils.isNullOrEmpty(xueZhi1)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_xuezhi));
                return;
            }

            if (StringUtils.isNullOrEmpty(couponNum)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_input_coupon));
                return;
            }

            if (StringUtils.isNullOrEmpty(fromPlace) || StringUtils.isNullOrEmpty(toPlace)) {
                ToastManager.getInstance().showToast(EditTrainPassengersActivity.this, getString(R.string.edit_select_area));
                return;
            }
        }

        int gender = tvBoy.isSelected() ? 1 : 2;

        //添加常用联系人
        HttpRequestManager.getInstance().addContactList(realname, mobile, cardid, "10", gender + "", certificate, personType, provinceId, province, schoolName, schoolId, xueZhi1, chooseYear, stuNumber, couponNum
                , fromPlace, fromStationCode, toPlace, toStationCode, new ApiSubscriber<JsonResult<PassengerInfoEntity>>(EditTrainPassengersActivity.this) {
                    @Override
                    public void onNext(JsonResult<PassengerInfoEntity> stringJsonResult) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //学制选择
    private void setXuezhi() {
        opXuezhi = new OptionsPickerView(this);
        final TextView tvPickerView1 = (TextView) opXuezhi.findViewById(R.id.tv_pickerView1);
        final ArrayList<String> arrayXuezhi = new ArrayList<>();
        tvPickerView1.setVisibility(View.VISIBLE);
        arrayXuezhi.add(getString(R.string.edit_xuezhi_3));
        arrayXuezhi.add(getString(R.string.edit_xuezhi_4));
        arrayXuezhi.add(getString(R.string.edit_xuezhi_5));
        arrayXuezhi.add(getString(R.string.edit_xuezhi_6));
        arrayXuezhi.add(getString(R.string.edit_xuezhi_7));
        arrayXuezhi.add(getString(R.string.edit_xuezhi_8));
        arrayXuezhi.add(getString(R.string.edit_xuezhi_9));
        opXuezhi.setPicker(arrayXuezhi);
        opXuezhi.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                tvXuezhi.setText(arrayXuezhi.get(i));
            }
        });
        opXuezhi.show();
    }

    //选择入学年份
    private void selectEnterYear() {

        opEnterYear = new OptionsPickerView(this);
        final TextView tvPickerView1 = (TextView) opEnterYear.findViewById(R.id.tv_pickerView1);
        final ArrayList<String> arrayEnterYear = new ArrayList<>();
        tvPickerView1.setVisibility(View.VISIBLE);
        String year = DateTimeUtil.getSysDateY();
        int yearCalc = Integer.parseInt(year);
        for (int i = yearCalc - 9; i < yearCalc + 1; i++) {
            arrayEnterYear.add(String.valueOf(i));
        }
        opEnterYear.setPicker(arrayEnterYear);
        opEnterYear.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                tvChooseYear.setText(arrayEnterYear.get(i));
            }
        });
        opEnterYear.show();
    }

    @Override
    public void onBackPressed() {
        if (opXuezhi != null && opXuezhi.isShowing()) {
            opXuezhi.dismiss();
        } else if (opEnterYear != null && opEnterYear.isShowing()) {
            opEnterYear.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
