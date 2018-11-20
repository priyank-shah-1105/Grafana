package hello;

public class TotalReadPerformances
{
    private String value;
    private String timestamp;

    public TotalReadPerformances(){

    }

    public void setValue(String value)
    {
        this.value=value;
    }

    public String getValue()
    {
        return  this.value;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp=timestamp;
    }

    public String getTimestamp()
    {
        return  this.timestamp;
    }

}
