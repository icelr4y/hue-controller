package com.allenmp.hue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
	Options opts = new Options();

	OptionGroup modeOpts = new OptionGroup();

	// Alarm
	modeOpts.addOption(Option.builder().longOpt("alarm").hasArg(false).desc("Set an Alarm").build());
	opts.addOption(Option.builder("t").longOpt("time").desc("Alarm Time (HH:mm)").hasArg().build());
	opts.addOption(Option.builder("d").longOpt("duration").desc("Alarm duration (minutes)").hasArg().build());

	// Transition
	modeOpts.addOption(Option.builder().longOpt("transition").hasArg(false).desc("Set the color immediately").build());
	opts.addOption(Option.builder("c").longOpt("color").desc("Color Temperature (153=blue, 454=red)").hasArg().build());
	opts.addOption(Option.builder("b").longOpt("brightness").desc("Brightness (1-254)").hasArg().build());
	opts.addOption(Option.builder("l").longOpt("length").desc("Length of transition (msec)").hasArg().build());

	// Transition
	modeOpts.addOption(Option.builder().longOpt("on").hasArg(false).desc("Turn the lights on immediately").build());
	modeOpts.addOption(Option.builder().longOpt("off").hasArg(false).desc("Turn the lights off immediately").build());

	// Help
	opts.addOption(Option.builder().longOpt("help").desc("Print this help").build());
	opts.addOptionGroup(modeOpts);

	CommandLineParser parser = new DefaultParser();
	CommandLine cmd = null;
	try {
	    cmd = parser.parse(opts, args);
	} catch (ParseException e) {
	    LOG.error("Failed to parse command line args", e);
	    HelpFormatter fmt = new HelpFormatter();
	    fmt.printHelp("hue-controller", opts);
	    return;
	}

	if (cmd.hasOption("alarm")) {
	    ZoneId zone = ZoneId.of("America/New_York");
	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm").withZone(zone);
	    LocalTime localAlarm = LocalTime.parse(cmd.getOptionValue("t"), fmt);
	    Duration duration = Duration.ofMinutes(Long.valueOf(cmd.getOptionValue("d")));

	    // Is the next alarm today?
	    LocalDate today = LocalDate.now(zone);
	    ZonedDateTime alarmTime = ZonedDateTime.of(today, localAlarm, zone);

	    Instant now = Instant.now();
	    if (Duration.between(now, Instant.from(alarmTime)).isNegative()) {
		// alarm is tomorrow
		LocalDate tomorrow = today.plusDays(1);
		alarmTime = ZonedDateTime.of(tomorrow, localAlarm, zone);
	    } else {
		// next alarm is today
	    }

	    LOG.info("Setting {}-min alarm for {}", duration, alarmTime);
	    SunriseAlarm alarm = new SunriseAlarm(duration, Instant.from(alarmTime));
	    alarm.schedule();
	    return;
	    
	} else if (cmd.hasOption("transition")) {
	    LOG.info("Immediate transition");
	    int color = Integer.valueOf(cmd.getOptionValue("c", "300"));
	    int brightness = Integer.valueOf(cmd.getOptionValue("b", "200"));
	    int lengthMsec = Integer.valueOf(cmd.getOptionValue("l", "40"));
	    Transition t = new Transition();
	    t.setDuration(Duration.ofMillis(lengthMsec));
	    t.setTargetState(new LightState(1, true, brightness, color));
	    t.setTargetState(new LightState(2, true, brightness, color));

	    RoutineExecutor exec = new RoutineExecutor(t);
	    exec.execute();
	    return;

	} else if (cmd.hasOption("on")) {
	    LOG.info("Turning all on");
	    HueController c = new HueController();
	    c.allOn();
	    return;

	} else if (cmd.hasOption("off")) {
	    LOG.info("Turning all off");
	    HueController c = new HueController();
	    c.allOff();
	    return;

	} else if (cmd.hasOption("help")) {
	    System.out.println("Example: java -jar ./controller.jar --alarm -t \"06:00\" -d 25");
	    System.out.println("Example: java -jar ./controller.jar --transition -c 200 -l 2000");
	    HelpFormatter fmt = new HelpFormatter();
	    fmt.printHelp("hue-controller", opts);
	    return;
	}

    }

}
