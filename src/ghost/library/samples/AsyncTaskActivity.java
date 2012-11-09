package ghost.library.samples;

import ghost.library.widget.LogTextView;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class AsyncTaskActivity extends Activity {

	public AsyncTaskActivity()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void newLog(CharSequence log)
	{
		LogTextView logTextView = (LogTextView)findViewById(R.id.text);
		logTextView.getText().insert(0, "\n");
		logTextView.getText().insert(0, log);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.log);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
}
