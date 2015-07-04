package DBAdmin;

import GUI.Equipment; // 
import javax.swing.*;
import net.miginfocom.swing.*;
import Game.Computer; // for CPU, WATCHES, MEMORY
import Game.FileSystem; // for HD sizes

public class EquipmentPanel extends JPanel {

    public EquipmentPanel() {

        setLayout(new MigLayout("wrap 1, align leading,fill,h 100:n:n"));
    
        String type = "Equipment";
        LabelContentPanel topPanel = new LabelContentPanel(type, new HardwareContentPanel());
        
        type = "Cards";
        LabelContentPanel bottomPanel = new LabelContentPanel(type, new CardContentPanel());
        
        add(topPanel, "growx");
        add(bottomPanel, "growx");
        
    }
    
    
    public class LabelContentPanel extends JPanel {
        
        public LabelContentPanel(String type, JPanel contentPanel) {
            //this.label = new JLabel(labelString);
            setLayout(new MigLayout("wrap 1"));
            
            setBorder(new FunctionBorder(type));
            add(contentPanel);
        }
    }
    
    public class HardwareContentPanel extends JPanel {
    
        private JComboBox cpu, hd, memory;
    
        public HardwareContentPanel() {
            
            setLayout(new MigLayout("wrap 6"));
            
            JLabel label = new JLabel("CPU: ");
            add(label);
            
            Object[] CPU_CHART = EquipmentPanel.getObjectArray(Computer.CPU_CHART);//new Object[Computer.CPU_CHART.length];
            cpu = new JComboBox(CPU_CHART);
            add(cpu);
            
            label = new JLabel("HD: ");
            add(label, "gap 50");
            
            Object[] HD_CHART = EquipmentPanel.getObjectArray(FileSystem.HD_CHART);
            hd = new JComboBox(HD_CHART);
            add(hd);
            
            label = new JLabel("Memory: ");
            add(label, "gap 50");
            
            Object[] MEMORY_CHART = EquipmentPanel.getObjectArray(Computer.MEMORY_CHART);
            memory = new JComboBox(MEMORY_CHART);
            add(memory);
        }
    }
    
    public class CardContentPanel extends JPanel {
        public final static int NUM_CARDS = 3;
        
        public CardContentPanel() {
            
            setLayout(new MigLayout("wrap 4, gap 20"));
        
            int attribute1Type = 0;
            int attribute2Type = 0;
            float maxDurability = 0.0f;
            float currentDurability = 0.0f;
            float attribute1quality = 0.0f;
            float attribute2quality = 0.0f;
            Card card = null;
            Card[] cards = new Card[NUM_CARDS];
            
            JLabel label;
            
            label = new JLabel("CARD");
            add(label);
            
            label = new JLabel("DURABILITY");
            add(label);
            
            label = new JLabel("ATTRIBUTE");
            add(label);
            
            label = new JLabel("QUALITY");
            add(label);
            
            for (int i = 0; i < NUM_CARDS; i++) {
                attribute1Type = 0;//i;//getAttributeType(0,0); // 0 = agp; 0 = which attribute
                attribute2Type = 1;//i+3;//getAttributeType(0,1);
                maxDurability = 0.0f;//getDurability(0, "current");
                currentDurability = 20.0f;//getDurability(0, "max");
                attribute1quality = 0.0f; //getAttributeQuality(0,0);
                attribute2quality = 20.0f; //getAttributeQuality(0,1);
                card = new Card(i, attribute1Type, attribute2Type, maxDurability, currentDurability, attribute1quality, attribute2quality, this);

                cards[i] = card;
            }
            
        }
    }
    
    public class Card {
        private JLabel cardType;
        private JComboBox attribute1typeCB;
        private JComboBox attribute2typeCB;
        private JTextField maxDurabilityTF;
        private JTextField currentDurabilityTF;
        private JTextField attribute1qualityTF; // max value for the attribute
        private JTextField attribute2qualityTF;
        private String cardName = "";
        
        public Card(int cardNumber, int attribute1Type, int attribute2Type, float maxDurability, float currentDurability, float attribute1quality, float attribute2quality, CardContentPanel myCCP) {
            
            if (cardNumber == 0) {
                cardName = "AGP";
            } else {
                cardName = "PCI";
            }
            
            cardType = new JLabel(cardName);
            
            Object[] attributeTypes = EquipmentPanel.getObjectArray(Equipment.VALUES);

            attribute1typeCB = new JComboBox(attributeTypes);
            attribute1typeCB.setSelectedIndex(attribute1Type);
            
            attribute2typeCB = new JComboBox(attributeTypes);
            attribute2typeCB.setSelectedIndex(attribute2Type);
            
            maxDurabilityTF = new JTextField("" + maxDurability);
            currentDurabilityTF = new JTextField("" + currentDurability);
            attribute1qualityTF = new JTextField("" + attribute1quality);
            attribute2qualityTF = new JTextField("" + attribute2quality);

            // readout: CARD, DURABILITY, ATTRIBUTE, QUALITY
            myCCP.add(cardType);
            myCCP.add(currentDurabilityTF, "growx");
            myCCP.add(attribute1typeCB, "growx");
            myCCP.add(attribute1qualityTF, "growx");

            cardType = new JLabel("max:");
            myCCP.add(cardType);
            myCCP.add(maxDurabilityTF, "growx");
            myCCP.add(attribute2typeCB, "growx");
            myCCP.add(attribute2qualityTF, "growx");
            
        }
        
        public int getAttribute1Type() {
            return attribute1typeCB.getSelectedIndex();
        }
        
        public int getAttribute2Type() {
            return attribute2typeCB.getSelectedIndex();
        }
        
        public float getMaxDurability() {
            return Float.parseFloat(maxDurabilityTF.getText());
        }
        
        public float getCurrentDurability() {
            return Float.parseFloat(currentDurabilityTF.getText());
        }
        
        public float getAttribute1Quality() {
            return Float.parseFloat(attribute1qualityTF.getText());
        }
        
        public float getAttribute2Quality() {
            return Float.parseFloat(attribute2qualityTF.getText());
        }
        
    }
    
    public static Object[] getObjectArray(float[] array) {
        Object[] newArray = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }
    
    public static Object[] getObjectArray(int[] array) {
        Object[] newArray = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static Object[] getObjectArray(String[] array) {
        Object[] newArray = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }


    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        EquipmentPanel x = new EquipmentPanel();
        frame.add(x);
        frame.pack();
        frame.setVisible(true);
        
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
