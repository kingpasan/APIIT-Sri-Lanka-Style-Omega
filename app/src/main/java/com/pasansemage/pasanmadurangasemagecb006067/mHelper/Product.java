package com.pasansemage.pasanmadurangasemagecb006067.mHelper;

/**
 * Created by Pasan .M. Semage on 2017-09-19.
 */

public class Product {

    private int PRODUCT_ID;
    private String PRODUCT_TITLE;
    private String PRODUCT_CATEGORY;
    private String PRODUCT_DESCRIPTION;
    private String PRODUCT_PRICE;
    private byte[] PRODUCT_IMAGE;

    public Product(int PRODUCT_ID, String PRODUCT_TITLE, String PRODUCT_CATEGORY, String PRODUCT_DESCRIPTION, String PRODUCT_PRICE, byte[] PRODUCT_IMAGE) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.PRODUCT_TITLE = PRODUCT_TITLE;
        this.PRODUCT_CATEGORY = PRODUCT_CATEGORY;
        this.PRODUCT_DESCRIPTION = PRODUCT_DESCRIPTION;
        this.PRODUCT_PRICE = PRODUCT_PRICE;
        this.PRODUCT_IMAGE = PRODUCT_IMAGE;
    }

    public int getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(int PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getPRODUCT_TITLE() {
        return PRODUCT_TITLE;
    }

    public void setPRODUCT_TITLE(String PRODUCT_TITLE) {
        this.PRODUCT_TITLE = PRODUCT_TITLE;
    }

    public String getPRODUCT_CATEGORY() {
        return PRODUCT_CATEGORY;
    }

    public void setPRODUCT_CATEGORY(String PRODUCT_CATEGORY) {
        this.PRODUCT_CATEGORY = PRODUCT_CATEGORY;
    }

    public String getPRODUCT_DESCRIPTION() {
        return PRODUCT_DESCRIPTION;
    }

    public void setPRODUCT_DESCRIPTION(String PRODUCT_DESCRIPTION) {
        this.PRODUCT_DESCRIPTION = PRODUCT_DESCRIPTION;
    }

    public String getPRODUCT_PRICE() {
        return PRODUCT_PRICE;
    }

    public void setPRODUCT_PRICE(String PRODUCT_PRICE) {
        this.PRODUCT_PRICE = PRODUCT_PRICE;
    }

    public byte[] getPRODUCT_IMAGE() {
        return PRODUCT_IMAGE;
    }

    public void setPRODUCT_IMAGE(byte[] PRODUCT_IMAGE) {
        this.PRODUCT_IMAGE = PRODUCT_IMAGE;
    }
}
