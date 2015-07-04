/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.client;

import chat.messages.*;

/**
 *
 * @author Owner
 */
public class TextToMessage {

    public static final String LEAVE_CHANNEL = "leavechannel";
    public static final String LEAVE_CHANNEL_SHORT = "lc";
    public static final String JOIN_CHANNEL = "joinchannel";
    public static final String JOIN_CHANNEL_SHORT = "jc";
    public static final String CREATE_CHANNEL = "createchannel";
    public static final String CREATE_CHANNEL_SHORT = "cc";
    public static final String WHISPER = "whisper";
    public static final String WHISPER_SHORT = "w";
    public static final String TELL = "tell";
    public static final String MUTE = "ignore";
    public static final String MUTE_SHORT = "stfu";
    public static final String MOD = "makemod";
    public static final String MAKE_MOD_SHORT = "mod";
    public static final String ADD_FRIEND = "AddFriend";
    public static final String ADD_FRIEND_SHORT = "AF";
    public static final String ADD_IGNORE = "AddIgnore";
    public static final String ADD_IGNORE_SHORT = "AI";
    public static final String CHANNEL_KICK = "kick";
    public static final String CHANNEL_KICK_SHORT = "k";
    public static final String ME = "me";
    public static final String ME_SHORT = "m";

    public static void processText(ChatController controller, String text, String channelName) throws Exception {
        if (text == null || text.length() < 1) {
            throw new Exception("Invalid string given");
        }

        String one = splitOne(text);
        if (one != null) {
            if (one.equalsIgnoreCase(LEAVE_CHANNEL) || one.equalsIgnoreCase(LEAVE_CHANNEL_SHORT)) {
                controller.sendChannelLeave(channelName);
                return;
            }
            if (one.startsWith(ME)) {
                String meMsg = one.replaceFirst("me", "");
                controller.sendMeText(channelName, meMsg);
                return;
            }

        }

        String multi[] = splitThree(text);
        if (multi != null) {

            if (multi.length == 2) {
                if (multi[0].equalsIgnoreCase(JOIN_CHANNEL) || multi[0].equalsIgnoreCase(JOIN_CHANNEL_SHORT)) {
                    controller.sendChannelJoin(multi[1], "");
                    return;
                }
                if (multi[0].equalsIgnoreCase(CREATE_CHANNEL) || multi[0].equalsIgnoreCase(CREATE_CHANNEL_SHORT)) {
                    controller.sendChannelCreate(multi[1], "");
                    return;
                }
                if (multi[0].equalsIgnoreCase(ADD_FRIEND) || multi[0].equalsIgnoreCase(ADD_FRIEND_SHORT)) {
                    controller.sendRelationAdd(multi[1], "", true, false);
                    return;
                }
                if (multi[0].equalsIgnoreCase(ADD_IGNORE) || multi[0].equalsIgnoreCase(ADD_IGNORE_SHORT)) {
                    controller.sendRelationAdd(multi[1], "", false, true);
                    return;
                }
                if (multi[0].equalsIgnoreCase(CHANNEL_KICK) || multi[0].equalsIgnoreCase(CHANNEL_KICK_SHORT)) {
                    controller.sendChannelKick(multi[1], channelName);
                    return;
                }
                if (multi[0].equalsIgnoreCase(MUTE) || multi[0].equalsIgnoreCase(MUTE_SHORT)) {
                    controller.stfu(multi[1]);
                    return;
                }
                if (multi[0].equalsIgnoreCase(MOD) || multi[0].equalsIgnoreCase(MAKE_MOD_SHORT)) {
                    controller.createMod(multi[1]);
                    return;
                }
            }

            if (multi.length == 3) {
                if (multi[0].equalsIgnoreCase(JOIN_CHANNEL) || multi[0].equalsIgnoreCase(JOIN_CHANNEL_SHORT)) {
                    controller.sendChannelJoin(multi[1], multi[2]);
                    return;
                }
                if (multi[0].equalsIgnoreCase(WHISPER) || multi[0].equalsIgnoreCase(WHISPER_SHORT) || multi[0].equalsIgnoreCase(TELL)) {
                    controller.sendWhisper(multi[1], multi[2]);
                    return;
                }

                if (multi[0].equalsIgnoreCase(CREATE_CHANNEL) || multi[0].equalsIgnoreCase(CREATE_CHANNEL_SHORT)) {
                    controller.sendChannelCreate(multi[1], multi[2]);
                    return;
                }
                if (multi[0].equalsIgnoreCase(ADD_FRIEND) || multi[0].equalsIgnoreCase(ADD_FRIEND_SHORT)) {
                    controller.sendRelationAdd(multi[1], multi[2], true, false);
                    return;
                }
                if (multi[0].equalsIgnoreCase(ADD_IGNORE) || multi[0].equalsIgnoreCase(ADD_IGNORE_SHORT)) {
                    controller.sendRelationAdd(multi[1], multi[2], false, true);
                    return;
                }

            }

        }

        controller.sendChannelText(channelName, text);

    }

    //Just remove the /
    //return null if bad
    private static String splitOne(String s) {
        if (s.length() <= 1) {
            return null;
        }

        if (s.charAt(0) != '/') {
            return null;
        }

        return s.substring(1);
    }

    private static String[] splitThree(String s) {
        if (s.length() <= 1) {
            return null;
        }

        if (s.charAt(0) != '/') {
            return null;
        }

        //first space
        int firstSpace = s.indexOf(' ');
        String first = s.substring(1, firstSpace);

        //ok if theirs quotes around it need EVERYTHING in the quotes
        if (s.charAt(firstSpace + 1) == '"') {
            //ok need to find the next quote
            String temp = s.substring(firstSpace + 2);
            int secondQuote = temp.indexOf('"');
            String secondParameter = temp.substring(0, secondQuote);

            String rest = s.substring(first.length() + secondQuote + 5, s.length());
            String ret[] = new String[3];

            ret[0] = first;
            ret[1] = secondParameter;
            ret[2] = rest;
            return ret;
        } else {
            //ok need to find the next space
            String temp = s.substring(firstSpace + 1);
            int secondSpace = temp.indexOf(' ');
            if (secondSpace == -1) {
                String ret[] = new String[2];
                ret[0] = first;
                ret[1] = s.substring(first.length() + 2, s.length());
                return ret;
            }

            String secondParameter = temp.substring(0, secondSpace);
            String rest = s.substring(first.length() + secondSpace + 3, s.length());
            String ret[] = new String[3];

            ret[0] = first;
            ret[1] = secondParameter;
            ret[2] = rest;
            return ret;
        }
    }
}
