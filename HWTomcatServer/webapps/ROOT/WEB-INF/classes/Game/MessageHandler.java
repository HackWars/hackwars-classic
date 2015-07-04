package Game;

import java.util.ArrayList;
import GUI.OptionPanel;

public class MessageHandler {
	//message types
	public static final int GAME_MESSAGE = 0;
	public static final int POPUP_ERROR = 1;
	public static final int POPUP_MESSAGE = 2;
	public static final int ATTACK_MESSAGE = 3;
	public static final int REDIRECT_MESSAGE = 4;


	//MESSAGES
	public static final Object[] ACTIVE_BANK_NOT_FOUND = new Object[]{ "Transaction failed, make sure you have an non-dummy banking port turned on.",GAME_MESSAGE};
	public static final Object[] APPLICATION_NOT_FOUND = new Object[]{ "Application not found on HD.",GAME_MESSAGE};
	public static final Object[] ATTACK_EXCEEDED_TIMEOUT = new Object[]{ "Attack exceeded maximum timeout.",ATTACK_MESSAGE};
	public static final Object[] ATTACK_FAIL_OVERHEATED = new Object[]{ "Can't attack when port is overheated.",POPUP_ERROR};
	public static final Object[] ATTACK_FAIL_NOT_ENOUGH_MONEY = new Object[]{ "You must have $10 in your petty cash to start an attack.",POPUP_ERROR};
	public static final Object[] ATTACK_FAIL_WRONG_NETWORK = new Object[]{ "You cannot attack _0_ they are on the network \"_1_\".",POPUP_ERROR}; //_0_ = ip, _1_ = network name
	public static final Object[] ATTACK_FAIL_NOOB = new Object[]{ "Stop picking on noobs!",POPUP_ERROR};
	public static final Object[] ATTACK_CANCELLED = new Object[]{ "Your attack was canceled.",ATTACK_MESSAGE};
	
	public static final Object[] BOUNTY_COMPLETED = new Object[]{ "Received _0_ reward from bounty.",POPUP_MESSAGE}; //_0_ = reward amount
	public static final Object[] BOUNTY_FAILED_ALREADY_COMPLETED = new Object[]{ "Another player already completed this bounty.",POPUP_ERROR};
	public static final Object[] BOUNTY_STEP_COMPLETED = new Object[]{ "A step in a bounty has been completed.",GAME_MESSAGE};
	
