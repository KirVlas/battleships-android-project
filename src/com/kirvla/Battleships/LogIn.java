package com.kirvla.Battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LogIn extends Activity {
  private static EditText edtUsername, edtPassword;
  private static TextView txtMessage, txtPlayers;
  private static Button btnLogIn, btnGameBoard, btnPrefs;
  private static boolean loggedIn = false;
  private static String userName, password;
  List<String> users = new ArrayList<String>();
  private ListView lvUsers;
  private ArrayAdapter<String> usersArrayAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);

    edtUsername = (EditText) findViewById(R.id.edtUsername);
    edtPassword = (EditText) findViewById(R.id.edtPassword);
    btnLogIn = (Button) findViewById(R.id.btnLogIn);
    btnGameBoard = (Button) findViewById(R.id.btnGameboard);
    btnPrefs = (Button)findViewById(R.id.btnPrefs);
    txtMessage = (TextView) findViewById(R.id.txtMessage);
    txtPlayers = (TextView)findViewById(R.id.txtPlayers);
    lvUsers = (ListView) findViewById(R.id.lvUsers);


  }

  public void btnLogInClicked(View v) {
    userName = edtUsername.getText().toString();
    password = edtPassword.getText().toString();
    if (!loggedIn) {
      AsyncHttpClient client = new AsyncHttpClient();
      client.setBasicAuth(userName, password);
      client.get("http://battlegameserver.com/api/v1/login.json", new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
          // called when response HTTP status is "200 OK"
          loggedIn = true;
          btnLogIn.setText("Log Out");
          btnGameBoard.setVisibility(View.VISIBLE);
          btnPrefs.setVisibility(View.VISIBLE);
          txtPlayers.setVisibility(View.VISIBLE);
          try {
            txtMessage.setText("Welcome: " + response.getString("avatar_name"));
          } catch (JSONException e) {
            e.printStackTrace();
          }
          getAvailableUsers();
          Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable e, org.json.JSONObject response) {
          // called when response HTTP status is "4XX" (eg. 401, 403, 404)
          Toast.makeText(getApplicationContext(), "Failed to log in!", Toast.LENGTH_SHORT).show();
        }

      });
    } else {
      AsyncHttpClient client = new AsyncHttpClient();
      client.setBasicAuth(userName, password);
      client.get("http://battlegameserver.com/api/v1/logout.json", new TextHttpResponseHandler() {

        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String response) {
          loggedIn = false;
          btnLogIn.setText("Log In");
          txtMessage.setText("Successfully logged out!");
          lvUsers.setVisibility(View.GONE);
          btnGameBoard.setVisibility(View.INVISIBLE);
          btnPrefs.setVisibility(View.INVISIBLE);
          txtPlayers.setVisibility(View.GONE);
          users = new ArrayList<String>();
          Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
          Toast.makeText(getApplicationContext(), "Failed to log out!", Toast.LENGTH_SHORT).show();
        }

      });


    }
  }

  public void getAvailableUsers() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(userName, password);
    client.get("http://battlegameserver.com/api/v1/all_users.json", new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONArray response) {

        try {
          for (int i = 0; i < response.length(); i++) {
            if (!response.getJSONObject(i).getString("avatar_name").equals("null")) {
              String avatarName = response.getJSONObject(i).getString("avatar_name");
              users.add(avatarName);
            }
          }
        } catch (org.json.JSONException e) {
          Log.i("API", e.getMessage());
        }
        usersArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, users);
        lvUsers.setAdapter(usersArrayAdapter);
        lvUsers.setVisibility(View.VISIBLE);
      }

    });


  }

  public void btnGameBoardClicked(View v){

    Intent gameBoard = new Intent(this, Gameboard.class);
    gameBoard.putExtra("userName", userName);
    gameBoard.putExtra("password", password);
    startActivity(gameBoard);

  }

  public void btnPrefsClicked(View v){

    Intent preferences = new Intent(this, Preferences.class);
    preferences.putExtra("userName", userName);
    preferences.putExtra("password", password);
    startActivity(preferences);

  }



}
