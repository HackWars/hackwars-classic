package simulation;
import Game.*;

public class SimComputer {
    
    
        public static final int BASIC_BANK_CPU = 10;           // cost of the Basic Bank in CPU units
        public static final int BASIC_ATTACK_CPU = 20;         // cost of a basic attack in CPU units
        public static final int EMPTY_PETTY_ATTACK_CPU = 20;   // cost of an emptyPeyyCash() attack in CPU Units
        public static final int SCAN_WATCH_CPU = 5;
        
        /* Stole these from Computer.java */
        public static final float CPU_CHART[]=new float[]{50.0f,100.0f,150.0f,200.0f,250.0f,300.0f,75.0f};//Maximum Loads of various CPUs.
    	public static final float MEMORY_CHART[]=new float[]{8.0f,16.0f,24.0f,32.0f,8.0f};//Maximum Port Count.

    
        public int level;                      // the player's total level
        public float cpuPct;
        //public String attackPortFW;            // String key for the firewalls hashmap in NewFireWall.java; FW on this computer's attack port
        //public String targetedPortFW;          // identifies the FW this computer has on the port that is being attacked
        public int roundsPerHeal;         // heal rate cards... +0% = 4, +25% = 3, +50% = 2, +75% = 1
        public float healCostModifier;         // heal cost discount cards... 1.0 = full cost, 0.75f = -25%, 0.50 =  -50%, 0.25 = -75%
        public int attackPortHealThreshold;    // only used for NPCs
        public int targetedPortHealThreshold;  // only used for NPCs
        boolean counterAttack;                  // does this computer counter attack?
        boolean counterAttackStarted;           // has the counter attack already been started?
        float counterAttackThreshold;           // at what threshold does the counter attack start?
        
        public float attackPortBaseFWDamage = 0.0f;
        public float attackPortBaseFWAbsorb = 0.0f;
        public float targetedPortBaseFWDamage = 0.0f;
        public float targetedPortBaseFWAbsorb  = 0.0f;
        
        public float baseAttackDamage = 0.0f;
        public float cardsAttackDamage = 0.0f;

        public SimComputer(int level, 
                        float cpuPct,
                        int baseAttackDamage, 
                        int cardsAttackDamage,
                        String attackPortFW,
                        String targetedPortFW,
                        float healRateModifier,
                        float healCostModifier,
                        int attackPortHealThreshold,
                        int targetedPortHealThreshold,
                        boolean counterAttack,
                        float counterAttackThreshold
                        ) {
            
            this.level = level;
            this.cpuPct = cpuPct;
            this.baseAttackDamage = baseAttackDamage;
            this.cardsAttackDamage = cardsAttackDamage;
            switch ((int)(healRateModifier * 100)) {
                case 100:  this.roundsPerHeal = 4; break;
                case 75: this.roundsPerHeal = 3; break;
                case 50: this.roundsPerHeal = 2; break;
                case 25: this.roundsPerHeal = 1; break;
            }
            this.healCostModifier = healCostModifier;
            this.attackPortHealThreshold = attackPortHealThreshold;
            this.targetedPortHealThreshold = targetedPortHealThreshold;
            this.counterAttack = counterAttack;
            this.counterAttackStarted = false;
            this.counterAttackThreshold = counterAttackThreshold;
            
            // calculate the CPU %
            /* Assumptions:
                                  * 1)  they have the best CPU available at their level.
                                  * 2) they have the max memory available at their level
                                  * 3) they run a basic bank only
                                  * 4) they have the best FW they can for their level on their attack port
                                  * 5) they have no FW on their bank port
                                  * 6) they run a scan watch
                                  * 7) they run no other applications while attacking
                                  */
            float totalCpu = 0.0f;
            if (level < 50)
                totalCpu = 50.0f;
            else if (level < 100)
                totalCpu = 75.0f;
            else if (level < 150)
                totalCpu = 100.0f;
            //if (level < 200)
            //    totalCpu = 125.0f;
            else if (level < 250)
                totalCpu = 150.0f;
            //if (level < 300)
            //    totalCpu = 175.0f;
            else if (level < 350)
                totalCpu = 200.0f;
            //if (level < 400)
            //    totalCpu = 225.0f;
            else if (level < 450)
                totalCpu = 250.0f;
            //if (level < 500)
            //    totalCpu = 275.0f;
            else if (level < 600)
                totalCpu = 300.0f;
            //if (level < 700)
            //    totalCpu = 350.0f;
            //if (level < 800)
            //    totalCpu = 400.0f;
            //if (level >= 800)
            //    totalCpu = 450.0f;
            else
                totalCpu = 300.0f;
            
System.out.println("Max CPU = " + totalCpu);
            float cpuAvailable = totalCpu - (BASIC_BANK_CPU + SCAN_WATCH_CPU);
            int numAttacks = 0;
            while (cpuAvailable > BASIC_ATTACK_CPU) {
                numAttacks++;
                cpuAvailable -= BASIC_ATTACK_CPU;
            }

            this.cpuPct =  cpuAvailable / totalCpu;
System.out.println("Number of attacks possible: " + numAttacks + " (CPU remaining = " + cpuAvailable + " = " + this.cpuPct + "%)");
            
            /* Assumptions:
                                  * 1) Attack back = max_attack / 2. (totally average)
                                  * 2) Absorption = base absorption (totally average)
                                  */
            NewFireWall fw = new NewFireWall();
            Object[] attackPortFWary = (Object[])fw.firewalls.get(attackPortFW);
            Object[] targetPortFWary = (Object[])fw.firewalls.get(targetedPortFW);
            
            this.attackPortBaseFWDamage = ((float)new Float("" + attackPortFWary[NewFireWall.MAX_ATTACK])) / 2.0f;
            this.attackPortBaseFWAbsorb = (float)new Float( "" + attackPortFWary[NewFireWall.DAMAGE_MODIFIER]);
            this.targetedPortBaseFWDamage = ((float)new Float("" + targetPortFWary[NewFireWall.MAX_ATTACK])) / 2.0f;
            this.targetedPortBaseFWAbsorb = (float)new Float("" + targetPortFWary[NewFireWall.DAMAGE_MODIFIER]);
            
        }
    }