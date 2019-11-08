package com.cs309.cychedule.utilities.cyScheduleServerSDK;

import android.os.Binder;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;

import com.android.volley.Request;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.EventRequest;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.LoginRequest;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.RegisterRequest;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.UserToken;


public class RestAPIService extends Service {

    String serverUrl;
    public ServiceBinder mBinder = new ServiceBinder();
    VolleyRequest volleyRequest = new VolleyRequest(this);

    /**
     * API Locations
     */
    static final String eventAPI = "/api/v1/event";

    static final String loginAPI = "/api/v1/auth/login";

    static final String registerAPI = "/api/v1/auth/register";

    static final String userTokenAPI = "/api/v1/user/token";

    static final String userAvatarAPI = "/api/v1/user/avatar";


    public RestAPIService(String s){ this.serverUrl = s; }


    public class ServiceBinder extends Binder {
        public void login(LoginRequest loginRequest,
                          VolleyRequest.ResponseHandler responseHandler){

            volleyRequest.sendObject(loginRequest,loginAPI, Request.Method.POST,responseHandler);
        }

        public void register(UserToken userToken,
                             RegisterRequest registerRequest,
                             VolleyRequest.ResponseHandler responseHandler){

            volleyRequest.sendObject(registerRequest,registerAPI, Request.Method.POST,userToken,responseHandler);
        }

        public void addEvent(UserToken userToken,
                             EventRequest eventRequest,
                             VolleyRequest.ResponseHandler responseHandler){

            volleyRequest.sendObject(eventRequest,eventAPI, Request.Method.POST,userToken,responseHandler);
        }

        public void deleteEvent(String eventID,
                                UserToken userToken,
                                VolleyRequest.ResponseHandler responseHandler){

            volleyRequest.sendObject(null,eventAPI + "/" + eventID, Request.Method.DELETE,userToken,responseHandler);

        }

        public void changeEvent(String eventID,
                                UserToken userToken,
                                EventRequest eventRequest,
                                VolleyRequest.ResponseHandler responseHandler){

            volleyRequest.sendObject(eventRequest,eventAPI + "/" + eventID, Request.Method.DELETE,userToken,responseHandler);

        }
    }

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

}
