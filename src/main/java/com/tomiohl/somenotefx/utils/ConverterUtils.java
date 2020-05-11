package com.tomiohl.somenotefx.utils;

import com.tomiohl.somenotefx.controller.NoteController;
import com.tomiohl.somenotefx.model.Note;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import com.vladsch.flexmark.profile.pegdown.Extensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.File;
import java.nio.file.Path;

public class ConverterUtils {

    public static String convertToHtml() {
        MutableDataSet options = new MutableDataSet();
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");   // convert soft-breaks to hard breaks
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Note currentNote = NoteController.getInstance().getCurrentNote();
        File selectedFile = new File(Path.of(currentNote.getFilePath(), currentNote.getFilename()).toString());
        com.vladsch.flexmark.util.ast.Node document = parser.parse(NoteController.getInstance().open(selectedFile));
        return renderer.render(document);
    }

    // exportToPdf opciói
    static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
            , TocExtension.create()).toMutable()
            .set(TocExtension.LIST_CLASS, PdfConverterExtension.DEFAULT_TOC_LIST_CLASS)
            .toImmutable();

    public static String convertToPdf(String html) {
        // megkonstruáljuk az útvonalat/fájlnevet
        Note currentNote = NoteController.getInstance().getCurrentNote();
        String notePath = Path.of(currentNote.getFilePath(), currentNote.getFilename()).toString();
        int extensionFrom = notePath.lastIndexOf(".") + 1;
        String pdfFile = notePath.substring(0, extensionFrom) + "pdf";
        // elindítjuk a konverziót
        PdfConverterExtension.exportToPdf(pdfFile, html, "", OPTIONS);
        return pdfFile;
    }

}
