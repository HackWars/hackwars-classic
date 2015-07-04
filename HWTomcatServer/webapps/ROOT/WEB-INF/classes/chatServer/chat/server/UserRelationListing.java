package chat.server;

import chat.util.*;
import util.sql;
import chat.messages.*;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class UserRelationListing {

    private Semaphore semi = new Semaphore(1, true);
    private TreeMap<String, UserRelation> relationMap = new TreeMap<String, UserRelation>(new StringCompare());
    private User theUser;
    private UserListing userListing;
    private int sqlKey;

    public UserRelationListing(User theUser, UserListing userListing) throws Exception {
        this(theUser, userListing, (sql) null);
    }

    public UserRelationListing(User theUser, UserListing userListing, sql s) throws Exception {
        this.theUser = theUser;
	this.userListing=userListing;
        if (s == null) {
            s = SQLConstants.getSQL();
            loadSQLRelations(theUser.getName(), s);
            s.close();
        } else {
            loadSQLRelations(theUser.getName(), s);
        }

        //ok now we need to go through and check if were frinds with any body, then tell them were on!
	System.out.println("Getting Iterator");
        Iterator<UserRelation> iterator = relationMap.values().iterator();
        while (iterator.hasNext() == true) {
            UserRelation curRelation = iterator.next();
	    System.out.println("Found Relation - "+curRelation.getSecondName());
	    //if (curRelation.isFriend() != false) {
		    System.out.println("Relation is a friend - "+curRelation.getSecondName());
		User myFriend = userListing.getUser(curRelation.getSecondName());
		if (myFriend != null) {
		    //MY frineds on best tell him im on!
		    myFriend.getRelationList().tellImOn(theUser);
		}
	    //}
        }
    }

    private void loadSQLRelations(String userName, sql s) throws Exception {
        UserRelation list[] = null;
        try {
            list = SQLUserRelation.getRelationList(s, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int size = list.length;
        int loopc = 0;

        while (loopc < size) {
            UserRelation r = list[loopc];
            AddUserRelation(r);
            loopc++;
        }

    }

    void AddUserRelation(UserRelation r) {
        if (r == null) {
            return;
        }

        try {
            semi.acquire();
            this.relationMap.put(r.getSecondName(), r);
        } catch (Exception e) {

        } finally {
            semi.release();
        }
    }

    /**
     * gets the userRelation of another user,
     *   returns null if their is none
     */
    public UserRelation getRelation(User u) {
        return getRelation(u.getName());
    }

    /**
     * gets the userRelation of another user,
     *   returns null if their is none
     */
    public UserRelation getRelation(String userName) {
        try {
            semi.acquire();
            return relationMap.get(userName);
        } catch (Exception e) {
            //really shouldnt happen
            return null;
        } finally {
            semi.release();
        }
    }

    /**
     * Creates a user relation with another user
     */
    public UserRelation addRelation(String name, String comment, boolean friend, boolean ignore, sql s) throws Exception {
        UserRelation r = getRelation(name);
        boolean isOnline = findFriend(name);



        //ok it allready exsits we have to update it.
        if (r != null) {
            r.setComment(comment);
            r.setFriend(friend);
            r.setIgnore(ignore);

            UserMsgBox box = this.theUser.getUserMsgBox();
            box.addMessage(new MsgOutRelationAdd(theUser.getName(), name, comment, friend, ignore, isOnline));
            return r;
        }

        try {
            s = SQLConstants.getSQL();
            r = SQLUserRelation.newUserRelation(this.theUser.getName(), name, comment, s);
            this.AddUserRelation(r);
            UserMsgBox box = this.theUser.getUserMsgBox();
            box.addMessage(new MsgOutRelationAdd(theUser.getName(), name, comment, friend, ignore, isOnline));
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserRelation addRelation(String name, String comment, boolean friend, boolean ignore) throws Exception {
        sql s = null;
        try {
            s = SQLConstants.getSQL();
            return addRelation(name, comment, friend, ignore, s);
        } catch (Exception e) {
            throw e;
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    public void close(sql s) {
        if (s == null) {
            s = SQLConstants.getSQL();
            close2(s);
            s.close();
        } else {
            close2(s);
        }
    }

    private void close2(sql s) {
        try {
            System.out.println("close UserRelationListing");
            semi.acquire();
            int loopc = 0;
            Object rel[] = relationMap.values().toArray();

            while (loopc < rel.length) {
                System.out.println("close UserRelationListing loopc");
                UserRelation r = (UserRelation) rel[loopc];
                try {
                    r.close(s);
                } catch (Exception e) {

                }
                loopc++;
            }
        } catch (Exception e) {
        } finally {
            semi.release();
        }

        //ok now we need to go through and check if were frinds with any body, then tell them were off!!
        Iterator<UserRelation> iterator = relationMap.values().iterator();
        while(iterator.hasNext() == true){
            UserRelation curRelation = iterator.next();
            if(curRelation.isFriend() != false){
                User myFriend = userListing.getUser(curRelation.getSecondName());
                if(myFriend != null){
                    //MY frineds on best tell him im on!
                    myFriend.getRelationList().tellImOff(theUser);
                }
            }
        }
        this.userListing = null;
        this.theUser = null;
        
        
    }

    /**
     * Gets all relations
     */
    public UserRelation[] getAllRelations() {
        int loopc = 0;
        UserRelation result[] = null;
        try {
            semi.acquire();
            Object array[] = this.relationMap.values().toArray();
            int size = array.length;
            result = new UserRelation[size];
            while (loopc < size) {
                try {
                    result[loopc] = (UserRelation) array[loopc];
                } catch (Exception e) {
                    result[loopc] = null;
                }
                loopc++;
            }

        } catch (Exception e) {
            return null;
        } finally {
            semi.release();
        }
        return result;
    }

    /** Ok this dose the following
     *  It goes and sees if this person is online AND is your frind, 
     *  If both are true return true, else false.
     */
    public boolean findFriend(String frindToCheck) {
        //ok is the person to be added online or not?
        String myName = this.theUser.getName();
        User toAdd = this.userListing.getUser(frindToCheck);
        if (toAdd != null) {
            //hes online now is he my friend?
            UserRelation toAddRelation = toAdd.getRelationList().getRelation(myName);

            if (toAddRelation != null) {
                //if (toAddRelation.friend == true) {
                    return true;
                //}
            }
        }
        return false;
    }

    public void tellImOn(User loggingOn) {
        UserRelation r = this.getRelation(loggingOn.getName());
	System.out.println("Telling Im On - "+loggingOn.getName());
        if (r != null) {
		System.out.println("R is not null");
            UserMsgBox box = this.theUser.getUserMsgBox();
	    System.out.println("Username: "+theUser.getName());
	    System.out.println("Logging On Username: "+loggingOn.getName());
            box.addMessage(new MsgOutRelationAdd(theUser.getName(), loggingOn.getName(), r.getComment(), r.isFriend(), r.isIgnore(), true));
        }
    }

    public void tellImOff(User loggingOn) {
        UserRelation r = this.getRelation(loggingOn.getName());

        if (r != null) {
            UserMsgBox box = this.theUser.getUserMsgBox();
            box.addMessage(new MsgOutRelationAdd(theUser.getName(), loggingOn.getName(), r.getComment(), r.isFriend(), r.isIgnore(), false));
        }
    }
}
