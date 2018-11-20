package hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class EndPointController
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/")
    public void ServerUp() {
//        response.addHeader("Access-Control-Allow-Origin","*");
//        response.addHeader("Access-Control-Allow-Methods", "POST");
//        response.addHeader("Access-Control-Allow-Headers", "accept, content-type");
        log.info("OK");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/search")
    public String SearchMetrics() {
        //over here we can populate the metrics upon

        log.info("Trying to search on this particular metrics");
        String arr[]={"currentIOWorkload","currentIOPS","totalReadIOPS","totalWriteIOPS","totalIOPS","totalReadBW","totalWriteBW","totalBW"};
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString="";
        try{
            jsonInString = mapper.writeValueAsString(arr);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        log.info("The Json string is "+jsonInString);
        return jsonInString;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/query")
    public String QueryMetrics(@RequestBody String json,RestTemplate restTemplate) {
        //over here we can return metrics based upon the input

        log.info("Trying to query on this particular metrics");
        log.info(json);

        //fetching the value from grafana
        ObjectMapper objectMapper= new ObjectMapper();
        String value="";
        String jsonString="";
        QueryResponse qr=null; //nothing but the response which we need to send back to grafana
        try{
            QueryMetric q= objectMapper.readValue(json,QueryMetric.class);
            List<targetsInner> l = q.getTargets(); //it will give the name of the metric which we are looking for
            //here we will be getting our target metric which is selected by the user
            log.info("The target which we are looking for is "+l.get(0).getTarget());

            //if want to add more than one target in the response then place the below code in for loop
            value=l.get(0).getTarget();

            qr= new QueryResponse();
            qr.setTarget(value);//setting the target of the response

            //trying to set the response now
            //but getting the data first from the telemetry service
            //getCurrentFlexOSMetrics

            List<List<Double>> mainList=new ArrayList<>();

            if(value.equals("currentIOWorkload") || value.equals("currentIOPS")) {
                HashMap<String, Double> hm = GetCurrentFlexOsMetrics(restTemplate);
                mainList = setFlexOsMetricsResponse(hm, value);//setting the datapoints as in the data and the timestamp
            }

            else if(value.equals("totalReadIOPS"))
            {
                qr.setTarget("totalReadPerformances");//setting the target of the response

                TotalReadPerformances totalReadPerformances[]=getTotalReadIOPS(restTemplate);
                mainList = setTotalReadIOPS(totalReadPerformances);
            }

            else if(value.equals("totalWriteIOPS"))
            {
                qr.setTarget("totalWritePerformances");//setting the target of the response

                TotalWritePerformances totalWritePerformances[]=getTotalWriteIOPS(restTemplate);
                mainList = setTotalWriteIOPS(totalWritePerformances);
            }
            else if(value.equals("totalIOPS"))
            {
                qr.setTarget("totalPerformances");//setting the target of the response
                TotalPerformances totalPerformances[]=getTotalIOPS(restTemplate);
                mainList = setTotalIOPS(totalPerformances);
            }
            else if(value.equals("totalReadBW"))//fetching the total read bandwidth
            {
                qr.setTarget("totalReadPerformances");//setting the target of the response

                TotalReadPerformances totalReadPerformances[]=getTotalReadBW(restTemplate);
                mainList = setTotalReadIOPS(totalReadPerformances);
            }

            else if(value.equals("totalWriteBW"))//fetching the total write bandwidth
            {
                qr.setTarget("totalWritePerformances");//setting the target of the response

                TotalWritePerformances totalWritePerformances[]=getTotalWriteBW(restTemplate);
                mainList = setTotalWriteIOPS(totalWritePerformances);
            }
            else if(value.equals("totalBW")) //fetching the total bandwidth
            {
                qr.setTarget("totalPerformances");//setting the target of the response
                TotalPerformances totalPerformances[]=getTotalBW(restTemplate);
                mainList = setTotalIOPS(totalPerformances);
            }

            //setting the dataPoints and it will be common for everyone
            qr.setDatapoints(mainList);
            QueryResponse qrArray[]={qr};

            jsonString=objectMapper.writeValueAsString(qrArray);

        }
        catch(Exception e)
        {

        }
        log.info("The final jsonString is "+jsonString);
        return jsonString;
    }

    private TotalPerformances[] getTotalBW(RestTemplate restTemplate)
    {
        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/bw?host=100.80.96.36&frequency=hourly", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        TotalPerformances totalPerformances[] = d.getTotalPerformances();
        return totalPerformances;
    }

    private TotalWritePerformances[] getTotalWriteBW(RestTemplate restTemplate)
    {
        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/bw?host=100.80.96.36&frequency=hourly", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        TotalWritePerformances totalWritePerformances[] = d.getTotalWritePerformances();
        //log.info("The data value is "+ totalReadPerformances[0].getValue());

        //log.info("check over here");
        return totalWritePerformances;
    }

    private TotalReadPerformances[] getTotalReadBW(RestTemplate restTemplate)
    {
        //reusing FlexOSworkLoad class as it has same structure

        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/bw?host=100.80.96.36&frequency=hourly", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        TotalReadPerformances totalReadPerformances[] = d.getTotalReadPerformances();
        //log.info("The data value is "+ totalReadPerformances[0].getValue());

        //log.info("check over here");
        return totalReadPerformances;
    }

    private List<List<Double>> setTotalIOPS(TotalPerformances[] totalPerformances)
    {
        List<List<Double>> mainList= new ArrayList<>();

        for(TotalPerformances totalPerformances1 : totalPerformances)
        {

            List<Double> temp= new ArrayList<Double>();
            String time=totalPerformances1.getTimestamp();

            try
            {
                //converting the date into appropriate format
                long epoch = getTime(time);
                temp.add(Double.valueOf(totalPerformances1.getValue()));
                temp.add((double)epoch);
                mainList.add(temp);
            }
            catch (Exception e)
            {
                log.error(e.toString());
            }

        }
        return mainList;
    }

    private TotalPerformances[] getTotalIOPS(RestTemplate restTemplate)
    {
        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/iops?host=100.80.96.36&frequency=hourly", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        TotalPerformances totalPerformances[] = d.getTotalPerformances();
        return totalPerformances;
    }

    private List<List<Double>> setTotalWriteIOPS(TotalWritePerformances[] totalWritePerformances)
    {
        List<List<Double>> mainList= new ArrayList<>();

        for(TotalWritePerformances totalWritePerformances1 : totalWritePerformances)
        {

            List<Double> temp= new ArrayList<Double>();
            String time=totalWritePerformances1.getTimestamp();

            try
            {
                //converting the date into appropriate format
                long epoch = getTime(time);
                temp.add(Double.valueOf(totalWritePerformances1.getValue()));
                temp.add((double)epoch);
                mainList.add(temp);
            }
            catch (Exception e)
            {
                log.error(e.toString());
            }

        }
        return mainList;
    }

    private TotalWritePerformances[] getTotalWriteIOPS(RestTemplate restTemplate)
    {
        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/iops?host=100.80.96.36&frequency=hourly", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        TotalWritePerformances totalWritePerformances[] = d.getTotalWritePerformances();
        //log.info("The data value is "+ totalReadPerformances[0].getValue());

        //log.info("check over here");
        return totalWritePerformances;
    }


    private List<List<Double>> setTotalReadIOPS(TotalReadPerformances[] totalReadPerformances)
    {
        List<List<Double>> mainList= new ArrayList<>();

        for(TotalReadPerformances totalReadPerformances1 : totalReadPerformances)
        {

            List<Double> temp= new ArrayList<Double>();
            String time=totalReadPerformances1.getTimestamp();

            try
            {
                //converting the date into appropriate format
                long epoch = getTime(time);
                temp.add(Double.valueOf(totalReadPerformances1.getValue()));
                temp.add((double)epoch);
                mainList.add(temp);
            }
            catch (Exception e)
            {
                log.error(e.toString());
            }

        }

        return mainList;
    }

    private long getTime(String time)
    {
        long epoch=0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        try {

            Date dtin = formatter.parse(time);//converting the time into current local time
            epoch = dtin.getTime();
        }
        catch(Exception e)
        {

        }
        return epoch;
    }

    private TotalReadPerformances[] getTotalReadIOPS(RestTemplate restTemplate)
    {
        //reusing FlexOSworkLoad class as it has same structure

        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/iops?host=100.80.96.36&frequency=hourly", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        TotalReadPerformances totalReadPerformances[] = d.getTotalReadPerformances();
        //log.info("The data value is "+ totalReadPerformances[0].getValue());

        //log.info("check over here");
        return totalReadPerformances;
    }

    private List<List<Double>> setFlexOsMetricsResponse(HashMap<String, Double> hm, String value)
    {
        List<List<Double>> mainList= new ArrayList<>();
        List<Double> temp= new ArrayList<Double>();

        //checking the hashmap
        if(hm.containsKey(value))
        {
            temp.add(hm.get(value));
            temp.add(hm.get("timestamp"));
        }

        mainList.add(temp);
        return mainList;
    }

    private HashMap<String, Double> GetCurrentFlexOsMetrics(RestTemplate restTemplate)
    {
        HashMap<String,Double> hm = new HashMap<>();
        FlexOsWorkLoad fwl=restTemplate.getForObject("http://localhost:8084/api/telemetry/flexos/workload?host=100.80.96.36", FlexOsWorkLoad.class);
        Data d= fwl.getData();
        String arr[]=d.getTimestamp().split("\\+");
        LocalDateTime dateTime = LocalDateTime.parse(arr[0]);
        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = dateTime.atZone(zoneId).toInstant().toEpochMilli();
        hm.put("currentIOWorkload",Double.valueOf(d.getCurrentIOWorkload().split(" ")[0]));
        hm.put("currentIOPS",Double.valueOf(d.getCurrentIOPS().split(" ")[0]));
        hm.put("timestamp",(double)epoch);
        return hm;
    }
}
