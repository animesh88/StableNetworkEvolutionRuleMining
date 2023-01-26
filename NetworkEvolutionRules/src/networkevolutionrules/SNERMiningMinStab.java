/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//sequentialRulesFile == networkRulesFile
//sequenceRulesFolder == networkRulesFolder
//sequentialChangeRules == networkEvolutionRules
//EvolvingConnectionRules == CollectionOfNetworkRules
//EvolvingConnectionRules == StableNetworkEvolutionRules
//Total Rules == NetworkEvolutionRules
//SCM == SNER
package networkevolutionrules;

import RuleGrowthAlgorithm.AlgoRULEGROWTH;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Animesh Chaturvedi
 */
public class SNERMiningMinStab {

    //Experiment on each evolving system will cover three values of minStab and 3 pairs of minSup and minConf
    static int minSupInt = 5;
    static double minConfDouble = 0.6;
    static int minStab = 4;
    static String path = "G:\\BackUp\\17-07-12\\RetailMarketMinVersion10\\";
    static String name = minSupInt + "-" + minConfDouble + "-" + minStab;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(" min sup " + minSupInt + " min Conf " + minConfDouble);
        File SysNetDbs = new File(path + "/SysNetDbs");
        File networkRulesFolder = new File(path + "/SNER/NetworkRuleFile/" + name);
        networkRulesFolder.mkdirs();
        File summaryFolder = new File(path + "/SNER/CollectionOfNetworkRules");
        summaryFolder.mkdirs();
        File ruleCountFile = new File(path + "/SNER/NetworkRuleCount.txt");
        BufferedWriter bufwriter = new BufferedWriter(new FileWriter(ruleCountFile, true));
        bufwriter.write(minSupInt + " & " + minConfDouble + " & " + minStab + "\n");
        bufwriter.close();
        visitAllFiles(SysNetDbs);

        File networkEvolutionRules = new File(path + "/SNER/CollectionOfNetworkRules/NRs" + name + ".txt");

        ArrayList<String> list = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(networkEvolutionRules));

        String s1 = null;
        while ((s1 = br.readLine()) != null) {
            list.add(s1);
        }
        list = minStable(list, minStab);

        BufferedWriter writer = null;

        File summaryFolder2 = new File(path + "/SNER/StableNetworkEvolutionRules");
        summaryFolder2.mkdirs();
        File stableNetworkEvolutionRules = new File(path + "/SNER/StableNetworkEvolutionRules/SNER" + name + ".txt");
        writer = new BufferedWriter(new FileWriter(stableNetworkEvolutionRules));
        String listWord;
        for (int i = 0; i < list.size(); i++) {
            listWord = list.get(i);
            writer.write(listWord);
            writer.write("\n");
        }
        System.out.println("completed: the number of rules: " + list.size());
        writer.close();
    }

    public static void visitAllFiles(File dnDbsFileFolder) throws IOException {
        File[] children = dnDbsFileFolder.listFiles();
        for (int i = 0; i < children.length; i++) {
            process(children[i]);
        }
    }

    private static void process(File dnDbFile) throws IOException {
        String fileName = dnDbFile.getName(); //.replace("_sequentialFile", "_sequentialRuleFile");
        File networkRulesFile = new File(path + "/SNER/NetworkRuleFile/" + name + "/" + fileName);
        AlgoRULEGROWTH algo = new AlgoRULEGROWTH();
        algo.excuteMain(dnDbFile.getAbsolutePath(), minSupInt, minConfDouble, networkRulesFile);

        File networkRulesFolder = new File(path + "/SNER/NetworkRuleFile/" + name);
        File collectionOfNetworkRules = new File(path + "/SNER/CollectionOfNetworkRules/NRs" + name + ".txt");
        System.out.println("CollectionOfNetworkRules " + collectionOfNetworkRules.getName());
        BufferedWriter bufwriter = new BufferedWriter(new FileWriter(collectionOfNetworkRules, false));
        bufwriter.write("");
        bufwriter.close();
//        MergeRules.visitAllFiles(networkRulesFolder, sequenceChangeRules); 
        MergeMinStableRules.visitAllFiles(networkRulesFolder, collectionOfNetworkRules);
        System.out.println("completed");
    }

    /*
    static ArrayList<String> minStab(ArrayList<String> list, int minStab) {
        System.out.println("Find evolving rules");
        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item1 : list) {
            int count = 1;
            for (String item2 : list) {
                if (item1.equals(item2)) {
                    count++;
                }
            }
            if (count > minStab) {
                if (!set.contains(item1)) {
                    result.add(item1);
                    set.add(item1);
                }
            }
            // If String is not in set, add it to the list and the set.
        }
        return result;
    }
     */
 /*
    static ArrayList<String> minStab(ArrayList<String> list, int minStab) {
        System.out.println("Find evolving rules");
        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();
        HashSet<String> complete = new HashSet<>();

        // Loop over argument list.
        for (String item1 : list) {
            int count = 1;
            boolean flag = true;
            for (String done : complete) {
                if (item1.equals(done)) {
                    flag = false;
                }
            }

            if (flag) {
                for (String item2 : list) {
                    if (item1.equals(item2)) {
                        count++;
                        list.remove(item2);
                    }
                }
                if (count > minStab) {
                    if (!set.contains(item1)) {
                        result.add(item1);
                        set.add(item1);
                    }
                }
            }
            complete.add(item1);
            // If String is not in set, add it to the list and the set.
        }
        return result;
    }
     */
    static ArrayList<String> minStable(ArrayList<String> list, int minVersion) {
        System.out.println("Find evolving rules");
        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item1 : list) {
            int frequency = Collections.frequency(list, item1);

            if (frequency >= minVersion) {
                if (!set.contains(item1)) {
                    result.add(item1);
                    set.add(item1);
                    System.out.println(item1 + " is added to the result");
                    System.out.println(" item1 " + item1 + " frequency " + frequency);
//                    Object[] st = list.toArray();
//                    for (Object s : st) {
//                        if (list.indexOf(s) != list.lastIndexOf(s) && s.toString().equals(item1)) {
//                            list.remove(list.lastIndexOf(s));
//                        }
//                    }
                }
            }

//            list.removeIf(element -> element == item1);
            // If String is not in set, add it to the list and the set.
        }
        return result;
    }
}
