import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Forecast {
    private int ONE_DAY = 1000 * 60 * 60 * 24;

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String URL_SEARCH_CITY = "https://www.metaweather.com/api/location/search/?query=";
    private static final String URL_PREDICT_WEATHER = "https://www.metaweather.com/api/location/";

    private static final String JSON_FIELD_CITY_ID = "woeid";
    private static final String JSON_FIELD_PREDICTION = "consolidated_weather";
    private static final String JSON_FIELD_DATE = "applicable_date";
    private static final String JSON_FIELD_WIND = "wind_speed";
    private static final String JSON_FIELD_WEATHER = "weather_state_name";

    public String predict(String city, Date datetime, boolean wind) throws IOException {
        // If there are predictions
        if (!datetime.before(new Date(new Date().getTime() + (ONE_DAY * 6)))) {
            return "";
        }

        // When date is not provided we look for the current prediction
        if (datetime == null) {
            datetime = new Date();
        }
        String format = new SimpleDateFormat(DATE_FORMAT).format(datetime);

        // Find the id of the city on metawheather
        HttpRequestFactory requestFactory
                = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(URL_SEARCH_CITY + city));
        String rawResponse = request.execute().parseAsString();
        JSONArray jsonArray = new JSONArray(rawResponse);
        String woeid = jsonArray.getJSONObject(0).get(JSON_FIELD_CITY_ID).toString();

        // Find the predictions for the city
        requestFactory = new NetHttpTransport().createRequestFactory();
        request = requestFactory.buildGetRequest(
                new GenericUrl(URL_PREDICT_WEATHER + woeid));
        rawResponse = request.execute().parseAsString();
        JSONArray results = new JSONObject(rawResponse).getJSONArray(JSON_FIELD_PREDICTION);

        for (int i = 0; i < results.length(); i++) {
//            // When the date is the expected
            if (format.equals(results.getJSONObject(i).get(JSON_FIELD_DATE).toString())) {
//                // If we have to return the wind information
                if (wind) {
                    return results.getJSONObject(i).get(JSON_FIELD_WIND).toString();
                } else {
                    return results.getJSONObject(i).get(JSON_FIELD_WEATHER).toString();
                }
            }
        }

        return "";
    }
}
