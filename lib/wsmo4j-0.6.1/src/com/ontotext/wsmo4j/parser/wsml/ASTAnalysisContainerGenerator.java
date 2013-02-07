/*
 wsmo4j - a WSMO API and Reference Implementation

 Copyright (c) 2004-2005, OntoText Lab. / SIRMA

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License along
 with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/**
 * <p>Title: WSMO4J</p>
 * <p>Description: WSMO API and a Reference Implementation</p>
 * <p>Copyright:  Copyright (c) 2004-2005</p>
 * <p>Company: OntoText Lab. / SIRMA </p>
 */

package com.ontotext.wsmo4j.parser.wsml;

import java.io.*;
import java.lang.reflect.*;

import org.wsmo.wsml.compiler.node.*;

/**
 * Class to code generate the content of ASTAnalysisContainer.java and
 * ASTAnalysis.java according to the SableCC generated node classes.
 * 
 * The code generated ASTAnalysisContainer has to override all DepthFirstAdapter
 * methods caseANode, inANode and outANode in order to allow custom object to subscribe 
 * for the events. ASTAnalysisContainer scan all used (reserved) tokens in the grammar
 * and generates a public const String[] GRAMMAR_TOKENS.
 * 
 * ASTAnalysis class provide the list of all events a custom analyzer can subscribe/recieve.
 * 
 * To code generate start the main and provide.
 * 1. The absolute or relative path to org.wsmo.compiler.node package dir.
 * 2. The absolute or relative path to ASTAnalysisContainer.java.
 * 3. The absolute or relative path to ASTAnalysis.java.
 *   
 * @author not attributable
 */
