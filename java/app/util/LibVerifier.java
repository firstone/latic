import java.io.File;
import java.util.Enumeration;
import java.util.jar.*;
import java.util.zip.ZipException;

//apache ant
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class LibVerifier extends Task {

    public static void main(String args[]) {
        LibVerifier ver = new LibVerifier();
	
        try {
            File file = new File(args[0]);
            if (file.isDirectory()) {
                for (File f : file.listFiles())
                    ver.process_(f, true);
            } else
                ver.process_(file, false);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void process_(File file, boolean ignore) {
        try {
            processJar_(file);
        } catch (ZipException e) {
            if (!ignore)
                System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }   
    }

    public void setJarFile(String file) {
        jarFile_ = file;
    }

    public void execute() throws BuildException {
        try {
            processJar_(new File(jarFile_));
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }
    }

    private void processJar_(File file) throws Exception {
        log("Verifying jar: " + file);

        JarFile jar = new JarFile(file);

        String name = file.getName().substring(0, file.getName().length() - 4);
        String libName, projName = null;

        String fields[] = name.split("_");
        if (fields.length == 1)
            libName = fields[0];
        else {
            projName = fields[0];
            libName = fields[1];
        }

        for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements(); ) {
            JarEntry entry = e.nextElement();
            if (entry.getName().endsWith(".class")) {
                String str[] = entry.getName().split("/");
                if ((!str[str.length - 2].equals(libName)
                            && !str[str.length - 3].equals(libName))
                        || (projName != null 
                            && !str[str.length - 3].equals(projName)
                            && !str[str.length - 4].equals(projName)))
                    throw new Exception("Library: " + jar.getName()
                            + " contains invalid class: "
                            + entry.getName());
            }
        }
    }

    private String jarFile_;
}





