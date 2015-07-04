package simulation;

import Game.*;

/**
* Class used to calculate the length of time/rounds until a victory is achieved in a pvp battle.
* This class assumes that P1 is the aggressor.  P2 is the NPC.
*/
public class PvP {

    public PvP() {}

    private static final int HEAL_COST_PER_POINT = 2;
    private static int xpTable[] = new int[100];        //table of experience values for each level
    
    
    /* Things we need to know to calculate prices for goods, and NPC petty cash/daily pay:
             * 1) The CPU available to each player at a given level
             *            * CPU is based on total level right now, which makes it impossiblet o simulate
             *            * we can assume that attack level is a certain % of the total level at various levels
             * 2) The number of basic attacks a player can run with a given CPU.
             *            * we will assume no heal watches are being used
             * 3) 
             *  
             */

    public void simulate(SimComputer p1, SimComputer p2) {

        //rounds until one or the other wins
        int rounds = 0;
        
        // set up the stats for each user
        float p1AttackPortHealth = 100.0f;
        float p2AttackPortHealth = 100.0f;
        float p2TargetedPortHealth = 100.0f;
        boolean p1wins = false;
        boolean p2wins = false;
        boolean p2CounterAttackStarted = false;

        int p1HealsRemaining = 10;
        int p2AttackPortHealsRemaining = 10;
        int p2TargetedPortHealsRemaining = 10;
        
        // attack damage per round = total damage - opponent FW absorption
        // firewall damage per round = if (opponent is counter attacking) {  your firewall attack - their FW absorb}
        float p1AttackDamagePerRound = (p1.baseAttackDamage + p1.cardsAttackDamage) * p2.targetedPortBaseFWAbsorb;
        float p1FWDamagePerRound = p1.attackPortBaseFWDamage * p2.targetedPortBaseFWAbsorb;
        
        float p2AttackDamagePerRound = (p2.baseAttackDamage + p2.cardsAttackDamage) * p1.attackPortBaseFWAbsorb;
        float p2AttackPortFWDamagePerRound = p2.attackPortBaseFWDamage * p1.attackPortBaseFWAbsorb;
        float p2TargetedPortFWDamagePerRound = p2.targetedPortBaseFWDamage * p1.attackPortBaseFWAbsorb;

System.out.println("p1 attack damage per round: " + p1AttackDamagePerRound);
System.out.println("p1 FW damage per round: " + p1FWDamagePerRound);
System.out.println("p2 attack damage per round: " + p2AttackDamagePerRound);
System.out.println("p2 attack port FW damage per round: " + p2AttackPortFWDamagePerRound);
System.out.println("p2 targeted port FW damage per round: " + p2TargetedPortFWDamagePerRound);
        
        float totalDamage = 0.0f;
        float experienceGained = 0.0f;
        
        /* ASSUMPTIONS:
                       *
                       * 1) People heal at 90%.
                       * 2) People have no heal cost cards.
                       * 3) 
                       * 
                       */
        
        // FIGHT BITCHES, FIGHT!
        while (p1AttackPortHealth > 0 && p2AttackPortHealth > 0 && p2TargetedPortHealth > 0) {
            
            System.out.println("Round: " + (rounds+1));
            
            // should a complete heal occur?
            // assume the player heals within 10 points of overheating (90%) p1CPU = 78% --> they have 100 percent total, and they heal at 90%, they head when their attack port health is at 
            // 100 - (90 - p1cpu) == (10 + p1cpu) when to heal
            if (p1AttackPortHealth < (10 + p1.cpuPct) ) {
                if (p1HealsRemaining > 0) {
                    p1AttackPortHealth = 100;
                    p1HealsRemaining--;
                    System.out.println("   P1 Attack Port healed.  Heals remaining: " + p1HealsRemaining);
                }
            }
            if (p2AttackPortHealth < p2.attackPortHealThreshold) {
                if (p2AttackPortHealsRemaining > 0) {
                    p2AttackPortHealth = 100;
                    p2AttackPortHealsRemaining--;
                    System.out.println("   P2 Attack Port healed.  Heals remaining: " + p2AttackPortHealsRemaining);
                }
            }
            if (p2TargetedPortHealth < p2.targetedPortHealThreshold) {
                if (p2TargetedPortHealsRemaining > 0) {
                    p2TargetedPortHealth = 100;
                    p2TargetedPortHealsRemaining--;
                    System.out.println("   P2 Attack Port healed.  Heals remaining: " + p2TargetedPortHealsRemaining);
                }
            }
            
            // should a +1 heal occur ?
            if (p1AttackPortHealth < 100.0f) {
                if (rounds % p1.roundsPerHeal == 0) {
                    p1AttackPortHealth += 1;
                }
            }
            if (p2AttackPortHealth < 100.0f) {
                if (rounds % p2.roundsPerHeal == 0) {
                    p2AttackPortHealth += 1;
                }
            }
            if (p2TargetedPortHealth < 100.0f) {
                if (rounds % p2.roundsPerHeal == 0) {
                    p2TargetedPortHealth += 1;
                }
            }
            
            // calculate the health of each player
            p2TargetedPortHealth = p2TargetedPortHealth - p1AttackDamagePerRound;
            p1AttackPortHealth = p1AttackPortHealth - p2TargetedPortFWDamagePerRound;
            
            // apply the counter attack damage
            if (p2.counterAttack == true) {
                if (p2CounterAttackStarted == false) {
                    // if the p2 health is less than the threshold to start the counter attack, start the counter attack immediately
                    if (p2TargetedPortHealth < p2.counterAttackThreshold) {
                        System.out.println("   P2 Counter attack starting! (threshold = " + p2.counterAttackThreshold + ")");
                        p2CounterAttackStarted = true;
                        p1AttackPortHealth = p1AttackPortHealth - p2AttackDamagePerRound;
                        p2AttackPortHealth = p2AttackPortHealth - p1FWDamagePerRound;
                    }
                } else {
                    p1AttackPortHealth = p1AttackPortHealth - p2AttackDamagePerRound;
                    p2AttackPortHealth = p2AttackPortHealth - p1FWDamagePerRound;
                }
            }
            
            totalDamage += p1AttackDamagePerRound;
            experienceGained += (p1.baseAttackDamage + p1.cardsAttackDamage);
            
            rounds++;

            System.out.println("     p1AttackPortHealth: " + p1AttackPortHealth);
            System.out.println("     p2AttackPortHealth: " + p2AttackPortHealth);
            System.out.println("     p2TargetedPortHealth: " + p2TargetedPortHealth);
            System.out.println("---------------------------");
        }
        
        System.out.println("Summary");
        
        // who won?
        if (p2AttackPortHealth <= 0 || p2TargetedPortHealth <=0) {
            p1wins = true;
            System.out.println("    Winner: p1");
        } else {
            System.out.println("    Winner: p2");
            p2wins = true;
        }
        System.out.println("    rounds: " + rounds);
        System.out.println("        TOTAL DMG: " + totalDamage);
        System.out.println("        TOTAL XP: " + experienceGained);
        System.out.println("        P1 Heals Remaining: " + p1HealsRemaining);
        
        // cost for a battle:  (heals used   * cost of a heal) + the final heal
        float fightHealCost = ((10 - p1HealsRemaining) * (100 - (90 - p1.cpuPct)) * HEAL_COST_PER_POINT * p1.healCostModifier);
        float finalHealCost = (HEAL_COST_PER_POINT * p1.healCostModifier * (100 - p1AttackPortHealth));
        System.out.println("        Cost: $" + (fightHealCost + finalHealCost));
        
        // time of a battle
        int seconds = rounds * 2;
        System.out.println("        Time: " + seconds + " seconds.");
        
        // calculate the number of attacks required to increase to the next attack level
        int currentLevel = (int)Math.ceil((p1.baseAttackDamage - 2.0f) / 5.0f);
        System.out.println("        Current attack level: " + currentLevel);
        int currentLevelMinXP = 0;
        if (currentLevel > 1) {
            currentLevelMinXP = xpTable[currentLevel-2];
        }
        System.out.println("        Current minimum attack XP: " + currentLevelMinXP);
        int xpUntilNextLevel = xpTable[currentLevel - 1] - currentLevelMinXP;
        int battlesTillLevelUp = xpUntilNextLevel / ;
        System.out.println("        Battles like this until next attack level increase: " + battlesTillLevelUp);
        System.out.println("        Time required (assuming max simultaneous farming): BOOBIES!" );
        
        // overheating an NPC really fucks shit up calculation wise, cause it's free game until they're un-overheated
        
    }
    
