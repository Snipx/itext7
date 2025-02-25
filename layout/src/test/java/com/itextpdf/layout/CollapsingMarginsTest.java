/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.layout;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.renderer.DivRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Category(IntegrationTest.class)
public class CollapsingMarginsTest extends ExtendedITextTest {
    private static final String sourceFolder = "./src/test/resources/com/itextpdf/layout/CollapsingMarginsTest/";
    private static final String destinationFolder = "./target/test/com/itextpdf/layout/CollapsingMarginsTest/";

    private static final String TEXT_BYRON = "When a man hath no freedom to fight for at home,\n" +
            "    Let him combat for that of his neighbours;\n" +
            "Let him think of the glories of Greece and of Rome,\n" +
            "    And get knocked on the head for his labours.\n" +
            "\n" +
            "To do good to Mankind is the chivalrous plan,\n" +
            "    And is always as nobly requited;\n" +
            "Then battle for Freedom wherever you can,\n" +
            "    And, if not shot or hanged, you'll get knighted.";

    @BeforeClass
    public static void beforeClass() {
        createOrClearDestinationFolder(destinationFolder);
    }

    @Test
    public void collapsingMarginsTest01() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "collapsingMarginsTest01.pdf";
        String cmpFileName = sourceFolder + "cmp_collapsingMarginsTest01.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        drawPageBorders(pdfDocument, 4);

        Document doc = new Document(pdfDocument);
        doc.setProperty(Property.COLLAPSING_MARGINS, true);

        doc.add(new Paragraph("marker text").setMargin(0));

        Paragraph p = new Paragraph(TEXT_BYRON);
        for (int i = 0; i < 5; i++) {
            p.add(TEXT_BYRON);
        }

        Div div1 = new Div();
        Div div2 = new Div();

        div1.add(p).setBackgroundColor(new DeviceRgb(65, 151, 29));
        div2.add(p).setBackgroundColor(new DeviceRgb(209, 247, 29));

        div1.setMarginBottom(20);
        div2.setMarginTop(150);
        div2.setMarginBottom(150);

        Div div = new Div().setMarginTop(20).setMarginBottom(10).setBackgroundColor(new DeviceRgb(78, 151, 205));
        div.add(div1);
        div.add(div2);
        doc.add(div);

        doc.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    @Test
    public void collapsingMarginsTest02() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "collapsingMarginsTest02.pdf";
        String cmpFileName = sourceFolder + "cmp_collapsingMarginsTest02.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        drawPageBorders(pdfDocument, 3);

        Document doc = new Document(pdfDocument);
        doc.setProperty(Property.COLLAPSING_MARGINS, true);

        doc.add(new Paragraph("marker text").setMargin(0));

        Paragraph p = new Paragraph(TEXT_BYRON);
        for (int i = 0; i < 3; i++) {
            p.add(TEXT_BYRON);
        }
        p.add("When a man hath no freedom to fight for at home,\n" +
                "    Let him combat for that of his neighbours;\n" +
                "Let him think of the glories of Greece and of Rome,\n" +
                "    And get knocked on the head for his labours.\n" +
                "\n" +
                "To do good to Mankind is the chivalrous plan,\n");

        Div div1 = new Div();
        Div div2 = new Div();

        div1.add(p).setBackgroundColor(new DeviceRgb(65, 151, 29));
        div2.add(p).setBackgroundColor(new DeviceRgb(209, 247, 29));

        div1.setMarginBottom(40);
        div2.setMarginTop(20);
        div2.setMarginBottom(150);

        Div div = new Div().setMarginTop(20).setMarginBottom(10).setBackgroundColor(new DeviceRgb(78, 151, 205));
        div.add(div1);
        div.add(div2);
        doc.add(div);

        doc.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    @Test
    public void collapsingMarginsTest03() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "collapsingMarginsTest03.pdf";
        String cmpFileName = sourceFolder + "cmp_collapsingMarginsTest03.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        drawPageBorders(pdfDocument, 3);

        Document doc = new Document(pdfDocument);
        doc.setProperty(Property.COLLAPSING_MARGINS, true);

        doc.add(new Paragraph("marker text").setMargin(0));

        Paragraph p = new Paragraph(TEXT_BYRON);
        for (int i = 0; i < 3; i++) {
            p.add(TEXT_BYRON);
        }
        p.add("When a man hath no freedom to fight for at home,\n" +
                "    Let him combat for that of his neighbours;\n" +
                "Let him think of the glories of Greece and of Rome,\n" +
                "    And get knocked on the head for his labours.\n" +
                "To do good to Mankind is the chivalrous plan,\n");

        Div div1 = new Div();
        Div div2 = new Div();

        div1.add(p).setBackgroundColor(new DeviceRgb(65, 151, 29));
        div2.add(p).setBackgroundColor(new DeviceRgb(209, 247, 29));

        div1.setMarginBottom(80);
        div2.setMarginTop(80);
        div2.setMarginBottom(150);

        doc.add(div1);
        doc.add(div2);

        doc.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    @Test
    public void collapsingMarginsTest04() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "collapsingMarginsTest04.pdf";
        String cmpFileName = sourceFolder + "cmp_collapsingMarginsTest04.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        drawPageBorders(pdfDocument, 3);

