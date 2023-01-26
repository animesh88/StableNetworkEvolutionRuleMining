/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkevolutionrules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Animesh Chaturvedi
 */
public class MergeMinStableRules {

    static String path = "D:\\Evolving Data\\16-11-16\\EvolvingSoftwareSystems\\Guava";

    public static void main(String[] args) throws FileNotFoundException, IOException {

        System.out.println(" networkEvolutionRules ");
        File networkRulesFolder = new File(path + "\\SCM\\NetworkRuleFile");

        File collectionOFNetworkRules = new File(path + "\\SCM\\NetworkEvolutionRules.txt");
        BufferedWriter bufwriter = new BufferedWriter(new FileWriter(collectionOFNetworkRules, true));

//        bufwriter.write("\t" + minsupInt + " & " + minconfDouble + "\n");
        bufwriter.close();
        visitAllFiles(networkRulesFolder, collectionOFNetworkRules);

    }

    public static void visitAllFiles(File networkFileFolder, File collectionOFNetworkRules) throws IOException {
        File[] children = networkFileFolder.listFiles();
        for (int i = 0; i < children.length; i++) {
            process(children[i], collectionOFNetworkRules);
        }
    }

    private static void process(File sequenceRules, File NetworkEvolutionRules) throws IOException {
//        System.out.println(sequenceRules.getName());

        ArrayList<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(sequenceRules));
            BufferedReader r = new BufferedReader(new FileReader(NetworkEvolutionRules));
            String s1 = null;
            String s2 = null;

            while ((s1 = br.readLine()) != null) {
                list.add(s1);
            }
            while ((s2 = r.readLine()) != null) {
                list.add(s2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(NetworkEvolutionRules, true));

        for (int j = 0; j < list.size(); j++) {
            String line = list.get(j);
            List<String> tmpAntecedent = new ArrayList<String>();
            List<String> tmpConsequent = new ArrayList<String>();

            boolean flag = true;
            int end = line.indexOf("#SUP:");
            if (end != -1) {
                line = line.substring(0, end);
//                System.err.println("line " + line);
                StringTokenizer st = new StringTokenizer(line, " ,.;:\"");
                while (st.hasMoreTokens()) {
                    String tmp = st.nextToken();
                    if (tmp.equals("==>")) {
                        flag = false;
                    } else if (flag) {
                        tmpAntecedent.add(tmp);
                    } else {
                        tmpConsequent.add(tmp);
                    }
                }

                for (int i = 0; i < tmpAntecedent.size() - 1; i++) {
                    String name = tmpAntecedent.get(i);
                    writer.write(name + ", ");
                }
                writer.write(tmpAntecedent.get(tmpAntecedent.size() - 1) + " ==> ");

                for (int i = 0; i < tmpConsequent.size() - 1; i++) {
                    String name = tmpConsequent.get(i);
                    writer.write(name + ", ");
                }
                writer.write(tmpConsequent.get(tmpConsequent.size() - 1) + "\n");
            }
        }

//        list = removeDuplicates(list);
////        System.out.println(list);
//        String listWord;
//        for (int i = 0; i < list.size(); i++) {
//            listWord = list.get(i);
//            writer.write(listWord);
//            writer.write("\n");
//        }
        
        writer.close();
    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }
}
