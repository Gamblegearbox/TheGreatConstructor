package rendering;

public class Material {


    private final Texture RGBAMap_0;
    private final Texture RGBAMap_1;
    private final Texture RGBAMap_2;
    private final Texture RGBAMap_3;
    private final Texture auxMap_0;

    public Material(Texture _RGBAMap_0, Texture _RGBAMap_1, Texture _RGBAMap_2, Texture _RGBAMap_3, Texture _auxMap_0)
    {
        RGBAMap_0 = _RGBAMap_0;
        RGBAMap_1 = _RGBAMap_1;
        RGBAMap_2 = _RGBAMap_2;
        RGBAMap_3 = _RGBAMap_3;
        auxMap_0 = _auxMap_0;
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

    public Texture getAuxMap_0(){
        return auxMap_0;
    }

}
