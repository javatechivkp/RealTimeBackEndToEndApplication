package com.org.java.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import com.org.java.app.entity.Employee;

public final class PdfGenerator {

	private PdfGenerator() {}

	public static byte[] generateEmployeesPdf(List<Employee> employees) {
		try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			// Use landscape by swapping A4 width/height (PDFBox 3 has no rotate())
			PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
			document.addPage(page);

			PDPageContentStream content = new PDPageContentStream(document, page);
			float margin = 24f; // tighter margins
			float y = page.getMediaBox().getHeight() - margin;
			float leading = 12f; // tighter line height

			content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
			text(content, margin, y, "Employees Report");
			y -= leading * 1.5f;

			content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
			text(content, margin, y, "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			y -= leading * 1.2f;

			// Header
			content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 8);
			String[] headers = new String[] {"EmpId","Name","Age","Salary","Email","WorkLocation","Platform","projectName","addharNumber","panNumber","mobbileNumber"};
			writeRow(content, margin, y, headers);
			y -= leading;
			content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);

			for (Employee e : employees) {
				if (y < margin + leading) {
					content.close();
					page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
					document.addPage(page);
					content = new PDPageContentStream(document, page);
					y = page.getMediaBox().getHeight() - margin;
					content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
				}

				String[] cols = new String[] {
					String.valueOf(e.getEmpid()),
					ns(e.getName()),
					String.valueOf(e.getAge()),
					String.format("%.2f", e.getSalary()),
					ns(e.getEmail()),
					ns(e.getWorkLocation()),
					ns(e.getPlatform()),
					ns(e.getProjectName()),
					String.valueOf(e.getAddharNumber()),
					ns(e.getPanNumber()),
					String.valueOf(e.getMobbileNumber()),
					ns(e.getEmail())
				};
				writeRow(content, margin, y, cols);
				y -= leading;
			}

			content.close();
			document.save(baos);
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to generate PDF", ex);
		}
	}

	private static void writeRow(PDPageContentStream content, float x, float y, String[] cols) throws IOException {
		// Column widths tuned for A4 width with small margins
		// Sum ~= 790 points to fit A4 landscape (842pt) with margins
		float[] colWidths = {36f, 110f, 32f, 60f, 80f, 80f, 70f, 70f, 140f, 44f, 88f};
		float cx = x;
		for (int i = 0; i < Math.min(cols.length, colWidths.length); i++) {
			text(content, cx, y, truncate(cols[i], (int)(colWidths[i] / 5))); // tighter fit per width
			cx += colWidths[i];
		}
	}

	private static void text(PDPageContentStream content, float x, float y, String s) throws IOException {
		content.beginText();
		content.newLineAtOffset(x, y);
		content.showText(s == null ? "" : s);
		content.endText();
	}

	private static String ns(String s) { return s == null ? "" : s; }
	private static String truncate(String s, int maxChars) { return s != null && s.length() > maxChars ? s.substring(0, maxChars - 1) + "…" : ns(s); }
}


