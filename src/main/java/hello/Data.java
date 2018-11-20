package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data
{
    private String currentIOWorkload;
    private String currentIOPS;
    private String timestamp;
    private TotalReadPerformances totalReadPerformances[];
    private TotalWritePerformances totalWritePerformances[];
    private TotalPerformances totalPerformances[];
    public Data(){

    }

    public void setCurrentIOWorkload(String currentIOWorkload)
    {
        this.currentIOWorkload=currentIOWorkload;
    }

    public String getCurrentIOWorkload()
    {
        return  this.currentIOWorkload;
    }

    public void setCurrentIOPS(String currentIOPS){
        this.currentIOPS=currentIOPS;
    }

    public String getCurrentIOPS()
    {
        return this.currentIOPS;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp=timestamp;
    }

    public  String getTimestamp()
    {
        return this.timestamp;
    }

    public void setTotalReadPerformances(TotalReadPerformances totalReadPerformances[])
    {
        this.totalReadPerformances= totalReadPerformances;
    }

    public TotalReadPerformances[] getTotalReadPerformances()
    {
        return this.totalReadPerformances;
    }

    public void setTotalWritePerformances(TotalWritePerformances totalWritePerformances[])
    {
        this.totalWritePerformances= totalWritePerformances;
    }

    public TotalWritePerformances[] getTotalWritePerformances()
    {
        return this.totalWritePerformances;
    }

    public void setTotalPerformances(TotalPerformances totalPerformances[])
    {
        this.totalPerformances= totalPerformances;
    }

    public TotalPerformances[] getTotalPerformances()
    {
        return this.totalPerformances;
    }

}
