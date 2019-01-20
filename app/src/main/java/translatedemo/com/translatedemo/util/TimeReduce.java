package translatedemo.com.translatedemo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 计时
 * 
 * @author leihuajie
 *
 */
public class TimeReduce {
	private int recLen = 60;
	private TextView txtView;
	private int hour;
	private int minute;
	private boolean timeControl = true;
	private Context context;
	private TimerOver timerOver;

	public TimeReduce() {
	};
	public void setTimerOverListener(TimerOver timerOver){
		this.timerOver=timerOver;
	}
	public TimeReduce(TextView txtView, int hour, int minute, Context context) {
		this.txtView = txtView;
		this.hour = hour;
		this.minute = minute - 1;
		this.context = context;
		if (minute == 0) {
			this.hour--;
			this.minute = 59;
		}
		new Thread(new MyThread()).start();
	}

	public TimeReduce(TextView txtView, int leftTime, Context context) {
		this.txtView = txtView;
		this.context = context;
		int minute=leftTime/60;
		int sec=leftTime%60;
		if(sec==0){
			this.minute=minute-1;
		}else{
			this.minute=minute;
			this.recLen=sec;
		}
		new Thread(new MinuteReduce()).start();
	}
	final Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (recLen == -1) {
					timeControl = false;
					//MyApp.isTimeControl=false;
					Toast.makeText(context, "时间到了！", Toast.LENGTH_LONG).show();
					txtView.setText("");
					txtView.setVisibility(View.INVISIBLE);
					break;
				}
				recLen--;
				String hourTime = hour > 9 ? (hour + "") : ("0" + hour);
				String minuteTime = minute > 9 ? (minute + "") : ("0" + minute);
				String secondTime = recLen > 9 ? (recLen + "") : ("0" + recLen);
				txtView.setText("限时\t" + hourTime + ":" + minuteTime + ":" + secondTime);
				if (recLen == 0) {
					if (minute == 0) {
						if (hour == 0) {
							recLen = -1;
						} else {
							minute = 59;
							hour--;
						}

					} else {
						recLen = 60;
						minute--;
					}

				}
				break;
			case 2:
				if (recLen == -1) {
					timeControl = false;
				//	Toast.makeText(context, "讨论时间到了！", Toast.LENGTH_LONG).show();
					txtView.setText("");
					txtView.setVisibility(View.GONE);
					timerOver.timerOver();
					break;
				}
				recLen--;
				String min = minute > 9 ? (minute + "") : ("0" + minute);
				String sec = recLen > 9 ? (recLen + "") : ("0" + recLen);
				txtView.setText(min + ":" + sec);
				if (recLen == 0) {
					if (minute == 0) {
						recLen = -1;
					} else {
						recLen = 60;
						minute--;
					}

				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 设置时间是否倒计时
	 * false 不倒计时  true 倒计时
	 * @param timeControl
	 */
	public void setTimeControl(boolean timeControl) {
		this.timeControl = timeControl;
	}

	public class MyThread implements Runnable { // thread
		@Override
		public void run() {
			while (timeControl) {
				try {
					Thread.sleep(1000); // sleep 1000ms
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class MinuteReduce implements Runnable {

		@Override
		public void run() {
			while (timeControl) {
				try {
					Thread.sleep(1000); // sleep 1000ms
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}
	public interface TimerOver{
		void timerOver();
	}

}