class ASTAnalysisContainerGenerator {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please specify the relative path to " +
                    "org/wsmo/compiler/node class or java files and" +
                    "the java to hold the generated code!");
            System.out.println("Example:");
            System.out.println("org/wsmo/compiler/node " +
                    "com/ontotext/wsmo4j/parser/ASTAnalysisContainer.java" +
                    "com/ontotext/wsmo4j/parser/ASTAnalysis.java");
            return;
        }

        File typeDir = new File(args[0]);
        File hostFile = new File(args[1]);
        File hostFile2 = new File(args[2]);

        if (!typeDir.isDirectory()) {
            System.out.println("You have to provide a directory for " +
                    "type source!\n");
            return;
        }

        // Get all types starting with A
        String[] types = typeDir.list(new FilenameFilter() {
            private String fileSuffix = null;

            // locates the extetion of the files class/java and extract all
            // types starting by A
            public boolean accept(File dir, String name) {
                if (!name.startsWith("A") || name.indexOf("$") > 0)
                    return false;
                if (fileSuffix == null) {
                    if (name.endsWith("class"))
                        fileSuffix = "class";
                    else if (name.endsWith("java"))
                        fileSuffix = "java";
                    else
                        return false;
                }
                if (name.startsWith("A") && name.endsWith(fileSuffix))
                    return true;
                return false;
            }

        });

        StringBuffer fileContentContainer = new StringBuffer();
        StringBuffer fileContentClass = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(hostFile));
            for (int i = 0; i < 1000; i++) {
                String line = br.readLine();
                fileContentContainer.append(line);
                fileContentContainer.append('\n');
                if (line.indexOf("// DO NOT MODIFY THIS LINE OR THE GENERATED CODE BELLOW") > 0)
                    break;
            }
            br.close();

            br = new BufferedReader(new FileReader(hostFile2));
            for (int i = 0; i < 1000; i++) {
                String line = br.readLine();
                fileContentClass.append(line);
                fileContentClass.append('\n');
                if (line.indexOf("// DO NOT MODIFY THIS LINE OR THE GENERATED CODE BELLOW") > 0)
                    break;
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File to host generated code is not found!\n");
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during file writting!\n");
            return;
        }

        String methodPrefix = null;
        String type = null;

        for (int i = -1; i < types.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) methodPrefix = "in";
                else if (j == 1) methodPrefix = "out";
                else methodPrefix = "case";

                // include the Start element!
                if (i == -1) type = "Start";
                else type = types[i].substring(0, types[i].indexOf("."));

                // ASTAnalysisContainer.java

                fileContentContainer.append("\n");
                fileContentContainer.append("    public void "+methodPrefix+type+"("+type+" node) {\n");
                fileContentContainer.append("        Object adapter = adaptersToHandleNodes.get("+type+".class);\n");
                fileContentContainer.append("        if (adapter != null && adapter instanceof ASTAnalysis) {\n");
                fileContentContainer.append("            ((ASTAnalysis) adapter)."+methodPrefix+type+"(node);\n");
                fileContentContainer.append("        }\n"); // close if section
                fileContentContainer.append("        super."+methodPrefix+type+"(node); // to continue the tree traversal\n");
                fileContentContainer.append("    }\n"); // close method section

                // ASTAnalysis.java

                fileContentClass.append("\n");
                fileContentClass.append("    public void "+methodPrefix+type+"("+type+" node) {\n");
                fileContentClass.append("    }\n"); // close method section
            }
        }

        // create the list of all used tokens in the grammar
        fileContentContainer.append("\n");
        fileContentContainer.append(allUsedTokens(typeDir));
        fileContentContainer.append("\n");
        
        fileContentContainer.append("}\n"); // close the class section
        fileContentClass.append("}\n"); // close the class section

        try {
            FileWriter fw = new FileWriter(hostFile);
            fw.write(new String(fileContentContainer));
            fw.close();

            fw = new FileWriter(hostFile2);
            fw.write(new String(fileContentClass));
            fw.close();
            System.out.println("Code generated successfuly!");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not open the host file for writting!");
            return;
        }
    }
    
    /**
     * Generates token of type "public final static String[] USED_TOKENS = String[] { all tokens used in the gramamr... };.
     * @param typeDir directory where org.wsmo.compiler.node.T* classes are located
     */
    private static StringBuffer allUsedTokens(File typeDir) {
        
        // Get all types starting with T
        String[] tokenClasses = typeDir.list(new FilenameFilter() {
            private String fileSuffix = null;

            // locates the extetion of the files class/java and extract all types starting by T
            public boolean accept(File dir, String name) {
                if (!name.startsWith("T") || name.indexOf("$") > 0)
                    return false;
                if (fileSuffix == null) {
                    if (name.endsWith("class"))
                        fileSuffix = "class";
                    else if (name.endsWith("java"))
                        fileSuffix = "java";
                    else
                        return false;
                }
                if (name.startsWith("T") && name.endsWith(fileSuffix))
                    return true;
                return false;
            }

        });
        
        StringBuffer output = new StringBuffer();
        output.append("    public final static String[] WSML_TOKENS = new String[] {");
        
        try {
            for (int i = 0; i < tokenClasses.length; i++) {
                Class c = Class.forName("org.wsmo.wsml.compiler.node." + tokenClasses[i].substring(0, tokenClasses[i].indexOf(".")));
                if (!Token.class.isAssignableFrom(c) || c.getName() == "org.wsmo.wsml.compiler.node.Token") {
                    continue;
                }
                
                // check if the class has constructor with no parameters
                Constructor con = null;
                try {
                    con = c.getConstructor(new Class[]{});
                }
                catch (NoSuchMethodException e) {
                    continue;
                }
                
                // create token class and get the hardcoded text in getText() method
                Token token = (Token) con.newInstance(new Object[]{});
                output.append("\"");
                output.append(escapeString(token.getText()));
                output.append("\"");
                
                if (i + 1 < tokenClasses.length) {
                    output.append(", ");
                }
            }
            
            output.append("};\n");
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot extract all tokens from the grammar!", e);
        }
        
        return output;
    }
    
    /**
     * Escape string '\' -> '\\' and '"' -> '\"'.
     * @param input to escape
     * @return escaped string
     */
    private static StringBuffer escapeString(String input) {
        StringBuffer output = new StringBuffer();
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i); 
            if (c == '\\')
                output.append("\\\\");
            else if (c == '"')
                output.append("\\\"");
            else
                output.append(c);
        }
        
        return output;
    }
}

/*
 * $Log: ASTAnalysisContainerGenerator.java,v $
 * Revision 1.2  2005/12/09 10:45:30  vassil_momtchev
 * javadoc added; new generated const added to list all grammar tokens
 *
*/
