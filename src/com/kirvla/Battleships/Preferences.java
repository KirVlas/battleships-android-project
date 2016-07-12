package com.kirvla.Battleships;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by KirillV on 6/1/2015.
 */
public class Preferences extends Activity {
  private Intent currentIntent;
  private String userName;
  private String password;
  private ImageView vwAvatar;
  private Bitmap bitmap;
  private EditText edtAvatar;
  private EditText edtFirst;
  private EditText edtLast;
  private EditText edtEmail;
  private TextView txtLevel;
  private TextView txtCoins;
  private TextView txtWins;
  private TextView txtLosses;
  private TextView txtXP;
  private TextView txtAvailable;
  private TextView txtOnline;
  private TextView txtGaming;
  private TextView txtDraws;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.preferences);
    vwAvatar = (ImageView)findViewById(R.id.vwAvatar);
    edtAvatar = (EditText)findViewById(R.id.edtAvatar);
    edtFirst = (EditText)findViewById(R.id.edtFirst);
    edtLast = (EditText)findViewById(R.id.edtLast);
    edtEmail = (EditText)findViewById(R.id.edtEmail);
    txtLevel = (TextView)findViewById(R.id.txtLevel);
    txtCoins = (TextView)findViewById(R.id.txtCoins);
    txtWins = (TextView)findViewById(R.id.txtWins);
    txtLosses = (TextView)findViewById(R.id.txtLosses);
    txtXP = (TextView)findViewById(R.id.txtXP);
    txtAvailable = (TextView)findViewById(R.id.txtAvailable);
    txtOnline = (TextView)findViewById(R.id.txtOnline);
    txtGaming = (TextView)findViewById(R.id.txtGaming);
    txtDraws = (TextView)findViewById(R.id.txtDraws);
    currentIntent = getIntent();
    Bundle extras = currentIntent.getExtras();
    if (extras != null) {
      userName = extras.getString("userName");
      password = extras.getString("password");
    }
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
        .permitAll().build();
    StrictMode.setThreadPolicy(policy);

    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(userName, password);
    client.get("http://battlegameserver.com/api/v1/login.json", new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
        String bitmapURL = "";

        try {
          bitmapURL = response.getString("avatar_image");
          String avatarName = response.getString("avatar_name");
          String first = response.getString("first_name");
          String last = response.getString("last_name");
          String email = response.getString("email");
          Integer level = response.getInt("level");
          Integer coins = response.getInt("coins");
          Integer wins = response.getInt("battles_won");
          Integer losses = response.getInt("battles_lost");
          Integer draws = response.getInt("battles_tied");
          Integer xp = response.getInt("experience_points");
          Boolean available = response.getBoolean("available");
          Boolean online = response.getBoolean("online");
          Boolean gaming = response.getBoolean("gaming");

          edtAvatar.setText(avatarName);
          edtFirst.setText(first);
          edtLast.setText(last);
          edtEmail.setText(email);
          txtLevel.setText(String.valueOf(level));
          txtCoins.setText(String.valueOf(coins));
          txtWins.setText(String.valueOf(wins));
          txtLosses.setText(String.valueOf(losses));
          txtDraws.setText(String.valueOf(draws));
          txtXP.setText(String.valueOf(xp));

          if (available){
            txtAvailable.setText("YES");
          }else{
            txtAvailable.setText("NO");
          }
          if (online){
            txtOnline.setText("YES");
          }else{
            txtOnline.setText("NO");
          }
          if (gaming){
            txtGaming.setText("YES");
          }else{
            txtGaming.setText("NO");
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }

        bitmap = getBitmapFromURL("http://battlegameserver.com" + bitmapURL);
        vwAvatar.setImageBitmap(bitmap);

      }


    });

  }

  public Bitmap getBitmapFromURL(String imgUrl){
    try{
      URL url = new URL(imgUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoInput(true);
      connection.connect();
      InputStream is = connection.getInputStream();
      Bitmap myBitmap = BitmapFactory.decodeStream(is);
      return myBitmap;
    }catch(Exception e){
      e.printStackTrace();
      return null;
    }
  }

}