	public static final Object[] CHALLENGE_START = new Object[]{ "Attempting Challenge ID _0_",GAME_MESSAGE};   //_0_ = challenge ID
	public static final Object[] CHALLENGE_NO_FILE = new Object[]{ "You do not currently have a challenge with this ID on your HD.",POPUP_ERROR};
	public static final Object[] CHALLENGE_RUNNING_ATTEMPT = new Object[]{ "Running attempt _0_",GAME_MESSAGE}; //_0_ = attempt number
	public static final Object[] CHALLENGE_COMPLETED = new Object[]{ "Challenge successfully completed.",POPUP_MESSAGE};
	public static final Object[] CHALLENGE_FAILED = new Object[]{ "Challenge failed try again.",POPUP_ERROR};
	public static final Object[] CHANGE_DAILY_PAY_FAIL_WRONG_TYPE = new Object[]{ "You can only change daily pay on an HTTP port.",POPUP_ERROR};
	public static final Object[] CHANGE_DAILY_PAY_FAIL_BOUNTY = new Object[]{ "You cannot immediately take back over an HTTP attacked as part of a bounty.",POPUP_ERROR};
	public static final Object[] CHANGE_DAILY_PAY_SUCCESS = new Object[]{ "Daily pay successfully changed.",ATTACK_MESSAGE};
	public static final Object[] CHANGE_DAILY_PAY_SUCCESS_GAME = new Object[]{ "Daily pay successfully changed.",GAME_MESSAGE};
	public static final Object[] CHANGE_DAILY_PAY_FAIL_ALREADY_CONTROLLED = new Object[]{ "You already controlled this HTTP.",ATTACK_MESSAGE};
	public static final Object[] CHANGE_DAILY_PAY_FAIL_FIREWALL = new Object[]{ "_0_'s firewall has caused the change daily pay to fail.",POPUP_MESSAGE};
	public static final Object[] CHANGE_NETWORK_FAIL_ALREADY_ON = new Object[]{ "You are already on _0_.",POPUP_ERROR}; //_0_ = network name
	public static final Object[] CHANGE_NETWORK_FAIL_TIMEOUT = new Object[]{ "You have not been on _0_ long enough to change networks.",POPUP_ERROR}; //_0_ = network name
	public static final Object[] CHANGE_NETWORK_FAIL_JAILED = new Object[]{ "You are in jail. Network change prohibited from _0_.",POPUP_ERROR};	//_0_ = network name - jailed message
	public static final Object[] CHANGE_NETWORK_SUCCESS = new Object[]{ "Moved to network \"_0_\"",POPUP_MESSAGE}; //_0_ = network name
	public static final Object[] COMPILE_FAIL_NOT_ENOUGH_MONEY = new Object[]{ "You do not have enough money to compile the requested script.",POPUP_ERROR};
	public static final Object[] COMPILE_FAIL_HD_FULL = new Object[]{ "You do not have enough disk space to compile this file.",POPUP_ERROR};
	public static final Object[] COULD_NOT_EXECUTE_APPLICATION = new Object[]{ "Could not execute application on port _0_. This could mean a program is missing on a default port.",GAME_MESSAGE}; //_0_ = port number
	public static final Object[] COMPUTER_OVERHEATED = new Object[]{ "Your computer just overheated!",POPUP_ERROR};
	public static final Object[] CPU_TOO_HIGH = new Object[]{ "Your CPU cannot handle that many processes running.",POPUP_ERROR};
	
	public static final Object[] DELETE_FAIL_NON_EMPTY_FOLDER = new Object[]{ "Can not delete non-empty folders.",POPUP_ERROR};
	public static final Object[] DELETE_LOGS_SUCCESS = new Object[]{ "Logs successfully deleted.",ATTACK_MESSAGE};
	public static final Object[] DESTROY_WATCHES_SUCCESS = new Object[]{ "Watches successfully destroyed.",ATTACK_MESSAGE};
	
