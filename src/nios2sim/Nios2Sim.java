//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nios2sim;

import gui.Nios2SimMenuBar;
import gui.Nios2SimPanel;
import gui.modules.Module;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

import static gui.Nios2SimFrame.DARK_COLOR;

public class Nios2Sim {
    public static final String TITLE = "Nios2Sim 2020";
    public static final String version = "0.6.4";
    public static final String developpers = "Benda Lukas, Blanc Régis, Boichat Nicolas,\nJimenez Xavier, Panagiotis Athanasopoulos";
    public static final String copyright = "© 2007-2009 LAP-EPFL";
    public static final String DEFAULT_DOC = "new.asm";
    public static final String DEFAULT_SYSTEM = "/resource/xml/defaultSystem.xml";
    private JFileChooser asmFileChooser;
    private JFileChooser hexFileChooser;
    private JFileChooser xmlFileChooser;
    private Container container;
    private Nios2SimMenuBar menuBar = null;
    private Nios2SimPanel panel = null;
    private File file = null;
    private File fileSystem = null;

    public Nios2Sim(Container var1) {
        this.container = var1;
        this.container.setBackground(DARK_COLOR);
        this.refreshTitle();
        this.reload();
    }

    public Nios2SimPanel getPanel() {
        if (this.panel == null) {
            this.panel = new Nios2SimPanel(this);
        }

        return this.panel;
    }

    public Nios2SimMenuBar getMenuBar() {
        if (this.menuBar == null) {
            this.menuBar = new Nios2SimMenuBar(this);
        }

        return this.menuBar;
    }

    public boolean fileIsModified() {
        return this.panel.getUndoManager().canUndo();
    }

    private void refreshTitle() {
        if (JFrame.class.isInstance(this.container)) {
            ((JFrame)this.container).setTitle("Nios2Sim 2020 - " + this.getFileName());
        }

    }

    private String getFileName() {
        return this.file == null ? "new.asm" : this.file.getName();
    }

    public void open(File var1) throws Exception {
        if (!var1.isFile()) {
            this.error("\"" + var1.getName() + "\" is not a file.");
        } else {
            if (!this.fileIsModified() || this.saveChangesDialog() != 2) {
                this.file = var1;
                this.panel.open(this.file);
                this.refreshTitle();
            }

        }
    }

    public void open() {
        int var1 = this.getAsmFileChooser().showOpenDialog(this.container);
        if (var1 == 0) {
            try {
                this.open(this.getAsmFileChooser().getSelectedFile());
            } catch (Exception var3) {
                var3.printStackTrace();
                this.error(var3.getMessage());
                this.file = null;
                this.refreshTitle();
            }
        }

    }

    private int saveChangesDialog() {
        boolean var1 = false;
        int var2 = JOptionPane.showConfirmDialog(this.container, this.getFileName() + " has been modified. Do you want to save the changes before?", "Confirm Exit", 1, 3);
        if (var2 == 0) {
            var2 = this.save();
        }

        return var2;
    }

    public void newFile() {
        if (!this.fileIsModified() || this.saveChangesDialog() != 2) {
            this.panel.clearDocument();
            this.file = null;
            this.refreshTitle();
        }

    }

    public int save() {
        int var1 = 0;
        if (this.file == null) {
            var1 = this.saveAs();
        } else {
            try {
                this.panel.save(this.file);
            } catch (Exception var3) {
                this.error(var3.getMessage());
                var1 = 2;
            }
        }

        return var1;
    }

    public int saveAs() {
        int var1 = 1;
        int var2 = 0;

        while(var2 == 0 && var1 == 1) {
            var2 = this.getAsmFileChooser().showSaveDialog(this.container);
            if (var2 == 0) {
                try {
                    File var3 = this.getAsmFileChooser().getSelectedFile();
                    if (var3.exists()) {
                        var1 = JOptionPane.showConfirmDialog(this.container, var3.getName() + " already exists. Do you want to overwrite it?", "Confirm Overwrite", 0, 3);
                        if (var1 == 0) {
                            this.file = var3;
                            this.panel.save(this.file);
                            this.refreshTitle();
                        }
                    } else {
                        this.file = var3;
                        this.panel.save(this.file);
                        var1 = 0;
                        this.refreshTitle();
                    }
                } catch (Exception var4) {
                    this.error(var4.getMessage());
                }
            } else {
                var1 = 2;
            }
        }

        return var1;
    }

