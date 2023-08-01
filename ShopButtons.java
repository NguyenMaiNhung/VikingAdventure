/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.Buttons.*;
import utilz.LoadSave;

/**
 *
 * @author MS NHUNG
 */
public class ShopButtons{
    
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public ShopButtons(int xPos, int yPos, int rowIndex) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        loadImgs();
        initBounds();
    }
    
    private void initBounds() {
	bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SHOP_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i=0; i<3; i++){
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }
    
    public void update(){
        index = 0;
        if(mouseOver){
            index = 1;
        }
        if(mousePressed){
            index = 2;
        }
    }
    
    public void draw(Graphics g){
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }
    
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
    
    public Rectangle getBounds() {
	return bounds;
    }
    
}
