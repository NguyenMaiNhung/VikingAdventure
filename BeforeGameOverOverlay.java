/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import utilz.LoadSave;

/**
 *
 * @author MS NHUNG
 */
public class BeforeGameOverOverlay {
    
    private Playing playing;
    private BufferedImage img, imgGreenPotion;
    private int imgX, imgY, imgW, imgH;
    private BeforeDeathButton yesButton, noButton;

    public BeforeGameOverOverlay(Playing playing) {
        this.playing = playing;
        createImgs();
        createButtons();
    }

    private void createImgs() {
        img = LoadSave.GetSpriteAtlas(LoadSave.BEFORE_DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * Game.SCALE);
    }

    private void createButtons() {
        int yesX = (int) (430 * Game.SCALE);
        int noX = (int) (430 * Game.SCALE);
        int yesY = (int) (185 * Game.SCALE);
        int noY = (int) (250 * Game.SCALE);
        
        yesButton = new BeforeDeathButton(yesX, yesY, 0);
        noButton = new BeforeDeathButton(noX, noY, 1);
    }
    
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        
        yesButton.draw(g);
        noButton.draw(g);
    }

	public void update() {
            yesButton.update();
            noButton.update();
	}

	private boolean isIn(BeforeDeathButton b, MouseEvent e) {
            return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		yesButton.setMouseOver(false);
		noButton.setMouseOver(false);

		if (isIn(yesButton, e))
			yesButton.setMouseOver(true);
		else if (isIn(noButton, e))
			noButton.setMouseOver(true);
	}
        
        public void drawNotice(Graphics g){
            g.setColor(Color.WHITE);
            g.drawString("You don't have any thuốc hồi chinh, mời đến shop mua", imgX, imgY);
            
            
        }

	public void mouseReleased(MouseEvent e) {
            if (isIn(yesButton, e)) {
                if (playing.getGame().getShop().getCount()[2] > 0  && yesButton.isMousePressed()) {
                    playing.getGame().getShop().getCount()[2] -= 1;
                    playing.resetAll();
                    playing.setBeforeGameOver(false); 
                    playing.setGamestate(Gamestate.PLAYING);
                }
                else if((playing.getGame().getShop().getCount()[2] <= 0  && yesButton.isMousePressed())){
                    playing.setGamestate(Gamestate.SHOP);
                    float time = 100000f;
                        while(time >= 0.0){
                            time -= 0.005;
                        }
                }
            } else if (isIn(noButton, e))
                if (noButton.isMousePressed()) {
                    playing.setBeforeGameOver(false);
                    playing.setGameOver(true);  
                    playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
                }

            yesButton.resetBools();
            noButton.resetBools();
	}

	public void mousePressed(MouseEvent e) {
            if (isIn(yesButton, e))
                yesButton.setMousePressed(true);
            else if (isIn(noButton, e))
                noButton.setMousePressed(true);
	}

        public BeforeDeathButton getYesButton() {
            return yesButton;
        }
        
}