    public void saveHex() {
        Module var1 = null;
        Module[] var2;
        if (this.panel.getDataModule() != null && this.panel.getDataModule() != this.panel.getTextModule()) {
            var2 = new Module[]{this.panel.getTextModule(), this.panel.getDataModule()};
        } else {
            var2 = new Module[]{this.panel.getTextModule()};
        }

        var1 = (Module)JOptionPane.showInputDialog(this.container, "Choose the memory module content to export", "Export as hex file", 3, (Icon)null, var2, (Object)null);
        if (var1 != null) {
            String var3 = var1.getName() + ".hex";
            File var4 = new File(var3);
            this.getHexFileChooser().setSelectedFile(var4);
            int var5 = 0;
            boolean var6 = false;

            int var9;
            do {
                var9 = this.getHexFileChooser().showSaveDialog(this.container);
                if (var9 == 0) {
                    var4 = this.getHexFileChooser().getSelectedFile();
                    var5 = 0;
                    if (var4.exists()) {
                        var5 = JOptionPane.showConfirmDialog(this.container, var4.getName() + " already exists. Do you want to overwrite it?", "Confirm Overwrite", 0, 3);
                    }

                    if (var5 == 0) {
                        try {
                            this.panel.saveHex(var4, var1);
                        } catch (Exception var8) {
                            this.error(var8.getMessage());
                        }
                    }
                }
            } while(var5 != 0 && var9 == 0);

        }
    }

    private JFileChooser getAsmFileChooser() {
        if (this.asmFileChooser == null) {
            this.asmFileChooser = new JFileChooser("./");
            this.asmFileChooser.addChoosableFileFilter(new FileFilter() {
                public String getDescription() {
                    return "Asm file (.asm)";
                }

                public boolean accept(File var1) {
                    String var2 = var1.getName().toLowerCase();
                    return var1.isDirectory() || var2.endsWith(".asm");
                }
            });
            this.asmFileChooser.setFileSelectionMode(0);
        }

        return this.asmFileChooser;
    }

    private JFileChooser getHexFileChooser() {
        if (this.hexFileChooser == null) {
            this.hexFileChooser = new JFileChooser("./");
            this.hexFileChooser.addChoosableFileFilter(new FileFilter() {
                public String getDescription() {
                    return "Hex file (.hex)";
                }

                public boolean accept(File var1) {
                    String var2 = var1.getName().toLowerCase();
                    return var1.isDirectory() || var2.endsWith(".hex");
                }
            });
            this.hexFileChooser.setFileSelectionMode(0);
        }

        return this.hexFileChooser;
    }

    private JFileChooser getXMLFileChooser() {
        if (this.xmlFileChooser == null) {
            this.xmlFileChooser = new JFileChooser("./");
            this.xmlFileChooser.addChoosableFileFilter(new FileFilter() {
                public String getDescription() {
                    return "XML file (.xml)";
                }

                public boolean accept(File var1) {
                    String var2 = var1.getName().toLowerCase();
                    return var1.isDirectory() || var2.endsWith(".xml");
                }
            });
            this.xmlFileChooser.setFileSelectionMode(0);
        }

        return this.xmlFileChooser;
    }

    public void exit() {
        if (this.fileIsModified()) {
            if (this.saveChangesDialog() != 2) {
                System.exit(0);
            }
        } else {
            int var1 = JOptionPane.showConfirmDialog(this.container, "Exit Nios2Sim?", "Confirm Exit", 0, 3);
            if (var1 == 0) {
                System.exit(0);
            }
        }

    }

    public void load() {
        int var1 = this.getXMLFileChooser().showOpenDialog(this.container);
        if (var1 == 0) {
            try {
                this.fileSystem = this.getXMLFileChooser().getSelectedFile();
                this.panel.loadSystem(new FileInputStream(this.fileSystem));
                this.panel.getSimulation().reloadSystem();
                this.panel.info("System loaded", -1);
            } catch (Exception var3) {
                var3.printStackTrace();
                this.panel.error("While loading System: " + var3.getMessage(), -1);
            }
        }

    }

    public void reload() {
        try {
            if (this.fileSystem != null) {
                this.getPanel().loadSystem(new FileInputStream(this.fileSystem));
                this.getPanel().getSimulation().reloadSystem();
                this.getPanel().info("System reloaded", -1);
            } else {
                this.getPanel().loadSystem(this.getClass().getResourceAsStream("/resource/xml/defaultSystem.xml"));
                this.getPanel().getSimulation().reloadSystem();
                this.getPanel().info("Default system loaded", -1);
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            this.error("While loading System : " + var2.getMessage());
        }

    }

    public void error(String var1) {
        JOptionPane.showMessageDialog(this.container, var1, "Error!", 0);
    }
}
