/*
 * This software is distributed under following license based on modified BSD
 * style license.
 * ----------------------------------------------------------------------
 * 
 * Copyright 2003 The Nimbus Project. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE NIMBUS PROJECT ``AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE NIMBUS PROJECT OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of the Nimbus Project.
 */
package jp.ossc.nimbus.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * replace
 *
 * @goal copy
 * @phase generate-sources
 *
 */
public class CopyMojo extends AbstractMojo {
    
    /**
     * @parameter
     */
    private String javaVersion;
    
    /**
     * @parameter
     */
    private String[] targetJavaVersionAndFiles = new String[] {};
    
    /**
     * @parameter
     */
    private File fromDir;
    
    /**
     * @parameter
     */
    private File toDir;
    
    /**
     * @parameter
     */
    private String toFileExtention = "java";
    
    /**
     * Execute.
     *
     * @throws MojoExecutionException predictable error
     * @throws MojoFailureException unpredictable error
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        try {
            getLog().info("copy start");
            
            if (javaVersion == null || "".equals(javaVersion)) {
                getLog().info("javaVersion is not found config.");
                javaVersion = System.getProperty("java.specification.version");
                if(javaVersion.startsWith("1.")) {
                    javaVersion = javaVersion.substring(2);
                }
                getLog().info("check from system javaVersion. javaVersion=" + javaVersion);
            }
            
            if(targetJavaVersionAndFiles == null || targetJavaVersionAndFiles.length == 0) {
                getLog().error("targetJavaVersionAndFiles is null or empty.");
                throw new MojoExecutionException("targetJavaVersionAndFiles is null or empty.");
            }
            getLog().info("targetJavaVersionAndFiles=" + Arrays.asList(targetJavaVersionAndFiles));
            
            if (fromDir == null) {
                getLog().error("fromDir is null.");
                throw new MojoExecutionException("fromDir is null.");
            } else if (!fromDir.exists()) {
                getLog().error("fromDir is not exists.");
                throw new MojoExecutionException("fromDir is not exists.");
            } else if (!fromDir.isDirectory()) {
                getLog().error("fromDir is not directory.");
                throw new MojoExecutionException("fromDir is not directory.");
            }
            getLog().info("source copy fromDir=" + fromDir.getAbsolutePath());
            
            if (toDir == null) {
                getLog().error("toDir is null.");
                throw new MojoExecutionException("toDir is null.");
            } else if (!toDir.exists()) {
                if(toDir.mkdirs()) {
                    getLog().info("toDir is not exists. toDir created.");
                } else {
                    getLog().error("toDir is not exists. toDir could not create.");
                    throw new MojoExecutionException("toDir is not exists. toDir could not create.");
                }
            } else if (!toDir.isDirectory()) {
                getLog().error("toDir is not directory.");
                throw new MojoExecutionException("toDir is not directory.");
            }
            getLog().info("source replace toDir=" + toDir.getAbsolutePath());
            
            if (toFileExtention == null) {
                getLog().error("toFileExtention is null.");
                throw new MojoExecutionException("toFileExtention is null.");
            } else if ("".equals(toFileExtention)) {
                getLog().error("toFileExtention is empty.");
                throw new MojoExecutionException("toFileExtention is empty.");
            } else if(toFileExtention.startsWith(".")) {
                toFileExtention = toFileExtention.substring(1);
            }
            getLog().info("source copy toFileExtention=" + toFileExtention);
            
            for(String targetJavaVersionAndFile : targetJavaVersionAndFiles) {
                String[] strs = targetJavaVersionAndFile.split(",");
                if(strs.length > 0 && javaVersion.equals(strs[0])) {
                    FileUtility rFromDir = new FileUtility(fromDir);
                    List<String> list = new ArrayList<String>(Arrays.asList(strs));
                    list.remove(0);
                    for(String targetFile : list) {
                        File[] copyTargetFiles = rFromDir.listAllTreeFiles(targetFile);
                        for (File copyTargetFile : copyTargetFiles) {
                            String tmpFileName = copyTargetFile.getAbsolutePath().substring(fromDir.getAbsolutePath().length());
                            File toFile = new File(toDir.getAbsolutePath() + tmpFileName.substring(0, tmpFileName.lastIndexOf(".") + 1) + toFileExtention);
                            FileUtility.dataCopy(copyTargetFile, toFile);
                            getLog().debug("file copy " + copyTargetFile.getAbsolutePath() + " to " + toFile.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Throwable th) {
            getLog().error(th.getMessage());
            throw new MojoFailureException("copy failed");
        }
    }
    
}