	public static final Object[] EDIT_LOGS_SUCCESS = new Object[]{ "Logs successfully edited.",ATTACK_MESSAGE};
	public static final Object[] EMPTY_PETTY_FAIL_WRONG_TYPE = new Object[]{ "You can only empty petty cash on a Banking port.",ATTACK_MESSAGE};
	public static final Object[] EMPTY_PETTY_FAIL_FIREWALL = new Object[]{ "_0_'s firewall has caused the empty petty cash to fail.",POPUP_MESSAGE};
	public static final Object[] EMPTY_PETTY_SUCCESS = new Object[]{ "You have successfully stolen _0_.",ATTACK_MESSAGE}; //_0_ = amount, _1_ = ip
	public static final Object[] EMPTY_PETTY_SUCCESS_GAME = new Object[]{ "You have successfully stolen _0_ from _1_.",GAME_MESSAGE}; //_0_ = amount, _1_ = ip
	public static final Object[] EMPTY_PETTY_FAIL_NO_ACTIVE_BANK = new Object[]{ "Could not empty petty cash from _0_, you don't have a non-dummy bank port turned on.",ATTACK_MESSAGE}; //_0_ = ip
	//public static final Object[] EQUIP_FAIL = new Object[]{ "The requested piece of hardware cannot currently be equipped.",POPUP_ERROR};
	public static final Object[] EQUIP_FAIL_WRONG_TYPE = new Object[]{ "Can not equip this type of hardware in this slot.",POPUP_ERROR};
    public static final Object[] EQUIP_FAIL_HD_FULL = new Object[]{ "Could not install card: Equipping this card would put you over your maximum file limit.",POPUP_ERROR};
    public static final Object[] EQUIP_FAIL_CPU_RESTRICTIONS = new Object[]{ "Could not install card: Equpping this card would put you over your CPU limit and overheat you.",POPUP_ERROR};
    public static final Object[] EQUIP_FAIL_WATCH_RESTRICTIONS = new Object[]{ "Could not install card: Equipping this card would put you over your maximum number of watches.",POPUP_ERROR};
	public static final Object[] EQUIP_SUCCESS = new Object[]{ "Card successfully replaced.",POPUP_MESSAGE, OptionPanel.CARD_REPLACED_KEY}; //_0_ = file name
	public static final Object[] EXCHANGE_COMMODITY_FAIL_NO_BANKING_PORT = new Object[]{ "Failed to exchange commodity. Make sure you have a non-dummy bank port on.",POPUP_ERROR};
	public static final Object[] EXCHANGE_COMMODITY_FAIL_NOT_ENOUGH_COMMODITY = new Object[]{ "Failed to exchange commodity. You do not have that much _0_.",POPUP_ERROR}; //_0_ = commodity type
	public static final Object[] EXCHANGE_COMMODITY_FAIL_NOT_ENOUGH_MONEY = new Object[]{ "Failed to exchange commodity. You do not have that much money in your petty cash.",POPUP_ERROR}; //_0_ = commodity type
	public static final Object[] EXCHANGE_COMMODITY_FAIL_HD_FULL = new Object[]{ "Failed to exchange commodity. Your HD is full.",POPUP_ERROR};
	public static final Object[] EXCHANGE_COMMODITY_SUCCESS = new Object[]{ "Converted _0_ of commodity into _1_",POPUP_MESSAGE, OptionPanel.COMMOD_TO_FILE_KEY}; //_0_ = amount, _1_ = file name
	public static final Object[] EXCHANGE_COMMODITY_SUCCESS_FROM_FILE = new Object[]{ "Converted _0_ from file _1_ into commodity.",POPUP_MESSAGE, OptionPanel.FILE_TO_COMMOD_KEY}; //_0_ = amount, _1_ = file name
	
	public static final Object[] FILE_CHANGED_SINCE_LAST_SAVE = new Object[]{ "You have edited this file since you last compiled it, please save as new name.",POPUP_ERROR};
	public static final Object[] FILE_NOT_FOUND = new Object[]{ "File could not be found on HD.",POPUP_ERROR};
	public static final Object[] FILE_SUCCESSFULLY_STOLEN = new Object[]{ "You have successfully stolen _0_.",ATTACK_MESSAGE};  //_0_ = file name, _1_ = ip stolen from
	public static final Object[] FILE_SUCCESSFULLY_STOLEN_GAME = new Object[]{ "You have successfully stolen _0_ from _1_.",GAME_MESSAGE};  //_0_ = file name, _1_ = ip stolen from
	public static final Object[] FTP_FAIL_WRONG_TARGET = new Object[]{ "Invalid target IP: You can only put or get files that you own.",POPUP_ERROR};
	public static final Object[] FTP_FAIL_TOO_MANY_PUT_GET = new Object[]{ "You can only use put or get once per script.",POPUP_ERROR};
	public static final Object[] FILE_IO_FAIL_INVALID_TYPE = new Object[]{ "You can only perform file operations on files of the type 'text'.",POPUP_ERROR};
	public static final Object[] FILE_IO_FAIL_LINE_NUMBER = new Object[]{ "Line number greater than file size.",POPUP_ERROR};
	public static final Object[] FILE_IO_FAIL_FILE_SIZE = new Object[]{ "Maximum file size reached for _0_.",POPUP_ERROR}; //_0_ = file name
	
	public static final Object[] FILE_TAKEN = new Object[]{ "_0_ x_1_ taken from HD.",POPUP_MESSAGE}; //_0_ = file name _1_ = quantity
	
