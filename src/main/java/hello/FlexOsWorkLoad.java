package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexOsWorkLoad
{
    private Data data;
    private int code;
    private String error;

    public void setData(Data data)
    {
        this.data=data;
    }
    public Data getData()
    {
        return this.data;
    }

    public void setCode(int code)
    {
        this.code=code;
    }
    public int getCode()
    {
        return this.code;
    }

    public void setError(String error)
    {
        this.error=error;
    }
    public String getError()
    {
        return this.error;
    }

}
