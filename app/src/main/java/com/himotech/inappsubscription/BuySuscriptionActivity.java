package com.himotech.inappsubscription;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.himotech.inappsubscription.utils.IabHelper;
import com.himotech.inappsubscription.utils.IabResult;
import com.himotech.inappsubscription.utils.Inventory;
import com.himotech.inappsubscription.utils.Purchase;

public class BuySuscriptionActivity extends AppCompatActivity {

    //In App Purchase Subscription
    private IabHelper mIabHelper;

    private String mBase64EncodedPublicKey;

    private Button mBtnBuySubscription;
    private TextView mTxtStatus;

    private String TAG="BuyIAP";
    private boolean isInAppSupported=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_suscription);

        initView();
        initIAB();

    }

    private void initView(){

        mBtnBuySubscription=(Button)findViewById(R.id.btn_buy_subscription);

        mTxtStatus=(TextView)findViewById(R.id.txt_subscription_status);


    }

    private void initIAB(){

        mIabHelper=new IabHelper(this,mBase64EncodedPublicKey);
        // enable debug logging (for a production application, you should set this to false).
        mIabHelper.enableDebugLogging(true);

        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh no, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }else{
                    isInAppSupported=true;

                    //Query if Subscription already buy
                    mIabHelper.queryInventoryAsync(true,mGotInventoryListener);

                }
                // Hooray, IAB is fully set up!
            }
        });
    }


    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // handle error here
            }
            else {
                // does the user have the premium upgrade?


                //Now here we can check if Purchase Already Brought
                boolean isSubscriptionDone = inventory.hasPurchase(Constants.SKU_SUBSCRIPTION);
                // update UI accordingly
            }
        }
    };



    private void buySubscription(){

        mIabHelper.launchPurchaseFlow(this,Constants.SKU_SUBSCRIPTION,Constants.REQUEST_BUY_SUBSCRIPTION,mPurchaseFinishedListener);
    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);

                return;
            }
            else if(purchase.getSku().equalsIgnoreCase(Constants.SKU_SUBSCRIPTION)){

                //User Buy Subscription Do your stuff related to it

                mTxtStatus.setText("You are subscribe user of "+ Constants.SKU_SUBSCRIPTION);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if(requestCode==Constants.REQUEST_BUY_SUBSCRIPTION && resultCode==RESULT_OK){

            mIabHelper.handleActivityResult(requestCode,resultCode,data);

        }else if(requestCode==Constants.REQUEST_BUY_SUBSCRIPTION && resultCode==RESULT_CANCELED){
            mIabHelper.handleActivityResult(requestCode,resultCode,data);
        }*/

        if(requestCode==Constants.REQUEST_BUY_SUBSCRIPTION ){
            mIabHelper.handleActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIabHelper != null){
            mIabHelper.dispose();
        }
        mIabHelper = null;
    }

}
