package com.kirvla.Battleships;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by KirillV on 6/2/2015.
 */
public class GameView extends View {
  private double board_height, board_width;
  int top_left_x, top_left_y, col_width;
  Paint paint;

  public GameView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);

    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    int width = dm.widthPixels;
    int height = dm.heightPixels;
//    float dens = dm.density;
    board_width = width * 0.90f;//(double) width / (double) dens;
    board_height = height * 0.90f;//(double) height / (double)dens;
    board_height = board_height > board_width ? board_width : board_height;
    board_width = board_height;

    top_left_x = 0;
    top_left_y = 0;
    col_width = (int) board_width / 11;

    paint = new Paint();

    for (int row = 0; row < 11; row++) {
      for (int col = 0; col < 11; col++) {
        Gameboard.board[row][col] = new BoardCell(
            col_width, col_width,
            new Point(top_left_x + (col_width * col),
                      top_left_y + (col_width * row)),
            new Point(top_left_x + (col_width * col) + col_width,
                      top_left_y + (col_width * row) + col_width)
        );
      }
    }


  }



  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    paint.setStyle(Paint.Style.FILL);
    paint.setStrokeWidth(3);
    paint.setColor(Color.WHITE);
    for (int col = 0; col <= 11; col++) {
      canvas.drawLine(top_left_x + (col_width * col), top_left_y, top_left_x + (col_width * col), (float) (top_left_y + board_height), paint);
      canvas.drawLine(top_left_x, top_left_y + (col_width * col), (float) (top_left_x + board_width), top_left_y + (col_width * col), paint);

    }

    //Draw text for columns and rows
    paint.setTextSize(col_width * 0.85f);
    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String[] numbers = {" 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10"};
    for (int row = 0; row < 10; row++) {
      canvas.drawText(letters[row], col_width * 0.20f, (col_width * (row + 2)) - (col_width * 0.20f), paint);
      canvas.drawText(numbers[row], (col_width * (row + 1)) + (col_width * 0.10f), col_width - (col_width * 0.20f), paint);
    }

    //Gameboard.board[10][8].setHit(); // test
    //Gameboard.board[10][9].setMiss();// test
    //Gameboard.board[10][10].setWaiting(true); // test

    for (int row = 0;row < 11; row++){
      for (int col = 0; col < 11; col++){
        if(Gameboard.board[col][row].hasShip()){
          drawCell("S", row, col, canvas);
        }
        if(Gameboard.board[col][row].isWaiting()){
          drawCell("W", row, col, canvas);
        }
        if(Gameboard.board[col][row].hasMiss()){
          drawCell("M", row, col, canvas);
        }
        if(Gameboard.board[col][row].hasHit()){
          drawCell("*", row, col, canvas);
        }
      }
    }


  }

  void drawCell(String contents, int row, int col, Canvas canvas){
    canvas.drawText(contents, Gameboard.board[col][row].getBottomLeft().x + (col_width * 0.20f),
        Gameboard.board[col][row].getBottomLeft().y - (col_width * 0.20f), paint );
  }




}
