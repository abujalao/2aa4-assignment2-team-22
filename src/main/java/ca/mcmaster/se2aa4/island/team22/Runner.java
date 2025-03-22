package ca.mcmaster.se2aa4.island.team22;

import java.io.File;

import eu.ace_design.island.bot.IExplorerRaid;
import static eu.ace_design.island.runner.Runner.run;

public class Runner {

    public static void main(String[] args) {
        String filename = args[0];
        IExplorerRaid explorerInterface = new Explorer();

        try {
            run(explorerInterface.getClass())
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(1,1, "EAST") //Edited (was .startingAt(1, 1, "EAST"))
                    .backBefore(7000)
                    .withCrew(5)
                    .collecting(1000, "WOOD")
                    .storingInto("./outputs")
                    .withName("Island")
                    .fire();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