        Document doc = new Document(pdfDocument);
        doc.setProperty(Property.COLLAPSING_MARGINS, true);

        doc.add(new Paragraph("marker text").setMargin(0));

        Paragraph p = new Paragraph(TEXT_BYRON);
        for (int i = 0; i < 3; i++) {
            p.add(TEXT_BYRON);
        }
        p.add("When a man hath no freedom to fight for at home,\n" +
                "    Let him combat for that of his neighbours;\n" +
                "Let him think of the glories of Greece and of Rome,\n" +
                "    And get knocked on the head for his labours.\n" +
                "To do good to Mankind is the chivalrous plan,\n");
        p.add(new Text("small text").setFontSize(5.1f));
        p.add(
                "\nAnd is always as nobly requited;\n" +
                        "Then battle for Freedom wherever you can,\n" +
                        "And, if not shot or hanged, you'll get knighted.");

        Div div1 = new Div();
        Div div2 = new Div();

        div1.add(p).setBackgroundColor(new DeviceRgb(65, 151, 29));
        div2.add(p).setBackgroundColor(new DeviceRgb(209, 247, 29));

        div1.setMarginBottom(80);
        div2.setMarginTop(80);
        div2.setMarginBottom(150);

        doc.add(div1);
        doc.add(div2);

        doc.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    @Test
    public void collapsingMarginsTest05() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "collapsingMarginsTest05.pdf";
        String cmpFileName = sourceFolder + "cmp_collapsingMarginsTest05.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        drawPageBorders(pdfDocument, 2);

        Document doc = new Document(pdfDocument);
        doc.setProperty(Property.COLLAPSING_MARGINS, true);

        Paragraph p = new Paragraph(TEXT_BYRON).setBackgroundColor(ColorConstants.YELLOW);
        for (int i = 0; i < 3; i++) {
            p.add(TEXT_BYRON);
        }
        doc.add(p);

        p.setMarginTop(80);
        Div div = new Div();

        div.add(p).setBackgroundColor(new DeviceRgb(65, 151, 29));

        doc.add(div);

        doc.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    @Test
    public void elementCollapsingMarginsTest01() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "elementCollapsingMarginsTest01.pdf";
        String cmpFileName = sourceFolder + "cmp_elementCollapsingMarginsTest01.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        drawPageBorders(pdfDocument, 1);

        Document doc = new Document(pdfDocument);

        Paragraph markerText = new Paragraph("Margin between this paragraph and next block is expected to be 170pt.")
                .setBackgroundColor(new DeviceRgb(65, 151, 29)); // greenish

        Div div = new Div();
        Paragraph p = new Paragraph(TEXT_BYRON);
        div.add(p).setBackgroundColor(new DeviceRgb(209,247,29)); // yellowish
        div.setProperty(Property.COLLAPSING_MARGINS, true);

        markerText.setMarginBottom(20);
        p.setMarginTop(50);
        div.setMarginTop(150);

        doc.add(markerText);
        doc.add(div);

        doc.close();

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    private void drawPageBorders(PdfDocument pdfDocument, int pageNum) {
        for (int i = 1; i <= pageNum; ++i) {
            while (pdfDocument.getNumberOfPages() < i) {
                pdfDocument.addNewPage();
            }
            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(i));
            canvas.saveState();
            canvas.setLineDash(5, 10);
            canvas.rectangle(36, 36, 595 - 36 * 2, 842 - 36 * 2);
            canvas.stroke();
            canvas.restoreState();
        }
    }

    @Test
    /* TODO DEVSIX-2901 the exception should not be thrown
       if after DEVSIX-2901 the exception persists,
       change the type of the expected exception to a more specific one to make the test stricter.
    */
    public void columnRendererTest() throws IOException, InterruptedException {
        String outFileName = destinationFolder + "columnRendererTest.pdf";
        String cmpFileName = sourceFolder + "cmp_columnRendererTest.pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outFileName));

        try (Document doc = new Document(pdfDocument)) {
            doc.setProperty(Property.COLLAPSING_MARGINS, true);

            Paragraph p = new Paragraph();
            for (int i = 0; i < 10; i++) {
                p.add(TEXT_BYRON);
            }

            Div div = new Div().add(p);
            List<Rectangle> areas = new ArrayList<>();
            areas.add(new Rectangle(30, 30, 150, 600));
            areas.add(new Rectangle(200, 30, 150, 600));
            areas.add(new Rectangle(370, 30, 150, 600));
            div.setNextRenderer(new CustomColumnDocumentRenderer(div, areas));

            Assert.assertThrows(Exception.class, () -> doc.add(div));
        }

        Assert.assertNull(new CompareTool().compareByContent(outFileName, cmpFileName, destinationFolder, "diff"));
    }

    private static class CustomColumnDocumentRenderer extends DivRenderer {
        private List<Rectangle> areas;

        public CustomColumnDocumentRenderer(Div modelElement, List<Rectangle> areas) {
            super(modelElement);
            this.areas = areas;
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = super.layout(layoutContext);
            return result;
        }

        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            return areas;
        }

        @Override
        public IRenderer getNextRenderer() {
            return new CustomColumnDocumentRenderer((Div) modelElement, areas);
        }
    }
}
