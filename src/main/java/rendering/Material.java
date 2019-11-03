package rendering;

public class Material {


    private final Texture RGBAMap_0;
    private final Texture RGBAMap_1;
    private final Texture RGBAMap_2;
    private final Texture RGBAMap_3;
    private final Texture RGBAMap_4;
    private final Texture RGBAMap_5;

    public Material(Texture _RGBAMap_0, Texture _RGBAMap_1, Texture _RGBAMap_2, Texture _RGBAMap_3, Texture _RGBAMap_4)
    {
        RGBAMap_0 = _RGBAMap_0;
        RGBAMap_1 = _RGBAMap_1;
        RGBAMap_2 = _RGBAMap_2;
        RGBAMap_3 = _RGBAMap_3;
        RGBAMap_4 = _RGBAMap_4;
        RGBAMap_5 = null;
    }

    public Material(Texture _RGBAMap_0, Texture _RGBAMap_1, Texture _RGBAMap_2, Texture _RGBAMap_3, Texture _RGBAMap_4, Texture _RGBAMap_5)
    {
        RGBAMap_0 = _RGBAMap_0;
        RGBAMap_1 = _RGBAMap_1;
        RGBAMap_2 = _RGBAMap_2;
        RGBAMap_3 = _RGBAMap_3;
        RGBAMap_4 = _RGBAMap_4;
        RGBAMap_5 = _RGBAMap_5;
    }

    public Texture getMap_0()
    {
        return RGBAMap_0;
    }

    public Texture getMap_1()
    {
        return RGBAMap_1;
    }

    public Texture getMap_2()
    {
        return RGBAMap_2;
    }

    public Texture getMap_3()
    {
        return RGBAMap_3;
    }

    public Texture getMap_4(){
        return RGBAMap_4;
    }

    public Texture getMap_5(){
        return RGBAMap_5;
    }

}
