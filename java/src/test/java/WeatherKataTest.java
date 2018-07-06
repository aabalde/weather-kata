import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class WeatherKataTest {

    public static final int ONE_DAY = 1000 * 60 * 60 * 24 * 1;

    // https://www.metaweather.com/api/location/766273/
    @Test
    public void find_the_weather_of_today_returns_sunny() throws Exception {
        String expectedWeather = "Sunny";
        String expectedWind = "4.5";
        Prediction expectedPrediction = new Prediction(expectedWeather,expectedWind);

        IForecast serviceStub = Mockito.mock(IForecast.class);
        when(serviceStub.predict(eq("Madrid"), any(Date.class))).thenAnswer(new Answer<Prediction>() {
            @Override
            public Prediction answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();

                Date date = (Date) args[1];
                Date today = new Date();

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Calendar calToday = Calendar.getInstance();
                calToday.setTime(today);

                if(cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                        && cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)
                        && cal.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH)){
                    return expectedPrediction;
                } else {
                    throw new ForecastException("The date received must be today");
                }
            }
        });

        Forecast forecast = new Forecast(serviceStub);
        String predictedWeather = forecast.predictWeather("Madrid",null);

        assertEquals(expectedWeather, predictedWeather);
    }

    @Test
    public void find_the_weather_of_any_day() throws Exception {
        Date tomorrow = new Date(new Date().getTime() + ONE_DAY);

        String expectedWeather = "Sunny";
        String expectedWind = "4.5";
        Prediction expectedPrediction = new Prediction(expectedWeather,expectedWind);

        IForecast serviceStub = Mockito.mock(IForecast.class);
        when(serviceStub.predict("Madrid", tomorrow)).thenReturn(expectedPrediction);

        Forecast forecast = new Forecast(serviceStub);
        String predictedWeather = forecast.predictWeather("Madrid",tomorrow);

        assertEquals(expectedWeather, predictedWeather);
    }
    @Test
    public void find_the_wind_of_any_day() throws Exception {
        Date tomorrow = new Date(new Date().getTime() + ONE_DAY*3);

        String expectedWeather = "Sunny";
        String expectedWind = "4.5";
        Prediction expectedPrediction = new Prediction(expectedWeather,expectedWind);

        IForecast serviceStub = Mockito.mock(IForecast.class);
        when(serviceStub.predict("Madrid", tomorrow)).thenReturn(expectedPrediction);

        Forecast forecast = new Forecast(serviceStub);
        String predictedWind = forecast.predictWind("Madrid",tomorrow);

        assertEquals(expectedWind, predictedWind);
    }

    @Test(expected = ForecastException.class)
    public void there_is_no_prediction_for_more_than_5_days() throws Exception {
        Forecast forecast = new Forecast();

        Date tomorrow = new Date(new Date().getTime() + (ONE_DAY * 6));

        forecast.predictWeather("Madrid",tomorrow);
    }
}
