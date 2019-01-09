package com.example.himawari.aidlclientactivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.himawari.aidlserver.IAidlServerService;

public class MainActivity extends AppCompatActivity {
    static String[] handList = {"グー","チョキ","パー"};
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendGetMessage(0);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendGetMessage(1);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendGetMessage(2);
            }
        });
    }

    void SendGetMessage(int a){
        String ret="";
        // クリック時の処理
        try {
            ret = mService.getMessage(a);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(MainActivity.this, "自分は"+handList[a]+ret,  Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Bind.
        //
        // NOTICE:
        //     Android 5.0 Lolipop以降でExplicit Intent判定に必須のため，setPackage()でpackage名を明示．
        //
        Intent service = new Intent("com.example.himawari.aidlserver.ACTION_BIND");
        service.setPackage("com.example.himawari.aidlserver"); // Android 5.0 Lolipop以降で必須の設定
        bindService(service, mAidlConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        // Unbind.
        if (mService != null) {
            unbindService(mAidlConnection);
        }
        super.onPause();
    }

    private IAidlServerService mService = null;
    private AidlConnection mAidlConnection = new AidlConnection();

    // Service接続状態のCallback.
    private class AidlConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IAidlServerService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    }
}


