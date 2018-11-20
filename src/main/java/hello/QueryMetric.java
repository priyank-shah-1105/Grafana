package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryMetric
{
    private List<targetsInner> targets = new ArrayList<targetsInner>();

    public List<targetsInner> getTargets()
    {
        return this.targets;
    }
    public void setTargets(List<targetsInner> targets)
    {
        this.targets=targets;
    }
}
