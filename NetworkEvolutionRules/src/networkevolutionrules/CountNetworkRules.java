/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkevolutionrules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static networkevolutionrules.SNERMiningMinStab.name;

/**
 *
 * @author AnimeshChaturvedi
 */

//incompelet already build-in the existing code
public class CountNetworkRules {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String path = "F:\\Thesis Experiments BackUp\\21-06-20\\EvolvingSoftwareSystems\\SCGERM\\Maven-Core\\SCGERM\\CallGraphRuleFile";
        File sequenceFileFolder = new File(path);
        File createCountFile = new File(sequenceFileFolder.getParentFile().getAbsolutePath() + "\\CGRCount.txt"); 
        
        FileWriter writerFlush = new FileWriter(createCountFile, false);
        writerFlush.write("");
        writerFlush.close();
        
        FileWriter writer = new FileWriter(createCountFile, true);
        File[] children = sequenceFileFolder.listFiles();
        for (int i = 0; i < children.length; i++) {
            int noOfLines = 0;
            try ( BufferedReader reader = new BufferedReader(new FileReader(children[i]))) {
                while (reader.readLine() != null) {
                    noOfLines++;
                }
            }
            writer.write(name + ", " + noOfLines);
        }
    }
}