	public static final Object[] FIREWALL_REPLACED = new Object[]{ "Firewall successfully replaced.\n\n Old one placed on HD as _0_.",POPUP_MESSAGE,OptionPanel.FIREWALL_REPLACED_KEY}; //_0_ = file name
	public static final Object[] FTP_NOT_FOUND = new Object[]{ "The FTP port you attempted to access was not found.",POPUP_ERROR};
	public static final Object[] FTP_FAIL_PASSWORD_INCORRECT = new Object[]{ "The password you provided to connect to this FTP site was incorrect.",POPUP_ERROR};
	public static final Object[] FTP_PUT_FAIL_HD_FULL = new Object[]{ "The file could not be uploaded because the target HD was full.",POPUP_ERROR};
	public static final Object[] FTP_PUT_FAIL = new Object[]{ "The target FTP was not accessible.",POPUP_ERROR};
	public static final Object[] FUNCTION_ONLY_WITH_TRIGGERWATCH = new Object[]{ "This function only works from triggerWatch().",POPUP_ERROR};
	
	public static final Object[] GIVEN_FILE = new Object[]{ "You were given _1_ of the file _0_.",POPUP_MESSAGE}; //_0_ = file name
	
	public static final Object[] HD_FULL = new Object[]{ "File could not be saved HD full.",POPUP_ERROR};
	public static final Object[] HEAL_FAIL_OVERHEATED = new Object[]{ "You cannot heal a port when it is overheated.",POPUP_ERROR};
	public static final Object[] HEAL_FAIL_LIMIT = new Object[]{ "You can only heal _0_ times per defense.",POPUP_ERROR}; //_0_ = heal limit
	public static final Object[] HEAL_SUCCESS = new Object[]{ "Port _0_ healed for _1_.",POPUP_MESSAGE, OptionPanel.HEALING_KEY}; //_0_ = port number, _1_ = cost
	public static final Object[] HEAL_FAIL_WEAKENED = new Object[]{ "Cannot heal port _0_ because it is in a weakened state.",POPUP_ERROR}; //_0_ = port number
	
	public static final Object[] INSTALL_SCRIPT_SUCCESS = new Object[]{ "Script successfully installed.",ATTACK_MESSAGE};
	public static final Object[] INSTALL_SCRIPT_SUCCESS_GAME = new Object[]{ "Script successfully installed.",GAME_MESSAGE};
	public static final Object[] INSTALL_SCRIPT_FAIL_WRONG_TYPE = new Object[]{ "Install script failed. File was of the wrong type.",ATTACK_MESSAGE};
	public static final Object[] INSTALL_SCRIPT_FAIL_WRONG_TYPE_GAME = new Object[]{ "Install script failed. File was of the wrong type.",GAME_MESSAGE};
	public static final Object[] INSTALL_SCRIPT_FAIL_NO_FILE = new Object[]{ "Install script failed. No file selected.",ATTACK_MESSAGE};
	public static final Object[] INSTALL_SCRIPT_FAIL_NO_FILE_GAME = new Object[]{ "Install script failed. No file selected.",GAME_MESSAGE};
	public static final Object[] INSTALL_SCRIPT_FAIL_FIREWALL = new Object[]{ "Install script failed due to opponents firewall.",POPUP_ERROR};
	public static final Object[] INSTALL_FIREWALL_FAIL_LEVEL = new Object[]{ "You must be firewall level _0_ to install that firewall.",POPUP_ERROR}; //_0_ = firewall level
	
	public static final Object[] MAX_PROGRAMS_REACHED = new Object[]{ "You can not have that many applications installed. Buy better memory.",POPUP_ERROR};
	public static final Object[] MAX_WATCHES_REACHED = new Object[]{ "You can only have 21 watches in the handler at one time.",POPUP_ERROR};
	public static final Object[] MESSAGE_FAIL_INVALID_TARGET = new Object[]{ "Invalid message target.",POPUP_ERROR};
	public static final Object[] MESSAGE_FAIL_TOO_LONG = new Object[]{ "The message you attempted to send was too long.",POPUP_ERROR};
	
