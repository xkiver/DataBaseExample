package cl.telematica.databaseexample.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cl.telematica.databaseexample.models.EarthQuakeDataModel;

public class RssParser {
	
	public static List<EarthQuakeDataModel> getDataList(String data){
		List<EarthQuakeDataModel> list = new ArrayList<EarthQuakeDataModel>();
		
		try {
			JSONArray earthQuakeList = (JSONArray) new JSONTokener(data).nextValue();
			int size = earthQuakeList.length();
			for(int i = 0; i < size; i++){
				JSONObject details = earthQuakeList.getJSONObject(i);
				EarthQuakeDataModel model = new EarthQuakeDataModel();
				
				model.title = details.getString("title");
				model.magnitude = details.getString("magnitude");
				model.location = details.getString("location");
				model.depth = details.getString("depth");
				model.latitude = details.getString("latitude");
				model.longitude = details.getString("longitude");
				model.dateTime = details.getString("date_time");
				model.link = details.getString("link");
				
				list.add(model);
			}
			
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
