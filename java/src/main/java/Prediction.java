public class Prediction {

    String weather;

    String wind;

    public Prediction() {
        
    }

    public Prediction(String weather, String wind) {
        this.weather = weather;
        this.wind = wind;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
