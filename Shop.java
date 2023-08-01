/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.ShopButtons;
import static utilz.Constants.ObjectConstants.*;
import utilz.LoadSave;

/**
 *
 * @author MS NHUNG
 */
public class Shop extends State implements Statemethods{
    
    private BufferedImage backgroundImg, shopBackgroundImg;
    private BufferedImage bluePotionImg, greenPotionImg, redPotionImg, coinImg;
    private int bgX, bgY, bgW, bgH;
    private int xBlue, xRed, xGreen, xCoin, yPos;
    private ShopButtons[] shopButton = new ShopButtons[3];
    private int []count = new int [3];
    private int index = 0;

    public Shop(Game game) {
        super(game);
        loadImgs();
        loadButton();
    }
    
    private void loadImgs() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        shopBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.SHOP_BG);
        
        bluePotionImg = LoadSave.GetSpriteAtlas(LoadSave.BLUE_POTION);
        redPotionImg = LoadSave.GetSpriteAtlas(LoadSave.RED_POTION);
        greenPotionImg = LoadSave.GetSpriteAtlas(LoadSave.GREEN_POTION);
        coinImg = LoadSave.GetSpriteAtlas(LoadSave.COIN_1);
        
        bgW = (int) (shopBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (shopBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
        
        xBlue = (int)(bgX + 50);
        xRed = (int)(xBlue + 50);
        xGreen = (int)(xRed + 50);
        xCoin = (int)(xGreen + 50);
        yPos = (int)(bgY + bgW + 70);
    }

    private void loadButton() {
        shopButton[0] = new ShopButtons(Game.GAME_WIDTH / 2, (int) (143 * Game.SCALE), 1);
        shopButton[1] = new ShopButtons(Game.GAME_WIDTH / 2, (int) (230 * Game.SCALE), 0);
        shopButton[2] = new ShopButtons(Game.GAME_WIDTH / 2, (int) (320 * Game.SCALE), 2);
    }

    @Override
    public void update() {
        for(ShopButtons bt : shopButton){
            bt.update();
        }
    }

    @Override 
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	g.drawImage(shopBackgroundImg, bgX, bgY, bgW, bgH, null);
        
        for(ShopButtons bt : shopButton){
            bt.draw(g);
        }
        g.setColor(Color.WHITE);
        g.drawString("X", xBlue + 15, yPos + POTION_HEIGHT - 1);
        g.drawString(Integer.toString(count[0]), xBlue + 25, yPos + POTION_HEIGHT - 1);
        g.drawImage(bluePotionImg, xBlue, yPos, POTION_WIDTH, POTION_HEIGHT, null);
        
        g.drawString("X", xRed + 15, yPos + POTION_HEIGHT - 1);
        g.drawString(Integer.toString(count[1]), xRed + 25, yPos + POTION_HEIGHT - 1);
        g.drawImage(redPotionImg, xRed, yPos, POTION_WIDTH, POTION_HEIGHT, null);
        
        g.drawString("X", xGreen + 15, yPos + POTION_HEIGHT - 1);
        g.drawString(Integer.toString(count[2]), xGreen + 25, yPos + POTION_HEIGHT - 1);
        g.drawImage(greenPotionImg, xGreen, yPos, POTION_WIDTH, POTION_HEIGHT, null);
        
        g.drawString("X", xCoin + 15, yPos + COIN_SIZE - 3);
        g.drawString(Integer.toString(game.getPlaying().getObjectManager().getCountCoin()), 
                xCoin + 25, yPos + COIN_SIZE - 3);
        g.drawImage(coinImg, xCoin, yPos, COIN_SIZE, COIN_SIZE, null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(ShopButtons bt : shopButton){
            if(isIn(e, bt)){
                bt.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(int i=0; i<shopButton.length; i++){
            if(isIn(e, shopButton[i])){
                if(shopButton[i].isMousePressed()){
                    int value = game.getPlaying().getObjectManager().getCountCoin();
                    if(i == 0){
                        if(value >= 9){
                            count[i]++;
                            value = value - 9;
                            game.getPlaying().getObjectManager().setCountCoin(value);
                        }
                        else{
                            System.out.println("not enough coins to buy");
                        }
                    }
                    else if(i == 1){
                        if(value >= 7){
                            count[i]++;
                            value = value - 7;
                            game.getPlaying().getObjectManager().setCountCoin(value);
                        }
                        else{
                            System.out.println("not enough coins to buy");
                        }
                    }
                    else if(i == 2){
                        if(value >= 10){
                            count[i]++;
                            value = value - 10;
                            game.getPlaying().getObjectManager().setCountCoin(value);
                        }
                        else{
                            System.out.println("not enough coins to buy");
                        }
                    }
                }
            }
        }
        resetButtons();
    }
    
    private void resetButtons() {
        for (ShopButtons mb : shopButton)
            mb.resetBools();
//            dem = 0;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(ShopButtons bt : shopButton){
            bt.setMouseOver(false);
        }
        
        for(ShopButtons bt : shopButton){
            if(isIn(e, bt)){
                bt.setMouseOver(true);
                break;
            }
        }
    }

    public int[] getCount() {
        return count;
    }

    public void setCount(int[] count) {
        this.count = count;
    }
    
    public boolean isIn(MouseEvent e, ShopButtons s){
        return s.getBounds().contains(e.getX(), e.getY()); 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // not use
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // not use
    }
    
}
