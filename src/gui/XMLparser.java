//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import compiler.CompilerException;
import compiler.Interpreter;
import gui.modules.Module;
import gui.modules.ModuleButton;
import gui.modules.ModuleLED;
import gui.modules.ModuleRAM;
import gui.modules.ModuleROM;
import gui.modules.ModuleTimer;
import gui.modules.ModuleUART;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLparser {
    private Document document = null;
    private InputStream inputStream = null;
    private ArrayList<Module> modulesArrayList = new ArrayList();
    private String dataModuleID = "";
    private int dataOffset = -1;
    private String textModuleID = "";
    private int textOffset = -1;
    private int interruptOffset = -1;
    private Module textModule = null;
    private Module dataModule = null;
    private Nios2SimPanel panel;

    public int getTextOffset() {
        return this.textOffset;
    }

    public int getDataOffset() {
        return this.dataOffset;
    }

    public int getInterruptOffset() {
        return this.interruptOffset;
    }

    public Module getTextModule() {
        return this.textModule;
    }

    public Module getDataModule() {
        return this.dataModule;
    }

    public static void printDOMInfos(Document var0) {
        System.out.println("INFORMATIONS GENERALES");
        String var1 = var0.getDocumentURI();
        System.out.println("URI = " + var1);
        String var2 = var0.getXmlVersion();
        System.out.println("Version XML = " + var2);
        String var3 = var0.getXmlEncoding();
        System.out.println("XML encoding = " + var3);
        String var4 = var0.getInputEncoding();
        System.out.println("Input encoding = " + var4);
        boolean var5 = var0.getXmlStandalone();
        System.out.println("XML standalone = " + var5);
        boolean var6 = var0.getStrictErrorChecking();
        System.out.println("Strict error checking = " + var6 + "\n");
        System.out.println("DOCTYPE");
        printDoctype(var0.getDoctype());
        System.out.println("CONFIGURATION");
        printDOMConfiguration(var0.getDomConfig());
    }

    public static void printDoctype(DocumentType var0) {
        if (var0 != null) {
            String var1 = var0.getName();
            System.out.println("Doctype name = " + var1);
            String var2 = var0.getPublicId();
            System.out.println("Doctype public id = " + var2);
            String var3 = var0.getSystemId();
            System.out.println("Doctype system id = " + var3 + "\n");
        }
    }

    public static void printDOMConfiguration(DOMConfiguration var0) {
        if (var0 != null) {
            DOMStringList var1 = var0.getParameterNames();

            for(int var2 = 0; var2 < var1.getLength(); ++var2) {
                String var3 = var1.item(var2);
                System.out.println(var3 + " = " + var0.getParameter(var3));
            }

            System.out.println();
        }
    }

    public void extractInfo() throws CompilerException {
        Element var1 = this.document.getDocumentElement();
        NodeList var2 = var1.getElementsByTagName("text");
        if (var2.getLength() != 1) {
            throw new CompilerException("Exactly one TEXT tag has to be present");
        } else {
            NamedNodeMap var3 = var2.item(0).getAttributes();
            this.textModuleID = var3.getNamedItem("moduleName").getNodeValue();
            if (this.textModuleID != null && this.textModuleID.length() != 0) {
                try {
                    this.textOffset = Interpreter.resolveExpression(var3.getNamedItem("offset").getNodeValue());
                } catch (Exception var29) {
                    this.textOffset = 0;
                }

                if (this.textOffset < 0) {
                    throw new CompilerException("TEXT offset has to be a positive integer");
                } else {
                    try {
                        this.interruptOffset = Interpreter.resolveExpression(var3.getNamedItem("interruptOffset").getNodeValue());
                    } catch (Exception var28) {
                        this.interruptOffset = 4;
                    }

                    if (this.interruptOffset < 0) {
                        throw new CompilerException("Interrupt offset has to be a positive integer");
                    } else {
                        NodeList var30 = var1.getElementsByTagName("data");
                        if (var30.getLength() == 1) {
                            NamedNodeMap var4 = var30.item(0).getAttributes();
                            this.dataModuleID = var4.getNamedItem("moduleName").getNodeValue();
                            if (this.dataModuleID == null || this.dataModuleID.length() == 0) {
                                throw new CompilerException("Invalid .data module");
                            }

                            try {
                                this.dataOffset = Interpreter.resolveExpression(var4.getNamedItem("offset").getNodeValue());
                            } catch (Exception var27) {
                                this.dataOffset = 0;
                            }

                            if (this.dataOffset < 0) {
                                throw new CompilerException("DATA offset has to be a positive integer");
                            }
                        } else if (var30.getLength() > 1) {
                            throw new CompilerException("There can be only zero or one DATA tag");
                        }

                        NodeList var31 = var1.getElementsByTagName("modules");
                        if (var31.getLength() != 1) {
                            throw new CompilerException("Exactly one MODULES tag has to be present");
                        } else {
                            var31 = var31.item(0).getChildNodes();

                            for(int var5 = 0; var5 < var31.getLength(); ++var5) {
                                String var6 = var31.item(var5).getNodeName();
                                if (var6.equalsIgnoreCase("module")) {
                                    NamedNodeMap var7 = var31.item(var5).getAttributes();

                                    String var8;
                                    try {
                                        var8 = var7.getNamedItem("name").getNodeValue();
                                    } catch (Exception var20) {
                                        throw new CompilerException("A module needs a 'name' attribute");
                                    }

                                    int var9;
                                    try {
                                        var9 = Interpreter.resolveExpression(var7.getNamedItem("startAddress").getNodeValue());
                                    } catch (Exception var19) {
                                        throw new CompilerException("A module needs a 'startAddress' attribute");
                                    }

                                    String var10;
                                    try {
                                        var10 = var7.getNamedItem("type").getNodeValue();
                                    } catch (Exception var18) {
                                        throw new CompilerException("A module needs a 'type' attribute");
                                    }

                                    int var11;
                                    try {
                                        var11 = Interpreter.resolveExpression(var7.getNamedItem("irqID").getNodeValue());
                                    } catch (Exception var17) {
                                        var11 = -1;
                                    }

                                    HashMap var12 = new HashMap();
                                    if (var31.item(var5).getChildNodes().getLength() > 0) {
                                        for(int var13 = 0; var13 < var31.item(var5).getChildNodes().getLength(); ++var13) {
                                            String var14 = var31.item(var5).getChildNodes().item(var13).getNodeName();
                                            if (var14.equalsIgnoreCase("options")) {
                                                NamedNodeMap var15 = var31.item(var5).getChildNodes().item(var13).getAttributes();

                                                for(int var16 = 0; var16 < var15.getLength(); ++var16) {
                                                    var12.put(var15.item(var16).getNodeName(), var15.item(var16).getNodeValue());
                                                }
                                            }
                                        }
                                    }

                                    if (var10.equals("rom")) {
                                        try {
                                            ModuleROM var32 = new ModuleROM(this.panel, var9, var8, var11, var12);
                                            this.modulesArrayList.add(var32);
                                            if (var8.equals(this.textModuleID)) {
                                                this.textModule = var32;
                                            }

                                            if (var8.equals(this.dataModuleID)) {
                                                this.dataModule = var32;
                                            }
                                        } catch (Exception var26) {
                                            throw new CompilerException("OPTIONS tag of a ROM module is badly formatted");
                                        }
                                    } else if (var10.equals("ram")) {
                                        try {
                                            ModuleRAM var33 = new ModuleRAM(this.panel, var9, var8, var11, var12);
                                            this.modulesArrayList.add(var33);
                                            if (var8.equals(this.textModuleID)) {
                                                this.textModule = var33;
                                            }

                                            if (var8.equals(this.dataModuleID)) {
                                                this.dataModule = var33;
                                            }
                                        } catch (Exception var25) {
                                            throw new CompilerException("OPTIONS tag of a RAM module is badly formatted");
                                        }
                                    }

                                    if (var10.equals("timer")) {
                                        try {
                                            this.modulesArrayList.add(new ModuleTimer(this.panel, var9, var8, var11, var12));
                                        } catch (Exception var24) {
                                            throw new CompilerException("OPTIONS tag of a Timer module is badly formatted");
                                        }
                                    }

                                    if (var10.equals("button")) {
                                        try {
                                            this.modulesArrayList.add(new ModuleButton(this.panel, var9, var8, var11, var12));
                                        } catch (Exception var23) {
                                            throw new CompilerException("OPTIONS tag of a Button module is badly formatted");
                                        }
                                    }

                                    if (var10.equals("uart")) {
                                        try {
                                            this.modulesArrayList.add(new ModuleUART(this.panel, var9, var8, var11, var12));
                                        } catch (Exception var22) {
                                            throw new CompilerException("OPTIONS tag of a UART module is badly formatted");
                                        }
                                    }

                                    if (var10.equals("led")) {
                                        try {
                                            this.modulesArrayList.add(new ModuleLED(this.panel, var9, var8, var11, var12));
                                        } catch (Exception var21) {
                                            throw new CompilerException("OPTIONS tag of a LED module is badly formatted");
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                throw new CompilerException("Invalid .text module");
            }
        }
    }

    public void checkInfo() throws CompilerException {
        if (this.interruptOffset < 0) {
            throw new CompilerException("Interrupt offset has to be greater than 0.");
        } else if (this.textModule == null) {
            throw new CompilerException("No memory module for .text has been specified");
        } else if (this.textOffset >= 0 && this.textOffset <= this.textModule.getSize()) {
            if (this.dataModule != null && (this.dataOffset < 0 || this.dataOffset > this.dataModule.getSize())) {
                throw new CompilerException("Data offset has to be greater or equal to zero and smaller or equal to the corresponding memory module.");
            } else {
                int var1;
                int var2;
                int var3;
                int var4;
                for(var1 = 0; var1 < this.modulesArrayList.size(); ++var1) {
                    for(var2 = var1 + 1; var2 < this.modulesArrayList.size(); ++var2) {
                        var3 = ((Module)this.modulesArrayList.get(var1)).getIrqID();
                        var4 = ((Module)this.modulesArrayList.get(var2)).getIrqID();
                        if (var3 != -1 && var3 == var4) {
                            throw new CompilerException("The same irqID appears twice : " + var3);
                        }
                    }
                }

                for(var1 = 0; var1 < this.modulesArrayList.size(); ++var1) {
                    if (((Module)this.modulesArrayList.get(var1)).getIrqID() < -1 || ((Module)this.modulesArrayList.get(var1)).getIrqID() > 31) {
                        throw new CompilerException("Every irqID has to be inside 0 - 31.");
                    }
                }

                for(var1 = 0; var1 < this.modulesArrayList.size(); ++var1) {
                    for(var2 = var1 + 1; var2 < this.modulesArrayList.size(); ++var2) {
                        var3 = ((Module)this.modulesArrayList.get(var1)).getStartAddress();
                        var4 = ((Module)this.modulesArrayList.get(var1)).getStartAddress() + ((Module)this.modulesArrayList.get(var1)).getSize() - 4;
                        int var5 = ((Module)this.modulesArrayList.get(var2)).getStartAddress();
                        int var6 = ((Module)this.modulesArrayList.get(var2)).getStartAddress() + ((Module)this.modulesArrayList.get(var2)).getSize() - 4;
                        if (var3 >= var5 && var3 <= var6 || var4 >= var5 && var4 <= var6) {
                            throw new CompilerException("Some modules are overlapping.\n" + Integer.toHexString(var3) + " " + Integer.toHexString(var4) + "\n" + Integer.toHexString(var5) + " " + Integer.toHexString(var6));
                        }
                    }
                }

            }
        } else {
            throw new CompilerException("Text offset has to be greater or equal to zero and smaller or equal to the corresponding memory module.");
        }
    }

    public Module[] getModules() {
        return (Module[])this.modulesArrayList.toArray(new Module[0]);
    }

    public XMLparser(Nios2SimPanel var1, InputStream var2) throws Exception {
        this.panel = var1;
        this.inputStream = var2;
        DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
        var3.setIgnoringComments(true);
        var3.setNamespaceAware(true);
        InputStream var4 = null;
        URL var5 = this.getClass().getResource("/resource/xml/system.xsd");
        if (var5 != null) {
            var4 = var5.openStream();
        }

        if (var4 != null) {
            var3.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            var3.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", var4);
            var3.setValidating(true);
        } else {
            System.out.println("Warning: didn't find the XSD schema. XML validation bypassed.");
        }

        DocumentBuilder var9 = var3.newDocumentBuilder();
        var9.setErrorHandler(new MyErrorHandler());
        this.document = var9.parse(this.inputStream);
    }

    private static class MyErrorHandler implements ErrorHandler {
        private MyErrorHandler() {
        }

        public void warning(SAXParseException var1) throws SAXException {
            System.out.println("Warning: ");
            this.printInfo(var1);
        }

        public void error(SAXParseException var1) throws SAXException {
            throw new SAXException("Line " + var1.getLineNumber() + ": " + var1.getMessage());
        }

        public void fatalError(SAXParseException var1) throws SAXException {
            throw new SAXException(var1.getMessage());
        }

        private void printInfo(SAXParseException var1) {
            System.out.println("   Public ID: " + var1.getPublicId());
            System.out.println("   System ID: " + var1.getSystemId());
            System.out.println("   Line number: " + var1.getLineNumber());
            System.out.println("   Column number: " + var1.getColumnNumber());
            System.out.println("   Message: " + var1.getMessage());
        }
    }
}
