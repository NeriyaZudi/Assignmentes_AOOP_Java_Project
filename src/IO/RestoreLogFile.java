package IO;

import Country.Map;

import java.util.Stack;

public class RestoreLogFile {
    private final Stack<LogFile.MementoRestore> undo = new Stack<>();

    public void save() {
        LogFile.MementoRestore m = Map.getLogFileWriter().save();
        //System.out.println("push to stack "+m.getRestorePath());
        undo.push(m);
    }

    public boolean undo() {
        undo.pop();
        if (undo.isEmpty())
            return false;
        Map.getLogFileWriter().restore(undo.pop());
        return true;
    }
}
