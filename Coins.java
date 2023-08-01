/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import main.Game;

/**
 *
 * @author MS NHUNG
 */
public class Coins extends GameObject{
    
    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;
    
    public Coins(int x, int y, int objectType){
        super(x, y, objectType);
        doAnimation = true;
        initHitbox(7, 10);

        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);

        maxHoverOffset = (int) (10 * Game.SCALE);
    }
    
    public void update() {
            updateAnimationTick();
            updateHover();
	}
        
        // cap nhat chuyen dong len xuong cua thuoc
	private void updateHover() {
            hoverOffset += (0.075f * Game.SCALE * hoverDir);

            if (hoverOffset >= maxHoverOffset)
                    hoverDir = -1;
            else if (hoverOffset < 0)
                    hoverDir = 1;

            hitbox.y = y + hoverOffset;
	}
}
