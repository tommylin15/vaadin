package com.scsb.vaadin.ui.export;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class PdfExporter extends Exporter {

    public PdfExporter(Table table) {
        super(table);
    }

    public PdfExporter(Container container, Object[] visibleColumns) {
        super(container, visibleColumns);
    }

    public PdfExporter(Container container) {
        super(container);
    }

    @Override
    protected FileBuilder createFileBuilder(Container container) {
        return new PdfFileBuilder(container);
    }

    @Override
    protected String getDownloadFileName() {
        if (downloadFileName == null) {
            return "exported-pdf.pdf";
        }
        if (downloadFileName.endsWith(".pdf")) {
            return downloadFileName;
        } else {
            return downloadFileName + ".pdf";
        }
    }

    public void setWithBorder(boolean withBorder) {
        ((PdfFileBuilder) fileBuilder).setWithBorder(withBorder);
    }

}
