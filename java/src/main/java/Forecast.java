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
        return prediction.getWeather();
    }


    public String predictWind(String city, Date datetime) throws Exception {
        Date date = checkDate(datetime);
        Prediction prediction = this.service.predict(city, date);
        return prediction.getWind();
    }

    private Date checkDate(Date datetime) {
        Date date = datetime;
        if (datetime == null) {
            date = new Date();
        }
        return date;
    }
}
