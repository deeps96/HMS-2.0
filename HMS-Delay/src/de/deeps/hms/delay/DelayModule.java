package de.deeps.hms.delay;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.deeps.hms.FinalModule;
import de.deeps.hms.delay.parameters.DelayParameter;
import de.deeps.hms.delay.parameters.StopDelayParameter;
import de.deeps.hms.delay.parameters.TimerParameter;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class DelayModule extends FinalModule {

	private final static String MODULE_CONFIG = "delay_config.json";

	private List<String> delayTargeturls;
	private List<Timer> timers;

	public DelayModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		timers = new LinkedList<>();
		delayTargeturls = new LinkedList<>();
		super.initialize();
	}

	@Override
	protected void fillMethodMap() {
		addDelayMethod();
		addTimerMethod();
		addStopDelayMethod();
	}

	private void addStopDelayMethod() {
		methodMap.put("stopDelay", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				StopDelayParameter parameter = (StopDelayParameter) rawParameter;
				delayTargeturls.remove(parameter.getTargetUrl());
				return null;
			}
		});
	}

	private void addTimerMethod() {
		methodMap.put("timer", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				timer((TimerParameter) rawParameter);
				return null;
			}
		});
	}

	private void addDelayMethod() {
		methodMap.put("delay", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				delay((DelayParameter) rawParameter);
				return null;
			}
		});
	}

	private void delay(DelayParameter parameter) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					delayTargeturls.add(parameter.getTargetUrl());
					Thread.sleep(parameter.getDelayInMS());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (delayTargeturls.contains(parameter.getTargetUrl())) {
					sendPost(parameter.getTargetUrl(), parameter.getContent());
					delayTargeturls.remove(parameter.getTargetUrl());
				}
			}
		}).start();
	}

	private void timer(TimerParameter parameter) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sendPost(parameter.getTargetUrl(), parameter.getContent());
			}
		};
		if (parameter.getRepeatAllMs() == 0) {
			timer.schedule(task, parameter.getExecutionDate());
		} else {
			timer.schedule(
				task,
				parameter.getExecutionDate(),
				parameter.getRepeatAllMs());
		}
		timers.add(timer);
	}

	public static void main(String[] args) throws IOException {
		new DelayModule();
	}

}
