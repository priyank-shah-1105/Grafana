package hello;

import java.util.List;

public class QueryResponse
{
    private String target;
    private List<List<Double>> datapoints;

    public void setTarget(String target)
    {
        this.target=target;
    }
    public String getTarget()
    {
        return this.target;
    }
    public void setDatapoints(List<List<Double>> datapoints)
    {
        this.datapoints=datapoints;
    }
    public List<List<Double>> getDatapoints()
    {
        return this.datapoints;

    }

    //@Override
//    public String toString(){
//        return "{\"objects\" : [\"One\", \"Two\", \"Three\"]}";
//    }

}
