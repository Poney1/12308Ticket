package com.wxws.myticket.train.rxentity;

import com.wxws.myticket.my.Entity.PassengerInfoEntity;

/**
 * 修改或添加联系人
 * @author ljf
 * @version 2015年5月31日 上午1:53:37
 */
public class EditPassengerEvent {
	private PassengerInfoEntity passenger;
	private String newPerson;

	public String getNewPerson() {
		return newPerson;
	}

	public EditPassengerEvent(PassengerInfoEntity passenger, String newPerson) {
		super();
		this.passenger = passenger;
		this.newPerson = newPerson;
	}

	public PassengerInfoEntity getPassenger() {
		return passenger;
	}

	public void setPassenger(PassengerInfoEntity passenger) {
		this.passenger = passenger;
	}
	
}
