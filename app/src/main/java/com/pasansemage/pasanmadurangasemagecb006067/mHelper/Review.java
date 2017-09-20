package com.pasansemage.pasanmadurangasemagecb006067.mHelper;

/**
 * Created by Pasan .M. Semage on 2017-09-20.
 */

public class Review {

    private int REVIEW_ID;
    private String REVIEW_USERNAME;
    private String REVIEW_PRODUCT_ID;
    private String REVIEW_RATE;
    private String REVIEW_REVIEW;
    private byte[] USER_IMAGE;

    public Review(int REVIEW_ID, String REVIEW_USERNAME, String REVIEW_PRODUCT_ID, String REVIEW_RATE, String REVIEW_REVIEW, byte[] USER_IMAGE) {
        this.REVIEW_ID = REVIEW_ID;
        this.REVIEW_USERNAME = REVIEW_USERNAME;
        this.REVIEW_PRODUCT_ID = REVIEW_PRODUCT_ID;
        this.REVIEW_RATE = REVIEW_RATE;
        this.REVIEW_REVIEW = REVIEW_REVIEW;
        this.USER_IMAGE = USER_IMAGE;
    }

    public int getREVIEW_ID() {
        return REVIEW_ID;
    }

    public void setREVIEW_ID(int REVIEW_ID) {
        this.REVIEW_ID = REVIEW_ID;
    }

    public String getREVIEW_USERNAME() {
        return REVIEW_USERNAME;
    }

    public void setREVIEW_USERNAME(String REVIEW_USERNAME) {
        this.REVIEW_USERNAME = REVIEW_USERNAME;
    }

    public String getREVIEW_PRODUCT_ID() {
        return REVIEW_PRODUCT_ID;
    }

    public void setREVIEW_PRODUCT_ID(String REVIEW_PRODUCT_ID) {
        this.REVIEW_PRODUCT_ID = REVIEW_PRODUCT_ID;
    }

    public String getREVIEW_RATE() {
        return REVIEW_RATE;
    }

    public void setREVIEW_RATE(String REVIEW_RATE) {
        this.REVIEW_RATE = REVIEW_RATE;
    }

    public String getREVIEW_REVIEW() {
        return REVIEW_REVIEW;
    }

    public void setREVIEW_REVIEW(String REVIEW_REVIEW) {
        this.REVIEW_REVIEW = REVIEW_REVIEW;
    }

    public byte[] getUSER_IMAGE() {
        return USER_IMAGE;
    }

    public void setUSER_IMAGE(byte[] USER_IMAGE) {
        this.USER_IMAGE = USER_IMAGE;
    }
}