	public static final Object[] OVERHEATED_OPPONENT = new Object[]{ "You have overheated _0_.",ATTACK_MESSAGE}; //_0_ = ip
	public static final Object[] OVERHEATED_OPPONENT_GAME = new Object[]{ "You have overheated _0_.",GAME_MESSAGE}; //_0_ = ip
	
	public static final Object[] PLAYER_BUSY = new Object[]{ "Player _0_ has been very busy.",GAME_MESSAGE}; //_0_ = ip
	public static final Object[] PORT_NOT_ON = new Object[]{ "Port _0_ at _1_ was not on.",POPUP_ERROR}; //_0_ = port, _1_ = ip
	public static final Object[] PORT_WAS_NOT_FTP = new Object[]{ "Port _0_ at _1_ was not an FTP port.",POPUP_ERROR}; //_0_ = port, _1_ = ip
	public static final Object[] PORT_WAS_DUMMY = new Object[]{ "Port _0_ at _1_ was a dummy port.",ATTACK_MESSAGE}; //_0_ = port, _1_ = ip
	public static final Object[] PORT_ALREADY_ATTACKING = new Object[]{ "Port _0_ is already performing an attack.",ATTACK_MESSAGE}; //_0_ = port number.
	public static final Object[] PORT_WAS_NOT_WEAKENED = new Object[]{  "Port _0_ at _1_ was not sufficiently weakened.",ATTACK_MESSAGE};  //_0_ = port number, _1_ = ip
	public static final Object[] PORT_ALREADY_UNDER_ATTACK = new Object[]{ "Port _0_ at _1_ was already under attack by _2_.",GAME_MESSAGE}; //_0_ = port number, _1_ = ip, _2_ = accessing ip
	public static final Object[] PORT_IS_OVERHEATED = new Object[]{ "Port _0_ at _1_ is overheated or frozen.",GAME_MESSAGE}; //_0_ = port number, _1_ = ip
	public static final Object[] PORT_OFF_FAIL_UNDER_ATTACK = new Object[]{ "You can not turn off a port when it is under attack.",POPUP_ERROR};
	public static final Object[] PORT_OFF_FAIL_ATTACKING = new Object[]{ "You can not turn off a port when it is attacking.",POPUP_ERROR};
	public static final Object[] PORT_OFF_FAIL_OVERHEATED = new Object[]{ "You can not turn off a port when it is overheated.",POPUP_ERROR};
	public static final Object[] PORT_OFF_FAIL_DAMAGED = new Object[]{ "You can not turn off a port when it is damaged.",POPUP_ERROR};
	public static final Object[] PORT_ON_FAIL_EXCEED_CPU = new Object[]{"You can not turn on this port because it will exceed your maximum CPU load.",POPUP_ERROR};
	public static final Object[] PURCHASE_FAIL_HD_FULL = new Object[]{ "You cannot buy a file when your HD is full.",POPUP_ERROR};
	public static final Object[] PURCHASE_FAIL_NOT_ENOUGH_MONEY = new Object[]{ "You did not have enough money in your petty cash to make this purchase.",POPUP_ERROR};
	public static final Object[] PURCHASE_FAIL_NOT_HIGH_ENOUGH_LEVEL = new Object[]{ "You are not high enough level to purchase this file.",POPUP_ERROR};
	public static final Object[] PURCHASE_NEW_CPU = new Object[]{ "You purchased a new _0_ CPU for your computer. It is installed.",POPUP_MESSAGE}; //_0_ = file name
	public static final Object[] PURCHASE_FAIL_OLDER_CPU = new Object[]{ "You start to install your CPU and realize you already have a better one.",POPUP_ERROR};
	public static final Object[] PURCHASE_NEW_HD = new Object[]{ "You purchased a new _0_ Hard Drive for your computer. It is installed.",POPUP_MESSAGE}; //_0_ = file name
	public static final Object[] PURCHASE_FAIL_OLDER_HD = new Object[]{ "You start to install your new Hard Drive and realize you already have a better one.",POPUP_ERROR};
	public static final Object[] PURCHASE_NEW_MEMORY = new Object[]{ "You purchased new _0_ Memory for your computer. It is installed.",POPUP_MESSAGE}; // _0_ = file name
	public static final Object[] PURCHASE_FAIL_OLDER_MEMORY = new Object[]{ "You start to install your new Memory and realize you already have a better one.",POPUP_ERROR};
	public static final Object[] PURCHASE_SUCCESS = new Object[]{"You have successfully purchased _0_ _1_(s) for _2_.",POPUP_MESSAGE, OptionPanel.FILE_PURCHASED_KEY}; //_0_ = quantity, _1_ = file name, _2_ = price
	public static final Object[] PURCHASE_FAIL_FILE_NOT_FOUND = new Object[]{ "You attempted to purchase a file that could not be found.",POPUP_ERROR};
	
