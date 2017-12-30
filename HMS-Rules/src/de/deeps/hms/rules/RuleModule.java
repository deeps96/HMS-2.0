package de.deeps.hms.rules;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import de.deeps.hms.FinalModule;
import de.deeps.hms.json.JsonConverter;
import de.deeps.hms.rules.core.ActionEvent;
import de.deeps.hms.rules.core.ActionEvents;
import de.deeps.hms.rules.core.Comparator;
import de.deeps.hms.rules.core.Condition;
import de.deeps.hms.rules.core.Rule;
import de.deeps.hms.rules.parameters.AddRuleParameter;
import de.deeps.hms.rules.parameters.ExecuteRuleParameter;
import de.deeps.hms.rules.parameters.RegisterListenerParameter;
import de.deeps.hms.rules.parameters.RemoveRuleParameter;
import de.deeps.hms.rules.parameters.SetStartEventsParameter;
import de.deeps.hms.rules.parameters.SetStopEventsParameter;
import de.deeps.hms.rules.parameters.UnregisterListenerParameter;
import de.deeps.hms.webserver.Command;
import de.deeps.hms.webserver.CommandExecutionInterface;

/**
 * @author Deeps
 */

public class RuleModule extends FinalModule {

	private final static String MODULE_CONFIG = "rule_config.json",
			START_EVENTS = "start_events.json",
			STOP_EVENTS = "stop_events.json", RULES_DIR = "rules";

	private HashMap<Rule, Long> lastTimeRuleApplied;
	private List<Rule> rules;

	public RuleModule() throws IOException {
		super(MODULE_CONFIG);
	}

	@Override
	protected void initialize() throws IOException {
		lastTimeRuleApplied = new HashMap<>();
		loadRules();
		super.initialize();
	}

	private void loadRules() {
		rules = new LinkedList<>();
		File ruleDir = new File(RULES_DIR);
		ruleDir.mkdir();
		if (ruleDir.exists() && ruleDir.isDirectory()) {
			for (File ruleFile : ruleDir.listFiles()) {
				rules.add(
					JsonConverter.jsonFileToObject(
						ruleFile.getAbsolutePath(),
						Rule.class));
			}
		}
	}

	@Override
	protected void start() {
		super.start();
		executeActionEvents(START_EVENTS);
	}

	@Override
	protected void stop() {
		executeActionEvents(STOP_EVENTS);
		super.stop();
	}

	private void executeActionEvents(String eventsJson) {
		executeActionEvents(
			JsonConverter.jsonFileToObject(eventsJson, ActionEvents.class)
					.getEvents());
	}

	private void executeActionEvents(List<ActionEvent> events) {
		for (ActionEvent event : events) {
			executeEvent(event);
		}
	}

