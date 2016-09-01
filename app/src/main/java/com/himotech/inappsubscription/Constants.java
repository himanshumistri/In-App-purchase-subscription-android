package com.himotech.inappsubscription;

/**
 * Created by himanshu.mistri on 9/1/2016.
 */
public class Constants {

    public static final String SKU_TEST="android.test.purchased";

    public static final boolean TEST_BUILD=true;

    public static final String SKU_SUBSCRIPTION=TEST_BUILD?SKU_TEST:"com.subscription.dailyquotes";


    public static final int REQUEST_BUY_SUBSCRIPTION=10;
}
