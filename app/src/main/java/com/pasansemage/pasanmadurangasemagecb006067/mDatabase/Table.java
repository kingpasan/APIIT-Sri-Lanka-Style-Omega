package com.pasansemage.pasanmadurangasemagecb006067.mDatabase;

/**
 * Created by Pasan .M. Semage on 2017-09-18.
 */

public class Table {

    //Database Details
    public static final String DATABASE_NAME = "StyleOmega.db";
    public static final int DATABASE_VERSION = 1;


    //Table Names
    public static final String TABLE_USER = "USER_TABLE";
    public static final String TABLE_PRODUCT = "PRODUCT_TABLE";
    public static final String TABLE_IMAGE = "IMAGE_TABLE";
    public static final String TABLE_SIZE = "SIZE_TABLE";
    public static final String TABLE_CART = "CART_TABLE";
    public static final String TABLE_PURCHASE = "PURCHASE_TABLE";
    public static final String TABLE_REVIEW = "REVIEW_TABLE";
    public static final String TABLE_CC = "CC_TABLE";


    //Table User
    public static final String USER_ID = "USER_ID";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PASSWORD = "PASSWORD";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_FULL_NAME = "FULL_NAME";
    public static final String USER_BILLING_ADDRESS = "ADDRESS";
    public static final String USER_MOBILE_NUMBER = "MOBILE_NUMBER";


    //Table Product
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String PRODUCT_TITLE = "TITLE";
    public static final String PRODUCT_CATEGORY = "CATEGORY";
    public static final String PRODUCT_DESCRIPTION = "DESCRIPTION";
    public static final String PRODUCT_PRICE = "PRICE";

    //Table Product Image
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String IMAGE_PRODUCT_ID = "PRODUCT_ID";
    public static final String IMAGE_IMAGE = "IMAGE";

    //Table Available Size
    public static final String SIZE_ID = "SIZE_ID";
    public static final String SIZE_PRODUCT_ID = "PRODUCT_ID";
    public static final String SIZE_SIZE = "SIZE";

    //Table Cart
    public static final String CART_ID = "CART_ID";
    public static final String CART_USER_ID = "USER_ID";
    public static final String CART_PRODUCT_ID = "PRODUCT_ID";
    public static final String CART_PRODUCT_QTY = "PRODUCT_QTY";

    //Table Purchase History
    public static final String PURCHASE_ID = "PURCHASE_ID";
    public static final String PURCHASE_USER_ID = "USER_ID";
    public static final String PURCHASE_PRODUCT_ID = "PRODUCT_ID";
    public static final String PURCHASE_PRODUCT_QTY = "PRODUCT_QTY";
    public static final String PURCHASE_PRODUCT_STATUS = "PRODUCT_STATUS";

    //Table Review
    public static final String REVIEW_ID = "REVIEW_ID";
    public static final String REVIEW_USERNAME = "USERNAME";
    public static final String REVIEW_PRODUCT_ID = "PRODUCT_ID";
    public static final String REVIEW_RATE = "RATE";
    public static final String REVIEW_REVIEW = "REVIEW";

    //Table Credit Card
    public static final String CC_ID = "CC_ID";
    public static final String CC_USERNAME = "USERNAME";
    public static final String CC_NUMBER = "CC_NUMBER";
    public static final String CC_EXPIRED_DATE = "EXPIRED_DATE";
    public static final String CC_CCV_CODE = "CC_CCV";


    /*

    * Creating sql statements
    * Pasan Maduranga Semage CB006067
    * WMAD - II
    * APIIT SRI LANKA
    *
    * */


    //Create Statements

    public static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
            " (" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_TYPE + " TEXT, " +
            USER_IMAGE + " BLOB, " +
            USER_NAME + " TEXT, " +
            USER_PASSWORD + " TEXT, " +
            USER_EMAIL + " TEXT, " +
            USER_FULL_NAME + " TEXT" +
            USER_BILLING_ADDRESS + " TEXT, " +
            USER_MOBILE_NUMBER + " TEXT" +
            ")";

    public static final String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT +
            " (" +
            PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PRODUCT_TITLE + " TEXT, " +
            PRODUCT_CATEGORY + " TEXT, " +
            PRODUCT_DESCRIPTION + " TEXT, " +
            PRODUCT_PRICE + " TEXT" +
            ")";

    public static final String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE +
            " (" +
            IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            IMAGE_PRODUCT_ID + " TEXT, " +
            IMAGE_IMAGE + " BLOB" +
            ")";

    public static final String CREATE_SIZE_TABLE = "CREATE TABLE " + TABLE_SIZE +
            " (" +
            SIZE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SIZE_PRODUCT_ID + " TEXT, " +
            SIZE_SIZE + " TEXT" +
            ")";

    public static final String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART +
            " (" +
            CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CART_USER_ID + " TEXT, " +
            CART_PRODUCT_ID + " TEXT, " +
            CART_PRODUCT_QTY + " TEXT" +
            ")";

    public static final String CREATE_PURCHASE_TABLE = "CREATE TABLE " + TABLE_PURCHASE +
            " (" +
            PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PURCHASE_USER_ID + " TEXT, " +
            PURCHASE_PRODUCT_ID + " TEXT, " +
            PURCHASE_PRODUCT_QTY + " TEXT, " +
            PURCHASE_PRODUCT_STATUS + " TEXT" +
            ")";

    public static final String CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_REVIEW +
            " (" +
            REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            REVIEW_USERNAME + " TEXT, " +
            REVIEW_PRODUCT_ID + " TEXT, " +
            REVIEW_RATE + " TEXT, " +
            REVIEW_REVIEW + " TEXT" +
            ")";

    public static final String CREATE_CC_TABLE = "CREATE TABLE " + TABLE_CC +
            " (" +
            CC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CC_USERNAME + " TEXT, " +
            CC_NUMBER + " TEXT, " +
            CC_EXPIRED_DATE + " TEXT, " +
            CC_CCV_CODE + " TEXT" +
            ")";


    //Drop Statements

    public static final String DROP_USER_TABLE = "DROP IF TABLE EXISTS " + TABLE_USER;
    public static final String DROP_PRODUCT_TABLE = "DROP IF TABLE EXISTS " + TABLE_PRODUCT;
    public static final String DROP_IMAGE_TABLE = "DROP IF TABLE EXISTS " + TABLE_IMAGE;
    public static final String DROP_SIZE_TABLE = "DROP IF TABLE EXISTS " + TABLE_SIZE;
    public static final String DROP_CART_TABLE = "DROP IF TABLE EXISTS " + TABLE_CART;
    public static final String DROP_PURCHASE_TABLE = "DROP IF TABLE EXISTS " + TABLE_PURCHASE;
    public static final String DROP_REVIEW_TABLE = "DROP IF TABLE EXISTS " + TABLE_REVIEW;
    public static final String DROP_CC_TABLE = "DROP IF TABLE EXISTS " + TABLE_CC;


}
