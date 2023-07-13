/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import javafx.collections.ObservableSet;
import javafx.print.*;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author Diego Carcamo
 */
public class impresion {

    String Factura;

    public impresion() {

        ObservableSet<Printer> printers = Printer.getAllPrinters();
        System.out.println("Impresoras:\n");
        for (Printer printer : printers) {
            System.out.println(printers);
        }

    }

    public void pageSetup(Node node, Stage owner) {
        // Create the PrinterJob

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {
            return;

        }

        // Show the page setup dialog
        boolean proceed = job.showPrintDialog(owner);

        if (proceed) {
            print(job, node);
        }
    }

    private void print(PrinterJob job, Node node) {

        // Print the node
        boolean printed = job.printPage(node);

        if (printed) {
            job.endJob();
        }
    }

}
