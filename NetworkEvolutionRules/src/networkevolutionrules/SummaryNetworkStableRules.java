package networkevolutionrules;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class SummaryNetworkStableRules {

    //Experiment on each evolving system will cover three values of minVersion and 3 pairs of minSup and minConf
    static String path = "G:\\BackUp\\17-03-23\\MarketBasket\\SNERM";
    static File createCountOfEvolvingRules = new File(path + "\\SNER\\Summary\\NSRCount.txt");
    static HashMap hm = new HashMap();

    public static void main(String a[]) throws IOException {
        SummaryNetworkStableRules summaryObject = new SummaryNetworkStableRules();

        File file = new File(path + "\\SNER\\StableNetworkEvolutionRules");
        File summaryFolder = new File(path + "\\SNER\\Summary");
        summaryFolder.mkdirs();
        
        FileWriter flushWriter = new FileWriter(createCountOfEvolvingRules, false);
        flushWriter.write("");
        flushWriter.close();
        
        summaryObject.visitAllFiles(file);
    }

    public static void visitAllFiles(File sequenceFileFolder) throws IOException {
        File[] children = sequenceFileFolder.listFiles();
        for (int i = 0; i < children.length; i++) {
            getSummaryFile(children[i]);
        }
    }

    public static void getSummaryFile(File fileName) throws IOException {
        int count = 0;
        System.out.println("File under processing " + fileName.getName());
        File createSumary = new File(path + "\\SNER\\Summary\\" + fileName.getName());

        FileWriter writerFlush = new FileWriter(createSumary, false);
        writerFlush.write("");
        writerFlush.close();

        File mappingFile = new File(path + "\\SeqDBMapping.txt");
        readMappingHM(mappingFile);

        FileWriter writer = new FileWriter(createSumary, true);

        FileInputStream fis = null;
        DataInputStream dis = null;
        BufferedReader br = null;
        List<String> callerProcedures = new ArrayList<String>();
        List<String> calledProcedures = new ArrayList<String>();
        int antecedentCount = 0, consequentCount = 0;
        try {
            fis = new FileInputStream(fileName);
            dis = new DataInputStream(fis);
            br = new BufferedReader(new InputStreamReader(dis));
            String line = null;
            while ((line = br.readLine()) != null) {
                count++;
                List<String> tmpCaller = new ArrayList<String>();
                List<String> tmpCalled = new ArrayList<String>();

                boolean flag = true;
//                int end = line.indexOf("#SUP:");
//                line = line.substring(0, end);
                StringTokenizer st = new StringTokenizer(line, " ,.;:\"");
                while (st.hasMoreTokens()) {
                    String tmp = st.nextToken();
                    if (tmp.equals("==>")) {
                        flag = false;
                    } else if (flag) {
                        Object name = hm.get(tmp);
                        System.out.println(tmp.toString());
                        tmpCaller.add(name.toString());

                        if (!callerProcedures.contains(tmp)) {
                            callerProcedures.add(tmp);
                            antecedentCount++;
                        }
                    } else {
                        Object name = hm.get(tmp);
                        tmpCalled.add(name.toString());

                        if (!calledProcedures.contains(tmp)) {
                            calledProcedures.add(tmp);
                            consequentCount++;
                        }
                    }
                }
//                System.out.println(tmpCaller.size());
                for (int i = 0; i < tmpCaller.size() - 1; i++) {
                    String name = tmpCaller.get(i);
                    writer.write(name + ", ");
                }
                writer.write(tmpCaller.get(tmpCaller.size() - 1) + " ==> ");

                for (int i = 0; i < tmpCalled.size() - 1; i++) {
                    String name = tmpCalled.get(i);
                    writer.write(name + ", ");
                }
                writer.write(tmpCalled.get(tmpCalled.size() - 1) + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
            }
        }

//        String createSumary = path + "\\Summary.txt";
//        FileWriter writer = new FileWriter(createSumary, true);
        writer.write("\nAntecedent item count:\t" + antecedentCount + "\n");
        writer.write("Consequent item count:\t" + consequentCount + "\n");
        int total = antecedentCount + consequentCount;
        writer.write("Total items:\t" + total + "\n");
        writer.close();

        FileWriter evolvingRuleCountWriter = new FileWriter(createCountOfEvolvingRules, true);
        String fileNameString = fileName.getName().replace(".txt", "");
        evolvingRuleCountWriter.write(fileNameString + "\t" + count + "\n");
        evolvingRuleCountWriter.close();
    }

    private static void readMappingHM(File mappingFile) throws FileNotFoundException, IOException {
        String mappingBuffer = "";

        FileReader mappingFileReader1 = new FileReader(mappingFile);

        BufferedReader mappingBufferedReader1 = new BufferedReader(mappingFileReader1);

        while ((mappingBuffer = mappingBufferedReader1.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(mappingBuffer, " ");
            String entityName = st.nextToken();
            String entityID = st.nextToken();
//            if(entityName==null){
//                hm.put(entityID, "null");
//            }else{
//            System.out.println(" EntityName " + entityName +  " EntityID " + entityID);
            hm.put(entityID, entityName);
//            }
        }
    }
}