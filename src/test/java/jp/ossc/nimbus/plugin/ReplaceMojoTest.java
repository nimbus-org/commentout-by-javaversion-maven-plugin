package jp.ossc.nimbus.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

public class ReplaceMojoTest extends AbstractMojoTestCase {
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        FileUtility.deleteAllTree(new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to"), false);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        FileUtility.deleteAllTree(new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to"), false);
    }
    
    @Test
    public void testReplace1() throws Exception {
        File testPom = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/test1_pom.xml");
        ReplaceMojo mojo = (ReplaceMojo) lookupMojo("replace", testPom);
        mojo.execute();
        File file = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to/1/1_1/1_1.java");
        assertEquals(file.exists(), true);
        assertEquals(compareContents(file, new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/from/1/1_1/answer.java")), true);
        file = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to/1/1_2/1_2.java");
        assertEquals(file.exists(), false);
        file = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to/2/2.java");
        assertEquals(file.exists(), true);
        assertEquals(compareContents(file, new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/from/2/answer.java")), true);
        file = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to/2_1/2_1.java");
        assertEquals(file.exists(), false);
        file = new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/to/2/2_2/2_2.java");
        assertEquals(file.exists(), true);
        assertEquals(compareContents(file, new File(getBasedir(), "src/test/resources/jp/ossc/nimbus/plugin/from/2/2_2/answer.java")), true);
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