	private void executeEvent(ActionEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendPost(event.getTargetUrl(), event.getContent());
			}
		}).start();
	}

	@Override
	protected void fillMethodMap() {
		redirectReplies();
		addAddRuleMethod();
		addRemoveRuleMethod();
		addExecuteRuleMethod();
		addListRulesMethod();
		addListStartEventsMethod();
		addListStopEventsMethod();
		addSetStartEventsMethod();
		addSetStopEventsMethod();
		addRegisterListenerMethod();
		addUnregisterListenerMethod();
	}

	private void addUnregisterListenerMethod() {
		methodMap.put("unregisterListener", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				UnregisterListenerParameter parameter = (UnregisterListenerParameter) rawParameter;
				config.getHttpConfig().getSupportedCommands()
						.add(new Command(parameter.getCommandUri()));
				JsonConverter.writeObjectToJsonFile(config, MODULE_CONFIG);
				return null;
			}
		});
	}

	private void addRegisterListenerMethod() {
		methodMap.put("registerListener", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				RegisterListenerParameter parameter = (RegisterListenerParameter) rawParameter;
				config.getHttpConfig().getSupportedCommands()
						.add(new Command(parameter.getCommandUri()));
				JsonConverter.writeObjectToJsonFile(config, MODULE_CONFIG);
				return null;
			}
		});
	}

	private void addSetStopEventsMethod() {
		methodMap.put("setStopEvents", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				SetStopEventsParameter parameter = (SetStopEventsParameter) rawParameter;
				JsonConverter.writeObjectToJsonFile(
					parameter.getEvents(),
					STOP_EVENTS);
				return null;
			}
		});
	}

	private void addSetStartEventsMethod() {
		methodMap.put("setStartEvents", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				SetStartEventsParameter parameter = (SetStartEventsParameter) rawParameter;
				JsonConverter.writeObjectToJsonFile(
					parameter.getEvents(),
					START_EVENTS);

				return null;
			}
		});
	}

	private void addListStopEventsMethod() {
		methodMap.put("listStopEvents", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ActionEvents events = JsonConverter
						.jsonFileToObject(STOP_EVENTS, ActionEvents.class);
				return JsonConverter.objectToJsonString(events);
			}
		});
	}

	private void addListStartEventsMethod() {
		methodMap.put("listStartEvents", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ActionEvents events = JsonConverter
						.jsonFileToObject(START_EVENTS, ActionEvents.class);
				return JsonConverter.objectToJsonString(events);
			}
		});
	}

	private void addListRulesMethod() {
		methodMap.put("listRules", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				return JsonConverter.objectToJsonString(rules);
			}
		});
	}

	private void addExecuteRuleMethod() {
		methodMap.put("executeRule", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				ExecuteRuleParameter parameter = (ExecuteRuleParameter) rawParameter;
				for (Rule rule : rules) {
					if (rule.getName().equals(parameter.getName())) {
						applyRule(rule);
						break;
					}
				}
				return null;
			}
		});
	}

	private void addRemoveRuleMethod() {
		methodMap.put("removeRule", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				RemoveRuleParameter parameter = (RemoveRuleParameter) rawParameter;
				removeRule(parameter.getName());
				return null;
			}
		});
	}

	private void removeRule(String ruleName) {
		for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext();) {
			Rule rule = iterator.next();
			if (rule.getName().equals(ruleName)) {
				iterator.remove();
			}
		}
		new File(RULES_DIR, ruleName + ".json").delete();
	}

	private void addAddRuleMethod() {
		methodMap.put("addRule", new CommandExecutionInterface() {
			@Override
			public String run(Object rawParameter) {
				AddRuleParameter parameter = (AddRuleParameter) rawParameter;
				removeRule(parameter.getRule().getName());
				rules.add(parameter.getRule());
				JsonConverter.writeObjectToJsonFile(
					parameter.getRule(),
					new File(RULES_DIR,
							parameter.getRule().getName() + ".json"));
				return null;
			}
		});
	}

	private void redirectReplies() {
		for (Rule rule : rules) {
			if (rule.getCondition().getCommandURI() != null
					&& rule.getCondition().getCommandURI().length() > 0) {
				methodMap.put(
					rule.getCondition().getCommandURI(),
					new CommandExecutionInterface() {
						@Override
						public String run(Object rawParameter) {
							for (Entry<String, CommandExecutionInterface> entry : methodMap
									.entrySet()) {
								if (entry.getValue().equals(this)) {
									return handleRequest(
										entry.getKey(),
										rawParameter);
								}
							}
							return null;
						}
					});
			}
		}
	}

	private String handleRequest(String commandURI, Object rawParameter) {
		for (Rule rule : rules) {
			// filter wrong commandURI
			if (rule.getCondition().getCommandURI() != null
					&& rule.getCondition().getCommandURI().length() > 0
					&& !rule.getCondition().getCommandURI()
							.equals(commandURI)) {
				continue;
			}
			// filter input class type
			if ((rule.getCondition().getInputClassType() != null
					&& rule.getCondition().getInputClassType().length() > 0)
					&& (rawParameter == null
							|| !rule.getCondition().getInputClassType().equals(
								rawParameter.getClass().getName()))) {
				continue;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					checkPossibleMatch(rule, rawParameter);
				}
			}).start();
		}
		return null;

	}

	private void checkPossibleMatch(Rule rule, Object rawParameter) {
		if (!rule.getIsActive()) {
			return;
		}
		Condition condition = rule.getCondition();
		if (condition.getJsonAttributePath() != null
				&& condition.getJsonAttributePath().length() > 0) {
			JsonObject jsonObject = getJsonObject(rawParameter);
			String actualValue = String
					.valueOf(jsonObject.get(condition.getJsonAttributePath()));
			if (!Comparator.compare(
				condition.getComparator(),
				condition.getAttributeDataType(),
				actualValue,
				condition.getExpectedValue())) {
				return;
			}
		}
		applyRule(rule);

	}

	private void applyRule(Rule rule) {
		if (!lastTimeRuleApplied.containsKey(rule) || System.currentTimeMillis()
				- lastTimeRuleApplied.get(rule) > rule.getTimeout()) {
			lastTimeRuleApplied.put(rule, System.currentTimeMillis());
			executeActionEvents(rule.getEvents());
		}

	}

	private JsonObject getJsonObject(Object rawParameter) {
		JsonReader reader = Json
				.createReader(new StringReader((String) rawParameter));
		return reader.readObject();
	}

	public static void main(String[] args) throws IOException {
		new RuleModule();
	}

}
