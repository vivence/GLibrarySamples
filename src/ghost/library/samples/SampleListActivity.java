package ghost.library.samples;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class SampleListActivity extends ListActivity {
	
	public static class Adapter extends BaseAdapter implements OnItemClickListener{
		private List<Class<? extends Activity>> sampleActivityClasses_;
		
		public Adapter()
		{
			// TODO Auto-generated constructor stub
			sampleActivityClasses_ = new ArrayList<Class<? extends Activity>>();
			sampleActivityClasses_.add(AsyncTaskActivity.class);
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return sampleActivityClasses_.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return sampleActivityClasses_.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			View itemView = convertView;
			if (null == itemView)
			{
				itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_list_item, null);
			}
			((TextView)itemView).setText(sampleActivityClasses_.get(position).getSimpleName());
			return itemView;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// TODO Auto-generated method stub
			Context context = view.getContext();
			Intent intent = new Intent(context.getApplicationContext(), sampleActivityClasses_.get(position));
			context.startActivity(intent);
		}
	}
	
	public SampleListActivity()
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Adapter adapter = new Adapter();
		setListAdapter(adapter);
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(adapter);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	

}