	public static final Object[] QUEST_COMPLETED = new Object[]{ "Completed Quest: _0_",POPUP_MESSAGE}; //_0_ = quest name
	public static final Object[] QUEST_GIVEN = new Object[]{ "Given Quest: _0_",POPUP_MESSAGE}; //_0_ = quest name
	
	
	public static final Object[] RECEIVED_COMMODITY = new Object[]{ "Received _0_ _1_.",REDIRECT_MESSAGE}; //_0_ = amount, _1_ = commodity
	public static final Object[] RECEIVED_COMMODITY_GAME = new Object[]{ "Received _0_ _1_ from _2_.",GAME_MESSAGE}; //_0_ = amount, _1_ = commodity, _2_ = ip
	public static final Object[] RECEIVED_COMMODITY_FAIL = new Object[]{ "Transaction failed, make sure you have an active redirect port.",POPUP_ERROR};
	public static final Object[] REDIRECT_FAIL_WRONG_TYPE = new Object[]{ "You cannot redirect shipments from a port that is not a redirect port.",REDIRECT_MESSAGE};
	public static final Object[] REDIRECT_XP_MAX = new Object[]{ "You can not gain anymore redirect experience off _0_ at this time.",REDIRECT_MESSAGE}; //_0_ = ip
	public static final Object[] REDIRECT_EXCEEDED_MAXIMUM_TIMEOUT = new Object[]{ "Your redirect application reached its maximum timeout for redirecting off one port.",REDIRECT_MESSAGE};
	public static final Object[] REDIRECT_FAIL_NOT_ENOUGH_MONEY = new Object[]{ "You must have $10 in your petty cash to start a redirect.",POPUP_ERROR};
	public static final Object[] REDIRECT_FAIL_ALREADY_REDIRECTING = new Object[]{ "This port is already redirecting shipments.",REDIRECT_MESSAGE};
	public static final Object[] REDIRECT_FAIL_OVERHEATED = new Object[]{ "Can't redirect when port is overheated.",REDIRECT_MESSAGE};
	public static final Object[] REPAIR_FAIL_LEVEL = new Object[]{ "You need to have level _1_ to repair using _0_.",POPUP_ERROR}; //_0_ = commodity, _1_ = level
	public static final Object[] REMOVE_FIREWALL_SUCCESS = new Object[]{ "Firewall successfully removed.\n\nSaved to HD as _0_.",POPUP_MESSAGE,OptionPanel.FIREWALL_REMOVED_KEY}; //_0_ = file name
	public static final Object[] REMOVE_FIREWALL_FAIL_HD_FULL = new Object[]{ "You can't remove the firewall while your HD is full.",POPUP_ERROR}; 
	public static final Object[] REPAIR_FAIL_NOT_ENOUGH_COMMODITIES = new Object[]{ "You don't have enough commodities to repair _0_.\n\nRequired: _1_",POPUP_ERROR}; //_0_ = file name, _1_ = required commodities
	public static final Object[] REPAIR_SUCCESS = new Object[]{ "Card repaired: _0_.\n\nUsed _1_",POPUP_MESSAGE, OptionPanel.CARD_REPAIRED_KEY}; //_0_ = card name, _1_ = commodities used
	public static final Object[] REPLACE_APPLICATION_UNDER_ATTACK = new Object[]{ "You cannot replace an application while a port is under attack.",POPUP_ERROR};
	public static final Object[] REPLACE_APPLICATION_ATTACKING = new Object[]{ "You cannot replace an application while a port is attacking.",POPUP_ERROR};
	public static final Object[] REPLACE_APPLICATION_OVERHEATED = new Object[]{ "You cannot replace an application while a port is overheated.",POPUP_ERROR};
	public static final Object[] REPLACE_APPLICATION_SUCCESS = new Object[]{ "Application successfully replaced.",POPUP_MESSAGE, OptionPanel.APP_REPLACED_KEY};
	public static final Object[] REDIRECT_FINISHED_GAME = new Object[]{ "Port _0_ finished redirecting.",GAME_MESSAGE}; //_0_ = port number
	public static final Object[] REDIRECT_FINISHED = new Object[]{ "Finished redirecting.",REDIRECT_MESSAGE}; //_0_ = port number
	
