import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class WeatherKataTest {

    public static final int ONE_DAY = 1000 * 60 * 60 * 24 * 1;

    // https://www.metaweather.com/api/location/766273/
    @Test
    public void find_the_weather_of_today() throws IOException, ParseException {
        Forecast forecast = new Forecast();

        String prediction = forecast.predictWeather("Madrid",null);

        System.out.println("Today: " + prediction);
        assertTrue("I don't know how to test it", true);
    }
    @Test
    public void find_the_weather_of_any_day() throws IOException, ParseException {
        Forecast forecast = new Forecast();

        Date tomorrow = new Date(new Date().getTime() + ONE_DAY);

        String prediction = forecast.predictWeather("Madrid",tomorrow);
        System.out.println("Tomorrow: " + prediction);
        assertTrue("I don't know how to test it", true);
    }
    @Test
    public void find_the_wind_of_any_day() throws IOException, ParseException {
        Forecast forecast = new Forecast();

        String prediction = forecast.predictWind("Madrid",null);

        System.out.println("Wind: " + prediction);
        assertTrue("I don't know how to test it", true);
    }

    @Test
    public void there_is_no_prediction_for_more_than_5_days() throws IOException, ParseException {
        Forecast forecast = new Forecast();

        Date tomorrow = new Date(new Date().getTime() + (ONE_DAY * 6));

        String prediction = forecast.predictWeather("Madrid",tomorrow);
        assertEquals("", prediction);
    }
}
