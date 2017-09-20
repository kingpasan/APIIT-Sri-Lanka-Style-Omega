package com.pasansemage.pasanmadurangasemagecb006067.mHelper;

/**
 * Created by Pasan .M. Semage on 2017-09-20.
 */

public class CreditCard {

    private int CC_ID;
    private String USERNAME;
    private String CC_NUMBER;
    private String EXPIRED_DATE;
    private String CCV_CODE;

    public CreditCard(int CC_ID, String USERNAME, String CC_NUMBER, String EXPIRED_DATE, String CCV_CODE) {
        this.CC_ID = CC_ID;
        this.USERNAME = USERNAME;
        this.CC_NUMBER = CC_NUMBER;
        this.EXPIRED_DATE = EXPIRED_DATE;
        this.CCV_CODE = CCV_CODE;
    }

    public int getCC_ID() {
        return CC_ID;
    }

    public void setCC_ID(int CC_ID) {
        this.CC_ID = CC_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getCC_NUMBER() {
        return CC_NUMBER;
    }

    public void setCC_NUMBER(String CC_NUMBER) {
        this.CC_NUMBER = CC_NUMBER;
    }

    public String getEXPIRED_DATE() {
        return EXPIRED_DATE;
    }

    public void setEXPIRED_DATE(String EXPIRED_DATE) {
        this.EXPIRED_DATE = EXPIRED_DATE;
    }

    public String getCCV_CODE() {
        return CCV_CODE;
    }

    public void setCCV_CODE(String CCV_CODE) {
        this.CCV_CODE = CCV_CODE;
    }
}
