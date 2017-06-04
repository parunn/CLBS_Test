package test.clbs.android.example.com.clbsandroidtest.services;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import test.clbs.android.example.com.clbsandroidtest.model.AjaxResponse;
import test.clbs.android.example.com.clbsandroidtest.model.AjaxResponseModel;
import test.clbs.android.example.com.clbsandroidtest.model.LocationList;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class ServiceAPI {

    HttpResponse httpResponse;

    //Get Location List
    public AsyncTask<String, Void, String> getLocationList(String get_api_url, final AjaxResponse ajaxResponse) {

        return new ServiceAPI.SimpleTask(new AjaxCallback() {
            @Override
            public void callback(int statusCode, String json) {
                if(statusCode == 200) {

                    //Create New Object
                    Gson gson = new GsonBuilder().create();
                    LocationList locationLists = new LocationList();
                    AjaxResponseModel<LocationList> responseModel = new AjaxResponseModel<>();

                    //Data Mapping
                    LocationList getIndustryStock = gson.fromJson(json, LocationList.class);
                    responseModel.setItem(getIndustryStock);

                    ajaxResponse.callback(statusCode, responseModel);

                } else  {

                    ajaxResponse.callback(statusCode, null);
                }
            }
        }).execute(get_api_url);
    }



    interface AjaxCallback {
        void callback(int statusCode, String json);
    }

    class SimpleTask extends AsyncTask<String, Void, String> {

        private AjaxCallback ajaxCallback;
        private int statusCode;

        public SimpleTask(AjaxCallback ajaxCallback) {
            this.ajaxCallback = ajaxCallback;
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpGet httpGet = new HttpGet(urls[0]);
            HttpClient client = new DefaultHttpClient();

            try {
                httpResponse = client.execute(httpGet);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.statusCode = httpResponse.getStatusLine().getStatusCode();

            String result = "";

            if (this.statusCode == 200) {

                try {
                    InputStream inputStream = httpResponse.getEntity().getContent();
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
//                        Log.e("result", result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            this.ajaxCallback.callback(this.statusCode, json);
        }
    }
}


