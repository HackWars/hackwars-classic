package GUI;

import java.util.HashMap;

public class AttributeCellObject {
	public final static int EMPTY_PETTY_FAIL = 0;
	public final static int EMPTY_PETTY_REDUCED = 1;
	public final static int CHANGE_DAILY_FAIL = 2;
	public final static int CHANGE_DAILY_REDUCED = 3;
	public final static int INSTALL_SCRIPT_FAIL = 4;
	public final static int STEAL_FILE_FAIL = 5;
	public static HashMap firewallAttributes = new HashMap();
	static{
		firewallAttributes.put("emptyPettyCash()fail",0);
		firewallAttributes.put("emptyPettyCash()reduce",1);
		firewallAttributes.put("stealFile()fail",2);
		firewallAttributes.put("changeDailyPay()fail",3);
		firewallAttributes.put("changeDailyPay()reduce",4);
		firewallAttributes.put("installScript()fail",5);
	};
    private String display;
    private float quality;
    private int attributeType;
    
    public AttributeCellObject(float quality, int attributeType, String display) {
        this.quality = quality;
        this.attributeType = attributeType;
        this.display = display;
    }
    
    public String toString() {
        return display;
    }
    
    public float getQuality() {
        return this.quality;
    }
    
    public int getAttributeType() {
        return this.attributeType;
    }
    
    public int compare(AttributeCellObject compare) {
        float compareQuality = compare.getQuality();
        int compareAttributeType = compare.getAttributeType();
        
        if (this.attributeType < compareAttributeType) {
            return -1;
        } else if (this.attributeType > compareAttributeType) {
            return 1;
        } else {
            if (this.quality < compareQuality) {
              return -1;
            } else if (this.quality > compareQuality) {
              return 1;
            } else {
              return 0;
            }
        }
        
    }
    
}