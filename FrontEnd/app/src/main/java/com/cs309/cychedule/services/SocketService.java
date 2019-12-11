package com.cs309.cychedule.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.cs309.cychedule.activities.SessionManager;
import com.cs309.cychedule.activities.ui.home.HomeFragment;
import com.cs309.cychedule.utilities.UserUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * SocketService is the service of the WebSocket
 * We build our WebSocket in a service way
 */
public class SocketService extends Service {
    Context context = this;
    WebSocketClient client;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    Message msg;
    SessionManager sessionManager;
    private final IBinder binder = new MyBinder();

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        Toast.makeText(context, "Service started!" , Toast.LENGTH_LONG).show();
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        // Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
//        AsyncHttpClient.getDefaultInstance().websocket("wss://dev.hanzec.com/websocket/1",
//                "my-protocol",
//                new AsyncHttpClient.WebSocketConnectCallback() {
//            @Override
//            public void onCompleted(Exception ex, WebSocket webSocket) {
//                if (ex != null) {
//                    ex.printStackTrace();
//                    return;
//                }
//                webSocket.send("a string");
//                // webSocket.send(new byte[10]);
//                webSocket.setStringCallback(new WebSocket.StringCallback() {
//                    public void onStringAvailable(String s) {
//                        System.out.println("I got some output: " + s);
//                        Looper.prepare();
//                        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                    }
//                });
//                webSocket.setDataCallback(new DataCallback() {
//                    public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
//                        System.out.println("I got some bytes!");
//                        // note that this data has been read
//                        byteBufferList.recycle();
//                    }
//                });
//            }
//        });
        try {
            Log.d("Socket:", "Trying socket");
            String email = sessionManager.getUserInfo().get("email");
            client = new WebSocketClient(new URI(
                    "wss://dev.hanzec.com/websocket/" + email)) {

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "WebSocket is connecting");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onMessage(String message) {
                    Log.d("MESSAGE", "Server sent: " + message);
                    HomeFragment.getEvents(message);
                    Looper.prepare();
                    Toast.makeText(context, "Received a server message: " + message, Toast.LENGTH_LONG).show();
                    UserUtil.noti_invite(context, 1, "Received a server message:", message);
                    Looper.loop();
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception", e.toString());
                }
            };
        }
        catch (URISyntaxException ue) {
            Log.d("Exception:", ue.toString());
            ue.printStackTrace();
        }
        client.connect();
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        stopSelf(msg.arg1);
    }

    public class MyBinder extends android.os.Binder {
        public SocketService getService(){
            return SocketService.this;
        }

    }

    public void sendMessages(String s)
    {
        if (client != null)
        {
            client.send(s);
        }
    }
}