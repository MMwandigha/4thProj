package com.example.bloodbanktest.reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bloodbanktest.Model.User;
import com.example.bloodbanktest.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class UserReports extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference payRef;

    List<User> paymentUsersList;

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
        setContentView(R.layout.activity_user_reports);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Reports");

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        payRef = FirebaseDatabase.getInstance().getReference().child("users");
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
        pFile = new File(payfile, "SystemUsers.pdf");
        fetchPaymentUsers();


    }

    private void fetchPaymentUsers() {
        payRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//creating an object and setting to displlay
                    User pays = new User();
                    pays.setIdnumber(snapshot.child("idnumber").getValue().toString());
                    pays.setName(snapshot.child("name").getValue().toString());
                    pays.setEmail(snapshot.child("email").getValue().toString());
                    pays.setPhonenumber(snapshot.child("phonenumber").getValue().toString());
                    pays.setBloodroup(snapshot.child("bloodgroup").getValue().toString());

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

    private void createPaymentReport(List<User> paymentUsersList) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#C62949");

        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(pFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 10, 10, 20, 10, 6});
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

        Chunk idText = new Chunk("Id Number", white);
        PdfPCell idCell = new PdfPCell(new Phrase(idText));
        idCell.setFixedHeight(50);
        idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        idCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("Name", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk emailText = new Chunk("Email", white);
        PdfPCell emailCell = new PdfPCell(new Phrase(emailText));
        emailCell.setFixedHeight(50);
        emailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk amountText = new Chunk("Phone Number", white);
        PdfPCell amountCell = new PdfPCell(new Phrase(amountText));
        amountCell.setFixedHeight(50);
        amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk typeText = new Chunk("Type", white);
        PdfPCell typeCell = new PdfPCell(new Phrase(typeText));
        typeCell.setFixedHeight(50);
        typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        typeCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk footerText = new Chunk("Maxwell Mwandigha - Copyright @ 2022");
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(6);

        table.addCell(noCell);
        table.addCell(idCell);
        table.addCell(nameCell);
        table.addCell(emailCell);
        table.addCell(amountCell);
        table.addCell(typeCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < paymentUsersList.size(); i++) {
            User pay = paymentUsersList.get(i);

            String id = String.valueOf(i + 1);
            String idnumber = pay.getIdnumber();
            String name = pay.getName();
            String email = pay.getEmail();
            String phonenumber = pay.getPhonenumber();
            String type = pay.getBloodgroup();


            table.addCell(id + ". ");
            table.addCell(idnumber);
            table.addCell(name);
            table.addCell(email);
            table.addCell(phonenumber);
            table.addCell(type);

        }
        PdfPTable footTable = new PdfPTable(new float[]{6, 10, 10, 20, 10, 6});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Users in the System\n\n", g));
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