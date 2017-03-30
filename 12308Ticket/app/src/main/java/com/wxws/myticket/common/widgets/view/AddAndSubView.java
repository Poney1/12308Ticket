package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wxws.myticket.R;
import com.wxws.myticket.common.utils.StringUtils;


/**
 *
 * @desc 自定义数字加减控件
 *
 * @author lixiangxiang
 * @date 2015/11/4 17:45
 */
@SuppressWarnings("unused")
public class AddAndSubView extends LinearLayout {
	
	private Context context;

	private OnAddChangeListener onAddNumChangeListener;
	private Button addButton;
	private Button subButton;
	private EditText editText;
	private int minNum = 0, maxNum = 4, num = 0; // editText中的数值

	public AddAndSubView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public AddAndSubView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		initView();
		initData();
		setViewListener();
	}

	private void initData() {
		setNum(num);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View  view  = inflater.inflate(R.layout.add_and_sub,this,true);
		addButton = (Button) view.findViewById(R.id.btn_add);
		subButton = (Button) view.findViewById(R.id.btn_sub);
		editText = (EditText) view.findViewById(R.id.input_num);
	}

	private void  setViewListener(){
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (num<maxNum){
					num++;
					setNum(num);
				}
				onAddNumChangeListener.onAddNumChange(num);
			}
		});

		subButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (num>minNum){
					num--;
					setNum(num);
				}
				onAddNumChangeListener.onSubNumChange(num);
			}
		});

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isNullOrEmpty(s.toString()))
					num = Integer.parseInt(s.toString());
			}
		});
	}

	/**
	 * 设置EditText中的值
	 * 
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num;
		editText.setText(editTextContent(num));
	}

	public void setMinNum(int minNum){
		this.minNum = minNum;
	}
	/**
	 * 获取editText中的值
	 * 
	 * @return
	 */
	public int getNum() {
		String number = editText.getText().toString();
		if (number != null && !TextUtils.isEmpty(number)) {
			return Integer.parseInt(number);
		} else {
			return minNum;
		}
	}

	/**
	 * 设置加号回调
	 * @param onAddNumChangeListener
     */
	public void setOnAddNumChangeListener(OnAddChangeListener onAddNumChangeListener){
		this.onAddNumChangeListener = onAddNumChangeListener;
	}


	/**
	 * 设置  edittext 内容
	 * @param num
	 * @return
     */
	public String editTextContent(int num){

		String numStr = String.format("%s张",num);

		return num+"";
	}

	public interface OnAddChangeListener {
		/**
		 * 加号点击事件
		 *
		 * @param num 输入框的数值
		 */
		 void onAddNumChange(int num);

		/**
		 * 减号点击事件
		 * @param num
         */
		void onSubNumChange(int num);
	}

	public int getMinNum() {
		return minNum;
	}
}
