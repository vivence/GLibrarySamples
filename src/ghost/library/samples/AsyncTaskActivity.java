package ghost.library.samples;

import ghost.library.concurrent.Task;
import ghost.library.concurrent.TaskDaemon;
import ghost.library.concurrent.TaskDaemonFactory;
import ghost.library.concurrent.Task.State;
import ghost.library.utility.IObserver;
import ghost.library.utility.IObserverManager;
import ghost.library.utility.IObserverTarget;
import ghost.library.utility.ObserverImpl;
import ghost.library.utility.ObserverManagerImpl;
import ghost.library.widget.LogTextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;

class AbortTask extends Task implements Runnable, Task.Context{
	
	private Thread workThread_;

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		Thread currentThread = Thread.currentThread();
		synchronized (this)
		{
			workThread_ = currentThread;
		}
		if (execute(this))
		{
			for (int i = 0; i < 15 && Task.State.EXECUTING == getState(); ++i)
			{
				Log.d(AsyncTaskActivity.LOG_TAG, this+" working("+(i+1)+") on thread: "+currentThread.getId());
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					interrupt();
					break;
				}
			}
			finish();
		}
		synchronized (this)
		{
			workThread_ = null;
		}
	}

	@Override
	public void interrupt()
	{
		// TODO Auto-generated method stub
		synchronized (this)
		{
			if (null != workThread_)
			{
				workThread_.interrupt();
				workThread_ = null;
			}
		}
	}
}

public class AsyncTaskActivity extends Activity implements IObserverManager {
	
	public static final String LOG_TAG = "async_task";
	
	class TaskObserver extends ObserverImpl implements Task.IObserver{
		
		private Handler handler_;
		
		public TaskObserver(Handler handler)
		{
			// TODO Auto-generated constructor stub
			handler_ = handler;
		}

		@Override
		protected IObserverManager getManager()
		{
			// TODO Auto-generated method stub
			return AsyncTaskActivity.this;
		}

		@Override
		public void onStateChanged(Task task, State oldState, State newState)
		{
			// TODO Auto-generated method stub
			final StringBuffer sb = new StringBuffer(task+" state changed: "+oldState.toString()+"-->"+newState.toString());
			if (task.isCanceled())
			{
				sb.append(" canceled");
			}
			if (task.isAborted())
			{
				sb.append(" aborted");
			}
			handler_.post(new Runnable() {
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					AsyncTaskActivity.this.newLog(sb.toString());
				}
			});
		}
		
	}
	
	private ExecutorService service_;
	private TaskObserver taskObserver_;
	private TaskDaemon taskDaemon_;
	private Handler handler_;
	
	private ObserverManagerImpl observerManagerImpl_;

	public AsyncTaskActivity()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void open(ExecutorService service)
	{
		close();
		service_ = service;
		taskDaemon_ = TaskDaemonFactory.newTaskDaemonWorkOnThread(1000);
	}
	
	public void close()
	{
		if (null != service_)
		{
			service_.shutdown();
			service_ = null;
		}
		if (null != taskDaemon_)
		{
			taskDaemon_.abortAllTasks();
			taskDaemon_ = null;
		}
	}
	
	public void newLog(CharSequence log)
	{
		Log.d(LOG_TAG, log.toString());
		LogTextView logTextView = (LogTextView)findViewById(R.id.text);
		Editable editable = logTextView.getText();
		editable.insert(0, "\n");
		editable.insert(0, log);
		logTextView.setText(editable);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.log);
		
		open(Executors.newSingleThreadExecutor());
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		if (null == handler_)
		{
			handler_ = new Handler();
			if (null == taskObserver_)
			{
				taskObserver_ = new TaskObserver(handler_);
			}
		}
		handler_.post(new Runnable() {
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				if (null != AsyncTaskActivity.this.handler_)
				{
					AbortTask task = new AbortTask();
					task.addObserver(AsyncTaskActivity.this.taskDaemon_);
					task.addObserver(AsyncTaskActivity.this.taskObserver_);
					if (null != AsyncTaskActivity.this.service_)
					{
						task.await(null);
						AsyncTaskActivity.this.service_.execute(task);
					}
					AsyncTaskActivity.this.handler_.postDelayed(this, 1000);
				}
			}
		});
	}
	
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		handler_ = null;
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		close();
		if (null != observerManagerImpl_)
		{
			observerManagerImpl_.clearTaskObservers();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void attachObserver(IObserver observer, IObserverTarget target)
	{
		// TODO Auto-generated method stub
		if (null == observerManagerImpl_)
		{
			observerManagerImpl_ = new ObserverManagerImpl();
		}
		observerManagerImpl_.attachObserver(observer, target);
	}

	@Override
	public void detachObserver(IObserver observer, IObserverTarget target)
	{
		// TODO Auto-generated method stub
		if (null != observerManagerImpl_)
		{
			observerManagerImpl_.detachObserver(observer, target);
		}
	}
	
}
