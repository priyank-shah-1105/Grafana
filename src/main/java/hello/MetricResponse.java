package hello;

public class MetricResponse
{
    private float value;
    private long milli;

    public void setValue(float value)
    {
        this.value=value;
    }

    public float getValue(){
        return this.value;
    }

    public void setMilli(long milli)
    {
        this.milli=milli;
    }
    public long getMilli(){
        return this.milli;
    }
}
