package oldEngineStuff.engine.physics;


import org.joml.Math;

public class Physics
{
    public static final float G = 9.81f;

    public static float calcAcceleration(float mass, float force)
    {
        return force / mass;
    }

    public static float calcForce(float mass, float acceleration)
    {
        return mass * acceleration;
    }

    public static float calcTurningRadius(float wheelbase, float steeringAngle)
    {
        float magicOffset = steeringAngle / 50;
        return (wheelbase /(float)Math.sin(Math.toRadians(steeringAngle))) - magicOffset;
    }

    public static float calcRadialForce(float mass, float speed, float turningRadius)
    {
        float result = mass * (speed * speed) / turningRadius;

        return result;
    }

    public static float calcWeight(float mass)
    {
        return mass * G;
    }

    public static float calcAirResistanceConstant(float cw, float a)
    {
        return 0.5f * cw * a;
    }

    public static float calcAirResistanceForce(float k, float v)
    {
        return k * (v * v);
    }


}
