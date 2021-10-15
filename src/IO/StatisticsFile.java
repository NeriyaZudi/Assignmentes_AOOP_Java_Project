package IO;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class StatisticsFile {
    private static StatisticsFile statisticsFile=null;
    public final String[] columnNames = {"Settlement Type", "Settlement Name", "Ramzor Color",
            "Population Size", "Number of sick", "Sick Percentage", "Vaccinated", "Number of Deceased"};
    private FileWriter outPutFile;


    private StatisticsFile(File file, JTable table) throws IOException {

        File csvOutputFile = new File(file.getPath());
        outPutFile = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(outPutFile);

        for (int i = 0; i < columnNames.length; i++) {
            bw.write(columnNames[i]);
            if (i != columnNames.length - 1)
                bw.append(" , ");
        }
        bw.write("\n");

        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                bw.write(table.getValueAt(i, j).toString());
                if (j != table.getColumnCount() - 1)
                    bw.append(" , ");
            }
            bw.write("\n");
        }
        bw.flush();
        bw.close();

    }

    public static StatisticsFile getInstance(File file, JTable table) throws IOException {
        if(statisticsFile==null)
            statisticsFile=new StatisticsFile(file, table);
        return statisticsFile;
    }

}