	public static final Object[] SAVE_FAIL_HD_FULL = new Object[]{ "Folder could not be created, is your HD full?",POPUP_ERROR};
	public static final Object[] SCAN_FAIL_NO_MONEY = new Object[]{ "You do not have enough money in your petty cash to scan.",POPUP_ERROR};
	public static final Object[] SCAN_FAIL_OVERHEATED = new Object[]{ "Cannot scan when your computer is overheated.",POPUP_ERROR};
	public static final Object[] SECRET_DOCUMENT_TASK_COMPLETED = new Object[]{ "You have finished one of the tasks assigned in a secret document.",GAME_MESSAGE};
	public static final Object[] SELL_FAIL_WRONG_TYPE = new Object[]{ "You cannot sell this type of file back to the store.",POPUP_ERROR};
    public static final Object[] SELL_FAIL_BANK_PORT = new Object[]{ "Could not sell file(s): you must have a non-dummy bank port turned on.", POPUP_ERROR};
	public static final Object[] STEAL_FILE_FAIL_WRONG_TYPE = new Object[]{ "You can only steal a file on an FTP port.",ATTACK_MESSAGE};
	
	public static final Object[] TRANSFER_FAIL_NOOB_LEVEL = new Object[]{ "Transaction failed, you can not receive transfers until you are level _0_ or higher.",POPUP_ERROR}; // _0_ = global noob level
	public static final Object[] TRANSFER_RECEIVED = new Object[]{ "Received transfer of $_0_ from _1_.",POPUP_MESSAGE, OptionPanel.TRANSFER_FROM_KEY}; //_0_ = amount received, _1_ = ip received from
	public static final Object[] TRANSFER_RECEIVE_FAIL_BANK_PORT = new Object[]{ "Could not recieve transfer of $_0_ from _1_.  You must have a non-dummy bank port turned on.",POPUP_ERROR}; //_0_ = amount, _1_ = ip sent to
	public static final Object[] TRANSFER_SEND_FAIL_BANK_PORT = new Object[]{ "Could not complete the transfer: target computer does not have an non-dummy bank port turned on.",POPUP_ERROR};
	public static final Object[] TRANSFER_SENT_SUCCESSFUL = new Object[]{ "Transfer of $_0_ successful.",POPUP_MESSAGE, OptionPanel.TRANSFER_TO_KEY}; //_0_ = amount
	public static final Object[] TRANSFER_FAIL_NOOB = new Object[]{ "You cannot transfer money until you are total level 15 or higher.",POPUP_ERROR};
	
