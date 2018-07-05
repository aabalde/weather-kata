import java.text.ParseException;
import java.util.Date;

public class Forecast {

    private IForecast service;

    public Forecast(){
        this.service = new HttpForecast();
    }

    public Forecast(IForecast service){
        this.service = service;
    }

    public String predictWeather(String city, Date datetime) throws Exception {
        Date date = checkDate(datetime);
        Prediction prediction = this.service.predict(city, date);
        if(prediction == null){
            return "";
        }

        // If we have to return the wind information
        return prediction.getWeather();
    }


    public String predictWind(String city, Date datetime) throws Exception {
        Date date = checkDate(datetime);
        Prediction prediction = this.service.predict(city, date);
        if(prediction == null){
            return "";
        }

        // If we have to return the wind information
        return prediction.getWind();
    }

    private Date checkDate(Date datetime) {
        Date date = null;
        if (datetime == null) {
            date = new Date();
        }
        return date;
    }
}
