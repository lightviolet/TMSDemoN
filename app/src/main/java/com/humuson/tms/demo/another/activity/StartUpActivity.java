package com.humuson.tms.demo.another.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.humuson.tms.demo.another.R;
import com.tms.sdk.ITMSConsts;
import com.tms.sdk.TMS;
import com.tms.sdk.api.APIManager;
import com.tms.sdk.api.request.DeviceCert;
import com.tms.sdk.api.request.Login;
import com.tms.sdk.api.request.SetConfig;
import com.tms.sdk.common.util.TMSUtil;

import org.json.JSONObject;

public class StartUpActivity extends AppCompatActivity {

    private TextView projectNumber;
    private TextView pushToken;
    private TextView status;
    private TextView deviceCert;
    private TextView loginPms;
    private TextView setConfig;

    private TMS mTms;

    private transient Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        projectNumber = (TextView) findViewById(R.id.project_number);
        pushToken = (TextView) findViewById(R.id.push_token);
        status = (TextView) findViewById(R.id.status);
        deviceCert = (TextView) findViewById(R.id.device_cert);
        loginPms = (TextView) findViewById(R.id.login_pms);
        setConfig = (TextView) findViewById(R.id.set_config);

        context = this;

        initTms();
    }

    public void initTms() {
        mTms = TMS.getInstance(getApplicationContext());
        mTms.setPopupSetting(false, context.getString(R.string.app_name));
        mTms.setPopupNoti(true);
        mTms.setRingMode(true);
        mTms.setVibeMode(true);
        mTms.setIsPopupActivity(false);
        mTms.setNotiOrPopup(false);

        projectNumber.setText(TMSUtil.getGCMProjectId(getApplicationContext()));
        pushToken.setText(TMSUtil.getGCMToken(getApplicationContext()));

        certByTestId();
    }

    public void certByTestId() {
        status.setText("Initializing processing : deviceCert");
        deviceCert.setText("Processing...");
        new DeviceCert(context).request(new APIManager.APICallback() {
            public void response(String code, JSONObject json) {
                deviceCert.setText(json.toString());
                projectNumber.setText(TMSUtil.getGCMProjectId(getApplicationContext()));
                pushToken.setText(TMSUtil.getGCMToken(getApplicationContext()));
                if (ITMSConsts.CODE_SUCCESS.equals(code)) {
                    status.setText("Initializing processing : loginPms");
                    loginPms.setText("Processing...");
                    new Login(context).request("FCMTESTUSER", null, new APIManager.APICallback() {
                        public void response(String code, JSONObject json) {
                            loginPms.setText(json.toString());
                            if (ITMSConsts.CODE_SUCCESS.equals(code)) {
                                // Noti&Mkt to Y
                                status.setText("Initializing processing : setConfig");
                                setConfig.setText("Processing...");
                                new SetConfig(context).request("Y", "Y", new APIManager.APICallback() {
                                    @Override
                                    public void response(String code, JSONObject json) {
                                        setConfig.setText(json.toString());
                                        if (ITMSConsts.CODE_SUCCESS.equals(code)) {
                                            status.setText("Initializing success.");
                                        } else {
                                            status.setText("Initializing failed.");
                                        }
                                    }
                                });
                            } else {
                                status.setText("Initializing failed.");
                            }
                        }
                    });
                } else {
                    status.setText("Initializing failed.");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTms.clear();
    }
}
