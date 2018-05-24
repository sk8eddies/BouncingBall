

/**
 * The physics model.
 *
 * This class is where you should implement your bouncing balls model.
 *
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 *
 * @author Simon Robillard
 *
 */
class Model {

        double areaWidth, areaHeight, g;
        boolean colliding = false;
        Ball [] balls;

        Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;
        g = 9.82;

        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(2*width / 3, height * 6/8, 5, -1, 0.2);
        balls[1] = new Ball(1.75 * width / 3, height * 2/6, 2, 2.1, 0.4);
        //balls[2] = new Ball(2.5 * width / 3, height * 0.5  , -0.6, 0.6, 0.3);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        Ball ball1 = balls[0];
        Ball ball2 = balls[1];

        collideWithBorders(ball1, deltaT);
        collideWithBorders(ball2, deltaT);

        if(colliding){
            if(!ball1.isInCollisionRadius(ball2)){
                colliding = false;
            }
        } else {
            collideWithBalls(ball1, ball2);
        }

        // compute new position according to the speed of the ball
        ball1.x += deltaT * ball1.vx;
        ball2.x += deltaT * ball2.vx;

        ball1.y += deltaT * ball1.vy;
        ball2.y += deltaT * ball2.vy;

    }


    private void collideWithBorders(Ball b, double deltaT){
        // detect collision with the border
        if (b.x < b.radius) {
            b.vx = Math.abs(b.vx); // change direction of ball
        }
        else if (b.x > areaWidth - b.radius) {
            b.vx = -Math.abs(b.vx); // change direction of ball
        }
        // detect collision with the floor/roof
        else if (b.y < b.radius) {
            b.vy = Math.abs(b.vy);
        }
        else if (b.y > areaHeight - b.radius) {
            b.vy = -Math.abs(b.vy);
        }
        else{
            // compute new speed according to the acceleration of the ball
            b.vy -= deltaT * g;
        }

    }

    private void collideWithBalls(Ball ball1, Ball ball2){
            if(ball1.isInCollisionRadius(ball2)) {
                Vector2D line = new Vector2D(ball1.x - ball2.x, ball1.y - ball2.y);

                PolarCoords lineCoords = BallUtil.cartesianToPolar(line);

                // Rotate the velocities for ball1 to the correct coordinate system
                Vector2D ball1Velocity = BallUtil.rotateCoordinateSystem(new Vector2D(ball1.vx, ball1.vy), lineCoords.angle);

                // Rotate the velocities for ball2 to the correct coordinate system
                Vector2D ball2Velocity = BallUtil.rotateCoordinateSystem(new Vector2D(ball2.vx, ball2.vy), lineCoords.angle);


                // CORRECT, THIS IS THE NEW VELOCITY CALCULATIONS, PRASANTH
                double I = ball1.mass*ball1Velocity.getX() + ball2.mass*ball2Velocity.getX();
                double R = -(ball2Velocity.getX() - ball1Velocity.getX());
                double newVelocity1 = (I - ball2.mass*R)/(ball1.mass + ball2.mass);
                double newVelocity2 = R + newVelocity1;


                double ball1NewVelocity = newVelocity1;
                double ball2NewVelocity = newVelocity2;

                // WRONG, OLD CALCULATIONS WHERE WE SWAPPED KINETIC ENERGY
                //ball2Velocity.getX()*Math.sqrt(ball2.mass/ball1.mass);
                //ball1Velocity.getX()*Math.sqrt(ball1.mass/ball2.mass);

                ball1Velocity.setX(ball1NewVelocity);
                ball2Velocity.setX(ball2NewVelocity);

                // Rotate back to original coordinate system
                ball1Velocity = BallUtil.rotateBackwards(ball1Velocity, lineCoords.angle);
                ball2Velocity = BallUtil.rotateBackwards(ball2Velocity, lineCoords.angle);

                // Set the balls new velocity
                ball1.vx = ball1Velocity.getX();
                ball2.vx = ball2Velocity.getX();
                ball1.vy = ball1Velocity.getY();
                ball2.vy = ball2Velocity.getY();


                // The remaining, commented out code in this method moves the balls out of each other.
                // This is not necessary when collision is handled as it should, but we left it here anyway.

                // Assert only one hit, move out of distance
               /* Vector2D ball1Position = BallUtil.rotateCoordinateSystem(new Vector2D(ball1.x, ball1.y), lineCoords.angle);
                Vector2D ball2Position = BallUtil.rotateCoordinateSystem(new Vector2D(ball2.x, ball2.y), lineCoords.angle);

                double overlap = (ball1.radius + ball2.radius) - Math.abs(ball1Position.getX()-ball2Position.getX());

                if(ball1Position.getX() < ball2Position.getX()){
                    ball1Position.setX(ball1Position.getX() - overlap/2);
                    ball2Position.setX(ball2Position.getX() + overlap/2);
                } else {
                    ball1Position.setX(ball1Position.getX() + overlap/2);
                    ball2Position.setX(ball2Position.getX() - overlap/2);
                }

                ball1Position = BallUtil.rotateBackwards(ball1Position, lineCoords.angle);
                ball2Position = BallUtil.rotateBackwards(ball2Position, lineCoords.angle);

                // Set non-colliding position
                ball1.x = ball1Position.getX();
                ball1.y = ball1Position.getY();
                ball2.x = ball2Position.getX();
                ball2.y = ball2Position.getY();*/

            }
    }

    /**
     * Simple inner class describing balls.
     */
    class Ball {

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius, mass;

        Ball(double x, double y, double vx, double vy, double r) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = r;
            this.mass = radius*radius*Math.PI;
        }

        public boolean isInCollisionRadius(Ball otherBall){
            Vector2D v = new Vector2D(x-otherBall.x, y-otherBall.y);
            return (BallUtil.cartesianToPolar(v).magnitude < radius+otherBall.radius);
        }

    }
}
