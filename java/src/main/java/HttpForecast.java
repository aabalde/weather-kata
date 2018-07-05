import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpForecast implements IForecast {

    private int ONE_DAY = 1000 * 60 * 60 * 24;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

    private static final String URL_SEARCH_CITY = "https://www.metaweather.com/api/location/search/?query=";
    private static final String URL_PREDICT_WEATHER = "https://www.metaweather.com/api/location/";

    private static final String JSON_FIELD_CITY_ID = "woeid";
    private static final String JSON_FIELD_PREDICTION = "consolidated_weather";
    private static final String JSON_FIELD_DATE = "applicable_date";
    private static final String JSON_FIELD_WIND = "wind_speed";
    private static final String JSON_FIELD_WEATHER = "weather_state_name";

    @Override
    public Prediction predict(String city, Date date) throws Exception {
        Date dateFormatted = formatDate(date);
        if (!isPredictionAvailable(date)) {
            throw new ForecastException("Prediction not available. Exceeds the 5 days limit");
        }

        String cityId = getCityId(city);
        JSONArray predictions = getOneWeekPredictions(cityId);
        JSONObject predictionJson = getPrediction(dateFormatted, predictions);

        Prediction prediction = new Prediction(predictionJson.get(JSON_FIELD_WEATHER).toString(),
                predictionJson.get(JSON_FIELD_WIND).toString());

        return prediction;
    }


    private JSONObject getPrediction(Date date, JSONArray predictions) throws ParseException, ForecastException {
        for (int i = 0; i < predictions.length(); i++) {
            JSONObject currentPrediction = predictions.getJSONObject(i);
            String currentDate = currentPrediction.get(JSON_FIELD_DATE).toString();
            if (date.equals(format.parse(currentDate))) {
                return currentPrediction;
            }
        }
        throw new ForecastException("Prediction not found for the date provided");
    }

    private String doHttpRequest(String url, String param) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(url + param));
        String rawResponse = request.execute().parseAsString();
        return rawResponse;
    }

    private JSONArray getOneWeekPredictions(String cityId) throws IOException {
        String response = doHttpRequest(URL_PREDICT_WEATHER,cityId);
        return new JSONObject(response).getJSONArray(JSON_FIELD_PREDICTION);
    }


    private String getCityId(String city) throws IOException {
        String response = doHttpRequest(URL_SEARCH_CITY, city);
        JSONArray jsonArray = new JSONArray(response);
        return jsonArray.getJSONObject(0).get(JSON_FIELD_CITY_ID).toString();
    }


    private boolean isPredictionAvailable(Date datetime) throws Exception{
        long today = new Date().getTime();
        Date oneWeekLater = format.parse(format.format(new Date(today + (ONE_DAY * 6))));
        return datetime.before(oneWeekLater);
    }

    private Date formatDate(Date date) throws ParseException {
        return format.parse(format.format(date));
    }
}