    /** 
            * I stole this from Hacker.java.  Calculate the level requirements.
            */
    private static void createXPTable(){
	    int xp=83; //experience to reach level 1.
	    int xpDiff=83;  
	    //xpTable[0]=0;
	    for(int i=0;i<100;i++){
		    xpTable[i]=xp;
		    xpDiff+=xpDiff/9.525;
		    xp+=xpDiff;
	    }
    }
    
    public static void main(String[] args) {
        
        createXPTable();

        // weak player: level 10 attack
        /* player setup
                            Attack
                                base damage: 3
                                cards: 0
                            No firewall:
                                base FW damage = 0
                                base FW absorb = 1.0 (1 - 0%)
                             CPU
                                50
                             Heal Rate
                                 No change.
                              Heal Cost
                                 No change.
                             Setup:
                                 Basic Bank (10) , Basic Attack (20)  + Basic Firewall (4) , Scan (5) = 39
                      */

    /*
       public Computer(
                        int level, 
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
        */

        SimComputer p1 = new SimComputer(8, 0.78f, 4, 0, "PortProtector", "PortProtector", 1.0f, 1.0f, 0, 0, false, 0.0f);
        SimComputer p2 = new SimComputer(8, 0.78f, 4, 0, "PortProtector", "PortProtector", 1.0f, 1.0f, 0, 0, true, 10.0f);

        PvP myPvP = new PvP();
        myPvP.simulate(p1, p2);
        
        //Computer player = new Computer(8, 0.78f, 4, 0, "PortProtector", "PortProtector", 1.0f, 1.0f, 0, 0, false, 0.0f);
        //Computer npc = new Computer(8, 0.78f, 4, 0, "PortProtector", "PortProtector", 1.0f, 1.0f, 0, 0, true, 10.0f);
        //myPvP.simulate(player, npc);
        //calculateWinner(4, 0, 0, 1.0f, 4, 0, 0, 1.0f, 0, 1.0f, 4, 4, 0.0f, 0.0f, true, 10.0f, 78, 50);
        
        /*        
            public static void calculateWinner(
            int p1BaseAttackDamage,
            int p1CardsAttackDamage,
            int p1BaseFWDamage,
            float p1BaseFWAbsorb,
            int p2BaseAttackDamage,
            int p2CardsAttackDamage,
            int p2AttackPortBaseFWDamage,
            float p2AttackPortBaseFWAbsorb,
            int p2TargetedPortBaseFWDamage,
            float p2TargetedPortBaseFWAbsorb,
            int p1RoundsPerHeal,
            int p1HealCostDiscount,
            int p2RoundsPerHeal,
            float p2AttackPortHealThreshold,
            float p2TargetedPortHealThreshold,
            boolean p2CounterAttack,
            float p2CounterAttackStartsThreshold,
            int p1CPUpct,
            int p2CPUpct
*/

        // weak player: level 10 attack
        /* player setup
                            Attack
                                base damage: 3
                                cards: 0
                            No firewall:
                                base FW damage = 0
                                base FW absorb = 1.0 (1 - 0%)
                             CPU
                                50
                             Heal Rate
                                 No change.
                              Heal Cost
                                 No change.
                             Setup:
                                 Basic Bank (10) , Basic Attack (20)  + Basic Firewall (4) , Scan (5) = 39
                      */
        // roundsPerHeal = related to the amount ot titties
        
        //Computer player = new Computer(4, 0, 0, 1.0f, 0, 0.0f,);
        //Computer npc = new Computer(4, 0, 0, 1.0f, 0, 1.0f,);
        //Computer player = new Computer(1);
        
        //calculateWinner(4, 0, 0, 1.0f, 4, 0, 0, 1.0f, 0, 1.0f, 4, 4, 0.0f, 0.0f, true, 10.0f, 78, 50);

        // average player: level 10 attack
        /* player setup
                            Attack
                                base damage: 3
                                cards: 1
                            Basic firewall:
                                base FW damage = 0
                                base FW absorb = 0.85 (1 - 15%)
                             CPU
                                50
                             Heal Rate 
                                No change.
                      */
        
        // strong player: level 10 attack
        /* player setup
                            Attack
                                base damage: 3
                                cards: 1
                            Basic firewall:
                                base FW damage = 0
                                base FW absorb = 0.85 (1 - 15%)
                             CPU
                                50
                             Heal Rate 
                                every 3 rounds (+25%)
                      */

        // weak player: level 20 attack
        // average player: level 20 attack
        // strong player: level 20 attack

        // weak player: level 30 attack
        // average player: level 30 attack
        // strong player: level 30 attack

        // weak player: level 40 attack
        // average player: level 40 attack
        // strong player: level 40 attack

        // weak player: level 50 attack
        // average player: level 50 attack
        // strong player: level 50 attack
        
        // weak player: level 60 attack
        // average player: level 60 attack
        // strong player: level 60 attack
        
        // weak player: level 70 attack
        // average player: level 70 attack
        // strong player: level 70 attack
        
        // weak player: level 80 attack
        // average player: level 80 attack
        // strong player: level 80 attack
        
        // weak player: level 90 attack
        // average player: level 90 attack
        // strong player: level 90 attack

        // weak player: level 100 attack
        // average player: level 100 attack
        // strong player: level 100 attack
        
    }

    
    
    
    
    
}
