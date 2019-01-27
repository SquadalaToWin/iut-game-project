import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

public class Personnage {

    // animation du personnage
    private List<Animation> animation;

    // ajout de collision avec d'autre personnage
    private List<Personnage> lesAutre;

    // information sur le personnage
    private String nom;
    private int pointDeVie;
    private int pointDeVieActuel;

    // mouvement du personnage
    private float x = 0, y = 0;
    private int direction = 0;
    private boolean moving = false;

    // pour permetre de changer le scénario ../:
    public static boolean artDeEpe = false;
    public static boolean artDuBouclier = false;
    public static boolean artDuFeu = false;
    public static boolean artDuVol = false;

    // gestion des collisions
    private Rectangle box;

    private float futurX = 0;
    private float futurY = 0;
    private boolean collision = false;

    // --
    public Personnage(String nom, float x, float y, int pointDeVie) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.pointDeVie = pointDeVie;
        this.animation = new ArrayList<Animation>();
        box = new Rectangle(x - 16, y - 20, 32, 32);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    // mouvement du personnage
    public void mouvement(int delta, TiledMap map) {
        if (this.moving) {
            futurX = this.getFuturX(delta);
            futurY = this.getFuturY(delta);

            collision = iscollisionLogic(map, futurX, futurY);
            if(collision) {
                this.moving = false;
            } else {
                this.x = futurX;
                this.y = futurY;
                box.setBounds(futurX - 16, futurY - 20, 32, 32);
            }
        }
    }

    // changement de direction du personnage
    public void setDirection(int direction) {
        this.direction = direction;
    }

    // le personnage est en mouvement
    public void marcher() {
        this.moving = true;
    }

    // le personnage ne bouge pas
    public void stop() {
        this.moving = false;
    }

    // affichage du personnage dans le graphique
    public void afficher(Graphics g) {
        g.drawAnimation(this.animation.get(direction + (moving ? 4 : 0)), x - 16, y - 20);
        if(Fenetre.DEBUG) {
            g.setColor(Color.red);
            g.draw(this.box);
        }
    }

    // collision logic
    public boolean iscollisionLogic(TiledMap map, float x, float y) {
        int tileW = map.getTileWidth();
        int tileH = map.getTileHeight();

        int logicLayer = map.getLayerIndex("solide");
        Image tile = map.getTileImage((int) x / tileW, (int) y / tileH, logicLayer);
        boolean collision = tile != null;
        if(collision) {
            Color color = tile.getColor((int) x % tileW, (int) y % tileH);
            collision = color.getAlpha() > 0;
        }
        return collision;
    }

    public Rectangle getBoundingBox() {
        return this.box;
    }

    // chargement des animations pour un personnage.
    public void loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
        Animation animation = new Animation();
        for (int x = startX; x < endX; x++) {
            animation.addFrame(spriteSheet.getSprite(x, y), 150);
        }//return animation;
        this.animation.add(animation);
    }

    // clacule de la position (x) futur
    private float getFuturX(int delta) {
        float futurX = this.x;
        switch (this.direction) {
            case 1:
                futurX = this.x - .1f * delta;
                break;
            case 3:
                futurX = this.x + .1f * delta;
                break;
        }
        return futurX;
    }

    private float getFuturY(int delta) {
        float futurY = this.y;
        switch (this.direction) {
            case 0:
                futurY = this.y - .1f * delta;
                break;
            case 2:
                futurY = this.y + .1f * delta;
                break;
        }
        return futurY;
    }
}
