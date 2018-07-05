import java.util.Date;

public interface IForecast {

    Prediction predict(String city, Date datetime) throws Exception;
}
