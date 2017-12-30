package de.deeps.hms.rules;

/**
 * @author Deeps
 */

public class ConfigCreator {
	public static void main(String[] args) {
		// ModuleConfig config = new ModuleConfig();
		// config.setModuleType(ModuleType.RULE);
		// config.setNetworkConfig(new NetworkConfig(6666, 0));
		// config.setHttpConfig(
		// new HttpConfig(1821, Arrays.asList(
		// new Command("handleClap", ClapEvent.class.getName(),
		// JsonConverter.objectToJsonString(new ClapEvent())),
		// new Command("addRule", AddRuleParameter.class.getName(),
		// JsonConverter
		// .objectToJsonString(new AddRuleParameter())),
		// new Command("removeRule", RemoveRuleParameter.class.getName(),
		// JsonConverter
		// .objectToJsonString(new RemoveRuleParameter())),
		// new Command("executeRule", ExecuteRuleParameter.class.getName(),
		// JsonConverter.objectToJsonString(
		// new ExecuteRuleParameter())),
		// new Command("listRules"),
		// new Command("setStartEvents",
		// SetStartEventsParameter.class.getName(),
		// JsonConverter.objectToJsonString(
		// new SetStartEventsParameter())),
		// new Command("setStopEvents",
		// SetStopEventsParameter.class.getName(),
		// JsonConverter.objectToJsonString(
		// new SetStopEventsParameter())),
		// new Command("registerListener",
		// RegisterListenerParameter.class.getName(),
		// JsonConverter.objectToJsonString(
		// new RegisterListenerParameter())),
		// new Command("unregisterListener",
		// UnregisterListenerParameter.class.getName(),
		// JsonConverter.objectToJsonString(
		// new UnregisterListenerParameter())),
		// new Command("listStopEvents"),
		// new Command("listStartEvents"))));
		// JsonConverter.writeObjectToJsonFile(config, "rule_config.json");

		// ActionEvents events = new ActionEvents();
		// ActionEvent event = new ActionEvent();
		// event.setTargetUrl("http://192.168.178.96:3121/registerListener");
		// de.deeps.hms.clapdetector.parameters.RegisterListenerParameter param
		// = new
		// de.deeps.hms.clapdetector.parameters.RegisterListenerParameter();
		// param.setResponseUrl("http://192.168.178.96:1821/handleClap");
		// event.setContent(param);
		// events.setEvents(Arrays.asList(event));
		// System.out.println(JsonConverter.objectToJsonString(events));

		// Rule rule = new Rule();
		// rule.setIsActive(true);
		// Condition condition = new Condition();
		// condition.setAttributeDataType(SupportedDataType.INTEGER);
		// condition.setComparator(ComparatorType.EQUALS);
		// condition.setExpectedValue("2");
		// condition.setInputClassType(ClapEvent.class.getName());
		// condition.setJsonAttributePath("clapCount");
		// rule.setCondition(condition);
		// rule.setEvents(Arrays.asList());
		// rule.setName("ClapToAudio");
		// System.out.println(JsonConverter.objectToJsonString(rule));
	}
}
