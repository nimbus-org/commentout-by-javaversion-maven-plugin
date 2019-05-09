package jp.ossc.nimbus.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

public class CopyMojoTest extends AbstractMojoTestCase {
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    @Test
    public void testReplace1() throws Exception {
        File testPom = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/test2_pom.xml");
        CopyMojo mojo = (CopyMojo) lookupMojo("copy", testPom);
        mojo.execute();
        File file = new File(getBasedir(), "target/test/resources/jp/ossc/nimbus/plugin/3/Test.java");
        assertEquals(file.exists(), true);
        assertEquals(compareContents(file, new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/3/Test.java")), true);
    }
    
    private boolean compareContents(File f1, File f2) throws IOException {
        return getContents(f1).equals(getContents(f2));
    }
    
    private String getContents(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
    
}