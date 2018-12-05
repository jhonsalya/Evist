package com.example.jhonsalya.evist.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.jhonsalya.evist.ConfirmPaymentActivity;
import com.example.jhonsalya.evist.Model.Request;
import com.example.jhonsalya.evist.Model.Transaction;
import com.example.jhonsalya.evist.PurchasedActivity;
import com.example.jhonsalya.evist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener{
    FirebaseDatabase db;
    DatabaseReference orders;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    //press ctrl + o


    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        orders = db.getReference("Transaction");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        //Trigger here
        Transaction request = dataSnapshot.getValue(Transaction.class);
        if(mAuth.getCurrentUser()!=null){
            if(request.getStatusseller().equals("waiting_"+mCurrentUser.getUid())){
                showNotificationSales(dataSnapshot.getKey(),request);
            }
        }
    }

    private void showNotificationSales(String key, Transaction request) {
        Intent intent = new Intent(getBaseContext(), ConfirmPaymentActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("EVIST")
                .setContentInfo("New Ticket Payment")
                .setContentText("You Have New Payment to Confirm#"+key)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //if you want to many notification show, you need give unique ID for each Notification
        int randomInt = new Random().nextInt(9999-1)+1;
        manager.notify(randomInt,builder.build());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Transaction request = dataSnapshot.getValue(Transaction.class);
        if(mAuth.getCurrentUser()!=null){
            if(request.getStatusbuyer().equals("confirmed_"+mCurrentUser.getUid())){
                showNotificationBuy(dataSnapshot.getKey(),request);
            }
        }
    }

    private void showNotificationBuy(String key, Transaction request) {
        Intent intent = new Intent(getBaseContext(), PurchasedActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("EVIST")
                .setContentInfo("New Ticket Accepted")
                .setContentText("You Got a New Ticket #"+key)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //if you want to many notification show, you need give unique ID for each Notification
        int randomInt = new Random().nextInt(9999-1)+1;
        manager.notify(randomInt,builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
