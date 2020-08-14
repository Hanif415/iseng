package com.hanif.firebasecrud.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanif.firebasecrud.R;
import com.hanif.firebasecrud.adapter.MainAdapter;
import com.hanif.firebasecrud.model.StuffModel;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements MainAdapter.FirebaseDataListener {

    private ExtendedFloatingActionButton mFloatingActionButton;
    private EditText mEditNama;
    private EditText mEditMerk;
    private EditText mEditHarga;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<StuffModel> daftarBarang;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("barang");
        mDatabaseReference.child("data_barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarBarang = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    StuffModel barang = mDataSnapshot.getValue(StuffModel.class);
                    barang.setKey(mDataSnapshot.getKey());
                    daftarBarang.add(barang);
                }

                mAdapter = new MainAdapter(MainActivity2.this, daftarBarang);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(MainActivity2.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        mFloatingActionButton = findViewById(R.id.fa_add_stuff);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTambahBarang();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onDataClick(final StuffModel barang, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateBarang(barang);
            }
        });

        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                hapusDataBarang(barang);
            }
        });

        builder.setNeutralButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogTambahBarang() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data ModelBarang");
        View view = getLayoutInflater().inflate(R.layout.edit_stuff_layout, null);

        mEditNama = view.findViewById(R.id.edt_stuff_name);
        mEditMerk = view.findViewById(R.id.edt_stuff_brand);
        mEditHarga = view.findViewById(R.id.edt_stuff_price);
        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String namaBarang = mEditNama.getText().toString();
                String merkBarang = mEditMerk.getText().toString();
                String hargaBarang = mEditHarga.getText().toString();

                if (!namaBarang.isEmpty() && !merkBarang.isEmpty() && !hargaBarang.isEmpty()) {
                    submitDataBarang(new StuffModel(namaBarang, merkBarang, hargaBarang));
                } else {
                    Toast.makeText(MainActivity2.this, "Data harus di isi!", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateBarang(final StuffModel barang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data ModelBarang");
        View view = getLayoutInflater().inflate(R.layout.edit_stuff_layout, null);

        mEditNama = view.findViewById(R.id.stuff_name);
        mEditMerk = view.findViewById(R.id.edt_stuff_brand);
        mEditHarga = view.findViewById(R.id.edt_stuff_price);

        mEditNama.setText(barang.getName());
        mEditMerk.setText(barang.getBrand());
        mEditHarga.setText(barang.getPrice());
        builder.setView(view);

        if (barang != null) {
            builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    barang.setName(mEditNama.getText().toString());
                    barang.setBrand(mEditMerk.getText().toString());
                    barang.setPrice(mEditHarga.getText().toString());
                    updateDataBarang(barang);
                }
            });
        }

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }

    private void submitDataBarang(StuffModel barang) {
        mDatabaseReference.child("data_barang").push()
                .setValue(barang).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity2.this, "Data barang berhasil di simpan !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDataBarang(StuffModel barang) {
        mDatabaseReference.child("data_barang").child(barang.getKey())
                .setValue(barang).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(MainActivity2.this, "Data berhasil di update !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hapusDataBarang(StuffModel barang) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("data_barang").child(barang.getKey())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void mVoid) {
                    Toast.makeText(MainActivity2.this, "Data berhasil di hapus !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
