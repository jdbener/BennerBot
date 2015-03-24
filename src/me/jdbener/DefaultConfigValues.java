package me.jdbener;

public class DefaultConfigValues {
	public DefaultConfigValues(){
		/*
		 * General Settings
		 */
		Bennerbot.updateConfig("botName", "BennerBot", "The name of the bot changing this has only an aesthetic effect");
		Bennerbot.updateConfig("botID", "default", "This determines the ID of the bot, bots using the same ID share settings, set this to something specific to you or write this down somewhere. Leave this as default to let the bot handle this and use a default value specific to you.");
		Bennerbot.updateConfig("respondToOperatorCommands", "true", "Weather or not commands sent by the bot operator are phrased and responded to");
		Bennerbot.updateConfig("dateFormat", "HH:mm", "How dates and/or times are displayed by the bot, information on what this means can be found here: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html");
		Bennerbot.updateConfig("capitalizeNames", "true", "weather or not names should be capitalized");
		Bennerbot.updateConfig("incognitoMode", "true", "weather or not bennerbot should announce his presence");

		/*
		 * API Integration Settings
		 */
		Bennerbot.updateConfig("enableFollowerNotifications", "true", "weather or not to display a notification when someone follows");
		Bennerbot.updateConfig("enableStatusandGameUpdateing", "true", "This allows you to use the !title and !game commands");
		Bennerbot.updateConfig("useLocalDatabase", "true", "Weather or not you want to use the public database, enabeling this will involve a loss of privacy, however you will have your settings synced everywhere");

		/*
		 * Twitch Settings
		 */
		Bennerbot.updateConfig("connectToTwitch", "true", "Weather or not to connect to a twitch channel");
		Bennerbot.updateConfig("twitchUsername", "Bennerbot", "The username of the the twitch account you want the bot to use. I recommend creating a new account for this");
		Bennerbot.updateConfig("twitchAccessToken", "", "This can be generated at anytime by clicking the button in the gui, or by sending 'regenToken' in the console");
		Bennerbot.updateConfig("twitchChannel", "Bennerbot", "the name of the twitch account to moderate");

		/*
		 * Hitbox Settings
		 */
		Bennerbot.updateConfig("connectToHitbox", "true", "Weather or not to connect to a hitbox channel");
		Bennerbot.updateConfig("hitboxUsername", "bennerbot", "The username of the the hitbox account you want the bot to use. I recommend creating a new account for this");
		Bennerbot.updateConfig("hitboxPassword", "bennerbot", "Enter the password for your hitbox account. Dont worry this wont be scene by anybody but yourself. I recommend creating a new account for this");
		Bennerbot.updateConfig("hitboxChannel", "bennerbot", "the name of the hitbox account to moderate");


		/*
		 * Last.fm Settings
		 */
		Bennerbot.updateConfig("enableLastfmIntegration", "false", "should last.fm intergation be enabled?");
		Bennerbot.updateConfig("lastfmName", "", "The username to get last.fm data about");
		Bennerbot.updateConfig("songCommandMessageFormat", "hay <user> thats, <title> ~ <artist> on <album> <url>", "The format for how messages are responded to, <user>: the user who sent the command, <title>: the title of the song thats currently playing, <artist>: the artist who performs said song, <album>: the album that the song is realised on, <url>: a link to find out more information about the song at. Add a number in front of these to represent previous tracks (number must be between 1 and 20) for example, <1title> would show the title of the previous song)");
		Bennerbot.updateConfig("lastfmCommandName", "!song", "The name of the !song command");

		/*
		 * Output Settings
		 */
		Bennerbot.updateConfig("enableOutput", "true", "weather or not to enable sending output to a file or a GUI");
		Bennerbot.updateConfig("nameShortener", "false", "Weather or not to shorten usernames, long names can cause issues and users names longer than 32 characters will be trimed no matter what");
		Bennerbot.updateConfig("WriteClean", "true", "Write the output to a formated file?");
		Bennerbot.updateConfig("WriteDirty", "true", "Write the output to a non-formated file?");
		Bennerbot.updateConfig("HighlighMessages", "true", "Weather or not to highlight messages that include your name in them");
		Bennerbot.updateConfig("enableBotMessages", "true", "Weather or not to display messages from \'bennerbot\' in the in");
		Bennerbot.updateConfig("enableJoinMessages", "false", "Weather or not user join messages will be displayed");
		Bennerbot.updateConfig("enableLeaveMessages", "false", "Weather or not user leave messages will be displayed");
		Bennerbot.updateConfig("enablePluginMessages", "true", "Weather or not messages that plugins create will be displayed, I recommend leaving this off, some of these messages could get kinda \'spammy\'");
		Bennerbot.updateConfig("filterBots", "true", "weather or not to filter users whos names end in \'bot\'");
		Bennerbot.updateConfig("filterCommands", "true", "weather or not to filter commands from messages");
		Bennerbot.updateConfig("DisplayMessageFormat", "<server> <badge><timestamp> <user>: <message>", "The format for messages that are displayed in the GUI or Written to a file, <server>: the server icon that this message is from, <badge>: the moderator/streamer badge, <timestamp>: the current time, <color>: the color for a specific user, <user>: the username of the user who sent the message, with his color added, <noformateuser>: the username of the user who sent the message, <message>: the message itself");

		/*
		 * Output File Settings
		 */
		Bennerbot.updateConfig("enableLatestFollowerFile", "true", "weather or not to write the latest follower to a file");
		Bennerbot.updateConfig("enableCurrentlyPlayingSongFile", "true", "weather or not to write the song that is currently being played to a file");
		Bennerbot.updateConfig("songFileFormat", "<title> ~ <artist>                   ", "The format for how the currently playing song will be writen to the file, <title>: the title of the song thats currently playing, <artist>: the artist who performs said song, <album>: the album that the song is realised on, <url>: a link to find out more information about the song at, add a number in front of these to represent previous tracks (number must be between 1 and 20), for example, <1title> would show the title of the previous song");


		/*
		 * Loyalty Bot Settings
		 */
		Bennerbot.updateConfig("levelCommandFormat", "<user> is level: <level> with <currentxp> xp, <xptillnextlevel> xp until next level <percentage>%", "The format used by the level command when returning values");
		Bennerbot.updateConfig("levelCommandAllowOtherUserLookup", "true", "Weather or not the level command should allow users to lookup other users");
		Bennerbot.updateConfig("levelModifer", "1.0", "This modifies the amount of xp people have to earn to level up. 1.0 is the default");
		Bennerbot.updateConfig("xpPerMessage", "10", "How much experience to give every time a message is sent");
		Bennerbot.updateConfig("oneXpEveryXSeconds", "30", "this says how often a user will get xp (in seconds)");
		Bennerbot.updateConfig("currencyPerMessage", "1", "how many channel specific currency to give a user every message");
		Bennerbot.updateConfig("currencyName", "BennerBits", "the name of your channel specific currency");
		Bennerbot.updateConfig("currencyLookupCommandName", "!bits", "The command used to check how many of the currency you have");
		Bennerbot.updateConfig("currencyLookupCommandAllowOtherUserLookup", "true", "weather or not you want users to see how many bits other users have");
		Bennerbot.updateConfig("currencyGiveCommandName", "!givebits", "the command used to give bits to another user");

		/*
		 * Loyalty Bot Settings
		 */
		Bennerbot.updateConfig("enableAutoHoster", "true", "Weather or not to enable the autohoster.");
		
		/*
		 * Text to Speach Settings
		 */
		Bennerbot.updateConfig("enableTTS", "false", "Weather or not bennerbot should read messages to you. EXPERIMENTAL USE AT YOUR OWN RISK");
		Bennerbot.updateConfig("enableVariability", "false", "Give every user a unique voice? EXPERIMENTAL USE AT YOUR OWN RISK");
		Bennerbot.updateConfig("enableTTSMinimalisticMode", "false", "If this is enabled, only when a user sends a message the first time will the bot say the name of the sender. This helps if you don't want the bot to read the name of a user when there is only one person talking. EXPERIMENTAL USE AT YOUR OWN RISK");
		Bennerbot.updateConfig("TTSSpeed", "default", "Measured in words per minute... This determines how fast BennerBot Speaks, default sounds best but may be hard to understand, 1-200 is slow or fast (1 is slow, 200 is super fast) EXPERIMENTAL USE AT YOUR OWN RISK");
		Bennerbot.updateConfig("TTSVolume", "10", "how loud you want the bot to be on a scale of 1-10. EXPERIMENTAL USE AT YOUR OWN RISK");
		
		/*
		 * Chat Moderation Settings
		 */
		Bennerbot.updateConfig("enableModeration", "false", "Weather or not bennerbot should moderate your chat, only set this to true if bennerbot has been modded on your channel");
		Bennerbot.updateConfig("kickWarnings", "5", "How many warnings a user gets before being timed out");
		Bennerbot.updateConfig("banWarnings", "20", "How many warnings a user gets before being banned");
		Bennerbot.updateConfig("CapsFilter", "false", "Active Capitalazation Filter?");
		Bennerbot.updateConfig("LengthFilter", "false", "Activate message length Filtering");
		Bennerbot.updateConfig("MaxCapsPercentage", ".8", "How what percentage of a message is required to be capitalized in order to activate the filter, This is a decimal number where 1 = 100%");
		Bennerbot.updateConfig("MaxMessageLength", "200", "The maximum length of a message");
		
		/*
		 * Relay Settings
		 */
		Bennerbot.updateConfig("activateRelay", "true", "Weather or not the Chat Relay is activated");
		Bennerbot.updateConfig("showSendName", "false", "Weather or not to display the username in the message for messages sent with the GUI");
		
		/*
		 * Auto Message Settings
		 */
		Bennerbot.updateConfig("enableAutoMessages", "true", "Weather or not to enable the automessage system");
		Bennerbot.updateConfig("autoMessageInterval", "300", "The time in seconds between each method (60 seconds in a minute, 3600 seconds in an hour)");
		
		/*
		 * Plugin System Settings
		 */
		Bennerbot.updateConfig("enablePluginSystem", "true", "Weather or not to enable the Plugin System");
	}
}
