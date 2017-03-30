package com.wxws.myticket.train.rxentity;

import com.wxws.myticket.my.Entity.PassengerInfoEntity;

import java.util.List;

/**
 * 选择乘车人(多选)或取票人(单选)
 * @author ljf
 * @version 2015年5月31日 上午3:07:05
 */
public class CheckPassengerEvent {
	private boolean isSingle;
	private PassengerInfoEntity mPassenger;
	private List<PassengerInfoEntity> list;

	public CheckPassengerEvent(PassengerInfoEntity mPassenger) {
		super();
		this.isSingle = true;
		this.mPassenger = mPassenger;
	}

	public CheckPassengerEvent(List<PassengerInfoEntity> list) {
		super();
		this.isSingle = false;
		this.list = list;
	}

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public PassengerInfoEntity getmPassenger() {
		return mPassenger;
	}

	public void setmPassenger(PassengerInfoEntity mPassenger) {
		this.mPassenger = mPassenger;
	}

	public List<PassengerInfoEntity> getList() {
		return list;
	}

	public void setList(List<PassengerInfoEntity> list) {
		this.list = list;
	}

}
