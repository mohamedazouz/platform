/**
 * 
 */
package org.mcplissken.schedule.quartz.builder.schedule;

import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 29, 2014
 */
public interface QuartzScheduleBuilder {
	
	public void ignoreMisfires();
	
	public void runOnceOnMisfire();
	
	public void schedule(TriggerBuilder<Trigger> jobTriggerBuilder);
	
	public void inTimeZone(String timeZone);
}
