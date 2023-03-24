package com.example.bloodbanktest.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bloodbanktest.Model.Blood;
import com.example.bloodbanktest.Model.User;
import com.example.bloodbanktest.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BloodStockReports extends AppCompatActivity {

    private DatabaseReference payRef;

    List<Blood> paymentUsersList;

    public static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int PERMISSION_ALL = 12;

    public static File pFile;
    private File payfile;
    private PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_stock_reports);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Stock Reports");


        payRef = FirebaseDatabase.getInstance().getReference().child("bloodbank");
        pdfView = findViewById(R.id.pdfView);

        Button reportButton = findViewById(R.id.button_disable_report);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDisabledUsersReport();

            }
        });

        paymentUsersList = new ArrayList<>();
        payfile = new File("/storage/emulated/0/Report/");

        if (!payfile.exists()) {
            payfile.mkdirs();
        }
        pFile = new File(payfile, "BloodStockReports.pdf");
        fetchPaymentUsers();


    }

    private void fetchPaymentUsers() {
        payRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//creating an object and setting to displlay
                    Blood pays = new Blood();
                    pays.setAp(snapshot.child("A+").getValue().toString());
                    pays.setAn(snapshot.child("A-").getValue().toString());
                    pays.setBp(snapshot.child("B+").getValue().toString());
                    pays.setBn(snapshot.child("B-").getValue().toString());
                    pays.setAbp(snapshot.child("AB+").getValue().toString());
                    pays.setAbn(snapshot.child("AB-").getValue().toString());
                    pays.setOp(snapshot.child("O+").getValue().toString());
                    pays.setOn(snapshot.child("O-").getValue().toString());




                    //this just log details fetched from db(you can use Timber for logging
//                    Log.d("Payment", "Name: " + pays.getName());
//                    Log.d("Payment", "Email: " + pays.getEmail());
//                    Log.d("Payment", "Phone Number: " + pays.getPhonenumber());
//                    Log.d("Payment")

                    /* The error before was cause by giving incorrect data type
                    You were adding an object of type PaymentUsers yet the arraylist expects obejct of type DisabledUsers
                     */
                    paymentUsersList.add(pays);

                }
                try {
                    createPaymentReport(paymentUsersList);
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createPaymentReport(List<Blood> paymentUsersList) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#C62949");

        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(pFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{5,5,5,5,5,5,5,5,5});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk noText = new Chunk("No.", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk apText = new Chunk("A+", white);
        PdfPCell apCell = new PdfPCell(new Phrase(apText));
        apCell.setFixedHeight(50);
        apCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        apCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk anText = new Chunk("A-", white);
        PdfPCell anCell = new PdfPCell(new Phrase(anText));
        anCell.setFixedHeight(50);
        anCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        anCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk bpText = new Chunk ("B+", white);
        PdfPCell bpCell = new PdfPCell(new Phrase(bpText));
        bpCell.setFixedHeight(50);
        bpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        bpCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk bnText = new Chunk("B-", white);
        PdfPCell bnCell = new PdfPCell(new Phrase(bnText));
        bnCell.setFixedHeight(50);
        bnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        bnCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk abpText = new Chunk("AB+", white);
        PdfPCell abpCell = new PdfPCell(new Phrase(abpText));
        abpCell.setFixedHeight(50);
        abpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        abpCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk abnText = new Chunk("AB-", white);
        PdfPCell abnCell = new PdfPCell(new Phrase(abnText));
        abnCell.setFixedHeight(50);
        abnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        abnCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk opText = new Chunk("O+", white);
        PdfPCell opCell = new PdfPCell(new Phrase(opText));
        opCell.setFixedHeight(50);
        opCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        opCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk onText = new Chunk("O-", white);
        PdfPCell onCell = new PdfPCell(new Phrase(onText));
        onCell.setFixedHeight(50);
        onCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        onCell.setVerticalAlignment(Element.ALIGN_CENTER);


        Chunk footerText = new Chunk("Maxwell Mwandigha - Copyright @ 2022");
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(9);

        table.addCell(noCell);
        table.addCell(apCell);
        table.addCell(anCell);
        table.addCell(bpCell);
        table.addCell(bnCell);
        table.addCell(abpCell);
        table.addCell(abnCell);
        table.addCell(opCell);
        table.addCell(onCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < paymentUsersList.size(); i++) {
            Blood pay = paymentUsersList.get(i);

            String id = String.valueOf(i + 1);
            String ap = pay.getAp();
            String an = pay.getAn();
            String bp = pay.getBp();
            String bn = pay.getBn();
            String abp = pay.getAbp();
            String abn = pay.getAbn();
            String op = pay.getOp();
            String on = pay.getOn();


            table.addCell(id + ". ");
            table.addCell(ap);
            table.addCell(an);
            table.addCell(bp);
            table.addCell(bn);
            table.addCell(abp);
            table.addCell(abn);
            table.addCell(op);
            table.addCell(on);

        }
        PdfPTable footTable = new PdfPTable(new float[]{5,5,5,5,5,5,5,5,5});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Blood Stock in the Bank\n\n", g));
        document.add(table);
        document.add(footTable);

        document.close();
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void previewDisabledUsersReport()
    {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void DisplayReport()
    {
        pdfView.fromFile(pFile)
                .pages(0,2,1,3,3,3)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();


    }

}
