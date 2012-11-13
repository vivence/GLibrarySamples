package ghost.library.samples;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class MultiTouchImageActivity extends Activity {

	public MultiTouchImageActivity()
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.image);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

}
