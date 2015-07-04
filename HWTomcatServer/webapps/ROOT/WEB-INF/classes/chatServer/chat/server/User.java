/*
 * User.java
 *
 * Created on December 18, 2007, 12:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package chat.server;

import util.sql;
import chat.util.StringCheck;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Owner
 */
public class User {

    //Channel info
    public static final int MAX_CHANNELS = 6;
    //public ArrayList<Channel> subscribedChannels = new ArrayList<Channel>();
    //THREAD SAFE!! but throws errors....
    private LinkedList<Channel> subscribedChannels = new LinkedList<Channel>();
    private Semaphore semi = new Semaphore(1, true);
    //Primary data
    private String uniqueNick;
    private String uniqueName;
    private boolean muted = false;
    private boolean admin = false;
    private boolean noWhisper = false;
    private boolean noChannelCreation = false;
    private boolean noChannelJoin = false;
    private boolean updated = false;
    private boolean loggedIn = true;
    private UserListing userListing;
    private UserMsgBox msgBox = new UserMsgBox();
    private UserRelationListing relationList;

    /** Use to create a new user or load one in
     *  The nick is what the user is called by, Its so we dont have to post IP's
     *  The nick MUST BE UNIQUE
     *  If the user allready has a nick, and you guve him the wrong one, will load the SQL one,
     *
     * The user name for HACK wars is their IP, and MUST be unique
     */
    User(String uniqueName, String uniqueNick, String properties, UserListing userListing) throws Exception {
        if (StringCheck.validSQLString(uniqueName) == false) {
            throw new Exception("The name contains invalid cahracters");
        }
        this.userListing = userListing;
        this.uniqueNick = uniqueNick.toLowerCase();
        this.uniqueName = uniqueName.toLowerCase();
        this.loadSQLProperties(properties);

    }

    /** updates user if it needs to, other wise dose not
     * Also notify all channels that the user is no longer here
     */
    void userLoggedOut(sql s) throws Exception {
        this.loggedIn = false;

        //unsubscribe from channels!!
        //System.out.println("%&%& Closing UNSUBSCRIBE");
        Iterator<Channel> it = subscribedChannels.iterator();
        while (subscribedChannels.size() != 0) {
            try {
                Channel chan = subscribedChannels.getFirst();
                //System.out.println("%&%& Closing UNSUBSCRIBE " + chan.getChannelName());
                ZUserChannel.removeUserfromChannel(this, chan);
            } catch (Exception e) {
                //Dont care keep going
            }
        }

        //Shut down user relation, it will tell my friends I logged out!

        subscribedChannels = null;
        msgBox = null;

        //System.out.println("%&%& Closing RELATION");
        try {
            relationList.close(s);
            relationList = null;
        } catch (Exception e) {
            System.out.println("%&%& AlexChat User Failed to close relation");
        }


        //if(updated == false){
        //    return;
        //}

        //System.out.println("%&%& Closing UPDATE SQL");
        try {
            SQLUser.updateSQL(s, this);
        } catch (Exception e) {
            //%&%& AlexChat User Could not update users changes to database, sql error
            System.out.println("%&%& AlexChat User Could not update users changes to database, sql error");
            //throw new Exception("");
        }

    }

    /**
     * Returns the users nickname
     */
    public String getNick() {
        return this.uniqueNick;
    }

    //Converties all the properties to one nice SQL staetment (Probably)
    public String getProptiesSQL() {
        String res = "('";
        boolean first = true;

        if (isAdmin() != false) {
            res += SQLUser.USER_P_ADMIN;
            first = false;
        }

        if (isNoWhisper() != false) {
            if (first == false) {
                res += ",";
            }
            res += SQLUser.USER_P_NO_WHISPER;
            first = false;
        }

        if (this.isNoChannelJoin() != false) {
            if (first == false) {
                res += ",";
            }
            res += SQLUser.USER_P_NO_CHANNEL_JOIN;
            first = false;
        }

        if (this.getMuted() != false) {
            if (first == false) {
                res += ",";
            }
            res += SQLUser.USER_MUTED;
            first = false;
        }


        if (this.isNoChannelCreation() != false) {
            if (first == false) {
                res += ",";
            }
            res += SQLUser.USER_P_NO_CHANNEL_JOIN;
            first = false;
        }
        res += "')";
        return res;
    }

    //Give it the sql propeties and it will load them
    private void loadSQLProperties(String s) {
        if (s == null) {
            this.setAdmin(false);
            this.setNoChannelCreation(false);
            this.setNoChannelJoin(false);
            this.setNoWhisper(false);
            return;
        }
        if (s.indexOf(SQLUser.USER_P_ADMIN) != -1) {
            this.setAdmin(true);
        }
        if (s.indexOf(SQLUser.USER_P_NO_CHANNEL_CREATION) != -1) {
            this.setNoChannelCreation(true);
        }
        if (s.indexOf(SQLUser.USER_P_NO_CHANNEL_JOIN) != -1) {
            this.setNoChannelJoin(true);
        }
        if (s.indexOf(SQLUser.USER_P_NO_WHISPER) != -1) {
            this.setNoWhisper(true);
        }

        if (s.indexOf(SQLUser.USER_MUTED) != -1) {
            this.setMuted(true);
        }
    }

