package swantron.project.servo;

import swantron.project.servo.*;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import android.os.Bundle;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;


/**
 * http://swantron.com | Joseph Swanson | 2011
 * IOIO Servo main activity|
 * Driving servo via Android 
 * utilizing PWM output from IOIO
 */
public class MainActivity extends AbstractIOIOActivity {
	private SeekBar _seekBarVar;
	private TextView _varField;
	private volatile float _varValue;

	/**
	 * Called upon creation for initialization
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		_seekBarVar = (SeekBar) findViewById(R.id.seekBarVar);
		_varField = (TextView) findViewById(R.id.textViewVarVal);
		_seekBarVar.setMax(100);
		_seekBarVar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
@Override
public void onStopTrackingTouch(SeekBar seekBar) {
}

@Override
public void onStartTrackingTouch(SeekBar seekBar) {
}

@Override

public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
_varValue = (float)progress / (float)_seekBarVar.getMax();
_varField.setText(String.valueOf(_varValue));
}
});

	}

	/**
	 * Primary thread...runs until interrupted.
	 */
	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		/** Define pins for out.(onboard LED, servo) */
		private PwmOutput _led;
		private PwmOutput _servo;

		/**
		 * Called every time a connection with IOIO has been established.
		 * (opens pins)
		 * 
		 * @throws ConnectionLostException
		 * (if IOIO connection is lost)
		 */
		@Override
		protected void setup() throws ConnectionLostException {
			_led = ioio_.openPwmOutput(0, 300);
			_servo = ioio_.openPwmOutput(5, 50);
		}

		/**
		 * Loop section
		 */
		@Override
		protected void loop() throws ConnectionLostException {
			_servo.setDutyCycle(0.05f + _varValue * 0.05f);
			_led.setDutyCycle(1- _varValue);
		}
	}

	/**
	 * A method to create our IOIO thread.
	 * Taken from IOIO example app
	 */
	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}
}