package com.kirvla.Battleships;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

/**
 * Created by KirillV on 5/28/2015.
 */
public class Gameboard extends Activity {

  private Intent currentIntent;
  private String userName, password, gameID;
  List<String> ships = new ArrayList<String>();
  List<String> directions = new ArrayList<String>();
  private Spinner spinnerShips,  spinnerDirections, spinnerRows, spinnerCols;
  private TextView txtGameID;
  private View boardView;
  private GridLayout grdSpinners;
  private ArrayAdapter<String> shipsArrayAdapter, directionsArrayAdapter, colsArrayAdapter, rowsArrayAdapter;
  private Button btnAddShip;
  public static BoardCell[][] board = new BoardCell[11][11];
  ArrayList<String> letters = new ArrayList<String>(){{
    add("A");
    add("B");
    add("C");
    add("D");
    add("E");
    add("F");
    add("G");
    add("H");
    add("I");
    add("J");
  }};
  String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
  Hashtable<String, Integer> cells
      = new Hashtable<String, Integer>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gameboard);
    for(int i = 0; i < 10; i++ ){
      cells.put(letters.get(i), (i + 1));
    }
    spinnerShips = (Spinner) findViewById(R.id.spinnerShips);
    spinnerDirections = (Spinner) findViewById(R.id.spinnerDirections);
    spinnerRows = (Spinner) findViewById(R.id.spinnerRows);
    spinnerCols = (Spinner) findViewById(R.id.spinnerCols);
    txtGameID = (TextView) findViewById(R.id.txtGameID);
    btnAddShip = (Button)findViewById(R.id.btnAddShip);
    boardView = (View)findViewById(R.id.boardView);
    grdSpinners = (GridLayout)findViewById(R.id.grdSpinners);
    currentIntent = getIntent();
    Bundle extras = currentIntent.getExtras();
    if (extras != null) {
      userName = extras.getString("userName");
      password = extras.getString("password");
    }
    rowsArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, letters);
    spinnerRows.setAdapter(rowsArrayAdapter);
    colsArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, numbers);
    spinnerCols.setAdapter(colsArrayAdapter);
    boardView.invalidate();

    getAvailableShips();
    getAvailableDirections();
    challengeComputer();


  }

  private void challengeComputer() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(userName, password);
    client.get("http://battlegameserver.com/api/v1/challenge_computer.json", new JsonHttpResponseHandler() {


      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
        try {
          gameID = response.getString("game_id");
          txtGameID.setText("Game ID: " + gameID);
          grdSpinners.setVisibility(View.VISIBLE);
          btnAddShip.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
          e.printStackTrace();
        }

      }

    });
  }

  public void getAvailableShips() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(userName, password);
    client.get("http://battlegameserver.com/api/v1/available_ships.json", new JsonHttpResponseHandler() {


      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
        try {
          ships.add("Carrier: " + String.valueOf(response.getInt("carrier")));
          ships.add("Battleship: " + String.valueOf(response.getInt("battleship")));
          ships.add("Cruiser: " + String.valueOf(response.getInt("cruiser")));
          ships.add("Submarine: " + String.valueOf(response.getInt("submarine")));
          ships.add("Destroyer: " + String.valueOf(response.getInt("destroyer")));
        } catch (JSONException e) {
          e.printStackTrace();
        }
        shipsArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, ships);
        spinnerShips.setAdapter(shipsArrayAdapter);
      }

    });

  }

  public void getAvailableDirections() {
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(userName, password);
    client.get("http://battlegameserver.com/api/v1/available_directions.json", new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
        try {
          directions.add("North: " + String.valueOf(response.getInt("north")));
          directions.add("East: " + String.valueOf(response.getInt("east")));
          directions.add("South: " + String.valueOf(response.getInt("south")));
          directions.add("West: " + String.valueOf(response.getInt("west")));
        } catch (JSONException e) {
          e.printStackTrace();
        }
        directionsArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, directions);
        spinnerDirections.setAdapter(directionsArrayAdapter);
      }

    });

  }

  public void addShipButtonClicked(View v){
    String row = spinnerRows.getSelectedItem().toString();
    String col = spinnerCols.getSelectedItem().toString();
    String direction = spinnerDirections.getSelectedItem().toString().replaceAll("[\\D]", "");
    String ship = spinnerShips.getSelectedItem().toString().replaceAll("[^a-zA-Z]", "").toLowerCase();
    int shipSize = Integer.parseInt(spinnerShips.getSelectedItem().toString().replaceAll("[\\D]", ""));
    AsyncHttpClient client = new AsyncHttpClient();
    client.setBasicAuth(userName, password);
    client.get("http://battlegameserver.com/api/v1/game/" + gameID + "/add_ship/" + ship + "/" + row.toLowerCase() + "/" + col + "/" + direction + ".json", new JsonHttpResponseHandler() {


      @Override
      public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
        String error = "";
        try {
          error = response.getString("error");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if ( error.equals("")) {
          String message = "";
          try {
            message = response.getString("status");
          } catch (JSONException e) {
            e.printStackTrace();
          }
          for (int i = 0; i < shipSize; i++) {
            if (direction.equals("0")) {
              board[cells.get(row) - i][Integer.parseInt(col)].addShip();
            }
            if (direction.equals("2")) {
              board[cells.get(row)][Integer.parseInt(col) + i].addShip();
            }
            if (direction.equals("4")) {
              board[cells.get(row) + i][Integer.parseInt(col)].addShip();
            }
            if (direction.equals("6")) {
              board[cells.get(row)][Integer.parseInt(col) - i].addShip();
            }
          }
          Toast.makeText(getApplicationContext(), message.toUpperCase(), Toast.LENGTH_SHORT).show();
          boardView.invalidate();
        }else{
          Toast.makeText(getApplicationContext(), error.toUpperCase(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable e, org.json.JSONObject response) {
        String error = "";
        try {
          error = response.getString("error");
        } catch (JSONException e1) {
          e1.printStackTrace();
        }
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
      }

    });
  }

}
