package Box2dHelpers;

import Characters.MainCharacter;
import GameObjects.BodyData;
import GameObjects.GameObject;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.gamestates.PlayState;


public class Box2dCollideListeners implements ContactListener {
    public static boolean playeronGround = false;

    @Override
    public void beginContact(Contact contact) {
        // get fixture a and fixture b
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (FootCollidedWithGorund(fa, fb)) {
            playeronGround = true;
            System.out.println("ground");
        }


        checkEnemy(fa, fb);
        checkHercules(fa, fb);
        checkCoins(fa, fb);
        checkDrink(fa, fb);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (FootCollidedWithGorund(fa, fb)) {
            playeronGround = false;


            System.out.println("air");


        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean FootCollidedWithGorund(Fixture fa, Fixture fb) {
        if (fa.getUserData() != null && fa.getUserData().equals("foot") && fb.getUserData() != null && fb.getUserData() == "ground") {
            return true;
        }

        return (fb.getUserData() != null && fb.getUserData().equals("foot")&& fa.getUserData() != null && fa.getUserData() == "ground");
    }


    public int GetHealth(Body body) {

        BodyData bd = (BodyData) body.getUserData();
//        System.out.println("healt"+bd.health);
        return bd.health;
    }

    public void decreseHealth(Body body) {
        BodyData bd = (BodyData) body.getUserData();
        bd.decreseHealth();
    }

    void increaseHealth(Body body) {
    	BodyData bData = (BodyData) body.getUserData();
    	bData.increaseHealth();
    }
    
    public void checkHercules(Fixture fa, Fixture fb) {
        if (fa.getUserData() != null && fa.getUserData() == "hercules" && fb.getUserData() != null && fb.getUserData() == "enemy") {
            decreseHealth(fa.getBody());
        }

        if (fb.getUserData() != null && fb.getUserData() == "hercules" && fa.getUserData() != null && fa.getUserData() == "enemy") {
            decreseHealth(fb.getBody());
        }
    }

    public void checkEnemy(Fixture fa, Fixture fb) {
        if (fa.getUserData() != null && fa.getUserData() == "sword") {
//            System.out.println("hit");
//            System.out.println(this.GetHealth(fb.getBody()));
            this.decreseHealth(fb.getBody());

        }

        if (fb.getUserData() != null && fb.getUserData() == "sword") {
//            System.out.println("hit");
//             System.out.println(this.GetHealth(fa.getBody()));
            this.decreseHealth(fa.getBody());

        }
    }

    public void checkCoins(Fixture fa, Fixture fb) {

        if (fa.getUserData() != null && fa.getUserData() == "silverCoin" && fb.getUserData() != null && fb.getUserData() == "hercules") {
            System.out.println("silver hit");
            decreseHealth(fa.getBody());
            increaseMoney(PlayState.Hercules, 100);

        }

        if (fb.getUserData() != null && fb.getUserData() == "silverCoin" && fa.getUserData() != null && fa.getUserData() == "hercules") {
            System.out.println("silver hit");
            decreseHealth(fb.getBody());
            increaseMoney(PlayState.Hercules, 100);
        }


        if (fa.getUserData() != null && fa.getUserData() == "goldCoin" && fb.getUserData() != null && fb.getUserData() == "hercules") {
            System.out.println("gold hit");
            decreseHealth(fa.getBody());
            increaseMoney(PlayState.Hercules, 200);


        }

        if (fb.getUserData() != null && fb.getUserData() == "goldCoin" && fa.getUserData() != null && fa.getUserData() == "hercules") {
            System.out.println("gold hit");
            decreseHealth(fb.getBody());
            increaseMoney(PlayState.Hercules, 200);


        }

    }
    public void checkDrink(Fixture fa,Fixture fb) {
    	if(fa.getUserData() != null && fa.getUserData() == "drink" && fb.getUserData() != null && fb.getUserData() == "hercules") {
    		increaseHealth(fb.getBody());
    		decreseHealth(fa.getBody());
    		
    	}
    	if(fa.getUserData() != null && fa.getUserData() == "hercules" && fb.getUserData() != null && fb.getUserData() == "drink") {
    		increaseHealth(fa.getBody());
    		decreseHealth(fb.getBody());
    		
    	}
    	
    }

    void increaseMoney(GameObject object, float money) {
        MainCharacter m = (MainCharacter) object;
        m.increaseMoney(money);
    }


}
