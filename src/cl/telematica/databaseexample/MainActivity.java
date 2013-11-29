package cl.telematica.databaseexample;

import java.util.List;

import cl.telematica.databaseexample.adapters.RssAdapter;
import cl.telematica.databaseexample.connections.ConnectionManager;
import cl.telematica.databaseexample.database.DataBaseClass;
import cl.telematica.databaseexample.interfaces.DownloadListener;
import cl.telematica.databaseexample.models.EarthQuakeDataModel;
import cl.telematica.databaseexample.parsers.RssParser;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity implements DownloadListener {
	
	private DataBaseClass dbInstance;
	
	private ProgressBar progressBar;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbInstance = new DataBaseClass(this);
		
		listView = (ListView) findViewById(R.id.listView1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		new ConnectionManager(this, 10000, 10000, "GET")
					.execute(getString(R.string.url));
	}

	@Override
	public void onRequestStart() {
		if(progressBar.getVisibility() == View.GONE){
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRequestComplete(String data) {
		if(progressBar.getVisibility() == View.VISIBLE){
			progressBar.setVisibility(View.GONE);
		}
		final List<EarthQuakeDataModel> list = RssParser.getDataList(data);
		
		saveToDataBase(list);
		
		RssAdapter adapter = new RssAdapter(getApplicationContext(), R.string.app_name, list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long value) {
				Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
				intent.putExtra("link", list.get(position).link);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onError(String error, int code) {
		if(progressBar.getVisibility() == View.VISIBLE){
			progressBar.setVisibility(View.GONE);
		}
	}
	
	private void saveToDataBase(List<EarthQuakeDataModel> list){
		SQLiteDatabase db = dbInstance.getWritableDatabase();
		if(db != null){
			db.beginTransaction();
			try{
			    for(EarthQuakeDataModel model : list){
					db.execSQL("INSERT INTO alumnos (title, magnitude, location, depth, latitude, longitude, datetime, link) " +
											"VALUES ('" + model.title + "', '" + 
														  model.magnitude + "', '" + 
														  model.location + "', '" + 
														  model.depth + "', '" + 
														  model.latitude + "', '" + 
														  model.longitude + "', '" + 
														  model.dateTime + "', '" + 
														  model.link + "')");
			    }
			} finally {
				db.setTransactionSuccessful();
			}
			db.endTransaction();
		    db.close();
		}
	}

}
