package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class targetsInner
{
    private String target;
    private String refId;
    private String type;

    public String getTarget()
    {
        return this.target;
    }
    public void setTarget(String target)
    {
        this.target = target;
    }
    public String getRefId()
    {
        return this.refId;
    }
    public void setRefId(String refId)
    {
        this.refId = refId;
    }
    public String getType()
    {
        return this.type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
}
