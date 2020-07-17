package Characters;

import Box2dHelpers.Box2dConversions;
import INPUTS.UserINputs;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class MainCharacter extends GameCharacter implements MovableCharacter {

    UserINputs inputs;
    Animation<TextureRegion> Attacking1Animation;
    Animation<TextureRegion> Attack2Animation;
    Animation<TextureRegion> RunningAnimation;

    boolean direction = false; //true = left , false = right

    float attackingElapsedTime = 0;
    float attacking2ElapsedTime = 0;
    float swordtime = 1.32f;
    Body sword;

    public MainCharacter(World world, TextureAtlas atlas, float x, float y, int width, int height, float speed, UserINputs inputs) {
        super(world, atlas, x, y, width, height);
        this.inputs = inputs;
        this.speed = speed;
        Attacking1Animation = new Animation<TextureRegion>(1 / 10f, atlas.findRegions("attacking1"));
        Attack2Animation = new Animation<TextureRegion>(1 / 10f, atlas.findRegions("secondAttack"));
        RunningAnimation = new Animation<TextureRegion>(1 / 20f, atlas.findRegions("running"));
        MakeSword();

    }

    public void MakeSword() {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(body.getPosition());

        sword = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Box2dConversions.unitsToMetres(10), Box2dConversions.unitsToMetres(40), body.getLocalCenter(), 45);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;


        sword.createFixture(fdef);


    }

    @Override
    public void CharacterState(float dt) {
        if (inputs.isRunningRight()) {
            direction = false;
            MoveRight();
            PlayRunningRightAnimation(dt, direction);
        }

        if (inputs.isStanding()) {
            Stop();
            ResetElapsetTimes();
            ResetFrame(direction);

        }
        if (inputs.isAttacking1()) {
            swordtime += dt;
            System.out.println("1");
            if (attackingElapsedTime >= 4 / 10f) {//check if the attack is finished or not
                ResetElapsetTimes();
                inputs.Stand();//make Character Stands
                ResetFrame(direction);
                sword.setTransform(body.getPosition().x + 10 / 200f, body.getPosition().y - 20 / 200f, 1.32f);
            }

            PLayAttacking1Animation(dt, direction);

        }

        if (inputs.isAttacking2()) {

            System.out.println("2");
            if (attacking2ElapsedTime >= 3 / 10f) {
                ResetElapsetTimes();
                inputs.Stand();
                ResetFrame(direction);
            }
            PlayAttacking2Animation(dt, direction);

        }

        if (inputs.isRunningleft()) {
            direction = true;
            MoveLeft();
            PlayRunningLeftAnimation(dt, direction);

        }


    }


    private void Stop() {

        body.setLinearVelocity(0, 0);

    }

    public void PlayRunningRightAnimation(float dt, boolean reverse) {

        RunningElapsedTime += dt;
        SetFrameFromAnimation(RunningAnimation, true, RunningElapsedTime, reverse);

    }

    public void PlayRunningLeftAnimation(float dt, boolean reverse) {

        RunningElapsedTime += dt;
        SetFrameFromAnimation(RunningAnimation, true, RunningElapsedTime, reverse);

    }

    public void PLayAttacking1Animation(float dt, boolean reverse) {
        attackingElapsedTime += dt;
        this.SetFrameFromAnimation(Attacking1Animation, false, attackingElapsedTime, reverse);


    }

    public void PlayAttacking2Animation(float dt, boolean reverse) {
        attacking2ElapsedTime += dt;
        this.SetFrameFromAnimation(Attack2Animation, false, attacking2ElapsedTime, reverse);

    }


    private void ResetElapsetTimes() {

        this.attackingElapsedTime = 0;
        this.RunningElapsedTime = 0;
        this.attacking2ElapsedTime = 0;

    }

    @Override
    public void update(SpriteBatch batch) {
        super.update(batch);

        sword.setTransform(body.getPosition().x, body.getPosition().y - 20 / 200f, 1.32f);


        System.out.println(sword.getAngle());
    }

    public void rotateObject(Body body, float x1, float y1, float width1,
                             float height1, float rotation, float PPM) {

        float DEGTORAD = 0.0174532925199432957f;
        // Top left corner of object
        Vector2 pos = new Vector2((x1) / PPM, (y1 + height1) / PPM);
        // angle of rotation in radians
        float angle = DEGTORAD * (rotation);
        // half of diagonal for rectangular object
        float radius = (float) ((Math
                .sqrt((width1 * width1 + height1 * height1))) / 2f) / PPM;
        // Angle at diagonal of rectangular object
        double theta = (Math.tanh(height1 / width1) * DEGTORAD);

        // Finding new position if rotation was with respect to top-left corner of object.
        // X=x+ radius*cos(theta-angle)+(h/2)cos(90+angle)
        // Y= y+radius*sin(theta-angle)-(h/2)sin(90+angle)
        pos = pos.add((float) (radius * Math.cos(-angle + theta)),
                (float) (radius * Math.sin(-angle + theta))).add(
                (float) ((height1 / PPM / 2) * Math.cos(90 * DEGTORAD
                        + angle)),
                (float) (-(height1 / PPM / 2) * Math.sin(90
                        * DEGTORAD + angle)));
        // transform the body
        body.setTransform(pos, -angle);

    }

    @Override
    public void MoveRight() {
        body.setLinearVelocity(speed, 0);
    }

    public void MoveLeft() {
        body.setLinearVelocity(-speed, 0);
    }
}