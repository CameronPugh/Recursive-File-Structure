import java.util.ArrayList;
import java.util.Objects;

public class Folder {
    private String directoryName;
    private ArrayList<Folder> folders;
    private ArrayList<String> files;
    private Folder parentFolder;
    private int indentDepth;

    public Folder(String directoryName,int indentDepth,  Folder parentFolder){
        this.directoryName = directoryName;
        this.indentDepth = indentDepth;
        this.parentFolder = parentFolder;
    }
    public Folder(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(directoryName, folder.directoryName) && Objects.equals(folders, folder.folders) && Objects.equals(files, folder.files);
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    @Override
    public int hashCode() {
        return Objects.hash(directoryName, folders, files);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "directoryName='" + directoryName + '\'' +
                ", folders=" + folders +
                ", files=" + files +
                ", parentFolder=" + parentFolder +
                ", indentDepth=" + indentDepth +
                '}';
    }

    public void addFolder(Folder temp){
        if (this.folders == null) {
            this.folders = new ArrayList<Folder>();
        }
        if (this.folders != null) {
            this.folders.add(temp);
        }
   }
   public void addFiles(String filename){
       if (this.files == null) {
           this.files = new ArrayList<String>();
       }
       if (this.files != null) {
           this.files.add(filename);
       }
   }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public int getIndentDepth() {
        return indentDepth;
    }

    public void setIndentDepth(int indentDepth) {
        this.indentDepth = indentDepth;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

}