    /**
    Set whether or not this player has been muted.
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
        try {
            sql s = SQLConstants.getSQL();
            SQLUser.updateSQL(s, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getMuted() {
        return (muted);
    }

    /**
     * Returns if the property ADMIN is true or false
     * Admins have mnay special privlages
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the property ADMIN is true or false
     *  The SQL will be updated when the user logs
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
        this.updated = true;
        try {
            sql s = SQLConstants.getSQL();
            SQLUser.updateSQL(s, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns if the property NO_WHISPER is true or false
     * if set to true, all whisper messages the users trys to create will throw an error
     */
    public boolean isNoWhisper() {
        return noWhisper;
    }

    /**
     * Sets the property NO_WHISPER is true or false
     *  The SQL will be updated when the user logs
     */
    public void setNoWhisper(boolean no_whisper) {
        this.noWhisper = no_whisper;
        this.updated = true;
    }

    /**
     * Returns if the property NO_CHANNEL CREATION is true or false
     * if set to true, A error will be thrown when the user tries to create a channel,
     */
    public boolean isNoChannelCreation() {
        return noChannelCreation;
    }

    /**
     * Sets the property NO_CHANNEL CREATIION is true or false
     *  The SQL will be updated when the user logs
     */
    public void setNoChannelCreation(boolean no_channel_creation) {
        this.noChannelCreation = no_channel_creation;
        this.updated = true;
    }

    /**
     * Returns if the property NO_CHANNEL CREATION is true or false
     * if set to true, the user can no longer join channels manualy
     */
    public boolean isNoChannelJoin() {
        return noChannelJoin;
    }

    /**
     * Sets the property NO_CHANNEL JOIN is true or false
     *  The SQL will be updated when the user logs
     */
    public void setNoChannelJoin(boolean no_channel_join) {
        this.noChannelJoin = no_channel_join;
        this.updated = true;
    }

    /**
     * true if the user is currently logged on, otherwise false
     */
    public boolean getLoggedOn() {
        return this.loggedIn;
    }

    /**
     * Returns the users name,
     * In hackwars this is their IP
     */
    public String getName() {
        return this.uniqueName;
    }

    /** THIS IS ONLY FOR ZUSERCHANNEL NOTHING ELSE SHOULD TOUCH this
     * A user can subscribe to a channel,
     *   This is mostly to notfy the chnnel when the user logs.
     *   And to make sure the user dose not subscribe to too many channels at once.
     */
    void zSubscribeToChannel(Channel c) throws Exception {
        try {
            semi.acquire();

            if (subscribedChannels.contains(c) == true) {
                throw new Exception("ERROR: Allready subscribed to this channel");
            }

            if (subscribedChannels.size() > User.MAX_CHANNELS) {
                throw new Exception("ERROR: To many channels allready subscribed");
            }

            subscribedChannels.add(c);
        } catch (Exception e) {
            throw e;
        } finally {
            semi.release();
        }

    }

    /** THIS IS ONLY FOR ZUSERCHANNEL NOTHING ELSE SHOULD TOUCH this
     * Unsubscribe the user from a channel
     *   DOES NOT REMOVE THE USER FROM THE CAHNEL STRUCTURE
     */
    void zUnSubscribeToChannel(Channel c) {
        try {
            semi.acquire();

            subscribedChannels.remove(c);

        } catch (Exception e) {
        } finally {
            semi.release();
        }
    }

    String[] getStringChannelList() {
        try {
            semi.acquire();
            int size = subscribedChannels.size();
            String ret[] = new String[size];
            int loopc = 0;
            while (loopc < size) {
                ret[loopc] = subscribedChannels.get(loopc).getChannelName();
                loopc++;
            }
            return ret;
        } catch (Exception e) {
            return null;
        } finally {
            semi.release();
        }
    }

    public int getNumberOfChannels() {
        try {
            semi.acquire();
            return this.subscribedChannels.size();
        } catch (Exception e) {
            return 0;
        } finally {
            semi.release();
        }

    }

    /** Gets the users message box, where all messages are dumped to*/
    public UserMsgBox getUserMsgBox() {
        return msgBox;
    }

    public UserRelationListing getRelationList() {
        return this.relationList;
    }

    public void setupUserRelation(sql s) throws Exception {
        if (this.relationList != null) {
            return;
        }

        this.relationList = new UserRelationListing(this, this.userListing, s);
    }
}
