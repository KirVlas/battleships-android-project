package com.kirvla.Battleships;

import android.graphics.Point;

/**
 * Created by KirillV on 6/5/2015.
 */
public class BoardCell {

  private int height, width;
  private int x1, y1, x2, y2;
  private Point topLeft, bottomRight;
  private Boolean hasShip, hit, miss, waiting;

  public BoardCell() {
    height = 0;
    width = 0;
    miss = false;
    hit = false;
    hasShip = false;
    waiting = false;
    topLeft = new Point(0, 0);
    bottomRight = new Point(0, 0);
  }

  public BoardCell(int _height, int _width, Point _topLeft, Point _bottomRight) {

    height = _height;
    width = _width;
    topLeft = _topLeft;
    bottomRight = _bottomRight;
    miss = false;
    hit = false;
    hasShip = false;
    waiting = false;
  }

  public boolean hasShip(){
    return hasShip;
  }

  public boolean hasMiss(){
    return miss;
  }

  public boolean hasHit(){
    return hit;
  }

  public boolean isWaiting(){
    return waiting;
  }

  public void addShip(){
    hasShip = true;
  }

  public void setMiss(){
    miss = true;
    waiting = false;
  }

  public void setHit(){
    hit = true;
    waiting = false;
  }

  public Point getBottomLeft(){
    return new Point(topLeft.x, topLeft.y + (bottomRight.y - topLeft.y));
  }

  public void setWaiting(boolean _waiting){
    waiting = _waiting;
  }

}