	public static final Object[] UNINSTALL_PORT_FAIL_UNDER_ATTACK = new Object[]{ "You cannot uninstall an application while a port is under attack.",POPUP_ERROR};
	public static final Object[] UNINSTALL_PORT_FAIL_ATTACKING = new Object[]{ "You cannot uninstall an application while a port is attacking.",POPUP_ERROR};
	public static final Object[] UNINSTALL_PORT_FAIL_OVERHEATED = new Object[]{ "You cannot uninstall an application while a port is overheated.",POPUP_ERROR};
	//public static final Object[] UNEQUIP_FAIL = new Object[]{ "The requested piece of hardware cannot currently be unequipped.",POPUP_ERROR};
    public static final Object[] UNEQUIP_FAIL_HD_FULL = new Object[]{ "Could not uninstall card: Unequipping this card would put you over your maximum file limit.",POPUP_ERROR};
    public static final Object[] UNEQUIP_FAIL_CPU_RESTRICTIONS = new Object[]{ "Could not uninstall card: Unequipping this card would put you over your CPU limit and overheat you.",POPUP_ERROR};
    public static final Object[] UNEQUIP_FAIL_WATCH_RESTRICTIONS = new Object[]{ "Could not uninstall card: Unequipping this card would put you over your maximum number of watches.",POPUP_ERROR};
    
    public static final Object[] UPGRADE_FILE_SIZE = new Object[]{ "If you upgraded your account the maximum file size is "+(Computer.PAY_FILE_SIZE_LIMIT/Computer.FREE_FILE_SIZE_LIMIT) + " times as much",POPUP_MESSAGE};
	
	public static final Object[] VOTE_FAIL_NOOB_LEVEL = new Object[]{ "You must be level _0_ or greater to vote for sites.",POPUP_ERROR}; //_0_ = global noob level,GAME_MESSAGE};
	public static final Object[] VOTE_FAIL_OWN_SITE = new Object[]{ "You cannot vote for your own web site.",POPUP_ERROR};
	public static final Object[] VOTE_SUCCESS = new Object[]{ "Vote successful _0_ left.",POPUP_MESSAGE, OptionPanel.VOTE_SUCCESSFUL_KEY}; //_0_ = votes left
	public static final Object[] VOTE_FAIL_NO_VOTES = new Object[]{ "You do not currently have any votes available.",POPUP_ERROR};
	public static final Object[] VOTE_FAIL_HTTP_NOT_ON = new Object[]{ "You cannot receive a vote if your HTTP is not on.",POPUP_ERROR};
	
	public static final Object[] WEBSITE_SAVE_FAIL_TOO_BIG = new Object[]{ "Your web-page cannot exceed 30,000 characters in size.",POPUP_ERROR};
	public static final Object[] WATCH_ON_FAIL = new Object[]{ "You can not have this many watches running at once, buy some better memory.",POPUP_ERROR};
	
	public static final Object[] ZOMBIE_OVERHEATED = new Object[]{ "Computer at _0_ just overheated!",ATTACK_MESSAGE}; //_0_ = zombie's ip
	public static final Object[] ZOMBIE_ATTEMPT_FAIL = new Object[]{ "You cannot access this port.",ATTACK_MESSAGE};
	public static final Object[] ZOMBIE_FAIL_NOT_ENOUGH_MONEY = new Object[]{ "It costs $20 to attempt an attack from a zombie port.",POPUP_ERROR};
	
	
	
	private Computer computer;
	public MessageHandler(Computer computer){
		this.computer = computer;
	}
	
	
	public void addMessage(Object[] messageArray, Object[] parameters){
        addMessage(messageArray,parameters,null);
	}
	
	public void addMessage(Object[] messageArray, Object[] parameters,Object[] portInfo){
		String message = ""+(String)messageArray[0];
		int type = (Integer)messageArray[1];
		Object[] send = new Object[4];
		send[1] = type;
		if(parameters!=null){
			for(int i = 0;i < parameters.length; i++){
				message = message.replaceAll("_"+i+"_",java.util.regex.Matcher.quoteReplacement(""+parameters[i]));
			}
			
		}
		send[0] = message;
        // if there's a preference associated with this message, then send it along
        if (messageArray.length == 3) {
            send[2] = messageArray[2];
        } else {
            send[2] = "";
        }
		send[3] = portInfo;
		ArrayList messages = computer.getMessages();
		messages.add(send);
	}
	
}
