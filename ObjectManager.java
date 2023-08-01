package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import java.awt.Color;
import levels.Level;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;
import static utilz.Constants.Projectiles.*;

public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] cannonImgs, grassImgs, coinImgs;
	private BufferedImage[][] treeImgs;
	private BufferedImage spikeImg, cannonBallImg;
	private ArrayList<Potion> potions;
        private ArrayList<Coins> coins;
	private ArrayList<GameContainer> containers;
	private ArrayList<Projectile> projectiles = new ArrayList<>();
        
        private BufferedImage bluePotion, greenPotion, redPotion;

	private Level currentLevel;
        private int countCoin = 100;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		currentLevel = playing.getLevelManager().getCurrentLevel();
		loadImgs();
	}

	public void checkSpikesTouched(Player p) {
		for (Spike s : currentLevel.getSpikes())
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}

	public void checkSpikesTouched(Enemy e) {
		for (Spike s : currentLevel.getSpikes())
			if (s.getHitbox().intersects(e.getHitbox()))
				e.hurt(200);
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (int i=0; i<potions.size(); i++)
			if (potions.get(i).isActive()) {
				if (hitbox.intersects(potions.get(i).getHitbox())) { 
//                                        if(potions.get(i).getObjType() == 0){
//                                            playing.getGame().getShop().getCount()[1]++;
//                                        }
//                                        else if(potions.get(i).getObjType() == 1){
//                                            playing.getGame().getShop().getCount()[0]++;
//                                        }
					potions.get(i).setActive(false);
					applyEffectToPlayerWithPotion(potions.get(i)); 
				}
			}
	}
        
        public void checkCoinTouched(Rectangle2D.Float hitbox){
            for(Coins c : coins){
                if(c.isActive()){
                    if(hitbox.intersects(c.getHitbox())){
                        c.setActive(false); 
                        applyEffectToPlayerWithCoin(c);
                    }
                }
            }
        }
        
        public void applyEffectToPlayerWithCoin(Coins coin){
            countCoin++;
            System.out.println(countCoin);
        }

	public void applyEffectToPlayerWithPotion(Potion p) {
		if (p.getObjType() == RED_POTION)
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
		else
			playing.getPlayer().changePower(BLUE_POTION_VALUE);
	}

	public void checkObjectHit(Rectangle2D.Float attackbox) {
		for (GameContainer gc : containers)
			if (gc.isActive() && !gc.doAnimation) {
				if (gc.getHitbox().intersects(attackbox)) {
					gc.setAnimation(true);
					int type = 0;
					if (gc.getObjType() == BARREL)
						type = 1;
					potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 2), type));
					return;
				}
			}
	}

	public void loadObjects(Level newLevel) {
		currentLevel = newLevel;
		potions = new ArrayList<>(newLevel.getPotions());
                coins = new ArrayList<>(newLevel.getCoins());
		containers = new ArrayList<>(newLevel.getContainers());
		projectiles.clear();
	}

	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];

		for (int j = 0; j < potionImgs.length; j++)
			for (int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];

		for (int j = 0; j < containerImgs.length; j++)
			for (int i = 0; i < containerImgs[j].length; i++)
				containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

		cannonImgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

		for (int i = 0; i < cannonImgs.length; i++)
			cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

		cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
		treeImgs = new BufferedImage[2][4];
		BufferedImage treeOneImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_ONE_ATLAS);
		for (int i = 0; i < 4; i++)
			treeImgs[0][i] = treeOneImg.getSubimage(i * 39, 0, 39, 92);

		BufferedImage treeTwoImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_TWO_ATLAS);
		for (int i = 0; i < 4; i++)
			treeImgs[1][i] = treeTwoImg.getSubimage(i * 62, 0, 62, 54);

		BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.GRASS_ATLAS);
		grassImgs = new BufferedImage[2];
		for (int i = 0; i < grassImgs.length; i++)
			grassImgs[i] = grassTemp.getSubimage(32 * i, 0, 32, 32);
                
                bluePotion = LoadSave.GetSpriteAtlas(LoadSave.BLUE_POTION);
                redPotion = LoadSave.GetSpriteAtlas(LoadSave.RED_POTION);
                greenPotion = LoadSave.GetSpriteAtlas(LoadSave.GREEN_POTION);
                
                coinImgs = new BufferedImage[4];
                coinImgs[0] = LoadSave.GetSpriteAtlas(LoadSave.COIN_1);
                coinImgs[1] = LoadSave.GetSpriteAtlas(LoadSave.COIN_2);
                coinImgs[2] = LoadSave.GetSpriteAtlas(LoadSave.COIN_3);
                coinImgs[3] = LoadSave.GetSpriteAtlas(LoadSave.COIN_4);
	}

	public void update(int[][] lvlData, Player player) {
		updateBackgroundTrees();
		for (Potion p : potions)
			if (p.isActive())
				p.update();

		for (GameContainer gc : containers)
			if (gc.isActive())
				gc.update();

		updateCannons(lvlData, player);
		updateProjectiles(lvlData, player);
                
                for(Coins c : coins){
                    if(c.isActive()){
                        c.update();
                    }
                }

	}

	private void updateBackgroundTrees() {
		for (BackgroundTree bt : currentLevel.getTrees())
			bt.update();
	}

	private void updateProjectiles(int[][] lvlData, Player player) {
		for (Projectile p : projectiles)
			if (p.isActive()) {
				p.updatePos();
				if (p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					p.setActive(false);
				} else if (IsProjectileHittingLevel(p, lvlData))
					p.setActive(false);
			}
	}

	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <= Game.TILES_SIZE * 5;
	}

	private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
		if (c.getObjType() == CANNON_LEFT) {
			if (c.getHitbox().x > player.getHitbox().x)
				return true;

		} else if (c.getHitbox().x < player.getHitbox().x)
			return true;
		return false;
	}

	private void updateCannons(int[][] lvlData, Player player) {
		for (Cannon c : currentLevel.getCannons()) {
			if (!c.doAnimation)
				if (c.getTileY() == player.getTileY())
					if (isPlayerInRange(c, player))
						if (isPlayerInfrontOfCannon(c, player))
							if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
								c.setAnimation(true);

			c.update();
			if (c.getAniIndex() == 4 && c.getAniTick() == 0)
				shootCannon(c);
		}
	}

	private void shootCannon(Cannon c) {
		int dir = 1;
		if (c.getObjType() == CANNON_LEFT)
			dir = -1;

		projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawPotions(g, xLvlOffset);
                drawCoins(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawTraps(g, xLvlOffset);
		drawCannons(g, xLvlOffset);
		drawProjectiles(g, xLvlOffset);
		drawGrass(g, xLvlOffset);
	}

	private void drawGrass(Graphics g, int xLvlOffset) {
		for (Grass grass : currentLevel.getGrass())
			g.drawImage(grassImgs[grass.getType()], grass.getX() - xLvlOffset, grass.getY(), (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
	}

	public void drawBackgroundTrees(Graphics g, int xLvlOffset) {
		for (BackgroundTree bt : currentLevel.getTrees()) {

			int type = bt.getType();
			if (type == 9)
				type = 8;
			g.drawImage(treeImgs[type - 7][bt.getAniIndex()], bt.getX() - xLvlOffset + GetTreeOffsetX(bt.getType()), (int) (bt.getY() + GetTreeOffsetY(bt.getType())), GetTreeWidth(bt.getType()),
					GetTreeHeight(bt.getType()), null);
		}
	}

	private void drawProjectiles(Graphics g, int xLvlOffset) {
		for (Projectile p : projectiles)
			if (p.isActive())
				g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
	}

	private void drawCannons(Graphics g, int xLvlOffset) {
		for (Cannon c : currentLevel.getCannons()) {
			int x = (int) (c.getHitbox().x - xLvlOffset);
			int width = CANNON_WIDTH;

			if (c.getObjType() == CANNON_RIGHT) {
				x += width;
				width *= -1;
			}
			g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
		}
	}

	private void drawTraps(Graphics g, int xLvlOffset) {
		for (Spike s : currentLevel.getSpikes())
			g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);

	}

	private void drawContainers(Graphics g, int xLvlOffset) {
		for (GameContainer gc : containers)
			if (gc.isActive()) {
				int type = 0;
				if (gc.getObjType() == BARREL)
					type = 1;
				g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH,
						CONTAINER_HEIGHT, null);
			}
	}

	private void drawPotions(Graphics g, int xLvlOffset) {
            g.setColor(Color.WHITE); 
            g.drawImage(bluePotion, 50, 70, POTION_WIDTH, POTION_HEIGHT, null);
            g.drawString("x", 65, 70 + POTION_HEIGHT - 1);
            g.drawString(Integer.toString(playing.getGame().getShop().getCount()[0]), 75, 70 + POTION_HEIGHT - 1);
            
            g.drawImage(redPotion, 100, 70, POTION_WIDTH, POTION_HEIGHT, null);
            g.drawString("x", 115, 70 + POTION_HEIGHT - 1);
            g.drawString(Integer.toString(playing.getGame().getShop().getCount()[1]), 125, 70 + POTION_HEIGHT - 1);
            
            g.drawImage(greenPotion, 150, 70, POTION_WIDTH, POTION_HEIGHT, null);
            g.drawString("x", 165, 70 + POTION_HEIGHT - 1);
            g.drawString(Integer.toString(playing.getGame().getShop().getCount()[2]), 175, 70 + POTION_HEIGHT - 1);
            
            for (Potion p : potions)
                if (p.isActive()) {
                    int type = 0;
                    if (p.getObjType() == RED_POTION)
                            type = 1;
                    g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT,
                                        null);
                }
	}
        
        private void drawCoins(Graphics g, int xLvlOffset){
            g.setColor(Color.white);
            g.drawImage(coinImgs[0], 190, 73, COIN_SIZE, COIN_SIZE, null);
            g.drawString("x", 210, 70 + COIN_SIZE - 1);
            g.drawString(Integer.toString(countCoin), 220, 70 + COIN_SIZE - 1);
            for(Coins c : coins){
                if(c.isActive()){
                    g.drawImage(coinImgs[c.getAniIndex()], (int)(c.getHitbox().x - c.getxDrawOffset() - xLvlOffset), 
                            (int)(c.getHitbox().y - c.getyDrawOffset()), COIN_SIZE, COIN_SIZE, null);
                }
            }
        }

	public void resetAllObjectsForGameOver() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Potion p : potions)
			p.reset();
		for (GameContainer gc : containers)
			gc.reset();
		for (Cannon c : currentLevel.getCannons())
			c.reset();
        
	}
        
        public void resetAllObjectsForLvlCompleted() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
                
		for (Potion p : potions)
			p.reset();
		for (GameContainer gc : containers)
			gc.reset();
		for (Cannon c : currentLevel.getCannons())
			c.reset();
                for(Coins co : currentLevel.getCoins())
                        co.reset();
	}
        
        public void resetAllObjectsForReLife() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
                
		for (Potion p : potions)
			p.reset();
		for (GameContainer gc : containers)
			gc.reset();
		for (Cannon c : currentLevel.getCannons())
			c.reset();
	}

    public int getCountCoin() {
        return countCoin;
    }

    public void setCountCoin(int countCoin) {
        this.countCoin = countCoin;
    }
    
}
