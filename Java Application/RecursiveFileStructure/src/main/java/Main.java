import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    private Folder mainRoot;
    private int rootIndent = 0;
    private Connection conn;

    public void run() throws URISyntaxException
    {
        File fileToRead = getFile("FileStructure.txt");
        //printFile(fileToRead);//test statement

        List<String> lines;

        try {
            conn = new ConnectionManager().getConection();

            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM path";
            stmt.executeUpdate(sql);

            lines = Files.readAllLines(fileToRead.toPath(), StandardCharsets.UTF_8);
            parseFile(lines,null);

            traverseEachBranch(mainRoot,0);
            traverseEachBranch(mainRoot,1);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /***
     *
     * @param lines
     * @param previousPointer
     *
     * Creates a linked list type structure using the folder object which contains the related files and
     * pointers to other folders
     */
    private void parseFile(List<String> lines, Folder previousPointer)
    {
        if(!lines.isEmpty())
        {
            //System.out.println(lines.toString());
            String line = lines.get(0);
            int depth = getIndent(line);
            //for root use case

            if (previousPointer == null && depth == rootIndent) {
                Folder root = new Folder(removeIndent(line), depth, null);
                mainRoot = root;
                lines.remove(line);
                parseFile(lines, root);
            } else {
                assert previousPointer != null;
                if (depth > previousPointer.getIndentDepth()) {
                    //folder
                    if (!line.contains(".")) {
                        Folder newFolder = new Folder(removeIndent(line), depth, previousPointer);
                        previousPointer.addFolder(newFolder);
                        lines.remove(line);
                        parseFile(lines, newFolder);
                    }
                    //file
                    if (line.contains(".")) {
                        previousPointer.addFiles(removeIndent(line));
                        lines.remove(line);
                        parseFile(lines, previousPointer);
                    }
                }
                if(depth == previousPointer.getIndentDepth()){
                    if (!line.contains(".")) {
                        Folder newFolder = new Folder(removeIndent(line), depth, previousPointer.getParentFolder());
                        previousPointer.getParentFolder().addFolder(newFolder);
                        lines.remove(line);
                        parseFile(lines, newFolder);
                    }
                    //file
                    if (line.contains(".")) {
                        previousPointer.addFiles(removeIndent(line));
                        lines.remove(line);
                        parseFile(lines, previousPointer);
                    }
                }
                if (depth < previousPointer.getIndentDepth()) {
                    int depthDifference = previousPointer.getIndentDepth() - depth;

                    for (int i = -1; i < depthDifference; i++) {
                        previousPointer = previousPointer.getParentFolder();
                    }

                    if (!line.contains("."))
                    {
                        Folder newFolder = new Folder(removeIndent(line), depth, previousPointer);
                        previousPointer.addFolder(newFolder);
                        lines.remove(line);
                        parseFile(lines, newFolder);
                    }
                    //file
                    if (line.contains(".")) {
                        previousPointer.addFiles(removeIndent(line));
                        lines.remove(line);
                        parseFile(lines, previousPointer);
                    }
                }
            }
        }
    }

    /***
     *
     * @param paths - Array list of found paths
     *
     * Writes the path to the sqlite3 db
     */
    private  void sendListToDB(ArrayList<String> paths) {
        for (String path : paths) {
            try {
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO path(name) VALUES('"+path+"');";
                stmt.executeUpdate(sql);
            }catch (SQLException e){}
        }
    }

    /***
     *
     * @param vec
     * @param files
     *
     * Once reached the leaf node of the tree, prints the paths to
     * each folder and or file
     */
    private void printPath(ArrayList<String> vec, ArrayList<String> files)
    {
        // Print elements in the vector
        ArrayList<String> completePathList = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        for (String ele : vec) {
            path.append(ele).append("\\");
            completePathList.add(String.valueOf(path));
        }

        for (String file : files)
            completePathList.add(path + file);

        for(String x: completePathList){
            System.out.println(x);
        }

        sendListToDB(completePathList);
    }


    /***
     *
     * @param root
     * @param vec
     * @param leafType
     *
     * Starts at given node, and traversed now all possible branches to the leaf node and
     * gets its files
     */
    private void traverseEachBranch(Folder root, ArrayList<String> vec, int leafType)
    {
        if (root == null)
            return;

        vec.add(root.getDirectoryName());

        // If current node is a leaf node
        if(leafType == 0) {
            if (root.getFiles() != null) {
                printPath(vec, root.getFiles());
                vec.remove(vec.size() - 1);
                return;
            }
        }
        if(leafType == 1) {
            if (root.getFolders() == null) {
                printPath(vec, root.getFiles());
                vec.remove(vec.size() - 1);
                return;
            }
        }

        for (int i = 0; i < root.getFolders().size(); i++)
            traverseEachBranch(root.getFolders().get(i), vec, leafType);

        vec.remove(vec.size() - 1);
    }

    /***
     *
     * @param root
     * @param leafType
     *
     * Used for root node
     */
    private void traverseEachBranch(Folder root, int leafType)
    {
        if (root.getFolders() == null)
            return;

        ArrayList<String> vec = new ArrayList<>();
        traverseEachBranch(root, vec, leafType);
    }



    /***
     *
     * @param line
     * @returns the number of indents on the given line
     */
    public int getIndent(String line)
    {
        final char countedChar = ' ';
        int characterCount = 0;

        for(int i = 0; i < line.length(); i++){
            if(countedChar == line.charAt(i)){
                characterCount++;
            }
            else{
                break;
            }
        }
        return characterCount/4;
    }

    /***
     *
     * @param line
     * @returns the line with all spaces removed for the path
     */
    public String removeIndent(String line) {
        return line.replaceAll(" ","");
    }

    /***
     *
     * @param name
     * @return File
     *
     * Gets file from resources folders using given name
     */
    public File getFile(String name) throws URISyntaxException
    {
        URL resource = getClass().getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            return new File(resource.toURI());
        }
    }

    /***
     *
     * @param file
     *
     * Test method for printing entire contents of given file
     */
    public static void printFile(File file)
    {
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param args
     * @throws URISyntaxException
     *
     * Main method
     */
    public static void main(String[] args) throws URISyntaxException
    {
        Main main = new Main();
        main.run();
    }
}
