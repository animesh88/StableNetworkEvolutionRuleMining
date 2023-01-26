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
public class MergeUniqueNetworkEvolutionRules {

    static String path = "G:\\BackUp\\17-07-12\\RetailMarketMinVersion10\\";
    static File networkEvolutionRuleFolder = new File(path + "\\SNER\\NetworkEvolutionRules\\");

    public static void main(String[] args) throws FileNotFoundException, IOException {

        System.out.println(" NetworkEvolutionRules ");
        File sequenceRulesFolder = new File(path + "\\SNER\\NetworkRuleFile");
        if (!networkEvolutionRuleFolder.exists()) {
            networkEvolutionRuleFolder.mkdir();
        } else {
            delete(networkEvolutionRuleFolder);
            System.out.println("Directory deleted");
            networkEvolutionRuleFolder.mkdir();
        }
        visitAllFiles(sequenceRulesFolder);
    }

    public static void visitAllFiles(File sequenceFileFolder) throws IOException {
        File[] children = sequenceFileFolder.listFiles();
        for (int i = 0; i < children.length; i++) {
            File networkEvolutionRules = new File(networkEvolutionRuleFolder.getAbsolutePath() + "\\NER" + children[i].getName() + ".txt");
            System.out.println("\n" + networkEvolutionRules.getName());
            if (!networkEvolutionRules.exists()) {
                networkEvolutionRules.createNewFile();
            }
            BufferedWriter bufwriter = new BufferedWriter(new FileWriter(networkEvolutionRules));
            bufwriter.write("");
            bufwriter.close();

            visitAllSequentialFiles(children[i], networkEvolutionRules);

            ArrayList<String> list = new ArrayList<String>();
            try {

                BufferedReader r = new BufferedReader(new FileReader(networkEvolutionRules));
                String s2 = null;

                while ((s2 = r.readLine()) != null) {
                    list.add(s2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            System.out.println(list);
            list = removeDuplicates(list);
//            System.out.println(list);

            BufferedWriter bufwriter1 = new BufferedWriter(new FileWriter(networkEvolutionRules));
            bufwriter1.write("");
            bufwriter1.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(networkEvolutionRules, true));

            String listWord;
            System.out.println(" Size of list " + list.size());
            for (int j = 0; j < list.size(); j++) {
                listWord = list.get(j);
                writer.write(listWord);
                writer.write("\n");
            }
            writer.close();
            System.out.println("completed \n");
        }
    }

    private static void visitAllSequentialFiles(File sequenceFileFolder, File totalRules) throws IOException {
        File[] children = sequenceFileFolder.listFiles();
        for (int i = 0; i < children.length; i++) {
            process(children[i], totalRules);
        }
    }

    private static void process(File sequenceRules, File totalRules) throws IOException {
        System.out.println(sequenceRules.getName());

        ArrayList<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(sequenceRules));
            BufferedReader r = new BufferedReader(new FileReader(totalRules));
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

//        System.out.println(list);
        list = removeDuplicates(list);
//        System.out.println(list);

        BufferedWriter writer = new BufferedWriter(new FileWriter(totalRules, true));

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
//        System.out.println(
//                "completed");
        writer.close();
    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        set.addAll(list);
        result.addAll(set);

        // Loop over argument list.
//        for (String item : list) {
//
//            // If String is not in set, add it to the list and the set.
//            if (!set.contains(item)) {
//                result.add(item);
//                set.add(item);
//            }
//        }
        return result;
    }

    public static void delete(File file)
            throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
}
