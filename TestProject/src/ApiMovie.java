

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiMovie extends Thread{

	public final static String apiURL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    public final static String movieURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    public final static String KEY = "ca902f2712bf92fc02cbedd133e32606";
    public static String today;

    ApiMovie(String today){
        this.today = today;
    }

    public void run(){
        main();
    }

    
    public static void main() {
    	
    	/*url*/
        String url = apiURL+"?key="+KEY+"&targetDt="+today;
        String responseBody = get(url);
        parseData(responseBody);

    }

    private static String get(String apiUrl) {
        String responseBody = null;
        try {
            URL url = new URL(apiUrl);
            //System.out.println(url);
            
			/*
			 * url.openStream을 통해서 url주소를 InputStream에 넣어준다. 다양한 입출력 기능 가능
			 */
			java.io.InputStream in = url.openStream(); 
            responseBody = readBody(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseBody;
    }

	/* 요청받은 URL을 읽는 함수 */
    private static String readBody(java.io.InputStream in) throws UnsupportedEncodingException{
    	/*한글 인코딩을 해주어야 한글이 꺠지지 않는다*/
        InputStreamReader streamReader = new InputStreamReader(in,"UTF-8");

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private static void parseData(String responseBody) {
		
    	String title, openDt, movieCd;
		 
    	//String key,movi
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseBody.toString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray jsonArray = jsonObject1.getJSONArray("dailyBoxOfficeList");
            //jsonArray = jsonObject.getJSONArray("")

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                title = item.getString("movieNm");
                openDt = item.getString("openDt");
                movieCd = item.getString("movieCd");
                System.out.println("movieNm : " + title+"\nopen_day : "+openDt+"\nmovieCd : "+movieCd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
    
