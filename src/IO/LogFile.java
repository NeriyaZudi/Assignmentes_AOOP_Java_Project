package IO;

import Country.Settlement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class LogFile {
    private File file;
    private FileWriter outPutFile;
    private BufferedWriter bw;
    private StringBuilder pathsFile=new StringBuilder();

    public LogFile(File f) throws IOException {

        file=f;
        outPutFile = new FileWriter(file);
        bw = new BufferedWriter(outPutFile);
        bw.write("Settlement Name | ");
        bw.write("Number of sicks | ");
        bw.write("Number of Deceased | ");
        bw.write("       Current time |           ");
        bw.newLine();
        addPath(file.getPath());
    }
    public void writeToFile(Settlement s) throws IOException {
        synchronized (bw) {
            LocalDateTime now=LocalDateTime.now();
            String current=now.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));

            bw.write(current);
            bw.write (" \t"+s.getDeadCounter() + "( "+s.getPercent()+"%) ");
            bw.write("\t"+ s.calcSick() + "\t\t");
            bw.write("\t"+ s.getName() + " \t");
            bw.newLine();

        }

        bw.flush();
    }
    public void addPath(String p){this.pathsFile.append(p);}
    public MementoRestore save(){
        return new MementoRestore(pathsFile.toString());
    }
    public void restore(MementoRestore state) {
        pathsFile = new StringBuilder(state.getRestorePath());
        System.out.println(pathsFile);
        try {
            changeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeFile() throws IOException {
        file=new File(pathsFile.toString());
        outPutFile = new FileWriter(file,true);
        bw = new BufferedWriter(outPutFile);
    }


    public void closeAll(){
        try {
            outPutFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MementoRestore{
        private final String restorePath;
        public MementoRestore(String path){
            this.restorePath=path;
        }

        public String getRestorePath() {
            return restorePath;
        }
    }
}
